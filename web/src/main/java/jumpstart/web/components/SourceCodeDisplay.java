package jumpstart.web.components;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Context;

public class SourceCodeDisplay {
	static private String LINE_SEPARATOR = System.getProperty("line.separator");

	// The source file path from the project root eg. "/web/src/main/jumpstart/web/pages/Start.java"
	@Parameter(required = true, defaultPrefix = BindingConstants.LITERAL)
	private String _src;

	@Inject
	private Context _context;

	boolean beginRender(MarkupWriter writer) {

		// Print start of the source block

		writer.write(LINE_SEPARATOR);
		writer.writeRaw("<!-- Start of source code inserted by SourceCodeDisplay component. -->");
		writer.write(LINE_SEPARATOR);
		writer.write(LINE_SEPARATOR);

		// Print a div with style info to make a pretty block

		String style = "font-family: Arial, Helvetica, sans-serif; font-size: 12px; font-weight: normal; background: #99ffcc; margin: 10px 0px 0px 0px; text-align: left; ";
		writer.element("div", "style", style);
		writer.write(LINE_SEPARATOR);
		
		writer.write("Source code: " + extractSimpleName(_src));
		writer.write(LINE_SEPARATOR);

		// Print the source

		if (_src != null) {
			printSource(writer, _src);
		}

		writer.element("hr");
		writer.end();
		writer.write(LINE_SEPARATOR);

		// Print end of div

		writer.write(LINE_SEPARATOR);
		writer.end();

		// Print end of source block

		writer.write(LINE_SEPARATOR);
		writer.write(LINE_SEPARATOR);
		writer.writeRaw("<!-- End of source code inserted by SourceCodeDisplay component. -->");
		writer.write(LINE_SEPARATOR);

		return true;
	}
	
	private String extractSimpleName(String path) {
		String simpleName = path;
		
		int i = path.lastIndexOf("/");
		simpleName = path.substring(i + 1);
		
		return simpleName;
	}

	private void printSource(MarkupWriter writer, String givenPath) {
		if (givenPath != null) {
			printSourceFromInputStream(writer, givenPath, "/WEB-INF/sourcecode" + givenPath);
		}
	}

	private void printSourceFromInputStream(MarkupWriter writer, String title, String givenPath) {
		if (givenPath != null) {
			writer.element("hr");
			writer.end();
			writer.write(LINE_SEPARATOR);
			URL url = _context.getResource(givenPath);
			try {
				if (url != null) {
					InputStream templateStream = url.openStream();
					if (templateStream != null) {
						BufferedReader templateReader = new BufferedReader(new InputStreamReader(templateStream));
						printSource(writer, templateReader);
					}
					else {
						printResourceNotFound(writer, givenPath);
					}
				}
				else {
					printResourceNotFound(writer, givenPath);
				}
			}
			catch (IOException e) {
				printResourceNotFound(writer, givenPath);
			}
		}
	}

	private void printSource(MarkupWriter writer, BufferedReader sourceReader) {
		writer.element("pre");
		writer.write(LINE_SEPARATOR);
		writer.element("code");
		writer.write(LINE_SEPARATOR);

		String s;
		try {
			while ((s = sourceReader.readLine()) != null) {
				writer.write(s);
				writer.write(LINE_SEPARATOR);
			}
		}
		catch (IOException e) {
			writer.write("Error reading .....?");
			e.printStackTrace();
		}

		writer.end();
		writer.write(LINE_SEPARATOR);
		writer.end();
		writer.write(LINE_SEPARATOR);
	}

	private void printResourceNotFound(MarkupWriter writer, String resourcePath) {
		writer.write(LINE_SEPARATOR);
		writer.write("The file was not found.");
		writer.element("br");
		writer.end();
		writer.write(LINE_SEPARATOR);
		writer.element("br");
		writer.end();
		writer.write(LINE_SEPARATOR);
		writer.write("Path given was " + resourcePath);
		writer.element("br");
		writer.end();
		writer.write(LINE_SEPARATOR);
	}
}
