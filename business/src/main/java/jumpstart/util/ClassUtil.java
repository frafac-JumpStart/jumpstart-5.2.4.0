package jumpstart.util;

public class ClassUtil {

	@SuppressWarnings("unchecked")
	static public String extractUnqualifiedName(Object obj) {
		Class cls = obj.getClass();
		return extractUnqualifiedName(cls);
	}

	@SuppressWarnings("unchecked")
	static public String extractUnqualifiedName(Class cls) {
		String s = cls.getName();
		String[] s1 = s.split("\\.");
		return s1[s1.length - 1];
	}

}