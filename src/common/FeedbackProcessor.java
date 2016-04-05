package common;

//this processes the feedback upon user input by giving the feedback type string to pass into GUI object
public class FeedbackProcessor {
	public static String processFeedback(String message) {
		if (message.contains("added")){
			return "ADD";
		} else if (message.contains("deleted")) {
			return "DELETE";
		} else if (message.contains("marked")) {
			return "MARK";
		} else if (message.contains("Redo operation")) {
			return "REDO";
		} else if (message.contains("deadline you're attempting to add")) {
			return "CLASH";
		} else {
			return message;
		}
	}

}
