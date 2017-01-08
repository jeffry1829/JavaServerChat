package me.petjelinux.ServerChat.Client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import javax.net.ssl.SSLSocket;

public class SystemInputListener implements Runnable {
	SSLSocket ss;

	public SystemInputListener(SSLSocket ss) {
		this.ss = ss;
	}

	@Override
	public void run() {
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(ss.getOutputStream()));
			String line;
			while (true) {
				while ((line = br.readLine()) != null) {
					System.out.print("\033[1A\033[2K");
					bw.write(line + '\n');
					bw.flush();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
