public class LinkedListDeque<T>{
    private IntNode sentinel;
    private int size;

    private class IntNode {
        public T item;
        public IntNode prev, next;

        public IntNode(T item) {
            this.item = item;
            this.prev = null;
            this.next = null;
        }

        public IntNode() {
            this.item = null;
            this.prev = null;
            this.next = null;
        }
    }
    /*Creates an empty linked list deque.*/
    public LinkedListDeque() {
        sentinel = new IntNode();
        sentinel.next = sentinel;
        sentinel.prev = sentinel;
    }

    /*constructor method.*/
    public LinkedListDeque(T item) {
        sentinel = new IntNode(item);
        if (item != null) {
            IntNode first = new IntNode(item);
            sentinel.next = first;
            sentinel.prev = first;
            first.prev = sentinel;
            first.next = sentinel;
            size = 1;
        } else {
            sentinel.next = sentinel;
            sentinel.prev = sentinel;
            size = 0;
        }
    }

    /* Adds an item of type T to the front of th deque.*/
    public void addFirst(T item) {
        IntNode newFirst = new IntNode(item);
        sentinel.next.prev = newFirst;
        newFirst.next  = sentinel.next;
        sentinel.next = newFirst;
        newFirst.prev = sentinel;
        size++;
    }
    /* Adds an item of type T to the back of the deque.*/
    public void addLast(T item) {
        IntNode newLast = new IntNode(item);
        sentinel.prev.next = newLast;
        newLast.prev = sentinel.prev;
        sentinel.prev = newLast;
        newLast.next = sentinel;
        size++;
    }

    /* Returns  true if deque is empty, false otherwise.*/
    public boolean isEmpty() {
        if (size == 0) {
            return true;
        }
        return false;
    }

    /* Returns the number of items in the deque. */
    public int size() {
        return size;
    }

    /*Prints the items in the deque from first to last, separated by a space.*/
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

    /*Removes and returns the item at the front of the deque. If no such item exists, returns null.*/
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

    /*Removes and returns the item at the back of the deque. If no such item exists, returns null.*/
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

    /**Gets the item at the given index, where 0 is the front, 1 is the next item, and so forth.
     * If no such item exists, returns null. Must not alter the deque!  */
    public T get(int index) {
        if (index > size() || isEmpty()) {
            return null;
        } else {
            IntNode ptr = sentinel.next;
            for (int i = 1; i < index; i++) {
                ptr = ptr.next;
            }
            return ptr.item;
        }
    }

    /*Same as get, but uses recursion.*/
    public T getRecursive(int index) {
        if (index > size() || isEmpty()) {
            return null;
        } else {
            return getRecursivehelper(sentinel.next, index).item;
        }
    }

    /* helper method for recursive get */
    private IntNode getRecursivehelper(IntNode ptr, int index) {
        if (index == 1) {
            return ptr;
        } else {
            return getRecursivehelper(ptr.next, index - 1);
        }
    }


    public static void main(String[] args) {

    }
}