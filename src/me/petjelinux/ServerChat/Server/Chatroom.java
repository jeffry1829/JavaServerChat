package me.petjelinux.ServerChat.Server;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Iterator;

import javax.net.ssl.SSLSocket;

import me.petjelinux.ServerChat.Server.Exceptions.ChatroomExistsException;
import me.petjelinux.ServerChat.Server.Exceptions.NoSuchChannelException;

public class Chatroom {
	public static ArrayList<Chatroom> chatroom_list = new ArrayList<Chatroom>();
	private String name = "";
	private String log = "";
	private SSLSocket[] online_sslsockets = {};

	public Chatroom(String ch_name) throws ChatroomExistsException {
		if(!isCreated(ch_name)){
			this.name = ch_name;
			chatroom_list.add(this);
		}else{
			throw new ChatroomExistsException("this chatroom is exists, you cannot re-create it");
		}
	}
	
	public String getName() {
		return name;
	}

	public String getLog(int line_amount) {
		String[] lines = log.split("\n");
		int offset = lines.length - line_amount < 0 ? 0 : lines.length - line_amount;
		String result = "";
		for (int i = offset; i < lines.length; i++) {
			result.concat(lines[i]);
		}
		return result;
	}
	
	public SSLSocket[] getSSLSockets(){
		return online_sslsockets;
	}
	
	public void messageUsers(String message) throws IOException{
		for(SSLSocket ss : online_sslsockets){
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(ss.getOutputStream()));
			bw.write(message);
			bw.flush();
		}
	}
	
	public void appendLog(String line) throws IOException{
		log.concat(line);
		messageUsers(line);
	}

	public static boolean isCreated(String channel) {
		Iterator<Chatroom> it = chatroom_list.iterator();
		while(it.hasNext()){
			if(it.next().getName() == channel){
				return true;
			}
		}
		return false;
	}
	
	public static Chatroom getChatroom(String channel) throws NoSuchChannelException{
		Iterator<Chatroom> it = chatroom_list.iterator();
		while(it.hasNext()){
			Chatroom c = it.next();
			if(c.getName() == channel){
				return c;
			}
		}
		throw new NoSuchChannelException("NoSuchChannel!");
	}
}
