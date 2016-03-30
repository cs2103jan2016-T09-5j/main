package logic;

import parser.ParsedInput;
import storage.Memory;

/**
 * The EmailCommand class handles all user commands with "email" as the first
 * keyword and processes parsed input to detect when users want to be notified
 * of relevant tasks that are impending. User just have to give their email,
 * which adjusts the internal settings file, and they may choose to unsubscribe at anytime.
 */

public class EmailCommand extends Command {

	/**
	 * Creates an EmailCommand object.
	 * 
	 * @param input
	 *            the ParsedInput object containing the parameters (saved user email).
	 * @param memory
	 *            the memory containing the Todos to which the changes impending
	 *            notifications can be detected.
	 */
	public EmailCommand(ParsedInput input, Memory memory) {
		super(input, memory);
	}
	
	@Override
	public Signal execute() {
		// TODO Auto-generated method stub
		return null;
	}

}
