

package ClockworkLogic;

import java.util.ArrayList;

import org.joda.time.DateTime;

import ClockworkExceptions.InvalidParamException;
import ClockworkExceptions.NotRecurringException;
import ClockworkExceptions.NullRuleException;
import ClockworkExceptions.NullTodoException;
import ClockworkParser.ParsedInput;
import ClockworkStorage.Memory;

/**
 * Houses a method which processes the edit request from the user.
 */
public class EditCommand extends Command {

	/**
	 * Creates an EditCommand object.
	 * 
	 * @param input the ParsedInput object containing the parameters.
	 * @param memory the memory containing the Todos to which the changes should
	 *            be committed.
	 */
	public EditCommand(ParsedInput input, Memory memory) {
		super(input, memory);
	}

	/**
	 * Processes a ParsedInput object containing the edit command and its
	 * accompanying parameters and commits those changes to the memory.
	 * 
	 * Reverts the Todo to its original state if the changes are invalid.
	 * 
	 * @return a Signal object with a message denoting success or failure in
	 *         processing.
	 */
	@Override
	public Signal execute() {
		try {
			int id;
			boolean containsNewName = false;
			boolean hasRuleFlag = false;
			String title = new String(); // Stub initialization
			
			// Check if first param has any text appended to it intended as Todo name
			String[] firstKeywordParams = keyParamPairs.get(0).getParam().trim().split("\\s", 2);
			if (firstKeywordParams.length > 1) {
				// Try to parse first sub-param as int. If fail send invalidParams Signal.
				id = Integer.parseInt(firstKeywordParams[0].trim());
				title = firstKeywordParams[1];
				containsNewName = true;
			} else {
				// Check if input contains only 1 keyword (keyParamPairs.size() == 1)
				if (input.containsOnlyCommand()) {
					return new Signal(Signal.EDIT_INVALID_PARAMS, false);
				}
				id = Integer.parseInt(keyParamPairs.get(0).getParam().trim());
			}

			// Check for presence of -r flag
			hasRuleFlag = input.containsFlag(Keywords.RULE);
			
			if (input.isRecurring() || hasRuleFlag) {
				Todo stubTodo = memory.getTodo(id).copy();
				
				// Parameter loading and validation
				DateTime startTime = stubTodo.getStartTime();
				DateTime endTime = stubTodo.getEndTime();
				ArrayList<DateTime> newDateTimes = new ArrayList<DateTime>();
                boolean startTimeEdited = false;
				
				// Date checks
				if(!dateTimes.isEmpty()) { // Keywords will always exist if dateTimes 

                    if (dateTimes.size() == 2) {
                        startTime = dateTimes.remove(0);
                        endTime = dateTimes.remove(0);
                    } else {
                        for (int i = 1; i < keyParamPairs.size(); i++) {
                            Keywords keyword = keyParamPairs.get(i)
                                    .getKeyword();

                            switch (keyword) {
                                case FROM :
                                    startTime = dateTimes.remove(0);
                                    startTimeEdited = true;
                                    break;
                                case BY :
                                case ON :
                                case AT :
                                    if (!dateTimes.isEmpty()) {
                                        // Prevent overwriting of edit by FROM
                                        // keyword
                                        if (!startTimeEdited) {
                                            startTime = null;
                                        }
                                        endTime = dateTimes.remove(0);
                                    }
                                    break;
                                case TO :
                                    if (!dateTimes.isEmpty()) {
                                        endTime = dateTimes.remove(0);
                                    }
                                    break;
                                case RULE :
                                case UNTIL :
                                case EVERY :
                                    // Ignore
                                    break;
                                default :
                                    return new Signal(
                                            Signal.EDIT_INVALID_PARAMS, false);
                            }
                        }
                    }
					
					if(startTime != null && endTime != null) {
						if(startTime.isAfter(endTime)) {
							return new Signal(Signal.EDIT_END_BEFORE_START, false);
						}
						newDateTimes.add(startTime);
						newDateTimes.add(endTime);
					} else if(startTime != null) {
						newDateTimes.add(startTime);
					} else if(endTime != null) {
						newDateTimes.add(endTime);
					}
				}
				
				// Limit checks
				if(input.hasLimit()) {
					if(input.getLimit().isBeforeNow()) {
						return new Signal(Signal.EDIT_LIMIT_BEFORE_NOW, false);
					}
				}
				
				// End parameter loading and validation
				
				// Commit edited fields
				RecurringTodoRule rule = memory.getToModifyRule(memory.getTodo(id).getRecurringId());
				RecurringTodoRule ruleOld = rule.copy();
				
				// If input contains new title
				if(containsNewName) {
					rule.setOriginalName(title);
				}
				
				// If input has a limit
				if (input.hasLimit()) {
					rule.setRecurrenceLimit(input.getLimit());
				}
				
				// If input has a period
				if (input.hasPeriod()) {
					rule.setRecurringInterval(input.getPeriod());
				}
				
				if(!newDateTimes.isEmpty()) {
					rule.setDateTimes(newDateTimes);
				}
				
				// End commit
				
				memory.saveToFile();
				return new Signal(String.format(Signal.EDIT_RULE_SUCCESS_FORMAT, ruleOld, rule), true);	
			} else {
				
				// Parameter loading and validation
				Todo stubTodo = memory.getTodo(id).copy();
				DateTime startTime = stubTodo.getStartTime();
				DateTime endTime = stubTodo.getEndTime();
                boolean startTimeEdited = false;
				
				if(!dateTimes.isEmpty()) {
                    if (dateTimes.size() == 2) {
                        startTime = dateTimes.remove(0);
                        endTime = dateTimes.remove(0);
                    } else {
                        for (int i = 1; i < keyParamPairs.size(); i++) {
                            Keywords keyword = keyParamPairs.get(i)
                                    .getKeyword();

                            switch (keyword) {
                                case FROM :
                                    startTime = dateTimes.remove(0);
                                    startTimeEdited = true;
                                    break;
                                case BY :
                                case ON :
                                case AT :
                                    if (!dateTimes.isEmpty()) {
                                        // Prevent overwriting of edit by FROM
                                        // keyword
                                        if (!startTimeEdited) {
                                            startTime = null;
                                        }
                                        endTime = dateTimes.remove(0);
                                    }
                                    break;
                                case TO :
                                    if (!dateTimes.isEmpty()) {
                                        endTime = dateTimes.remove(0);
                                    }
                                    break;
                                default :
                                    return new Signal(
                                            Signal.EDIT_INVALID_PARAMS, false);
                            }
                        }
                    }

					if(startTime != null && endTime != null) {
						if(startTime.isAfter(endTime)) {
							return new Signal(Signal.EDIT_END_BEFORE_START, false);
						}
					}
				}
				
				// Commit edited fields
				Todo todo = memory.getToModifyTodo(id);
				Todo oldTodo = todo.copy();
				
				// If input contains new title
				if(containsNewName) {
					memory.updateMaps(id, title, todo.getName());
					todo.setName(title);
				}
				
				memory.updateMaps(id, startTime, todo.getStartTime());
				memory.updateMaps(id, endTime, todo.getEndTime());
				todo.setStartTime(startTime);
				todo.setEndTime(endTime);
				todo.updateType();

				memory.saveToFile();
				return new Signal(String.format(Signal.EDIT_SUCCESS_FORMAT, oldTodo, todo), true);
			}
		} catch (NullTodoException e) {
			return new Signal(e.getMessage(), false);
		} catch (NumberFormatException e) {
			return new Signal(Signal.EDIT_INVALID_PARAMS, false);
		} catch (NullRuleException e) {
			return new Signal(Signal.EDIT_NO_LONGER_RECURS, false);
		} catch (NotRecurringException e) {
			return new Signal(e.getMessage(), false);
        } catch (InvalidParamException e) {
            return new Signal(Signal.EDIT_INVALID_PARAMS, false);
        }
	}

}
