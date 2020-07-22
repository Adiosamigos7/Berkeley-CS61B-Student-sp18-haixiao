public class Palindrome {
    public Deque<Character> wordToDeque(String word) {
        LinkedListDeque<Character> l = new LinkedListDeque<Character>();
        for (int i = 0; i < word.length(); i++) {
            l.addLast(word.charAt(i));
        }
        return l;
    }

    public boolean isPalindrome(String word) {
        if (word == null) {
            return false;
        }
        Deque<Character> l;
        l = wordToDeque(word);
        int size = l.size();
        for (int i = 0; i < size / 2; i++) {
            if (l.removeFirst() != l.removeLast()) {
                return false;
            }
        }
        return true;
    }

    /** Overload method of isPalindrome using CharacterComparator */
    public boolean isPalindrome(String word, CharacterComparator cc) {
        if (word == null) {
            return false;
        }
        Deque<Character> l;
        l = wordToDeque(word);
        int size = l.size();
        for (int i = 0; i < size / 2; i++) {
            if (!cc.equalChars(l.removeFirst(), l.removeLast())) {
                return false;
            }
        }
        return true;
    }
}
