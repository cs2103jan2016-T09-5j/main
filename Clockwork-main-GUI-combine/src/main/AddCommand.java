package main;

/**
 * The AddCommand class handles all user commands with "add" as the first
 * keyword and processes ParsedInput to generate Todo objects and adds them into
 * memory. *
*/

public class AddCommand extends Command {

	public AddCommand(ParsedInput input, Memory memory) {
		super(input, memory);
	}

	/**
	 * It takes in a ParsedInput object and generates a Todo object with respect
	 * to the ParsedInput object. The Todo object can be a floating task,
	 * deadline or event.
	 * 
	 * It returns a Signal object to indicate success or failure (if exception
	 * is thrown).
	 * 
	 * @param input
	 *            A ParsedInput object that contains a KEYWORDS type and an
	 *            ArrayList<KeyParamPair>
	 * @param memory
	 *            A Memory object that stores Todo objects
	 * @return It returns a Signal object to indicate success or failure.
	 */
	@Override
	public Signal execute() {
		// Check for empty string params
		if (input.containsEmptyParams()) {
            return new Signal(Signal.GENERIC_EMPTY_PARAM, false);
		}

		// Check for valid number of keywords
		int numberOfKeywords = input.getParamPairList().size();
		if (numberOfKeywords > 3) {
            return new Signal(Signal.ADD_INVALID_PARAMS, false);
		}
		
		String todoName = keyParamPairList.get(0).getParam();

		try {

			int numberOfElements = keyParamPairList.size();

			switch (numberOfElements) {
			// Floating task
			// Example:
			// KeyParamPair 0: add <task>
				case 1:
					Todo floatingTask = new Todo(memory, todoName);
					memory.add(floatingTask);
					return new Signal(String.format(
                            Signal.ADD_SUCCESS_SIGNAL_FORMAT, floatingTask),
                            true);

					// Deadline
					// Example:
					// KeyParamPair 0: add <task>, KeyParamPair 1: by <date>

					// Event
					// Example:
					// KeyParamPair 0: add <event>, KeyParamPair 1: from
					// <startDate to endDate>

				case 2:
				// TODO Todo can now determine if it is a Single date or Double
				// date String and dynamically adjust its type accordingly.
					
				// Use a while loop, keep catching DateUndefinedException and
				// append the keyword to the todoName so that we can avoid
				// catching keywords in title. Catch until the Todo is
				// successfully created.
					
					String secondKeyword = keyParamPairList.get(1).getKeyword();

					// Deadline
					if (secondKeyword.equals("by")
							|| secondKeyword.equals("on")
							|| secondKeyword.equals("at")) {
						String deadlineTime = keyParamPairList.get(1).getParam();
						Todo deadline = new Todo(memory, todoName, deadlineTime);
							memory.add(deadline);
							return new Signal(String.format(
	                                Signal.ADD_SUCCESS_SIGNAL_FORMAT, deadline),
	                                true);
					}			
	

					// Event
					else if (secondKeyword.equals("from")) {
						String eventStartEndTime = keyParamPairList.get(1).getParam();
						Todo event = new Todo(memory, todoName, eventStartEndTime);
						if (event.isValid()) {
							memory.add(event);
							return new Signal(
									String.format(Signal.ADD_SUCCESS_SIGNAL_FORMAT, event), true);
							// Start time is after end time error.
						} else {
							return new Signal(Signal.ADD_END_BEFORE_START_ERROR, false);
						}

					}
			}

		} catch (DateUndefinedException e) {
			e.printStackTrace();
			String exceptionMessage = e.getMessage();
			return new Signal(String.format(Signal.GENERIC_DATE_UNDEFINED_FORMAT,
 exceptionMessage),
                    false);
		}

        return new Signal(Signal.ADD_UNKNOWN_ERROR, false);
	}
}
