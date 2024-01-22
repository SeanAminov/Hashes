import java.util.Arrays;
import java.util.LinkedList;

// TODO: Auto-generated Javadoc
/**
 * The Class MyIntHash.
 */
public class MyIntHash {
	
	/**
	 * The Enum MODE.
	 */
	enum MODE {
/** The Linear. */
Linear, 
 /** The Quadratic. */
 Quadratic,  
  /** The Linked list. */
  LinkedList,  
  /** The Cuckoo. */
  Cuckoo};
	
	/** The Constant INITIAL_SIZE. */
	private final static int INITIAL_SIZE = 31;
	
	/** The max qp offset. */
	private final int MAX_QP_OFFSET = 2<<15;
	
	/** The mode of operation. */
	private MODE mode = MODE.Linear;
	
	/** The physical table size. */
	private int tableSize;
	
	/** The size of the hash - the number of elements in the hash. */
	private int size;
	
	/** The load factor. */
	private double load_factor; 
	
	/** The hash table 1. */
	private int[] hashTable1;
	
	
	// The following variables will be defined but not used until later in the project..
	/** The hash table 2. */
	private int[] hashTable2;
	
	/** The hash table LL. */
	private LinkedList<Integer>[] hashTableLL;
	
	
	/**
	 * Instantiates a new my int hash. For Part1 JUnit Testing, the load_factor will be set to 1.0
	 *
	 * @param mode the mode
	 * @param load_factor the load factor
	 */
	public MyIntHash(MODE mode, double load_factor) {
		// TODO Part1: initialize table size, size, mode, and load_factor
		//             Instantiate hashTable1 and initialize it
		this.mode = mode;
		this.load_factor = load_factor;
		tableSize = INITIAL_SIZE; // sets table size
		size = 0;
		if (mode == mode.LinkedList) { // different modes have different initializes
			hashTableLL = new LinkedList[tableSize];
			initHashTableLL(hashTableLL);
		} else if (mode == mode.Cuckoo) {
			hashTable2 = new int[tableSize];
			initHashTable(hashTable2); // need to initialize
			hashTable1 = new int[tableSize]; 
			initHashTable(hashTable1); // need to initialize
		} else {
			hashTable1 = new int[tableSize]; 
			initHashTable(hashTable1); // need to initialize
		}
		
	}
	
	public MyIntHash(MODE mode, double load_factor, int tableSize) {
		// TODO Part1: initialize table size, size, mode, and load_factor
		//             Instantiate hashTable1 and initialize it
		this.mode = mode;
		this.load_factor = load_factor;
		this.tableSize = tableSize; // sets table size
		size = 0;
		if (mode == mode.LinkedList) { // different modes have different initializes
			hashTableLL = new LinkedList[this.tableSize];
			initHashTableLL(hashTableLL);
		} else if (mode == mode.Cuckoo) {
			hashTable2 = new int[tableSize];
			initHashTable(hashTable2); // need to initialize
			hashTable1 = new int[tableSize]; 
			initHashTable(hashTable1); // need to initialize
		} else {
			hashTable1 = new int[tableSize]; 
			initHashTable(hashTable1); // need to initialize
		}
		
	}
	
	public double getCurrHashLoad() {
		if (mode == mode.Cuckoo) {
			return ((size * 1.00) / tableSize * 2); // need to do (1.00) so it does multiplication as a double
		}
		return (size * 1.0 / tableSize);
	}

	/**
	 * Initializes the provided int[] hashTable - setting all entries to -1
	 * Note that this function will be overloaded to initialize hash tables in other modes
	 * of operation. This method should also reset size to 0!
	 *
	 * @param hashTable the hash table
	 */
	private void initHashTable(int[] hashTable) {
		// TODO Part1: Write this method 
		Arrays.fill(hashTable, -1); // rewrote, for linked list, but forgot it's a different method
		size = 0;
	}
	
	private void initHashTableLL(LinkedList<Integer>[] hashTableLL) {
		// TODO Part1: Write this method 
		Arrays.fill(hashTableLL, null); // it's null instead of -1
		size = 0;
	}
	
	/**
	 * Hash fx.  This is the hash function that translates the key into the index into the hash table.
	 *
	 * @param key the key
	 * @return the int
	 */
	private int hashFx(int key) {
		// TODO Part1: Write this method.
		
		return key%tableSize; // finds the spot
	}
	
