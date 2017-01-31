package com.heliomug.game.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;

public class Utils {
	public static InetAddress getExternalAddress() {
		try {
			URL az = new URL("http://checkip.amazonaws.com");
			try (BufferedReader in = new BufferedReader(new InputStreamReader(az.openStream()))) {
				String ip = in.readLine();
				return InetAddress.getByName(ip);
			} catch (IOException e) {
				e.printStackTrace();
			} 
		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return null;
	}
}
