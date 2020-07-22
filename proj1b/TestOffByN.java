import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class TestOffByN {
    @Test
    public void testOffByN() {
        OffByN offBy5 = new OffByN(5);
        assertTrue(offBy5.equalChars('a', 'f'));
        assertTrue(offBy5.equalChars('f', 'a'));
        assertTrue(!offBy5.equalChars('f', 'h'));
    }
}
