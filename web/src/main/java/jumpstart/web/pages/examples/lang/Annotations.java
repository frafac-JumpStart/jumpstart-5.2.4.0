package jumpstart.web.pages.examples.lang;

import org.apache.tapestry5.EventConstants;
import org.apache.tapestry5.annotations.CleanupRender;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SetupRender;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.slf4j.Logger;

public class Annotations {
	@Inject
	private Logger _logger;

	@Property
	private String _message;

	@OnEvent(value = EventConstants.ACTIVATE)
	void thisPageBeenRequested() {
		_message = "thisPageBeenRequested() called...";
		_logger.info("thisPageBeenRequested() called...");
	}

	@SetupRender
	void beforeWeDoAnyRendering() {
		_message += "beforeWeDoAnyRendering() called... ";
		_logger.info("beforeWeDoAnyRendering() called...");
	}

	@CleanupRender
	void tidyUp() {
		_message += "tidyUp() called... ";
		_logger.info("tidyUp() called...");
	}

}