	private int hashFx2(int key) {
		return (key/tableSize) % tableSize;
	}
	
	/**
	 * Adds the key to the hash table. Note that this is a helper function that will call the 
	 * required add function based upon the operating mode. However, before calling the specific
	 * add function, determine if the hash should be resized; if so, grow the hash.
	 *
	 * @param key the key
	 * @return true, if successful
	 */
	public boolean add(int key) {
		
		
		// TODO: Part2 - if adding this key would cause the the hash load to exceed the load_factor, grow the hash.
		//      Note that you cannot just use size in the numerator... 
		//      Write the code to implement this check and call growHash() if required (no parameters)
		
		switch (mode) {
			case Linear : 
				if (size +1 > (tableSize * 1.00) * load_factor) { // need *1.00 for double values
					growHash();
				}
				return add_LP(key); 
			case Quadratic :  // same as linear
				if (size +1 > (tableSize * 1.00) * load_factor) { // need *1.00 for double values
					growHash();
				}
				return add_QP(key);
			case LinkedList:
				if (size +1 > (tableSize * 1.00) * load_factor) { // need *1.00 for double values
					growHash();
				}
				return add_LL(key);
			case Cuckoo: 
				if (size +1 > (tableSize * 2) * (load_factor * 1.00)) { // need *1.00 for double values
					growHash();
				}
				return add_CK(key);
				
			default : return add_LP(key);
		}
	}
	
	/**
	 * Contains. Note that this is a helper function that will call the 
	 * required contains function based upon the operating mode
	 *
	 * @param key the key
	 * @return true, if successful
	 */
	public boolean contains(int key) {
		switch (mode) {
			case Linear : 
				return contains_LP(key); 
			case Quadratic : 
				return contains_QP(key); // need to add
			case LinkedList:
				return contains_LL(key);
			case Cuckoo:
				return contains_CK(key);
			default : return contains_LP(key);
		}
	}
	
	/**
	 * Remove. Note that this is a helper function that will call the 
	 * required remove function based upon the operating mode
	 *
	 * @param key the key
	 * @return true, if successful
	 */
	public boolean remove(int key) {
		switch (mode) {
			case Linear : 
				return remove_LP(key); 
			case Quadratic : 
				return remove_QP(key); // need to add
			case LinkedList:
				return remove_LL(key);
			case Cuckoo: 
				return remove_CK(key);
			default : return remove_LP(key);
		}
	}
	
	/**
	 * Grow hash. Note that this is a helper function that will call the 
	 * required overloaded growHash function based upon the operating mode.
	 * It will get the new size of the table, and then grow the Hash. Linear case
	 * is provided as an example....
	 */
	private void growHash() {
	//	System.out.println("AHAHAHAH");
		int newSize = getNewTableSize(tableSize); // makes new size
		switch (mode) {
		case Linear: 
			growHash(hashTable1,newSize); 
			break;
		case Quadratic: // need to add
			growHash(hashTable1, newSize);
			break;
		case LinkedList:
			growHash(hashTableLL, newSize);
			break;
		case Cuckoo:
			growHash(hashTable1,hashTable2, newSize);
			break;
		}
	}
	
	/**
	 * Grow hash. This the specific function that will grow the hash table in Linear or 
	 * Quadratic modes. This method will:
	 * 	1. save the current hash table, 
	 *  2. create a new version of hashTable1
	 *  3. update tableSize and size
	 *  4. add all valid entries from the old hash table into the new hash table
	 * 
	 * @param table the table
	 * @param newSize the new size
	 */
	private void growHash(int[] table, int newSize) {
		// TODO Part2:  Write this method
		int[] currHashTable = table;
		hashTable1 = new int[newSize];
		tableSize = newSize;
		initHashTable(hashTable1); // have to reinitialize
		for (int i = 0; i < currHashTable.length; i++) {
			if (table[i] != -1) {
				add(table[i]);
			}
		}
		
	}
	
