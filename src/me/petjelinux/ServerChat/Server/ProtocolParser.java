package me.petjelinux.ServerChat.Server;

public class ProtocolParser {
	public static String[] parse(String input) {
		String[] tokens = input.split(" ");

		switch (tokens[0]) {
		case "SAY":
			/*
			 * args0 SAY 
			 * args1.. line
			 */
			if(tokens.length != 2){
				return null;
			}
			if (tokens[1] == null) {
				return null;
			}

			String line = "";
			for (int i = 1; i < tokens.length; i++) {
				line = line.concat(tokens[i]);
			}
			if (line.equals("")) {
				return null;
			}

			return new String[] { "SAY", line };
		case "VIEW":
			/*
			 * args0 VIEW 
			 * args1 which channel 
			 * optional[args2] how many lines | default=100
			 */
			if(tokens.length < 2){
				return null;
			}
			if (tokens[1] == null) {
				return null;
			}
 
			String amount = tokens.length >= 3 ? tokens[2].matches("^-?\\d+$") ? tokens[2] : "100" : "100";

			return new String[] { "VIEW", tokens[1], amount };
		case "JOIN":
			/*
			 * args0 JOIN
			 * args1 ch_name
			 */
			if(tokens.length != 2){
				return null;
			}
			if (tokens[1] == null) {
				return null;
			}
			return new String[] { "JOIN", tokens[1] };
		case "NICK":
			/*
			 * args0 NICK
			 * args1 nickname
			 */
			if(tokens.length != 2){
				return null;
			}
			if (tokens[1] == null) {
				return null;
			}
			return new String[] { "NICK", tokens[1] };
		default:
			return null;
		}
	}
}
