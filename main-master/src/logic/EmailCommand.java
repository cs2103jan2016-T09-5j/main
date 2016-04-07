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
	
	//TODO: ACCEPT COMMANDS FOR EMAIL PREFERENCE AND PARSE TO SETTINGS
	@Override
	public Signal execute() {
		return null;
	}
	
	//TODO: PAYLOAD WILL BE VALIDATED AND SETTINGS WILL BE UPDATED
	public void saveNewEmail(String userEmail, String frequency) {
		
	}
	
	//TODO: A SEPARATE THREAD WILL RUN IN THE BACKGROUND TO ACCEPT
	//EMAIL REQUESTS AND SEND PERIODIC REMINDERS
	public void mailDaemonProgram() {
		
	}

}
