package me.petjelinux.ServerChat.Client;

import java.io.FileInputStream;
import java.security.KeyStore;
import java.security.SecureRandom;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

public class CMD {
	/*
	 * args0 cert pw
	 * args1 host
	 * args2 port
	 */
	public static void main(String args[]) throws Exception {
		KeyStore ks = KeyStore.getInstance("JKS");
		ks.load(new FileInputStream("./server.cer"), args[0].toCharArray());

		TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
		tmf.init(ks);

		SSLContext sc = SSLContext.getInstance("TLS");
		sc.init(null, tmf.getTrustManagers(), SecureRandom.getInstanceStrong());

		SSLSocketFactory sf = sc.getSocketFactory();
		SSLSocket ss = (SSLSocket) sf.createSocket(args[1], Integer.parseInt(args[2]));
		
		(new Thread(new SocketInputListener(ss))).start();
		(new Thread(new SystemInputListener(ss))).start();
	}
}
