package com.heliomug.utils.server;

import java.io.Serializable;
import java.net.InetAddress;

public class Address implements Serializable {
	private static final long serialVersionUID = 3019317116610656760L;

	private InetAddress lanAddress;
	private InetAddress externalAddress;
	
	public Address() {
		externalAddress = NetworkUtils.getExternalAddress();
		lanAddress = NetworkUtils.getLanAddress();
	}
	
	public InetAddress getExternalAddress() {
		return externalAddress;
	}
	
	public InetAddress getLanAddress() {
		return lanAddress;
	}
	
	public InetAddress getIP() {
		if (externalAddress.equals(NetworkUtils.getExternalAddress())) {
			return lanAddress;
		} else {
			return externalAddress;
		}
	}
	
	public String toString() {
		return String.format("lan:%s, ext:%s", lanAddress, externalAddress);
	}
}
