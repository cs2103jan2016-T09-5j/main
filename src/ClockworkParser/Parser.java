package ClockworkParser;

import java.util.ArrayList;
import java.util.Date;
import java.util.EnumSet;
import java.util.List;
import java.util.TimeZone;

import org.joda.time.DateTime;
import org.joda.time.Period;

import com.joestelmach.natty.DateGroup;

import ClockworkExceptions.ExceptionMessages;
import ClockworkExceptions.InvalidDateException;
import ClockworkExceptions.InvalidPeriodException;
import ClockworkExceptions.InvalidRecurringException;
import ClockworkExceptions.InvalidTodoNameException;
import ClockworkExceptions.ParsingFailureException;

public class Parser {
	private static final String STRING_MARCH = "march ";
	private static final String STRING_TO = "to";
	private static final String INIT_STRING = "today";
	private static final String STRING_DAY = "day";
	private static final String STRING_WEEK = "week";
	private static final String STRING_MONTH = "month";
	private static final String STRING_YEAR = "year";
	private static final char CHAR_SPACE = ' ';
	private static final String REGEX_SPACE = "\\s";
	private static final String STRING_MON = "mon";
	private static final String STRING_TUE = "tue";
	private static final String STRING_WED = "wed";
	private static final String STRING_THU = "thu";
	private static final String STRING_FRI = "fri";
	private static final String STRING_SAT = "sat";
	private static final String STRING_SUN = "sun";
	private static final String STRING_MONDAY = "monday";
	private static final String STRING_TUESDAY = "tuesday";
	private static final String STRING_WEDNESDAY = "wednesday";
	private static final String STRING_THURSDAY = "thursday";
	private static final String STRING_FRIDAY = "friday";
	private static final String STRING_SATURDAY = "saturday";
	private static final String STRING_SUNDAY = "sunday";
	private static final String EMPTY_STRING = "";

