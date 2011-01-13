package jumpstart.web.pages.examples.state;

import jumpstart.web.pages.Index;

import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;

public class StoringDataInAPage {

	// Screen fields

	@Property
	@Persist
	private int _count;

	// Generally useful bits and pieces

	@Inject
	private ComponentResources _resources;

	// The code
	
	void setupRender() {
		_count++;
	}

	Object onActionFromClear() {
		_resources.discardPersistentFieldChanges();
		return Index.class;
	}
}
