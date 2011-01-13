package jumpstart.util;

public class StringUtil {

	static public boolean isEmpty(String s) {
		if ((s == null) || (s.trim().length() == 0)) {
			return true;
		}
		else {
			return false;
		}
	}
	
	public static boolean isNotEmpty(String s) {
		return !isEmpty(s);
	}

	public static String abbreviate(String s, int maxLen) {
		if (s == null) {
			return null;
		}
		else {
			return s.length() > maxLen ? (s.substring(0, maxLen) + "...") : s.substring(0, s.length());
		}
	}
	
}