package jumpstart.web.pages.examples.navigation;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.tapestry5.Link;
import org.apache.tapestry5.PersistenceConstants;
import org.apache.tapestry5.StreamResponse;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.PageRenderLinkSource;
import org.apache.tapestry5.services.Response;

public class ReturnTypes1 {

	@SuppressWarnings("unused")
	@Persist(PersistenceConstants.FLASH)
	@Property
	private String _message;

	void onActionFromReturnVoid() {
		_message = "You chose to return void.";
	}

	Object onActionFromReturnNull() {
		_message = "You chose to return null.";
		return null;
	}

	@SuppressWarnings("unchecked")
	Class onActionFromReturnClass() {
		return ReturnTypesClass.class;
	}

	@InjectPage
	private ReturnTypesPageObject _pageObject;

	Object onActionFromReturnPageObject() {
		_pageObject.set("Hello");
		return _pageObject;
	}

	String onActionFromReturnString() {
		return "examples/navigation/ReturnTypesString";
	}

	@Inject
	private PageRenderLinkSource _pageRenderLinkSource;

	Link onActionFromReturnLink() {
		String parameters = "Howdy";
		Link link = _pageRenderLinkSource.createPageRenderLinkWithContext(ReturnTypesLink.class, parameters);
		return link;
	}

	StreamResponse onActionFromReturnStreamResponse() {
		return new StreamResponse() {
			InputStream inputStream;

			@Override
			public void prepareResponse(Response response) {
				ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
				inputStream = classLoader.getResourceAsStream("jumpstart/web/text/examples/ReturnTypeText.txt");

				// Set content length to prevent chunking - see
				// http://tapestry-users.832.n2.nabble.com/Disable-Transfer-Encoding-chunked-from-StreamResponse-td5269662.html#a5269662

				try {
					response.setHeader("Content-Length", "" + inputStream.available());
				}
				catch (IOException e) {
					// Ignore the exception in this simple example.
				}
			}
			
			@Override
			public String getContentType() {
				return "text/plain";
			}
			
			@Override
			public InputStream getStream() throws IOException {
				return inputStream;
			}
		};
	}

	URL onActionFromReturnURL() throws MalformedURLException {
		return new URL("http://tapestry.apache.org/tapestry5/");
	}

	boolean onActionFromReturnTrue() {
		_message = "You chose to return true.";
		return true;
	}

	boolean onActionFromReturnFalse() {
		_message = "You chose to return false.";
		return false;
	}
}
