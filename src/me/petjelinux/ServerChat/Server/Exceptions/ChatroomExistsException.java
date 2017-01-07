package me.petjelinux.ServerChat.Server.Exceptions;

@SuppressWarnings("serial")
public class ChatroomExistsException extends Exception {
	public ChatroomExistsException(String message) {
		super(message);
	}
}
