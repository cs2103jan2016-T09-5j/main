package clockwork.storage;

import java.util.EmptyStackException;
import java.util.HashMap;
import java.util.Stack;

import clockwork.exceptions.ExceptionMessages;
import clockwork.exceptions.NotRecurringException;
import clockwork.exceptions.StateUndefinedException;
import clockwork.logic.RecurringTodoRule;
import clockwork.logic.Todo;
import clockwork.logic.UndoRedoStack;
import clockwork.parser.IDBuffer;

/**
 * Functions as an auxiliary memory that supports the main memory by providing
 * the functionality of remembering the states of Todos and RecurringTodoRules,
 * and allows the undo and redo operation to track and restore the last modified
 * object.
 * <p>
 * The VolatileMemory object should be flushed by calling the flushStacks()
 * before the main Memory is serialized and saved so that UndoRedo states that
 * are brought out of context on the next load does not persist.
 */
public class VolatileMemory {
	private Stack<Boolean> undoIsRule;
	private Stack<Boolean> redoIsRule;
	private UndoRedoStack<Todo> todoStacks;
	private UndoRedoStack<RecurringTodoRule> ruleStacks;
	private static final int STATE_STACK_MAX_SIZE = 5;

	public VolatileMemory(HashMap<Integer, Todo> allTodos, IDBuffer<Todo> idBuffer, HashMap<Integer, RecurringTodoRule> recurringRules, IDBuffer<RecurringTodoRule> recurringIdBuffer) {
		this.undoIsRule = new Stack<Boolean>();
		this.redoIsRule = new Stack<Boolean>();
		this.todoStacks = new UndoRedoStack<Todo>(allTodos, idBuffer, STATE_STACK_MAX_SIZE);
		this.ruleStacks = new UndoRedoStack<RecurringTodoRule>(recurringRules, recurringIdBuffer, STATE_STACK_MAX_SIZE);
	}
	
	/**
	 * Saves the state of a Todo.
	 * 
	 * @param todo the original Todo to be saved.
	 */
	public void save(Todo todo) {
		todoStacks.save(todo);
		undoIsRule.push(false);
		flushRedoStacks();
	}
	
	/**
	 * Saves the state of a RecurringTodoRule.
	 * 
	 * @param rule the original RecurringTodoRule to be saved.
	 */
	public void save(RecurringTodoRule rule) {
		ruleStacks.save(rule);
		undoIsRule.push(true);
		flushRedoStacks();
	}

	/**
	 * Reverts the last change depending on the object type that was modified by the last command.
	 * 
	 * @throws StateUndefinedException if there are no states to revert to.
	 */
	public void undo() throws StateUndefinedException {
		RecurringTodoRule rule;
		Todo todo;
		
		try {
			boolean isRule = undoIsRule.pop();
			redoIsRule.push(isRule);
			if(isRule) {
				ruleStacks.restoreHistoryState();
			} else {
				todo = todoStacks.restoreHistoryState();
				if(todo.isRecurring() && undoIsRule.peek()) {
					rule = ruleStacks.peekHistoryState();
					if(rule.getId() == todo.getRecurringId()) {
						undo();
					}
				}
			}
		} catch (EmptyStackException e) {
			throw new StateUndefinedException(ExceptionMessages.NO_HISTORY_STATES);
		} catch (NotRecurringException e) {
			// Ignore
		}
	}
	

	/**
	 * Reverts the last reversion depending on the object type that was modified by the reversion.
	 * 
	 * @throws StateUndefinedException if there are no states to revert to.
	 */
	public void redo() throws StateUndefinedException {
		RecurringTodoRule rule;
		Todo todo;
		
		try {
			boolean isRule = redoIsRule.pop();
			undoIsRule.push(isRule);
			if (isRule) {
				rule = ruleStacks.restoreFutureState();
				if(!redoIsRule.peek()) {
					todo = todoStacks.peekFutureState();
					if(todo.isRecurring() && rule.getId() == todo.getRecurringId()) {
						redo();
					}
				}
			} else {
				todoStacks.restoreFutureState();
			}
		} catch (EmptyStackException e) {
			throw new StateUndefinedException(ExceptionMessages.NO_FUTURE_STATES);
		} catch (NotRecurringException e) {
			// Ignore
		}
	}
	
	/**
	 * Flushes both rule and todo stacks to release the IDs reserved by the Todo or RecurringTodoRule objects in the stacks.
	 */
	public void flushStacks() {
		todoStacks.flushStacks();
		ruleStacks.flushStacks();
	}
	
	/**
	 * Flushes the redo stack to release the IDs reserved by the Todo or Recurring Todo objects in the redo stacks.
	 */
	private void flushRedoStacks() {
		todoStacks.flushRedoStack();
		ruleStacks.flushRedoStack();
		redoIsRule.clear();
	}
	
}