	/**
	 * Tries to parse the specified String into ParsedInput object for various
	 * commands to work on. If the recurring todo which user is trying to add
	 * does not include any time frame or deadline, throws an
	 * InvalidRecurringException If the todo which user is trying to add has
	 * flag keywords as parameters, throws an InvalidTodoNameException.
	 * 
	 * @param input
	 *            the String read from the user.
	 * @return a ParsedInput object containing the command type,
	 *         keyword-parameter pairs and dates identified.
	 * @throws InvalidRecurringException
	 * @throws InvalidTodoNameException
	 * @throws ParsingFailureException
	 */
	public static ParsedInput parseInput(String input)
			throws InvalidRecurringException, InvalidTodoNameException,
			ParsingFailureException {
		boolean hasLimit = false;
		boolean isRecurring = false;
		Period period = new Period();
		ArrayList<String> words = tokenize(input);
		Keywords cType = getCommandType(words);

		// if command type is error
		if (cType == Keywords.ERROR) {
			return ParsedInput.getPlaceholder();
		}

		ArrayList<Integer> dateIndexes = new ArrayList<Integer>();
		DateTime limit = new DateTime(0);
		List<DateTime> dateTimes = new ArrayList<DateTime>();
		ParsedInput returnInput;
		ArrayList<KeyParamPair> keyParamPairs = extractParam(words);

		if (cType == Keywords.ADD) {

			// ignores thefirst pair as it is assumed to be the name of the todo
			for (int i = 1; i < keyParamPairs.size(); i++) {
				KeyParamPair currentPair = keyParamPairs.get(i);
				Keywords key = currentPair.getKeyword();

				if (InputStringKeyword.isFlag(currentPair.getKeyString())) {
					throw new InvalidTodoNameException();
				}
				// assumes that 'every _ until _' is at the end of user input
				if (key == Keywords.UNTIL) { // check if there is a recurring
												// limit parsed in
					if (isRecurring) {
						List<DateTime> parsedLimits = interpretAsDate(
								keyParamPairs, currentPair, true);

						if (!parsedLimits.isEmpty()) { // if parsing is
														// successful
							limit = parsedLimits.get(0);
							hasLimit = true;
						}
					} else {
						interpretAsName(keyParamPairs, currentPair);
					}
				} else if (key == Keywords.EVERY) {
					// tries to detect if there is a period in user input

					// tries to parse param as period
					period = interpretAsPeriod(period, keyParamPairs,
							currentPair);
					if (!period.equals(new Period())) { // if param is valid
														// period
						isRecurring = true;
						if (period.equals(new Period().withDays(1))
								&& dateTimes.isEmpty()) { // period = every day
							dateTimes
									.add(new DateTime().withTime(23, 59, 0, 0));
						}
					}
					// tries to parse param as date to extract the date
					// if (dateTimes.isEmpty()) {
					List<DateTime> parsedDate = interpretAsDate(keyParamPairs,
							currentPair, false);
					if (!parsedDate.isEmpty()) {
						if (!isRecurring) {
							period = period.withYears(1);
							isRecurring = true;
						}
						addToDateTimes(parsedDate, dateTimes, keyParamPairs,
								dateIndexes, i);
						// }
					} else if (!isRecurring) {
						interpretAsName(keyParamPairs, currentPair);
					}

				} else {
					// tries to parse param as date
					List<DateTime> parsedDates = interpretAsDate(keyParamPairs,
							currentPair, true);

					if (!parsedDates.isEmpty()) { // if parsing is successful
						addToDateTimes(parsedDates, dateTimes, keyParamPairs,
								dateIndexes, i);
					}

				}
			}
			for (int i = keyParamPairs.size() - 1; i > 0; i--) {
				keyParamPairs.remove(i);
			}

			// check parameters for recurring todos
			if (isRecurring) {
				if (!isValidRecurring(dateTimes)) {
					throw new InvalidRecurringException();
				}
			}

		} else if (cType == Keywords.EDIT) {
			// Post-process EDIT command parameter

			// ignores the first pair as it is assumed to be the name of the
			// todo
			for (int i = 1; i < keyParamPairs.size(); i++) {
				KeyParamPair currentPair = keyParamPairs.get(i);
				Keywords key = currentPair.getKeyword();

				if (InputStringKeyword.isFlag(currentPair.getKeyString())
						&& !InputStringKeyword.isRule(currentPair
								.getKeyString())) {
					throw new InvalidTodoNameException();
				}

				// assumes that 'every _ until _' is at the end of user input
				// check if there is a recurring limit parsed
				// in
				if (key == Keywords.UNTIL) {
					List<DateTime> parsedLimits = interpretAsDate(
							keyParamPairs, currentPair, true);

					if (!parsedLimits.isEmpty()) { // if parsing is
													// successful
						limit = parsedLimits.get(0);
						hasLimit = true;
						isRecurring = true;
					} else {
						interpretAsName(keyParamPairs, currentPair);
					}
				} else if (key == Keywords.EVERY) {
					// tries to detect if there is a period in user input

					// tries to parse param as period
					period = interpretAsPeriod(period, keyParamPairs,
							currentPair);
					if (!period.equals(new Period())) { // if param is valid
														// period
						isRecurring = true;
						if (period.equals(new Period().withDays(1))
								&& dateTimes.isEmpty()) { // period = every day
							dateTimes
									.add(new DateTime().withTime(23, 59, 0, 0));
						}
					}
					// tries to parse param as date to extract the date
					// if (dateTimes.isEmpty()) {
					List<DateTime> parsedDate = interpretAsDate(keyParamPairs,
							currentPair, false);
					if (!parsedDate.isEmpty()) {
						if (!isRecurring) {
							period = period.withYears(1);
							isRecurring = true;
						}
						addToDateTimes(parsedDate, dateTimes, keyParamPairs,
								dateIndexes, i);
					} else if (!isRecurring) {
						interpretAsName(keyParamPairs, currentPair);
					}

				} else if (key == Keywords.RULE) {
					// appends the param for rule as the name
					interpretAsName(keyParamPairs, currentPair);
					keyParamPairs.get(i).setParam(EMPTY_STRING);
				} else {
					// tries to parse param as date
					List<DateTime> parsedDates = interpretAsDate(keyParamPairs,
							currentPair, true);

					if (!parsedDates.isEmpty()) { // if parsing is successful
						addToDateTimes(parsedDates, dateTimes, keyParamPairs,
								dateIndexes, i);
					}

				}
			}
			int toIndex;
			for (int i = 0; i < keyParamPairs.size(); i++) {
				KeyParamPair currentPair = keyParamPairs.get(i);

				String currentParam = currentPair.getParam();
				if ((toIndex = currentParam.indexOf(STRING_TO)) != -1) {
					String toString = currentParam.substring(toIndex,
							currentParam.length());
					if (toString.length() != 2) {
						String afterTo = toString.substring(3,
								toString.length());
						currentPair.setParam(currentParam.substring(0,
								toIndex - 1));
						keyParamPairs.add(new KeyParamPair(Keywords.TO,
								STRING_TO, afterTo));
						if (i == 0) {
							List<DateTime> parsedDates = interpretAsDate(
									keyParamPairs,
									keyParamPairs.get(keyParamPairs.size() - 1),
									true);
							dateTimes.addAll(parsedDates);
						}
					}
				}

			}
		} else if (cType == Keywords.SEARCH) {
			// Post-process SEARCH command parameters
			for (KeyParamPair keyParamPair : keyParamPairs) {
				Keywords key = keyParamPair.getKeyword();
				if (!(key == Keywords.NAME || key == Keywords.SEARCH)) {
					if (key == Keywords.YEAR) {
						keyParamPair.setParam(STRING_MARCH.concat(keyParamPair
								.getParam()));
					}
					List<DateTime> parsedDates = interpretAsDate(keyParamPairs,
							keyParamPair, false);
					if (!parsedDates.isEmpty()) {
						dateTimes.add(parsedDates.get(0));
					}
				}
			}
		}

		returnInput = new ParsedInput(cType, keyParamPairs, dateTimes, period,
				isRecurring, hasLimit, limit);
		return returnInput;
	}

