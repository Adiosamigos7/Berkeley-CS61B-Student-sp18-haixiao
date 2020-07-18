/** Performs some basic linked list tests. */
public class ArrayDequeTest {
	
	/* Utility method for printing out empty checks. */
	public static boolean checkEmpty(boolean expected, boolean actual) {
		if (expected != actual) {
			System.out.println("isEmpty() returned " + actual + ", but expected: " + expected);
			return false;
		}
		return true;
	}

	/* Utility method for printing out empty checks. */
	public static boolean checkSize(int expected, int actual) {
		if (expected != actual) {
			System.out.println("size() returned " + actual + ", but expected: " + expected);
			return false;
		}
		return true;
	}

	/* Prints a nice message based on whether a test passed. 
	 * The \n means newline. */
	public static void printTestStatus(boolean passed) {
		if (passed) {
			System.out.println("Test passed!\n");
		} else {
			System.out.println("Test failed!\n");
		}
	}

	/** Adds a few things to the list, checking isEmpty() and size() are correct, 
	  * finally printing the results. 
	  *
	  * && is the "and" operation. */
	public static void addIsEmptySizeTest() {
		System.out.println("Running add/isEmpty/Size test.");
		System.out.println("Make sure to uncomment the lines below (and delete this print statement).");

		ArrayDeque<String> arrd = new ArrayDeque<String>();

		boolean passed = checkEmpty(true, arrd.isEmpty());

		arrd.addFirst("front");
		
		// The && operator is the same as "and" in Python.
		// It's a binary operator that returns true if both arguments true, and false otherwise.
		passed = checkSize(1, arrd.size()) && passed;
		passed = checkEmpty(false, arrd.isEmpty()) && passed;

		arrd.addLast("middle");
		passed = checkSize(2, arrd.size()) && passed;

		arrd.addLast("back");
		passed = checkSize(3, arrd.size()) && passed;

		System.out.println("Printing out deque: ");
		arrd.printDeque();

		printTestStatus(passed);

	}

	/** Adds an item, then removes an item, and ensures that dll is empty afterwards. */
	public static void addRemoveTest() {

		System.out.println("Running add/remove test.");

		System.out.println("Make sure to uncomment the lines below (and delete this print statement).");

		ArrayDeque<Integer> arrd = new ArrayDeque<Integer>();
		// should be empty 
		boolean passed = checkEmpty(true, arrd.isEmpty());

		arrd.addFirst(10);
		// should not be empty 
		passed = checkEmpty(false, arrd.isEmpty()) && passed;

		arrd.removeFirst();
		// should be empty 
		passed = checkEmpty(true, arrd.isEmpty()) && passed;

		printTestStatus(passed);

	}

	/** Adds an item, then removes an item, and ensures that dll is empty afterwards. */
	public static void selfTest() {
		ArrayDeque<Integer> arrd = new ArrayDeque<Integer>();
		arrd.addFirst(3);
		arrd.addLast(4);
		arrd.addFirst(2);
		arrd.addFirst(1);
		arrd.addFirst(0);
		arrd.addLast(5);
		arrd.addLast(6);
		arrd.addLast(7);
		arrd.removeFirst();
		arrd.removeLast();
		System.out.println("Test print");
		arrd.printDeque();
		System.out.println();
		System.out.println("Test size");
		System.out.println(arrd.size());
		System.out.println();
		System.out.println("Test get: expected 5");
		System.out.println(arrd.get(4));

	}

	/** Adds an item, then removes an item, and ensures that dll is empty afterwards. */
	public static void gradeTest() {
		ArrayDeque<Integer> ad1 = new ArrayDeque<>();
		for (int i = 0; i < 20; i++) {
			ad1.addLast(i);
		}
		System.out.println("test get: expected to get 19");
		System.out.println(ad1.get(19));
	}

	public static void main(String[] args) {
		System.out.println("Running tests.\n");
		addIsEmptySizeTest();
		addRemoveTest();
		System.out.println();
		System.out.println("Self Test");
		selfTest();
		gradeTest();
	}
} 