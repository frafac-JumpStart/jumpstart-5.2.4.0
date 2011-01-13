package jumpstart.web.pages.examples.state;

import org.apache.tapestry5.Link;
import org.apache.tapestry5.annotations.InjectPage;

public class PassingDataBetweenPages1 {

	@InjectPage
	private PassingDataBetweenPages2 _page2;

	@InjectPage
	private PassingDataBetweenPages3 _page3;

	@InjectPage
	private PassingDataBetweenPages4 _page4;
	
	Object onSuccessFromUseActivationContext() {
		_page2.set("Humpty", "Dumpty");
		return _page2;
	}

	Object onSuccessFromUsePersistence() {
		_page3.set("Humpty", "Dumpty");
		return _page3;
	}

	Object onSuccessFromUseRequestParameters() {
		Link link = _page4.set("Humpty", "Dumpty");
		return link;
	}
}
