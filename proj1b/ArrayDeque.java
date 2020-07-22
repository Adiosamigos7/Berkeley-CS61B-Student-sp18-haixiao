
/** Class for Array Deque.
 * @author haixiao */

public class ArrayDeque<T> implements Deque<T> {
    /** item array. */
    private T[] items;
    /** item size variable. */
    private int size;
    /** first position in the array. */
    private int firstpos;
    /** last position in the array. */
    private int lastpos;

    /** constructor method. */
    public ArrayDeque() {
        items = (T[]) new Object [8];
        size = 0;
        firstpos = 4;
        lastpos = 4;
    }

    /** Adds an item of type T to the front of th deque.
     * @param item generic item to be added.*/
    @Override
    public void addFirst(T item) {
        if (size == items.length - 1) {
            resize(size * 2);
        }
        if (size != 0) {
            if (firstpos == 0) {
                firstpos = items.length - 1;
            } else {
                firstpos--;
            }
        }
        items[firstpos] = item;
        size++;
    }
    /** Adds an item of type T to the back of the deque.
     * @param item generic item to be added.*/
    @Override
    public void addLast(T item) {
        if (size == items.length - 1) {
            resize(size * 2);
        }
        if (size != 0) {
            if (lastpos == items.length - 1) {
                lastpos = 0;
            } else {
                lastpos++;
            }
        }
        items[lastpos] = item;
        size++;
    }

    /** resize array. @param capacity for new array capacity. */
    private void resize(int capacity) {
        T[] a = (T[]) new Object[capacity];
        if (lastpos >= firstpos) {
            System.arraycopy(items, firstpos, a, 0, size);

        } else {
            System.arraycopy(items, firstpos, a, 0, items.length - firstpos);
            System.arraycopy(items, 0, a, items.length - firstpos, lastpos + 1);
        }
        firstpos = 0;
        lastpos = size - 1;
        items = a;
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

    /** Prints the items in the deque from
     * first to last, separated by a space.*/
    @Override
    public void printDeque() {
        if (size > 0) {
            if (firstpos <= lastpos) {
                for (int i = firstpos; i <= lastpos; i++) {
                    System.out.print(items[i]);
                    if (i != lastpos) {
                        System.out.print(" ");
                    }
                }
            } else {
                for (int i = firstpos; i <= items.length - 1; i++) {
                    System.out.print(items[i]);
                    System.out.print(" ");
                }
                for (int i = 0; i <= lastpos; i++) {
                    System.out.print(items[i]);
                    if (i != lastpos) {
                        System.out.print(" ");
                    }
                }
            }
        }
    }

    /**Removes and returns the item at the front of the deque.
    * If no such item exists, returns null.*/
    @Override
    public T removeFirst() {
        if (size == 0) {
            return null;
        } else {
            T item = items[firstpos];
            items[firstpos] = null;
            if (size > 1) {
                firstpos += 1;
            }
            if (firstpos == items.length) {
                firstpos = 0;
            }
            size--;
            if (size * 4 <= items.length && items.length >= 16) {
                resize(items.length / 2);
            }
            return item;
        }
    }

    /**Removes and returns the item at the back of the deque.
     * If no such item exists, returns null.*/
    @Override
    public T removeLast() {
        if (size == 0) {
            return null;
        } else {
            T item = items[lastpos];
            items[lastpos] = null;
            if (size > 1) {
                lastpos -= 1;
            }
            if (lastpos == -1) {
                lastpos = items.length - 1;
            }
            size--;
            if (size * 4 <= items.length && items.length >= 16) {
                resize(items.length / 2);
            }
            return item;
        }
    }

    /**Gets the item at the given index, where 0 is the front,
     * 1 is the next item, and so forth.
     * If no such item exists, returns null. Must not alter the deque!
     * @param index for the desired position. */
    @Override
    public T get(int index) {
        if (size == 0 || index >= size) {
            return null;
        } else {
            return items[(index + firstpos) % items.length];
        }
    }
}
