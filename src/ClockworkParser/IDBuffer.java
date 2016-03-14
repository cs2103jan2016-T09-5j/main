

package ClockworkParser;

import java.util.HashMap;
import java.util.TreeSet;

/**
 * Serves as a buffer of fixed size for new Todos to draw their ID from.
 * 
 * @param <E>
 */
public class IDBuffer<E> {
	// Constants
	private static final int ID_INITIAL = 0;
	private static final int ID_BUFFER_INITIAL_SIZE = 5;
	private static final int ID_BUFFER_MAX_SIZE = 2 * ID_BUFFER_INITIAL_SIZE;

	private TreeSet<Integer> buffer;
	private int minFreeId;
	private HashMap<Integer, E> memory;

	public IDBuffer(HashMap<Integer, E> memory) {
		this.buffer = new TreeSet<Integer>();
		this.minFreeId = ID_INITIAL;
		for (int i = ID_INITIAL; i < ID_INITIAL + ID_BUFFER_INITIAL_SIZE; i++) {
			buffer.add(i);
		}
		this.memory = memory;
	}
	
	public void setMemory(HashMap<Integer, E> memory) {
		this.memory = memory; 
	}

	public int get() {
		if (buffer.size() == 1) {
			loadToSize();
		}
		int returnId = buffer.pollFirst();
		minFreeId = buffer.first();
		return returnId;
	}

	public void put(int id) {
		if (id < minFreeId) {
			minFreeId = id;
		}
		buffer.add(id);
		if (buffer.size() > ID_BUFFER_MAX_SIZE) {
			unloadToSize();
		}
	}

	private void loadToSize() {
		int minUnloadedId = minFreeId + 1;
		int i = minUnloadedId;

		while (i < minUnloadedId + ID_BUFFER_INITIAL_SIZE) {
			if (memory.containsKey(i)) { // TODO: DEPENDENCY
				minUnloadedId++;
			} else {
				buffer.add(i);
				i++;
			}
		}
	}

	private void unloadToSize() {
		for (int i = 0; i < ID_BUFFER_INITIAL_SIZE; i++) {
			buffer.pollLast();
		}
	}
}