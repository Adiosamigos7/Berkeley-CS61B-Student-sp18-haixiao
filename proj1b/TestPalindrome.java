import org.junit.Test;
import static org.junit.Assert.*;

public class TestPalindrome {
    /* You must use this palindrome, and not instantiate
     new Palindromes, or the autograder might be upset. */
    static Palindrome palindrome = new Palindrome();

    @Test
    public void testWordToDeque() {
        Deque d = palindrome.wordToDeque("persiflage");
        String actual = "";
        for (int i = 0; i < "persiflage".length(); i++) {
            actual += d.removeFirst();
        }
        assertEquals("persiflage", actual);
    }

    /** test isPalindrome method */
    @Test
    public void testisPalindrome() {
        assertFalse(palindrome.isPalindrome("test1"));
        assertFalse(palindrome.isPalindrome("asdsaa"));
        assertTrue(palindrome.isPalindrome("aqqqqqqqa"));
        assertTrue(palindrome.isPalindrome("asddsa"));
    }

    /** test isPalindrome method */
    @Test
    public void testisPalindromeCharComp() {
        CharacterComparator cc = new OffByOne();
        assertFalse(palindrome.isPalindrome("test1", cc));
        assertFalse(palindrome.isPalindrome("adda", cc));
        assertFalse(palindrome.isPalindrome("acerb", cc));
        assertTrue(palindrome.isPalindrome("flake", cc));
        assertTrue(palindrome.isPalindrome("", cc));
        assertTrue(palindrome.isPalindrome("flke", cc));
    }
}
