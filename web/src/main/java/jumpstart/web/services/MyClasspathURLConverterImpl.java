package jumpstart.web.services;

import java.net.MalformedURLException;
import java.net.URL;

import org.apache.tapestry5.ioc.services.ClasspathURLConverter;

/**
 * This is an attempt to get Tapestry to work with JBoss 5, but it only works with exploded EARs.
 * The proper solution probably involves retaining the "vfszip" prefix and modifying Tapestry's ClassNameLocatorImpl. 
 * 
 * This is intended to override Tapestry's ClasspathURLConverter service. It recognizes JBoss 5's vfszip protocol, which
 * Tapestry does not understand, by replacing it with the traditional jar protocol, which Tapestry does understand.
 */
public class MyClasspathURLConverterImpl implements ClasspathURLConverter {
	public URL convert(URL url) {

		if (url.getProtocol().equals("vfszip")) {
			try {
				String urlStr = "jar:file:" + url.getPath().replaceAll(".jar/", ".jar!/");
				url = new URL(urlStr);
			}
			catch (MalformedURLException e) {
				throw new IllegalStateException("Should not get here. url = " + url);
			}
		}

		return url;
	}
}