	private static void interpretAsName(ArrayList<KeyParamPair> keyParamPairs,
			KeyParamPair currentPair) {
		String newName;
		int currentIndex = keyParamPairs.indexOf(currentPair);

		newName = appendParameters(keyParamPairs, 0, currentIndex);

		keyParamPairs.get(0).setParam(newName);
	}

	/**
	 * This method tries to parse parameters at given index as a period. If
	 * parsing is unsuccessful, method appends the parameters along with its
	 * keyword at the back of the parameters at the nameIndex
	 * 
	 * @param period
	 * @param keyParamPairs
	 * @param currentPair
	 * @return period stated in parameters if parsing is successful, original
	 *         period in parameters if unsuccessful.
	 * 
	 */
	private static Period interpretAsPeriod(Period period,
			ArrayList<KeyParamPair> keyParamPairs, KeyParamPair currentPair) {
		try {
			// tries to parse as period
			period = retrieveRecurringPeriod(currentPair.getParam()
					.toLowerCase());

		} catch (InvalidPeriodException e) { // no valid period

		}
		return period;
	}

	/**
	 * This method tries to parse the parameters at the given index as a date.
	 * If parsing as date is not successful and addToName is true, method
	 * interprets the parameters as part of the name. When append is false,
	 * method simply ignores the parameters.
	 * 
	 * @param keyParamPairs
	 * @param currentPair
	 * @param addToName
	 * @return list of datetimes parsed. If parsing is unsuccessful, returns
	 *         empty list.
	 */
	private static List<DateTime> interpretAsDate(
			ArrayList<KeyParamPair> keyParamPairs, KeyParamPair currentPair,
			boolean addToName) {
		List<DateTime> parsedDate = new ArrayList<DateTime>();
		try {
			parsedDate = parseDates(combineKeyPair(currentPair.getKeyString(),
					currentPair.getParam()));
		} catch (InvalidDateException e) { // no valid date given
			if (addToName) {
				interpretAsName(keyParamPairs, currentPair);
			}
		}
		return parsedDate;
	}

	private static String combineKeyPair(String keyString, String paramString) {
		StringBuilder sBuilder = new StringBuilder(keyString);
		sBuilder.append(CHAR_SPACE);
		sBuilder.append(paramString);
		return sBuilder.toString();
	}

