package jumpstart.web.state.examples.wizard;

import java.io.Serializable;

// In the real world we'd typically make this a business domain entity 
//@Entity
@SuppressWarnings("serial")
public class CreditRequest implements Serializable {

	private int _amount = 0;
	private String _applicantName = "";
	private Status _status = Status.INCOMPLETE;

	public enum Status {
		INCOMPLETE, COMPLETE
	}

	public String toString() {
		final String DIVIDER = ", ";

		StringBuffer buf = new StringBuffer();
		buf.append(this.getClass().getSimpleName() + ": ");
		buf.append("[");
		buf.append("amount=" + _amount + DIVIDER);
		buf.append("applicantName=" + _applicantName + DIVIDER);
		buf.append("status=" + _status);
		buf.append("]");
		return buf.toString();
	}

	public CreditRequest() {
	}

	public void validateAmountInfo() throws Exception {
		if (_amount < 10 || _amount > 9999) {
			throw new Exception("Amount must be between 10 and 9999.");
		}
	}

	public void validateApplicantInfo() throws Exception {
		if (_applicantName == null || _applicantName.trim().equals("")) {
			throw new Exception("Applicant name is required.");
		}
	}

	public void validate() throws Exception {
		validateAmountInfo();
		validateApplicantInfo();
	}

	public void complete() throws Exception {
		validate();
		_status = Status.COMPLETE;
	}

	public Status getStatus() {
		return _status;
	}

	public int getAmount() {
		return _amount;
	}

	public void setAmount(int amount) {
		_amount = amount;
	}

	public String getApplicantName() {
		return _applicantName;
	}

	public void setApplicantName(String applicantName) {
		_applicantName = applicantName;
	}

}
