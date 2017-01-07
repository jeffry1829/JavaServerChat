package me.petjelinux.ServerChat.Server.Exceptions;

@SuppressWarnings("serial")
public class NoSuchChannelException extends Exception{
	public NoSuchChannelException(String message) {
		super(message);
	}
}