	/**
	 * Check if given dateTimes has enough elements for todo to be a valid
	 * recurring todo
	 * 
	 * @param dateTimes
	 * @return isValidRecurring
	 */
	private static boolean isValidRecurring(List<DateTime> dateTimes) {
		return dateTimes.size() > 0;
	}

	/**
	 * Adds parsedDate to dateTimes depending on number of elements in
	 * dateTimes. If dateTimes already contains some elements, method tries to
	 * combine the parameters and re-parse them as dates to check if resulting
	 * dateTime is different
	 * 
	 * @param parsedDate
	 * @param dateTimes
	 * @param keyParamPairs
	 * @param dateIndexes
	 * @param currentIndex
	 * @throws ParsingFailureException
	 */
	private static void addToDateTimes(List<DateTime> parsedDate,
			List<DateTime> dateTimes, ArrayList<KeyParamPair> keyParamPairs,
			ArrayList<Integer> dateIndexes, int currentIndex)
			throws ParsingFailureException {

		if (dateTimes.size() > 0) {
			int appendedPairIndex = dateIndexes.get(0);
			List<DateTime> newDateTimes = new ArrayList<DateTime>();

			assert (dateTimes.size() < 3); // There should be at the most 2
											// dates only
			assert (dateIndexes.size() == 1); // There should be at the most 1
												// date param

			String newDateParam = appendParameters(keyParamPairs,
					appendedPairIndex, currentIndex);
			try {
				newDateTimes = parseDates(combineKeyPair(
						keyParamPairs.get(appendedPairIndex).getKeyString(),
						newDateParam));

			} catch (InvalidDateException e) {
				// should never enter this catch block as old parameters
				// were parse-able as dates
			}

			if (!newDateTimes.isEmpty() && newDateTimes.equals(dateTimes)) {

				// natty could not parse in the first order, try appending the
				// other way
				appendedPairIndex = currentIndex;
				currentIndex = dateIndexes.get(0);
				newDateParam = appendParameters(keyParamPairs,
						appendedPairIndex, currentIndex);
				try {
					newDateTimes = parseDates(combineKeyPair(
							keyParamPairs.get(appendedPairIndex).getKeyString(),
							newDateParam));

					// new dateTime parsed from 'param y param x' is same as
					// 'param y'
					if (newDateTimes.get(0).toLocalDate()
							.equals(parsedDate.get(0).toLocalDate())) {
						if (newDateTimes.get(0).getHourOfDay() == parsedDate
								.get(0).getHourOfDay()
								&& newDateTimes.get(0).getMinuteOfHour() == parsedDate
										.get(0).getMinuteOfHour()) {
							if (newDateTimes.size() == 2) {
								if (newDateTimes.get(1).getHourOfDay() == parsedDate
										.get(1).getHourOfDay()
										&& newDateTimes.get(1)
												.getMinuteOfHour() == parsedDate
												.get(1).getMinuteOfHour()) {
									throw new ParsingFailureException();
								}
							} else {
								throw new ParsingFailureException();
							}

						}
					}
				} catch (InvalidDateException e) {
					// should never enter this catch block as old date
					// parameters were parse-able
				}
			}

			assert (!newDateTimes.isEmpty()); // shouldn't be empty because
												// parameters should be
												// parse-able
			dateTimes.clear(); // removes all elements in dateTimes
			dateTimes.addAll(newDateTimes);
			keyParamPairs.get(appendedPairIndex).setParam(newDateParam);
			dateIndexes.remove(0);
			dateIndexes.add(appendedPairIndex);
		} else {
			dateTimes.addAll(parsedDate);
			dateIndexes.add(currentIndex);
		}
	}

