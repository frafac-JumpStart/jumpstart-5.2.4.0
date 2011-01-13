package jumpstart.web.services;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLConnection;

import org.apache.tapestry5.ioc.services.ClasspathURLConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClasspathURLConverterJBoss5 implements ClasspathURLConverter {
	private static Logger _logger = LoggerFactory.getLogger(ClasspathURLConverterJBoss5.class);

	public URL convert(URL url) {
		if (url != null && url.getProtocol().startsWith("vfs")) {
			// supports virtual filesystem used by JBoss 5.x
			try {
				URLConnection connection = url.openConnection();
				Object virtualFile = invokerGetter(connection, "getContent");
				Object zipEntryHandler = invokerGetter(virtualFile, "getHandler");
				Object realUrl = invokerGetter(zipEntryHandler, "getRealURL");
				return (URL) realUrl;
			}
			catch (Exception e) {
				_logger.info(e.getCause().toString());
			}
		}
		return url;
	}

	private Object invokerGetter(Object target, String getter) throws NoSuchMethodException, InvocationTargetException,
			IllegalAccessException {
		Class<?> type = target.getClass();
		Method method;
		try {
			method = type.getMethod(getter);
		}
		catch (NoSuchMethodException e) {
			method = type.getDeclaredMethod(getter);
			method.setAccessible(true);
		}
		return method.invoke(target);
	}

}
