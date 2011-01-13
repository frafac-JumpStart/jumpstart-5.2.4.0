package jumpstart.web.components.theapp;

import org.apache.tapestry5.annotations.Parameter;

public class NavBar {
	private static final String ACTIVE_TAB_CLASS = "activeTab";
	private static final String ACTIVE_SUB_TAB_CLASS = "activeSubTab";
	
	@Parameter
	private String _tab = "";
	
	@Parameter
	private String _subTab = "";

	// Tabs
	
	public boolean isSecurityTabActive() {
		return _tab.equals("Security");
	}

	public String getCSSClassForSecurityTab() {
		return getCSSClassForTab("Security");
	}

	private String getCSSClassForTab(String tabName) {
		return tabName.equals(_tab) ? ACTIVE_TAB_CLASS : "";
	}

	// SubTabs
	
	public String getCSSClassForRoleSubTab() {
		return getCSSClassForSubTab("Role");
	}

	public String getCSSClassForUserSubTab() {
		return getCSSClassForSubTab("User");
	}

	public String getCSSClassForUserRoleSubTab() {
		return getCSSClassForSubTab("UserRole");
	}

	private String getCSSClassForSubTab(String subTabName) {
		return subTabName.equals(_subTab) ? ACTIVE_SUB_TAB_CLASS : "";
	}
}
