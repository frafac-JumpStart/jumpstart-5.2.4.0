package jumpstart.web.components.examples.component;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;

public class SlidingSourceCodeDisplay {
	
	// Parameters

	// The source file path from the project root eg. "/web/src/main/jumpstart/web/pages/Start.java"
	@Parameter(required = true, defaultPrefix = BindingConstants.LITERAL)
	@Property
	private String _src;
	
	// The code

	public String getPanelTitle() {
		return extractSimpleName(_src);
	}

	private String extractSimpleName(String path) {
		String simpleName = path;

		int i = path.lastIndexOf("/");
		simpleName = path.substring(i + 1);

		return simpleName;
	}

}
