package jumpstart.web.state.theapp;

import java.io.Serializable;

import jumpstart.business.domain.security.User;
import jumpstart.business.domain.security.User.PageStyle;

@SuppressWarnings("serial")
public class Visit implements Serializable {

	private Long _myUserId = null;
	private String _myLoginId = null;

	private PageStyle _pageStyle = null;
	private String _dateInputPattern = null;
	private String _dateViewPattern = null;
	private String _dateListPattern = null;

	public void noteLogin(User user) {
		_myUserId = user.getId();
		saveUsefulStuff(user);
	}

	public void noteChanges(User user) {
		if (user == null) {
			throw new IllegalArgumentException();
		}
		else if (user.getId().equals(_myUserId)) {
			saveUsefulStuff(user);
		}
	}

	private void saveUsefulStuff(User user) {
		_myLoginId = user.getLoginId();
		_pageStyle = user.getPageStyle();
		_dateInputPattern = user.getDateInputPattern();
		_dateViewPattern = user.getDateViewPattern();
		_dateListPattern = user.getDateListPattern();
	}

	public Long getMyUserId() {
		return _myUserId;
	}

	public String getMyLoginId() {
		return _myLoginId;
	}

	public PageStyle getPageStyle() {
		return _pageStyle;
	}

	public String getDateInputPattern() {
		return _dateInputPattern;
	}

	public String getDateViewPattern() {
		return _dateViewPattern;
	}

	public String getDateListPattern() {
		return _dateListPattern;
	}

}
