package me.petjelinux.ServerChat.Client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import javax.net.ssl.SSLSocket;

public class InputListener implements Runnable {
	SSLSocket ss;

	public InputListener(SSLSocket ss) {
		this.ss = ss;
	}

	@Override
	public void run() {
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(ss.getOutputStream()));
			String line;
			while ((line = br.readLine()) != null) {
				bw.write(line);
				bw.flush();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
