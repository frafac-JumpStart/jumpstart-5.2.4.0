package jumpstart.web.pages.theapp.security;

import java.util.List;

import jumpstart.business.domain.security.Role;
import jumpstart.business.domain.security.User;
import jumpstart.business.domain.security.UserRole;
import jumpstart.business.domain.security.iface.ISecurityFinderServiceLocal;
import jumpstart.business.domain.security.iface.ISecurityManagerServiceLocal;
import jumpstart.business.domain.security.iface.UserRoleSearchFields;
import jumpstart.util.query.SearchOptions;
import jumpstart.web.annotation.ProtectedPage;
import jumpstart.web.base.theapp.SimpleBasePage;
import jumpstart.web.commons.IdSelectModel;
import jumpstart.web.commons.URLParameterHelper;

import org.apache.tapestry5.Link;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.services.PropertyAccess;
import org.apache.tapestry5.services.PageRenderLinkSource;
import org.apache.tapestry5.services.Request;

@ProtectedPage
public class UserRoleSearch extends SimpleBasePage {

	// The search filter parameter names

	static private String PARAM_USER_ID = "userid";
	static private String PARAM_ROLE_ID = "roleid";
	static private String PARAM_SHOW_RESULT = "show";

	// The search filter and the last displayed search filter.

	@Property
	private UserRoleSearchFields _searchFields = new UserRoleSearchFields();

	@Property
	private boolean _showResult = false;

	@Persist
	private UserRoleSearchFields _lastSearchFields;

	@Persist
	private boolean _lastShowResult;

	// The result: a list of user roles.

	@SuppressWarnings("unused")
	@Property
	private List<UserRole> _userRoles;

	@SuppressWarnings("unused")
	@Property
	private UserRole _userRole;

	// The search filters allow selection from lists of users and roles.

	@SuppressWarnings("unused")
	@Property
	private IdSelectModel<User> _userIds;

	@SuppressWarnings("unused")
	@Property
	private IdSelectModel<Role> _roleIds;

	@Inject
	private PropertyAccess _propertyAccess;

	// The pages we can call.

	@InjectPage
	private UserRoleAdd _addPage;

	@InjectPage
	private UserRoleView _viewPage;

	@InjectPage
	private UserRoleEdit _editPage;

	// Generally useful bits and pieces

	@Component(id = "form")
	private Form _form;

	@Inject
	private PageRenderLinkSource _pageRenderLinkSource;

	@Inject
	private Request _request;

	void onActivate() {
		extractSearchCriteriaFromRequest();
	}

	void setupRender() {
		_userRoles = null;

		// If needed, do the search.

		if (_showResult) {
			_userRoles = search(_searchFields);
		}

		// Put a selection list of users onto the page.

		List<User> users = getSecurityFinderService().findUsersShallowish();
		_userIds = new IdSelectModel<User>(users, User.class, "loginId", "id", _propertyAccess);

		// Put a selection list of roles onto the page.

		List<Role> roles = getSecurityFinderService().findRolesShallowish();
		_roleIds = new IdSelectModel<Role>(roles, Role.class, "name", "id", _propertyAccess);

		// Save the search criteria for later.

		_lastSearchFields = new UserRoleSearchFields(_searchFields);
		_lastShowResult = _showResult;
	}

	List<UserRole> search(UserRoleSearchFields searchFields) {
		SearchOptions searchOptions = new SearchOptions();
		List<UserRole> l = getSecurityFinderService().findUserRolesShallowish(searchFields, searchOptions);
		return l;
	}

	Link onSuccess() {
		return putSearchCriteriaInALink(_searchFields, true);
	}

	void onReset() {
		_searchFields = new UserRoleSearchFields();
		_showResult = false;
		_userIds = null;
	}

	Object onAdd() {
		_addPage.set(null, null, putSearchCriteriaInALink(_lastSearchFields, _lastShowResult));
		return _addPage;
	}

	Object onActionFromView(Long id) {
		_viewPage.set(id, putSearchCriteriaInALink(_lastSearchFields, _lastShowResult));
		return _viewPage;
	}

	Object onActionFromEdit(Long id) {
		_editPage.set(id, putSearchCriteriaInALink(_lastSearchFields, _lastShowResult));
		return _editPage;
	}

	Link onActionFromRemove(Long id, Integer version) {

		if (_form.isValid()) {

			// Delete the user from the database unless they've been modified elsewhere

			try {
				UserRole userRole = getSecurityFinderService().findUserRole(id);
				if (!userRole.getVersion().equals(version)) {
					_form
							.recordError("Cannot remove user role because has been updated or deleted since last displayed.  Please refresh and try again.");
				}
				else {
					getSecurityManagerService().removeUserRole(userRole);
				}
			}
			catch (Exception e) {
				_form.recordError(interpretBusinessServicesExceptionForRemove(e));
			}
		}
		return putSearchCriteriaInALink(_lastSearchFields, _lastShowResult);
	}

	void extractSearchCriteriaFromRequest() {

		// Set the search filter criteria from the request URL parameters.
		// We could have put the filter fields in the activation context, but arguably it's more RESTful to use
		// request parameters for filter criteria. The URL is certainly a more reliable bookmark this way.
		// Eg. See http://blpsilva.wordpress.com/2008/04/05/query-strings-in-restful-web-services/

		_searchFields.setUserId(URLParameterHelper.getLongParameter(_request, PARAM_USER_ID));
		_searchFields.setRoleId(URLParameterHelper.getLongParameter(_request, PARAM_ROLE_ID));
		Boolean showResultParam = URLParameterHelper.getBooleanParameter(_request, PARAM_SHOW_RESULT);
		_showResult = showResultParam == null ? false : showResultParam;
	}

	Link putSearchCriteriaInALink(UserRoleSearchFields search, boolean showResult) {

		// Return a link with the non-null search filter criteria in it.
		// We could have used onPassivate to output the search fields as the activation context, but arguably
		// it's more RESTful to use request parameters for filter criteria. The URL is certainly a more reliable
		// bookmark this way.
		// Eg. See http://blpsilva.wordpress.com/2008/04/05/query-strings-in-restful-web-services/

		Link link = _pageRenderLinkSource.createPageRenderLink(this.getClass());

		if (search != null) {
			URLParameterHelper.addLongParameter(link, PARAM_USER_ID, search.getUserId());
			URLParameterHelper.addLongParameter(link, PARAM_ROLE_ID, search.getRoleId());
			URLParameterHelper.addBooleanParameter(link, PARAM_SHOW_RESULT, showResult);
		}

		return link;
	}

	private ISecurityFinderServiceLocal getSecurityFinderService() {
		return getBusinessServicesLocator().getSecurityFinderServiceLocal();
	}

	private ISecurityManagerServiceLocal getSecurityManagerService() {
		return getBusinessServicesLocator().getSecurityManagerServiceLocal();
	}

}