	/**
	 * Appends the keyword and parameter of the second keyParamPair to the
	 * parameters of the first keyParamPair.
	 * 
	 * @param keyParamPairs
	 * @param indexOfFirstPair
	 * @param indexOfSecondPair
	 * @return appended string
	 */
	private static String appendParameters(
			ArrayList<KeyParamPair> keyParamPairs, int indexOfFirstPair,
			int indexOfSecondPair) {
		KeyParamPair firstPair = keyParamPairs.get(indexOfFirstPair);
		KeyParamPair secondPair = keyParamPairs.get(indexOfSecondPair);
		String key = secondPair.getKeyString();

		StringBuilder sBuilder = new StringBuilder(firstPair.getParam());
	
		if (sBuilder.length() != 0){
			if (!secondPair.getKeyword().equals(Keywords.RULE)) {
				sBuilder.append(CHAR_SPACE);
				sBuilder.append(key);
			}
		} else {
			sBuilder.append(key);
			
		} 
		if(!secondPair.getParam().isEmpty()) {
			sBuilder.append(CHAR_SPACE);
			sBuilder.append(secondPair.getParam());
		}
		
		return sBuilder.toString();
	}

	/**
	 * Retrieves period given string. Throws an InvalidPeriodException if
	 * parameter given is not a valid period.
	 * 
	 * @param param
	 * @return period for recurrence
	 * @throws InvalidPeriodException
	 */
	private static Period retrieveRecurringPeriod(String param)
			throws InvalidPeriodException {
		param.toLowerCase();
		Period period = new Period();
		switch (param) {
			case STRING_YEAR:
				return period.withYears(1);
			case STRING_MONTH:
				return period.withMonths(1);
			case STRING_WEEK:
				return period.withWeeks(1);
			case STRING_DAY:
				return period.withDays(1);
			case STRING_MON:
				return period.withWeeks(1);
			case STRING_TUE:
				return period.withWeeks(1);
			case STRING_WED:
				return period.withWeeks(1);
			case STRING_THU:
				return period.withWeeks(1);
			case STRING_FRI:
				return period.withWeeks(1);
			case STRING_SAT:
				return period.withWeeks(1);
			case STRING_SUN:
				return period.withWeeks(1);
			case STRING_MONDAY:
				return period.withWeeks(1);
			case STRING_TUESDAY:
				return period.withWeeks(1);
			case STRING_WEDNESDAY:
				return period.withWeeks(1);
			case STRING_THURSDAY:
				return period.withWeeks(1);
			case STRING_FRIDAY:
				return period.withWeeks(1);
			case STRING_SATURDAY:
				return period.withWeeks(1);
			case STRING_SUNDAY:
				return period.withWeeks(1);
			default:
				throw new InvalidPeriodException();
		}
	}

	/**
	 * Takes in a user input string and puts individual words into elements in a
	 * String ArrayList.
	 *
	 * @param input
	 *            the String read from the user.
	 * @return an ArrayList of words from the input String.
	 */
	private static ArrayList<String> tokenize(String input) {
		input = input.trim();
		String[] inputWords = input.split(REGEX_SPACE);
		ArrayList<String> words = new ArrayList<String>();
		for (int i = 0; i < inputWords.length; i++) {
			words.add(inputWords[i]);
		}
		return words;
	}

	/**
	 * Parses the tokenized input, wordList for keywords and their associated
	 * parameters, stores them in KeyParamPair objects and adds all KeyParamPair
	 * objects to an ArrayList which is returned. ASSUMPTION: The first word in
	 * user input is a keyword.
	 * 
	 * @param words
	 *            the ArrayList of words from the input String.
	 * @return an ArrayList of KeyParamPair objects.
	 */
	private static ArrayList<KeyParamPair> extractParam(ArrayList<String> words) {
		String key = words.get(0);
		ArrayList<KeyParamPair> keyParamPairs = new ArrayList<KeyParamPair>();
		Keywords keyword;
		EnumSet<Keywords> keywordOccurrence = EnumSet.noneOf(Keywords.class);
		StringBuilder paramBuilder = new StringBuilder();
		for (int i = 1; i < words.size(); i++) {
			String currentParam = words.get(i);

			// wordList.get(i) is a keyword. Create a KeyParamPair with previous
			// param
			// and paramStringBuilder and add to ArrayList. Update key and
			// paramBuilder.
			// If currentParam is a keyword:
			if (InputStringKeyword.isKeyword(currentParam)) {
				keyword = InputStringKeyword.getKeyword(key);
				// Ignore and append keyword if it has occurred before
				if (!keywordOccurrence.contains(keyword)) {
					keywordOccurrence.add(keyword);
					keyParamPairs.add(new KeyParamPair(keyword, key,
							paramBuilder.toString()));
					key = currentParam;
					paramBuilder = new StringBuilder();
				} else { // wordList.get(i) is a repeated keyword; append to
							// paramString
					buildParam(paramBuilder, currentParam);

				}
			} else { // wordList.get(i) is not a keyword; append to paramString
				buildParam(paramBuilder, currentParam);
			}
		}
		// last KeyParamPair to be added to ArrayList
		keyword = InputStringKeyword.getKeyword(key);
		keyParamPairs.add(new KeyParamPair(keyword, key, paramBuilder
				.toString()));
		return keyParamPairs;
	}

