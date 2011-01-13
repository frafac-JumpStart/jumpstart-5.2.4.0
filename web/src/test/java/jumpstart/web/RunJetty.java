package jumpstart.web;

import java.io.FileInputStream;

import org.mortbay.jetty.Connector;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.nio.SelectChannelConnector;
import org.mortbay.jetty.webapp.WebAppContext;
import org.mortbay.xml.XmlConfiguration;

public class RunJetty {

	public static void main(String[] args) throws Exception {
		String jetty_home = System.getProperty("jetty.home");

		// Create and configure a Jetty web server. 
		
		Server server = new Server();
		XmlConfiguration configuration = new XmlConfiguration(new FileInputStream(jetty_home + "/etc/jetty.xml"));
		configuration.configure(server);

		// Add a connector for http. Other connector types include https and ajp.
		
		Connector httpConnector = new SelectChannelConnector();
		httpConnector.setPort(Integer.getInteger("jetty.port", 8080));
		server.setConnectors(new Connector[] { httpConnector });

		// Connector httpsConnector = new SelectChannelConnector();
		// httpsConnector.setPort(Integer.getInteger("jetty.port", 8443));
		// server.setConnectors(new Connector[] { httpsConnector });

		// Connector ajpConnector = new Ajp13SocketConnector();
		// ajpConnector.setPort(Integer.getInteger("jetty.port", 8009));
		// server.setConnectors(new Connector[] { ajpConnector });

		// Describe our web app, jumpstart
		
		WebAppContext webapp = new WebAppContext();
		webapp.setContextPath("/jumpstart");
		webapp.setWar("collapsed/jumpstart.war");
		
		// Replace the web app's list of classes with one that excludes slf4j and jaas
		
		webapp.setServerClasses(new String[] { "-org.mortbay.jetty.plus.jaas.", "org.mortbay.jetty." });

		// Tell our Jetty web server to handle our web app
		
		server.setHandler(webapp);

		// Start the server then wait until it stops.
		
		server.start();
		server.join();
	}

}
