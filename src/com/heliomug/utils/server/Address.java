package com.heliomug.utils.server;

import java.io.Serializable;
import java.net.InetAddress;
import java.util.Objects;

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
	
	@Override
	public int hashCode() {
		return Objects.hash(lanAddress, externalAddress);
	}
	
	@Override
	public boolean equals(Object other) {
		if (other == null) {
			return false;
		}
		if (other == this) {
			return true;
		}
		if (other instanceof Address) {
			Address oa = (Address) other;
			return oa.lanAddress.equals(lanAddress) && oa.externalAddress.equals(externalAddress);
		}
		return false;
	}
	
	public String toString() {
		return String.format("lan:%s, ext:%s", lanAddress, externalAddress);
	}
}
