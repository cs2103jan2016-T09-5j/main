package testcases;

import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;

import exceptions.InvalidDateException;
import exceptions.NullTodoException;
import exceptions.StateUndefinedException;
import logic.Todo;
import storage.Memory;

//@@author A0133247L
public class MemoryTest {

	private static final String TASK_1 = "Read book";
	private static final String TASK_2 = "Do laundry";
	private static final String TASK_3 = "Do homework";
	Memory memory;
	Todo todo1, todo2, todo3;

	@Before
	public void setUp() throws InvalidDateException {
		memory = new Memory();
		todo1 = new Todo(0, TASK_1);
		memory.userAdd(todo1);
		todo2 = new Todo(1, TASK_2);
		memory.userAdd(todo2);
		todo3 = new Todo(2, TASK_3);
		memory.userAdd(todo3);
	}

	@Test
	public void testAddGet() throws NullTodoException {
		assertEquals("Todo1", todo1, memory.getTodo(todo1.getId()));
		assertEquals("Todo2", todo2, memory.getTodo(todo2.getId()));
		assertEquals("Todo3", todo3, memory.getTodo(todo3.getId()));
	}

	@Test
	public void testSetterGetUndo() throws StateUndefinedException,
			NullTodoException {
		memory.getToModifyTodo(todo1.getId());
		Todo todo1Copy = todo1.copy();
		todo1.setDone(true);
		assertEquals("Todo1", todo1, memory.getTodo(todo1.getId()));
		assertEquals("Todo2", todo2, memory.getTodo(todo2.getId()));
		assertEquals("Todo3", todo3, memory.getTodo(todo3.getId()));
		memory.undo();
		assertEquals("Todo1 Undo Mark", todo1Copy, memory.getTodo(todo1.getId()));
	}

	@Test
	public void testSetterGetUndoRedo() throws StateUndefinedException,
			NullTodoException {
		memory.getToModifyTodo(todo1.getId());
		Todo todo1Copy = todo1.copy();
		todo1.setDone(true);
		Todo todo1MarkCopy = todo1.copy();
		assertEquals("Todo1", todo1, memory.getTodo(todo1.getId()));
		assertEquals("Todo2", todo2, memory.getTodo(todo2.getId()));
		assertEquals("Todo3", todo3, memory.getTodo(todo3.getId()));
		memory.undo();
		assertEquals("Todo1 Undo Mark", todo1Copy, memory.getTodo(todo1.getId()));
		memory.redo();
		assertEquals("Todo1 Redo Mark", todo1MarkCopy,
				memory.getTodo(todo1.getId()));
	}

	@Test(expected = NullTodoException.class)
	public void testRemoveUndo() throws StateUndefinedException,
			NullTodoException {
		memory.removeTodo(todo2.getId());
		assertEquals("Todo1", todo1, memory.getTodo(todo1.getId()));
		memory.getTodo(todo2.getId()); // Exception
		assertEquals("Todo3", todo3, memory.getTodo(todo3.getId()));
		memory.undo();
		assertEquals("Todo2", todo2, memory.getTodo(todo2.getId()));
	}

	@Test
	public void testRemoveUndoRedo() throws StateUndefinedException,
			NullTodoException {
		memory.removeTodo(todo2.getId());
		memory.removeTodo(todo1.getId());
		memory.undo();
		assertEquals("Todo1", todo1, memory.getTodo(todo1.getId()));
		assertEquals("Todo3", todo3, memory.getTodo(todo3.getId()));
		memory.redo();
		assertEquals("Todo3", todo3, memory.getTodo(todo3.getId()));
	}

	@Test(expected = NullTodoException.class)
	public void testAddUndo() throws StateUndefinedException, NullTodoException {
		memory.undo();
		memory.getTodo(todo3.getId()); // Exception
	}

	@Test
	public void testAddUndoRedo() throws StateUndefinedException,
			NullTodoException {
		memory.undo();
		assertEquals("Todo1", todo1, memory.getTodo(todo1.getId()));
		assertEquals("Todo2", todo2, memory.getTodo(todo2.getId()));
		memory.redo();
		assertEquals("Todo3", todo3, memory.getTodo(todo3.getId()));
	}
	/*
	 * @Test public void testExternalStorage() throws NullTodoException { String
	 * jsonString = memory.exportAsJson(); Memory importedMemory =
	 * Memory.importFromJson(jsonString); Todo[] originalArray = (Todo[])
	 * memory.getAllTodos().toArray( new Todo[0]); Todo[] importedArray =
	 * (Todo[]) importedMemory.getAllTodos().toArray( new Todo[0]);
	 * assertArrayEquals(originalArray, importedArray); }
	 */
}
