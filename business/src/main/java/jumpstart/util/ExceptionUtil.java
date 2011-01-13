package jumpstart.util;

import java.io.PrintWriter;
import java.io.StringWriter;

public class ExceptionUtil {

	static public String getRootCause(Throwable t) {
		Throwable cause = t;
		Throwable subCause = cause.getCause();
		while (subCause != null && !subCause.equals(cause)) {
			cause = subCause;
			subCause = cause.getCause();
		}
		return cause.getMessage();
	}

	static public String printStackTrace(Throwable t) {
		StringWriter stringWriter = new StringWriter();
		PrintWriter printWriter = new PrintWriter(stringWriter, false);
		t.printStackTrace(printWriter);
		printWriter.close();
		String s = stringWriter.getBuffer().toString();
		return s;
	}

}
