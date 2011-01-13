package jumpstart.web.pages.theapp.security;

import java.util.ArrayList;
import java.util.List;

import jumpstart.business.domain.security.User;
import jumpstart.business.domain.security.iface.ISecurityFinderServiceLocal;
import jumpstart.business.domain.security.iface.ISecurityManagerServiceLocal;
import jumpstart.business.domain.security.iface.UserSearchFields;
import jumpstart.util.StringUtil;
import jumpstart.util.query.SearchOptions;
import jumpstart.web.annotation.ProtectedPage;
import jumpstart.web.base.theapp.SimpleBasePage;
import jumpstart.web.commons.URLParameterHelper;

import org.apache.tapestry5.Link;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.PageRenderLinkSource;
import org.apache.tapestry5.services.Request;

@ProtectedPage
public class UserSearch extends SimpleBasePage {

	// The search filter parameter names

	static private String PARAM_LOGIN_ID = "loginid";
	static private String PARAM_SALUTATION = "salutation";
	static private String PARAM_FIRST_NAME = "firstname";
	static private String PARAM_LAST_NAME = "lastname";
	static private String PARAM_EMAIL_ADDRESS = "email";
	static private String PARAM_EXPIRY_DATE = "expiry";
	static private String PARAM_ACTIVE = "active";
	static private String PARAM_SHOW_RESULT = "show";

	// The search filter and the last displayed search filter.

	@Property
	private UserSearchFields _searchFields = new UserSearchFields();

	@Property
	private boolean _showResult = false;

	@Persist
	private UserSearchFields _lastSearchFields;

	@Persist
	private boolean _lastShowResult;

	// The result: a list of users.

	@Property
	private List<User> _users;

	@SuppressWarnings("unused")
	@Property
	private User _user;

	// The search filters allow selection from a list of Active options.

	// The options for userSearchFields.active are null, true, and false but we have to present them using
	// String[] instead of Boolean[] because the inbuilt type coercer coerces null to false.
	// (see https://issues.apache.org/jira/browse/TAPESTRY-1928).
	static private final String[] ACTIVE_OPTIONS = { "true", "false" };

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
		_users = null;

		if (_showResult) {
			_users = search(_searchFields);
			if (_users == null) {
				_users = new ArrayList<User>();
			}
		}

		_lastSearchFields = new UserSearchFields(_searchFields);
		_lastShowResult = _showResult;
	}

	Link onSuccess() {
		return putSearchCriteriaInALink(_searchFields, true);
	}

	Link onSort() {
		return putSearchCriteriaInALink(_searchFields, true);
	}

	Link onActionFromGrid() {
		return putSearchCriteriaInALink(_searchFields, true);
	}

	void onReset() {
		_searchFields = new UserSearchFields();
		_showResult = false;
		_users = null;
	}

	Object onNew() {
		return UserCreate.class;
	}

	Link onActionFromDelete(Long id, Integer version) {

		if (_form.isValid()) {

			// Delete the user from the database unless they've been modified elsewhere

			try {
				User user = getSecurityFinderService().findUser(id);
				if (!user.getVersion().equals(version)) {
					_form
							.recordError("Cannot delete user because has been updated or deleted since last displayed.  Please refresh and try again.");
				}
				else {
					getSecurityManagerService().deleteUser(user);
				}
			}
			catch (Exception e) {
				_form.recordError(interpretBusinessServicesExceptionForDelete(e));
			}
		}
		return putSearchCriteriaInALink(_lastSearchFields, _lastShowResult);
	}

	List<User> search(UserSearchFields searchFields) {
		SearchOptions searchOptions = new SearchOptions();
		List<User> l = getSecurityFinderService().findUsersShallowish(searchFields, searchOptions);
		return l;
	}

	void extractSearchCriteriaFromRequest() {

		// Set the search filter criteria from the request URL parameters.
		// We could have put the filter fields in the activation context, but arguably it's more RESTful to use
		// request parameters for filter criteria. The URL is certainly a more reliable bookmark this way.
		// Eg. See http://blpsilva.wordpress.com/2008/04/05/query-strings-in-restful-web-services/

		_searchFields.setLoginId(URLParameterHelper.getStringParameter(_request, PARAM_LOGIN_ID));
		_searchFields.setSalutation(URLParameterHelper.getStringParameter(_request, PARAM_SALUTATION));
		_searchFields.setFirstName(URLParameterHelper.getStringParameter(_request, PARAM_FIRST_NAME));
		_searchFields.setLastName(URLParameterHelper.getStringParameter(_request, PARAM_LAST_NAME));
		_searchFields.setEmailAddress(URLParameterHelper.getStringParameter(_request, PARAM_EMAIL_ADDRESS));
		_searchFields.setExpiryDate(URLParameterHelper.getDateParameter(_request, PARAM_EXPIRY_DATE));
		_searchFields.setActive(URLParameterHelper.getBooleanParameter(_request, PARAM_ACTIVE));
		Boolean showParam = URLParameterHelper.getBooleanParameter(_request, PARAM_SHOW_RESULT);
		_showResult = showParam == null ? false : showParam;
	}

	Link putSearchCriteriaInALink(UserSearchFields search, boolean showResult) {

		// Return a link with the non-null search filter criteria in it.
		// We could have used onPassivate to output the search fields as the activation context, but arguably
		// it's more RESTful to use request parameters for filter criteria. The URL is certainly a more reliable
		// bookmark this way.
		// Eg. See http://blpsilva.wordpress.com/2008/04/05/query-strings-in-restful-web-services/

		Link link = _pageRenderLinkSource.createPageRenderLink(this.getClass());

		if (_lastSearchFields != null) {
			URLParameterHelper.addStringParameter(link, PARAM_LOGIN_ID, search.getLoginId());
			URLParameterHelper.addStringParameter(link, PARAM_SALUTATION, search.getSalutation());
			URLParameterHelper.addStringParameter(link, PARAM_FIRST_NAME, search.getFirstName());
			URLParameterHelper.addStringParameter(link, PARAM_LAST_NAME, search.getLastName());
			URLParameterHelper.addStringParameter(link, PARAM_EMAIL_ADDRESS, search.getEmailAddress());
			URLParameterHelper.addDateParameter(link, PARAM_EXPIRY_DATE, search.getExpiryDate());
			URLParameterHelper.addBooleanParameter(link, PARAM_ACTIVE, search.getActive());
			URLParameterHelper.addBooleanParameter(link, PARAM_SHOW_RESULT, showResult);
		}

		return link;
	}

	public Link createLinkWithLastSearch() {
		return putSearchCriteriaInALink(_lastSearchFields, _lastShowResult);
	}

	private ISecurityFinderServiceLocal getSecurityFinderService() {
		return getBusinessServicesLocator().getSecurityFinderServiceLocal();
	}

	private ISecurityManagerServiceLocal getSecurityManagerService() {
		return getBusinessServicesLocator().getSecurityManagerServiceLocal();
	}

	public String[] getSalutations() {
		return User.SALUTATIONS;
	}

	public String[] getActiveOptions() {
		return ACTIVE_OPTIONS;
	}

	public String getActiveAsString() {
		Boolean active = _searchFields.getActive();
		return active == null ? "" : active.toString();
	}

	public void setActiveAsString(String activeAsString) {

		// We can't simply use an encoder in the template to convert this field to/from a selected option because
		// the built in type coercer intercepts Booleans and converts null to false! We want null to mean null.

		Boolean active = StringUtil.isEmpty(activeAsString) ? null : Boolean.valueOf(activeAsString);
		_searchFields.setActive(active);
	}

}
