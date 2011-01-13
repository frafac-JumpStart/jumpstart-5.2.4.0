package jumpstart.web.pages.examples.lang;

import org.apache.tapestry5.ioc.annotations.Inject;
import org.slf4j.Logger;

public class Methods {
	@Inject
	private Logger _logger;

	private String _message;
	
	void onActivate() {
		_message = "onActivate() called...";
		_logger.info("onActivate() called...");
	}
	
	void setupRender() {
		_message += "setupRender() called... ";
		_logger.info("setupRender() called...");
	}

	void cleanupRender() {
		_message += "cleanupRender() called... ";
		_logger.info("cleanupRender() called...");
	}
	
	public String getMessage() {
		_message += "getMessage() called... ";
		_logger.info("getMessage() called...");
		return _message;
	}
	
}
