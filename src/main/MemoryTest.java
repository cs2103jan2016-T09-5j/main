package main;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class MemoryTest {

	private static final String TASK_1 = "Read book";
	private static final String TASK_2 = "Do laundry";
	private static final String TASK_3 = "Do homework";
	Memory memory;
	Todo todo1, todo2, todo3;

	@Before
	public void setUp() throws DateUndefinedException{
		memory = new Memory();
		todo1 = new Todo(memory, TASK_1);
		memory.add(todo1);
		todo2 = new Todo(memory, TASK_2);
		memory.add(todo2);
		todo3 = new Todo(memory, TASK_3);
		memory.add(todo3);
	}

	@Test
	public void testAddGet() throws NullTodoException {
		assertEquals("Todo1", todo1, memory.get(todo1.getId()));
		assertEquals("Todo2", todo2, memory.get(todo2.getId()));
		assertEquals("Todo3", todo3, memory.get(todo3.getId()));
	}

	@Test
	public void testSetterGetUndo() throws StateUndefinedException, NullTodoException {
		memory.setterGet(todo1.getId());
		Todo todo1Copy = new Todo(todo1);
		todo1.setDone(true);
		assertEquals("Todo1", todo1, memory.get(todo1.getId()));
		assertEquals("Todo2", todo2, memory.get(todo2.getId()));
		assertEquals("Todo3", todo3, memory.get(todo3.getId()));
		memory.restoreHistoryState();
		assertEquals("Todo1 Undo Mark", todo1Copy, memory.get(todo1.getId()));
	}
	
	@Test
	public void testSetterGetUndoRedo() throws StateUndefinedException, NullTodoException {
		memory.setterGet(todo1.getId());
		Todo todo1Copy = new Todo(todo1);
		todo1.setDone(true);
		Todo todo1MarkCopy = new Todo(todo1);
		assertEquals("Todo1", todo1, memory.get(todo1.getId()));
		assertEquals("Todo2", todo2, memory.get(todo2.getId()));
		assertEquals("Todo3", todo3, memory.get(todo3.getId()));
		memory.restoreHistoryState();
		assertEquals("Todo1 Undo Mark", todo1Copy, memory.get(todo1.getId()));
		memory.restoreFutureState();
		assertEquals("Todo1 Redo Mark", todo1MarkCopy, memory.get(todo1.getId()));
	}

	@Test (expected = NullTodoException.class)
	public void testRemoveUndo() throws StateUndefinedException, NullTodoException {
		memory.remove(todo2.getId());
		assertEquals("Todo1", todo1, memory.get(todo1.getId()));
		memory.get(todo2.getId()); // Exception
		assertEquals("Todo3", todo3, memory.get(todo3.getId()));
		memory.restoreHistoryState();
		assertEquals("Todo2", todo2, memory.get(todo2.getId()));
	}

	@Test
	public void testRemoveUndoRedo() throws StateUndefinedException, NullTodoException {
		memory.remove(todo2.getId());
		memory.remove(todo1.getId());
		memory.restoreHistoryState();
		assertEquals("Todo1", todo1, memory.get(todo1.getId()));
		assertEquals("Todo3", todo3, memory.get(todo3.getId()));
		memory.restoreFutureState();
		assertEquals("Todo3", todo3, memory.get(todo3.getId()));
	}
	
	@Test (expected = NullTodoException.class)
	public void testAddUndo() throws StateUndefinedException, NullTodoException {
		memory.restoreHistoryState();
		memory.get(todo3.getId()); // Exception
	}
	
	@Test
	public void testAddUndoRedo() throws StateUndefinedException, NullTodoException {
		memory.restoreHistoryState();
		assertEquals("Todo1", todo1, memory.get(todo1.getId()));
		assertEquals("Todo2", todo2, memory.get(todo2.getId()));
		memory.restoreFutureState();
		assertEquals("Todo3", todo3, memory.get(todo3.getId()));
	}

    @Test
    public void testExternalStorage() throws NullTodoException {
        String jsonString = memory.exportAsJson();
        Memory importedMemory = Memory.importFromJson(jsonString);
        Todo[] originalArray = (Todo[]) memory.getAllTodos().toArray(
                new Todo[0]);
        Todo[] importedArray = (Todo[]) importedMemory.getAllTodos().toArray(
                new Todo[0]);
        assertArrayEquals(originalArray, importedArray);
    }
}
