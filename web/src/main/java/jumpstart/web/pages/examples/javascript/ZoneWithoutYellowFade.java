package jumpstart.web.pages.examples.javascript;

import java.util.Date;

import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.corelib.components.Zone;

public class ZoneWithoutYellowFade {

	// Generally useful bits and pieces

	@InjectComponent
	private Zone _time2Zone;

	// The code
	
	void onActionFromRefreshPage() {
		// Nothing to do - the page will call getTime1() and getTime2() as it renders.
	}

	// Isn't called if the link is clicked before the DOM is fully loaded. See
	// https://issues.apache.org/jira/browse/TAP5-1 .
	Object onActionFromRefreshZone() {
		// Here we can do whatever updates we want, then return the content we want rendered.
		return _time2Zone.getBody();
	}

	public Date getTime1() {
		return new Date();
	}

	public Date getTime2() {
		return new Date();
	}
}