	private void growHash(LinkedList<Integer>[] table, int newSize) {
		// TODO Part2:  Write this method
		LinkedList[] currHashTable = table;
		hashTableLL = new LinkedList[newSize];
		tableSize = newSize;
		initHashTableLL(hashTableLL); // have to reinitialize
		for (int i = 0; i < table.length; i++) {
			if (table[i] != null) {
				for (int j = 0; j < table[i].size(); j++) {
						add((table[i].get(j)));
				}
			}
		}
		
	}
	
	private void growHash(int[] table1, int[] table2, int newSize) {
		// TODO Part2:  Write this method
		int[] currHashTable1 = table1;
		int[] currHashTable2 = table2;
		hashTable1 = new int[newSize];
		hashTable2 = new int[newSize];
		tableSize = newSize;
		initHashTable(hashTable1); // have to reinitialize
		initHashTable(hashTable2); // have to reinitialize
		for (int i = 0; i < table2.length; i++) {
			
			if (table2[i] != -1) {
				add(table2[i]);
			}
			if (table1[i] != -1) {
				add(table1[i]);
			}
		}
		
	}
	
	
	
	/**
	 * Gets the new table size. Finds the next prime number
	 * that is greater than 2x the passed in size (startSize)
	 *
	 * @param startSize the start size
	 * @return the new table size
	 */
	private int getNewTableSize(int startSize) {
		//System.out.println("here table");
		// TODO Part2: Write this method
		int primeSize = 0;
		if (mode == mode.Cuckoo) { // different mode
			primeSize = startSize +1000;
		} else {
			primeSize = startSize * 2; // need to do this to grow hash
		}
		while (!isPrime(primeSize)) {
			primeSize++;
		}
		return primeSize;
	}
	
