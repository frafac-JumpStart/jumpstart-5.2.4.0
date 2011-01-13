package jumpstart.web.components.theapp;

import jumpstart.web.annotation.ProtectedPage;
import jumpstart.web.pages.theapp.Login;
import jumpstart.web.state.theapp.Visit;

import org.apache.tapestry5.Asset;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Path;
import org.apache.tapestry5.annotations.SessionState;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.RequestGlobals;
import org.apache.tapestry5.services.Session;
import org.slf4j.Logger;

@ProtectedPage
public class Layout {

	@Parameter
	private String _tab = "";

	@Parameter
	private String _subTab = "";

	@Parameter
	private String _title = "";

	@Inject
	@Path("context:css/theapp/global.css")
	private Asset _stylesheet1;

	@Inject
	@Path("context:css/theapp/global_wide.css")
	private Asset _stylesheet2;

	@Inject
	private Logger _logger;

	@Inject
	private RequestGlobals _requestGlobals;

	@SessionState
	private Visit _visit;

	Object onActionFromLogout() {
		_logger.info(_visit.getMyLoginId() + " is logging out.");
		Session session = _requestGlobals.getRequest().getSession(false);
		if (session != null) {
			session.invalidate();
		}
		return Login.class;
	}

	public String getTitle() {
		return _title;
	}

	public Asset getStylesheet() {
		switch (_visit.getPageStyle()) {
		case BOXY:
			return _stylesheet1;
		case WIDE:
			return _stylesheet2;
		default:
			return _stylesheet1;
		}
	}

	public String getTab() {
		return _tab;
	}

	public String getSubTab() {
		return _subTab;
	}

	public String getLoginId() {
		return _visit.getMyLoginId();
	}
}