	private static void buildParam(StringBuilder paramBuilder,
			String currentParam) {
		if (paramBuilder.length() != 0) {
			paramBuilder.append(CHAR_SPACE);
		}
		paramBuilder.append(currentParam);
	}

	/**
	 * This operation gets the type of command of the user input.
	 * 
	 * ASSUMPTION: the first word in user input is the command type keyword.
	 * 
	 * @param words
	 *            the ArrayList of words from the input String.
	 * @return an ArrayList of KeyParamPair objects.
	 */
	private static Keywords getCommandType(ArrayList<String> words) {
		String typeString = words.get(0);
		return determineCommandType(typeString);
	}

	/**
	 * This operation checks if type string corresponds to the listed command
	 * types.
	 * 
	 * @param typeString
	 *            String specifying the type of command.
	 * @return KEYWORDS specifying the type, null if typeString does not contain
	 *         command.
	 */
	private static Keywords determineCommandType(String typeString) {
		Keywords type = Keywords.ERROR;

		if (InputStringKeyword.isCommand(typeString)) {
			type = InputStringKeyword.getCommand(typeString);
		}
		return type;
	}

	/**
	 * Parses a String with multiple dates provided to the DateParser, and
	 * returns a DateTime ArrayList.
	 * 
	 * @param dateString
	 *            String containing the date to be parsed
	 * @return A list of all immutable DateTime objects representing dates
	 *         processed in the string.
	 * @throws InvalidDateException
	 *             if dateString does not contain a valid date, is empty, or
	 *             null
	 * @throws InterruptedException
	 */
	public static List<DateTime> parseDates(String dateString)
			throws InvalidDateException {
		List<DateTime> dateTimes = new ArrayList<DateTime>();
		com.joestelmach.natty.Parser parser = new com.joestelmach.natty.Parser(
				TimeZone.getDefault());

		DateGroup parsedDate = null;
		DateGroup dateNow = null;
		DateTime currentDateTime = null;
		try {
			parsedDate = parser.parse(dateString).get(0);
			Thread.sleep(1);
			currentDateTime = new DateTime();
			Thread.sleep(1);
			// Parse the date again to detect dateString type
			dateNow = parser.parse(dateString).get(0);
		} catch (IndexOutOfBoundsException e) {
			throw new InvalidDateException(
					ExceptionMessages.DATE_UNDEFINED_EXCEPTION);
		} catch (InterruptedException e) {

		}

		List<Date> dates = parsedDate.getDates();
		List<Date> secondDates = dateNow.getDates();
		for (int i = 0; i < dates.size(); i++) {
			Date date = dates.get(i);
			DateTime dateTime = new DateTime(date);
			DateTime secondDateTime = new DateTime(secondDates.get(i));
			// date does not include a time
			if (dateTime.toLocalTime().isBefore(currentDateTime.toLocalTime())
					&& currentDateTime.toLocalTime().isBefore(
							secondDateTime.toLocalTime())) {
				// sets the default time to be 2359h
				dateTime = dateTime.withTime(23, 59, 0, 0);
			}
			dateTimes.add(dateTime);
		}
		return dateTimes;
	}

	/**
	 * Initializes the natty parser by test parsing a string to reduce execution
	 * time for future parsing
	 */
	public static void initialize() {
		com.joestelmach.natty.Parser parser = new com.joestelmach.natty.Parser(
				TimeZone.getDefault());
		parser.parse(INIT_STRING);
	}
}
