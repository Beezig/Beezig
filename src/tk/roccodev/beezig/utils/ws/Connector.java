package tk.roccodev.beezig.utils.ws;

import java.net.URI;
import java.net.URISyntaxException;

public class Connector {

	
	
	public static void main(String[] args) {
		connect();
	}
	
	public static void connect() {
		try {
			Client c = new Client(new URI("ws://beezignode-beezig-node.a3c1.starter-us-west-1.openshiftapps.com:80"));
			c.connect();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