	/**
	 * Checks if is prime.  
	 *
	 * @param size the size
	 * @return true, if is prime
	 */
	private boolean isPrime(int size) {
		// TODO Part2: Write this method
		
		for (int i = 2; i <= size/2; i++) { // start from 2 since 1 is prime
			if (size % i == 0) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Adds the key using the Linear probing strategy:
	 * 
	 * 1) Find the first empty slot sequentially, starting at the index from hashFx(key)
	 * 2) Update the hash table with the key
	 * 3) increment the size
	 * 
	 * If no empty slots are found, return false - this would indicate that the hash needs to grow...
	 *
	 * @param key the key
	 * @return true, if successful
	 */
	private boolean add_LP(int key) {
		int index = hashFx(key);
		if (hashTable1[index] == key)
			return false;
		if ((hashTable1[index] == -1 || hashTable1[index] == -2)) { // double check both -1 and -2
			hashTable1[index] = key;
			size++;
			return true;
		} else {
			int runner = index + 1;
			while (runner != index) {
				if (runner >= tableSize) {
					runner = 0;
					if (runner == index) {
						return false;
					}
				}
				if (hashTable1[index] == key) 
					return false;
				if ((hashTable1[runner] == -1 || hashTable1[runner] == -2)) {
					hashTable1[runner] = key;
					size++;
					return true;
				} else {
					runner++; // runner increment
				}
			}
			return false;
		}
	}
	
	/**
	 * Contains - uses the Linear Probing method to determine if the key exists in the hash
	 * A key condition is that there are no open spaces between any values with collisions, 
	 * independent of where they are stored.
	 * 
	 * Starting a the index from hashFx(key), sequentially search through the hash until:
	 * a) the key matches the value at the index --> return true
	 * b) there is no valid data at the current index --> return false
	 * 
	 * If no matches found after walking through the entire table, return false
	 *
	 * @param key the key
	 * @return true, if successful
	 */
	private boolean contains_LP(int key) {
		// TODO Part1: Write this method.
		int index = hashFx(key);
		if (hashTable1[index] == key) {
			return true;
		} else {
			int runner = index + 1; // similar to add but isn't adding 
			while (runner != index) {
				
				if (runner >= tableSize) {
					runner = 0;
					if (runner == index) {
						return false;
					}
				}
				if (hashTable1[runner] == -1) { // need this to exit and improve performance
					return false;
				}
				if (hashTable1[runner] == key ) {
					return true;
				} else {
					runner++;
				}
			}
			return false;
		}
		
	}
	
	/**
	 * Find target.
	 *
	 * @param key the key
	 * @return the int
	 */
	private int findTarget(int key) { // Helper method
		int target = 0;
		int index = 0;
		int i = hashFx(key);
			while (tableSize != index) {
				
				if (i >= tableSize) { // starts from 0 now
					i = 0;
			
				}
			if (key == hashTable1[i]) {
			
				target = i;
				return target;
			}
			index++;
			i++;
		}
			return -1;
	}
	
	/**
	 * Remove - uses the Linear Problem method to evict a key from the hash, if it exists
	 * A key requirement of this function is that the evicted key cannot introduce an open space
	 * if there are subsequent values which had collisions...
	 * 
	 * 1) Identify if the key exists by walking sequentially through the hash table, starting at hashFx(key) 
	 *    - if not return false,
	 * 2) from the index where the key value was found, search sequentially through the table, recording
	 *    any values that collide with hashFx(key) until either an open space if found or the full table has been processed.
	 *    If a collision was found, replace the key value with the collision value, and set the value at the collision index to an open space;
	 *    otherwise, set the key value to indicate an open space... I would recommend writing a helper method to implement this logic; it
	 *    would simply return the value to replace the key value with...
	 *
	 * @param key the key
	 * @return true, if successful
	 */
	private boolean remove_LP(int key) {
		// TODO Part2: Write this function
		int target = findTarget(key);
		if (!contains_LP(key)) {
			return false;
		}
		if (target + 1 >= hashTable1.length) { // difference between -1 and -2 when at end of table
			if (hashTable1[0] == -1) {
				hashTable1[target] = -1;
			} else {
			hashTable1[target] = -2;
			}
			size--;  // decreases size because taking out
			return true; 
		}
		if (hashTable1[target + 1] != -1) { // determines if value is -1 or -2
			hashTable1[target] = -2;
			size--;
			return true;
		} else {
			hashTable1[target] = -1;
			size--; 
			return true; 
		}
	}
	
	private boolean add_QP(int key) {
		int index = hashFx(key);
		if (hashTable1[index] == key)
			return false;
		if ((hashTable1[index] == -1 || hashTable1[index] == -2)) { // double check both -1 and -2
			hashTable1[index] = key;
			size++;
			return true;
		} else {
			int runner;
			int multiply = 1;
			while (multiply <= tableSize/2) {
				runner = (index + multiply * multiply) % tableSize; // different runner value since quadratic
				if (hashTable1[index] == key) 
					return false;
				if ((hashTable1[runner] == -1 || hashTable1[runner] == -2)) {
					hashTable1[runner] = key;
					size++;
					return true;
				} else {
					multiply++; // runner increment
				}
				if (multiply >= tableSize/2) {
					growHash(); // grows hash inside add
				}
			}
			
		}
		return false;
	}
	
	private boolean contains_QP(int key) {
		int index = hashFx(key);
		for (int i = 0; i <= tableSize/2; i++) {
			if (hashTable1[(index + i*i)% tableSize] == -1) {
				return false;
			}
			if (hashTable1[(index + i*i) %tableSize] == key) { // just looking for that spot that matches quadratic value
				return true;
			}
		}
		return false;
	}
	
	private boolean remove_QP(int key) {
		int index = hashFx(key);
		for (int i = 0; i <= tableSize/2; i++) {
			if (hashTable1[(index + i*i) %tableSize] == key) {
				index = (index + i*i) %tableSize; // changes the index
				break;
			}
		}
		hashTable1[index] = -2; // only sets to -2
		size--;
		return true;
	}
	
	private boolean add_LL(int key) {
		int index = hashFx(key);
		if(contains(key)) { // this is for duplicates
			return false;
		}
		if (hashTableLL[index] == null) {
			hashTableLL[index] = new LinkedList<Integer>();
			hashTableLL[index].add(key);
			size++;
			return true;
		} else {
			hashTableLL[index].add(key);
			size++;
			return true;
		}
	}
	
	private boolean contains_LL(int key) {
		int index = hashFx(key);
		if (hashTableLL[index] != null) { // need to insure not null before checking
			for (int i = 0; i < hashTableLL[index].size(); i++) {
				if (hashTableLL[index].get(i) == key) {
					return true;
				}
			}
		}
		return false;
	}
	
	private boolean remove_LL(int key) {
		int index = hashFx(key);
		try {
			if (hashTableLL[index].remove((Integer)key)) { //need to (Integer so it changes return
				if (hashTableLL[index].size() == 0) { // if 0 set null
					hashTableLL[index] = null;
				}
				size--;
				return true;
			}
		} catch (NullPointerException e ) {
			return false;
		}
		return false;
	}
	
	private boolean add_CK(int key) {
		int index = hashFx(key);
		int keyRemoved = key;
		boolean repeat = true;
		if (hashTable1[index] == key) // for duplicates
			return false;
		while (repeat || key != keyRemoved) {
			if ((hashTable1[index] == -1)) { // double check both -1 and -2
				hashTable1[index] = key;
				size++;
				return true;
			} 
			repeat = false;
			int key2 = hashTable1[index]; // checks both tables
			hashTable1[index] = keyRemoved;
			int index2 = hashFx2(key2); // 
			if ((hashTable2[index2] == -1)) { // double check both -1 and -2
				hashTable2[index2] = key2;
				size++;
				return true;
			}
			keyRemoved = hashTable2[index2];
			hashTable2[index2] = key2;
			index = hashFx(keyRemoved);
		}
		growHash();
		return add_CK(key);
	}
	
	private boolean contains_CK(int key) {
		int index = hashFx(key);
		if (hashTable1[index] == key) { // checks first table
			return true;
		} else {
			index = hashFx2(key); // checks second table
			return hashTable2[index] == key;
		}
		
		
		
	}
	
	private boolean remove_CK(int key) {
		int index = hashFx(key);
		if (hashTable1[index] == key) {
			hashTable1[index] = -1; // simple remove and set to -1
			size--;
			return true;
		}
		index = hashFx2(key);
		if (hashTable2[index] == key) {
			hashTable2[index] = -1; // checks both tables and sets to -1 if there
			size--;
			return true;
		}
		return false;
		
	}
		
	/**
	 * Gets the hash at. Returns the value of the hash at the specified index, and (if required by the operating mode) the specified offset.
	 * Use a switch statement to implement this code. This is FOR DEBUG AND TESTING PURPOSES ONLY
	 * 
	 * @param index the index
	 * @param offset the offset
	 * @return the hash at
	 */
	Integer getHashAt(int index, int offset) {
		// TODO Part1: as you code this project, you will add different cases. for now, complete the case for Linear Probing
		switch (mode) {
		case Linear : 
			return  hashTable1[index];
		case Quadratic: // need to add 
			return  hashTable1[index];
		case LinkedList:
			try {
				return hashTableLL[index].get(offset); // offset is the i inside in LinkedList array
			} catch (NullPointerException e) { // Check for null
				return null;
			} catch (IndexOutOfBoundsException e) { // check for out of bounds
				return -1; 
			}
		case Cuckoo:
			if (offset == 0) { // offset determines table
				return hashTable1[index];
			} else {
				return hashTable2[index];
			}
			
		
		// What needs to go here??? write this and uncomment
		
		}
		return -1;
	}
	
	/**
	 * Gets the number of elements in the Hash.
	 *
	 * @return size
	 */
	public int size() {
		// TODO Part1: Write this method
		return size;
	}

	/**
	 * resets all entries of the hash to -1. This should reuse existing code!!
	 *
	 */
	public void clear() {
		// TODO Part1: Write this method
		switch(mode) {
		case Linear:  
			initHashTable(hashTable1);
			break;
		case Quadratic:  
			initHashTable(hashTable1);
			break;
		case LinkedList:
			initHashTableLL(hashTableLL);
			break;
		case Cuckoo:
			initHashTable(hashTable1);
			initHashTable(hashTable2);
			break;
		}
	
	}

	/**
	 * Returns a boolean to indicate of the hash is empty.
	 *
	 * @return ????
	 */
	public boolean isEmpty() {
		// TODO Part1: Write this method
		
		return size == 0;
	}

	/**
	 * Gets the load factor.
	 *
	 * @return the load factor
	 */
	public double getLoad_factor() {
		return tableSize;
	}

	/**
	 * Sets the load factor.
	 *
	 * @param load_factor the new load factor
	 */
	public void setLoad_factor(double load_factor) {
		this.load_factor = load_factor;
	}

	/**
	 * Gets the table size.
	 *
	 * @return the table size
	 */
	public int getTableSize() {
		return tableSize;
	}

}
