package com.heliomug.utils.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.MalformedURLException;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.util.Enumeration;

public class NetworkUtils {
	private static InetAddress externalAddress;
	private static InetAddress lanAddress;
	
	static {
		System.out.println("External IP Address: " + getExternalAddress());
		System.out.println("Lan IP Address: " + getLanAddress());
	}
	
	public static InetAddress getExternalAddress() {
		if (externalAddress == null) {
			externalAddress = determineExternalAddress();
		}
		return externalAddress;
	}
	
	public static InetAddress getLanAddress() {
		if (lanAddress == null) {
			lanAddress = determineLocalAddress();
		}
		return lanAddress;
	}
	
	private static InetAddress determineExternalAddress() {
		try {
			URL az = new URL("http://checkip.amazonaws.com");
			try (BufferedReader in = new BufferedReader(new InputStreamReader(az.openStream()))) {
				String ip = in.readLine();
				return InetAddress.getByName(ip);
			} catch (IOException e) {
				// oh well; use local
			} 
		} catch (MalformedURLException e1) {
			// oh well; use local
		}
		return determineLocalAddress();
	}
	
	// someone else's code from stack overflow: 
	// http://stackoverflow.com/questions/2845279/how-to-get-the-lan-ip-of-a-client-using-java
	
	private static InetAddress determineLocalAddress() {
	    try {
			for (final Enumeration< NetworkInterface > interfaces = NetworkInterface.getNetworkInterfaces( ) ; 
			    interfaces.hasMoreElements( ) ; ) {
			    
				NetworkInterface cur = interfaces.nextElement( );
		
					if (cur.isLoopback()) {
					    continue;
					}
		
			    for (InterfaceAddress addr : cur.getInterfaceAddresses()) {
			        InetAddress inet_addr = addr.getAddress();
		
			        if (!(inet_addr instanceof Inet4Address)) {
			            continue;
			        }
		
			        return inet_addr;
			    }
			}
		} catch (SocketException e) {
			// couldn't open network whatevers.  use loopback address then.    
		}
		return InetAddress.getLoopbackAddress();
	}
}
