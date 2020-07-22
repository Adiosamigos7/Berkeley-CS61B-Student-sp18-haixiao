
/** Class for Linked List Deque.
 * @author haixiao */

public class LinkedListDeque<T> implements Deque<T> {
    /** sentinel node for linkedlist. */
    private IntNode sentinel;
    /** size of the linked list. */
    private int size;

    /** IntNode class. */
    private class IntNode {
        /** item. */
        private T item;
        /** link for previous node and next node. */
        private IntNode prev, next;

        /** constructor method.
         * @param item */
        public IntNode(T item) {
            this.item = item;
            this.prev = null;
            this.next = null;
        }

        /** constructor method.*/
        public IntNode() {
            this.item = null;
            this.prev = null;
            this.next = null;
        }
    }

    /** Creates an empty linked list deque.*/
    public LinkedListDeque() {
        sentinel = new IntNode();
        sentinel.next = sentinel;
        sentinel.prev = sentinel;
    }

    /** Adds an item of type T to the front of th deque.
     * @param item */
    @Override
    public void addFirst(T item) {
        IntNode newFirst = new IntNode(item);
        sentinel.next.prev = newFirst;
        newFirst.next  = sentinel.next;
        sentinel.next = newFirst;
        newFirst.prev = sentinel;
        size++;
    }
    /** Adds an item of type T to the back of the deque.
     * @param item */
    @Override
    public void addLast(T item) {
        IntNode newLast = new IntNode(item);
        sentinel.prev.next = newLast;
        newLast.prev = sentinel.prev;
        sentinel.prev = newLast;
        newLast.next = sentinel;
        size++;
    }

    /** Returns  true if deque is empty, false otherwise.*/
    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    /** Returns the number of items in the deque. */
    @Override
    public int size() {
        return size;
    }

    /** Prints the items in the deque from first to last,
     * separated by a space.*/
    @Override
    public void printDeque() {
        IntNode ptr = sentinel.next;
        while (ptr != sentinel) {
            System.out.print(ptr.item);
            if (ptr.next != null) {
                System.out.print(" ");
            }
            ptr = ptr.next;
        }
    }

    /** Removes and returns the item at the front of the deque.
     * If no such item exists, returns null.*/
    @Override
    public T removeFirst() {
        if (isEmpty()) {
            return null;
        } else {
            IntNode ptr = sentinel.next;
            sentinel.next = ptr.next;
            ptr.next.prev = sentinel;
            size--;
            return ptr.item;
        }
    }

    /** Removes and returns the item at the back of the deque.
     * If no such item exists, returns null.*/
    @Override
    public T removeLast() {
        if (isEmpty()) {
            return null;
        } else {
            IntNode ptr = sentinel.prev;
            sentinel.prev = ptr.prev;
            ptr.prev.next = sentinel;
            size--;
            return ptr.item;
        }
    }

    /**Gets the item at the given index, where 0 is the front,
     * 1 is the next item, and so forth.
     * If no such item exists, returns null. Must not alter the deque!
     * @param index */
    @Override
    public T get(int index) {
        if (index + 1 > size() || isEmpty()) {
            return null;
        } else {
            IntNode ptr = sentinel.next;
            for (int i = 1; i < index + 1; i++) {
                ptr = ptr.next;
            }
            return ptr.item;
        }
    }

    /** Same as get, but uses recursion.
     * @param index*/
    public T getRecursive(int index) {
        if (index + 1 > size() || isEmpty()) {
            return null;
        } else {
            return getRecursivehelper(sentinel.next, index + 1).item;
        }
    }

    /** helper method for recursive get.
     * @param ptr for current IntNode reference,
     * @param index for the updated position.*/
    private IntNode getRecursivehelper(IntNode ptr, int index) {
        if (index == 1) {
            return ptr;
        } else {
            return getRecursivehelper(ptr.next, index - 1);
        }
    }
}
