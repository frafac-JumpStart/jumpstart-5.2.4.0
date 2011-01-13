package jumpstart.web.pages.examples.wizard;

import org.apache.tapestry5.annotations.Property;

public class WizardUsingPagesSuccess {

	// The activation context

	@Property
	private int _approvedAmount;

	@Property
	private String _approvedApplicantName;

	// The code

	public void set(int approvedAmount, String approvedApplicantName) {
		_approvedAmount = approvedAmount;
		_approvedApplicantName = approvedApplicantName;
	}

	Object[] onPassivate() {
		// In the real world we would typically passivate the credit request's id instead of these fields
		return new Object[] { _approvedAmount, _approvedApplicantName };
	}

	void onActivate(int approvedAmount, String approvedApplicantName) throws Exception {
		// In the real world we would typically receive the credit request's id instead of these fields
		_approvedAmount = approvedAmount;
		_approvedApplicantName = approvedApplicantName;
	}

	void setupRender() {
		// In the real world we would typically have been passed the persisted credit requests's id, so we'd retrieve
		// the credit request from the database, but in this example we were passed the fields to render.
	}

	Object onRestart() {
		return WizardUsingPages1.class;
	}
}
