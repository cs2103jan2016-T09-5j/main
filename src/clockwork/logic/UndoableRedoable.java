

package clockwork.logic;

/**
 * Allows the object to be used in UndoRedoStacks.
 *
 * @param <T> the object type that can be saved into Undo and Redo stacks
 */
public interface UndoableRedoable<T extends UndoableRedoable<T>> {
	
	public int getId();
	public T getPlaceholder();
	public boolean isPlaceholder();
	public T copy();
}
