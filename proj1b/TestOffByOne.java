import org.junit.Test;
import static org.junit.Assert.*;

public class TestOffByOne {

    static CharacterComparator offByOne = new OffByOne();

    @Test
    public void testOffByOne() {
        assertTrue(offByOne.equalChars('a', 'b'));
        assertTrue(offByOne.equalChars('b', 'a'));
        assertTrue(offByOne.equalChars('（', '）'));
        assertFalse(offByOne.equalChars('a', 'a'));
        assertFalse(offByOne.equalChars('a', 'e'));
        assertTrue(offByOne.equalChars('@', 'A'));
        assertFalse(offByOne.equalChars('b', 'A'));
        assertTrue(offByOne.equalChars('B', 'A'));
        assertFalse(offByOne.equalChars('n', 'A'));
        assertFalse(offByOne.equalChars('1', '5'));
        assertTrue(offByOne.equalChars('1', '2'));
        assertTrue(offByOne.equalChars('/', '0'));
        assertTrue(offByOne.equalChars('`', 'a'));
        assertTrue(offByOne.equalChars('z', '{'));
    }
}
