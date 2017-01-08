package me.petjelinux.ServerChat.Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.net.ssl.SSLSocket;

public class SocketInputListener implements Runnable {
	SSLSocket ss;

	public SocketInputListener(SSLSocket ss) {
		this.ss = ss;
	}

	@Override
	public void run() {
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(ss.getInputStream()));
			String line;
			while (true) {
				while ((line = br.readLine()) != null) {
					System.out.println(line);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
