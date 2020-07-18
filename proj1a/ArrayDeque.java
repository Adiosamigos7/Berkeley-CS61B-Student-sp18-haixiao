import java.lang.reflect.Array;

public class ArrayDeque<T>{
    private T[] items;
    private int size;
    private int firstpos;
    private int lastpos;

    public ArrayDeque() {
        items = (T[]) new Object [8];
        size = 0;
        firstpos = 4;
        lastpos = 4;
    }

    /* Adds an item of type T to the front of th deque.*/
    public void addFirst(T item) {
        if (size == items.length - 1) {
            resize();
        }
        if (firstpos == 0) {
            firstpos = items.length - 1;
        } else {
            firstpos--;
        }
        items[firstpos] = item;
        size++;
    }
    /* Adds an item of type T to the back of the deque.*/
    public void addLast(T item) {
        if (size == items.length - 1) {
            resize();
        }
        if (lastpos == items.length - 1) {
            lastpos = 0;
        } else {
            lastpos++;
        }
        items[lastpos] = item;
        size++;
    }

    /*resize array*/
    private void resize() {

    }

    /* Returns  true if deque is empty, false otherwise.*/
    public boolean isEmpty() {
        return true;
    }

    /* Returns the number of items in the deque. */
    public int size() {
        return 1;
    }

    /*Prints the items in the deque from first to last, separated by a space.*/
    public void printDeque() {

    }

    /*Removes and returns the item at the front of the deque. If no such item exists, returns null.*/
    public T removeFirst() {
        return null;
    }

    /*Removes and returns the item at the back of the deque. If no such item exists, returns null.*/
    public T removeLast() {
        return null;
    }

    /**Gets the item at the given index, where 0 is the front, 1 is the next item, and so forth.
     * If no such item exists, returns null. Must not alter the deque!  */
    public T get(int index) {
        return null;
    }

    /*Same as get, but uses recursion.*/
    public T getRecursive(int index) {
        return null;
    }

    public static void main(String[] args) {

    }
}