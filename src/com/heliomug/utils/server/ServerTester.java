package com.heliomug.utils.server;

import java.io.IOException;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.function.Consumer;

public class ServerTester {
	public static void main(String[] args) throws InterruptedException {
		Server<Letter> server = new Server<>(new Letter("hey"), 8189);
		server.start();
		try {
			Client<Letter> client = new Client<>(InetAddress.getLocalHost(), 8189);
			client.start();
			for (int i = 0 ; i < 10 ; i++) {
				System.out.println(String.format("%d: %s", i, client.getThing()));
				client.sendCommand(new LetterMessageChange("step " + i));
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					break;
				}
			}
			
			client.close();

		} catch (UnknownHostException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		server.close();
		System.out.println("a");
		Thread.sleep(1000);
		System.out.println("b");
		server = new Server<>(new Letter("hey"), 8189);
		System.out.println(server);
		System.out.println("c");
	}
	
	private static class LetterMessageChange implements Consumer<Letter>, Serializable {
		private static final long serialVersionUID = 4050547975919808447L;

		String message;
		
		public LetterMessageChange(String s) {
			message = s;
		}
		
		public void accept(Letter letter) {
			letter.setMessage(message);
		}
	}
	
	@SuppressWarnings("serial")
	private static class Letter implements Serializable {
		String message;
		
		public Letter(String message) {
			this.message = message;
		}
		
		public void setMessage(String s) {
			message = s;
		}
		
		public String toString() {
			return message;
		}
	}
}
