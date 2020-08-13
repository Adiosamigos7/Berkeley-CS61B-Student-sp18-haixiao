package lab9;

import java.security.Key;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.Stack;

/**
 * Implementation of interface Map61B with BST as core data structure.
 *
 * @author Your name here
 */
public class BSTMap<K extends Comparable<K>, V> implements Map61B<K, V> {

    private V deletedValue;
    private K deletedKey;

    private class Node {
        /* (K, V) pair stored in this Node. */
        private K key;
        private V value;

        /* Children of this Node. */
        private Node left;
        private Node right;

        private Node(K k, V v) {
            key = k;
            value = v;
        }
    }

    private Node root;  /* Root node of the tree. */
    private int size; /* The number of key-value pairs in the tree */

    /* Creates an empty BSTMap. */
    public BSTMap() {
        this.clear();
    }

    /* Removes all of the mappings from this map. */
    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    /** Returns the value mapped to by KEY in the subtree rooted in P.
     *  or null if this map contains no mapping for the key.
     */
    private V getHelper(K key, Node p) {
        if (p == null) {
            return null;
        } else if (key.compareTo(p.key) == 0) {
            return p.value;
        } else if (key.compareTo(p.key) < 0) {
            return getHelper(key, p.left);
        } else {
            return getHelper(key, p.right);
        }
    }

    /** Returns the value to which the specified key is mapped, or null if this
     *  map contains no mapping for the key.
     */
    @Override
    public V get(K key) {
        return getHelper(key, root);
    }

    /** Returns a BSTMap rooted in p with (KEY, VALUE) added as a key-value mapping.
      * Or if p is null, it returns a one node BSTMap containing (KEY, VALUE).
     */
    private Node putHelper(K key, V value, Node p) {
        if (p == null) {
            size += 1;
            return new Node(key, value);
        } else if (key.compareTo(p.key) < 0) {
            p.left = putHelper(key, value, p.left);
        } else if (key.compareTo(p.key) > 0) {
            p.right = putHelper(key, value, p.right);
        } else {
            p.value = value;
            return p;
        }
        return p;
    }

    /** Inserts the key KEY
     *  If it is already present, updates value to be VALUE.
     */
    @Override
    public void put(K key, V value) {
        root = putHelper(key, value, root);
    }

    /* Returns the number of key-value mappings in this map. */
    @Override
    public int size() {
        return size;
    }

    //////////////// EVERYTHING BELOW THIS LINE IS OPTIONAL ////////////////

    /* Returns a Set view of the keys contained in this map. */
    @Override
    public Set<K> keySet() {
        Set<K> keys = new HashSet<K>();
        for (K key : this) {
            keys.add(key);
        }
        return keys;
    }


    /** Removes KEY from the tree if present
     *  returns VALUE removed,
     *  null on failed removal.
     */
    @Override
    public V remove (K key) {
        deletedKey = key;
        root = removeHelper(root, key);
        V val = deletedValue;
        deletedValue = null;
        deletedKey = null;
        return val;
    }

    private Node removeHelper(Node current, K key) {
        if (current == null) {
            return current;
        }
        if (key.compareTo(current.key) < 0) {
            current.left = removeHelper(current.left, key);
        } else if (key.compareTo(current.key) > 0) {
            current.right = removeHelper(current.right, key);
        } else {
            if (key == deletedKey) {
                deletedValue = current.value;
            }

            if (current.left == null) {
                return current.right;
            } else if (current.right == null) {
                return current.left;
            }
            current.key = findNextMinimum(current.right).key;
            current.value = findNextMinimum(current.right).value;
            current.right = removeHelper(current.right, current.key);
        }

        return current;
    }

    private Node findNextMinimum (Node current) {
        while (current.left != null) {
            current = current.left;
        }
        return current;
    }

    /** Removes the key-value entry for the specified key only if it is
     *  currently mapped to the specified value.  Returns the VALUE removed,
     *  null on failed removal.
     **/
    @Override
    public V remove(K key, V value) {
        if (get(key) == value) {
            return remove(key);
        }
        return null;
    }

    @Override
    public Iterator<K> iterator() {
        return new KeysIterator<K>(root);
    }

    class KeysIterator<K> implements Iterator<K> {
        private Stack<Node> nodes;

        public KeysIterator(Node root) {
            nodes = new Stack<Node>();
            while (root != null) {
                nodes.push(root);
                root = root.left;
            }
        }

        public boolean hasNext() {
            return !nodes.isEmpty();
        }

        public K next() {
            Node current = nodes.pop();
            K key = (K) current.key;
            if (current.right != null) {
                current = current.right;
                while (current != null) {
                    nodes.push(current);
                    current = current.left;
                }
            }
            return key;
        }
    }

}
