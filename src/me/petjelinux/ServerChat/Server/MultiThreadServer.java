package me.petjelinux.ServerChat.Server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.SocketException;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.util.HashMap;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;

import me.petjelinux.ServerChat.Server.Exceptions.ChatroomExistsException;
import me.petjelinux.ServerChat.Server.Exceptions.NoSuchChannelException;

public class MultiThreadServer implements Runnable {
	SSLSocket ss;
	public static Chatroom lobby;

	public static HashMap<SSLSocket, Chatroom> ss_current_channels = new HashMap<SSLSocket, Chatroom>();
	public static HashMap<SSLSocket, String> ss_nick = new HashMap<SSLSocket, String>();

	public MultiThreadServer(SSLSocket ss) {
		this.ss = ss;
	}

	/*
	 * args0 = jks file path => depreciated use file path "./server.cer" instead
	 * args0 = listen port 
	 * args1 = kspw
	 */
	public static void main(String args[]) throws Exception {
		lobby = new Chatroom("lobby");

		KeyStore ks = KeyStore.getInstance("JKS");
		ks.load(new FileInputStream("./server.cer"), args[1].toCharArray());

		KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
		kmf.init(ks, args[1].toCharArray());

		SSLContext sc = SSLContext.getInstance("TLS");
		sc.init(kmf.getKeyManagers(), null, SecureRandom.getInstanceStrong());

		SSLServerSocketFactory sssf = sc.getServerSocketFactory();
		SSLServerSocket sss = (SSLServerSocket) sssf.createServerSocket(Integer.parseInt(args[0]));

		while (true) {
			SSLSocket ss = (SSLSocket) sss.accept();
			System.out.println("Connected!");
			System.out.println(ss.hashCode());
			(new Thread(new MultiThreadServer(ss))).start();
		}
	}

	@Override
	public void run() {
		try {
			if (ss_nick.get(ss) == null) {
				ss_nick.put(ss, "unset");
			}
			if (ss_current_channels.get(ss) == null) {
				ss_current_channels.put(ss, lobby);
				lobby.UserJoin(ss);
			}
			
			BufferedReader br = new BufferedReader(new InputStreamReader(ss.getInputStream()));
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(ss.getOutputStream()));
			String in;
			while (true) {
				while ((in = br.readLine()) != null) {
					String[] command = ProtocolParser.parse(in);
					if (command == null) {
						continue;
					}
					
					switch (command[0]) {
					case "SAY":
						if (ss_current_channels.get(ss) == null) {
							bw.write("you have to join a channel first\n");
							bw.flush();
						} else {
							ss_current_channels.get(ss).appendLog(ss_nick.get(ss) + "> " + command[1]);
						}
						break;
					case "JOIN": // direct continue to VIEW
						try {
							ss_current_channels.get(ss).UserLeave(ss);
							
							ss_current_channels.put(ss, Chatroom.getChatroom(command[1])); // thrown at here
							Chatroom.getChatroom(command[1]).UserJoin(ss);
						} catch (NoSuchChannelException e) { //Create one
							try {
								new Chatroom(command[1]).UserJoin(ss);
								ss_current_channels.put(ss, Chatroom.getChatroom(command[1]));
							} catch (ChatroomExistsException e1) {
							} // not possible
						}
						
						// change array to fulfill the condition "VIEW" wants
						command = new String[]{ "VIEW", command[1], "100" };
					case "VIEW":
						String logs = Chatroom.getChatroom(command[1]).getLog(Integer.parseInt(command[2]));
						bw.write(logs + '\n');
						bw.flush();
						break;
					case "NICK":
						ss_current_channels.get(ss).appendLog(
								"Now " + ss_nick.get(ss) + " has changed his(her) nickname" + " to " + command[1]);
						ss_nick.put(ss, command[1]);
						break;
					default:
						break;
					}
				}
			}
		} catch (SocketException se){
			try {
				ss_current_channels.get(ss).UserLeave(ss);
				ss_nick.remove(ss);
				ss_current_channels.remove(ss);
				ss.close();
				Thread.currentThread().interrupt();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (NoSuchChannelException e) {
			try {
				BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(ss.getOutputStream()));
				bw.write("NoSuchChannel! Try JOIN channel!\n");
				bw.flush();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}
}
