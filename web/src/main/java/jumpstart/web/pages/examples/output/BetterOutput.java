package jumpstart.web.pages.examples.output;

public class BetterOutput {
	private String _message;
	
	void setupRender() {
		_message = "This message was generated in setupRender() on " + new java.util.Date() + ".";
	}
	
	public String getMessage() {
		return _message;
	}
}