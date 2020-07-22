
public class OffByOne implements CharacterComparator {
    /** Returns true if characters are equal by the rules of the implementing class. */
    @Override
    public boolean equalChars(char x, char y) {
        int diff = x - y;
        if (diff == -1 || diff == 1) {
            return true;
        }
        return false;
    }
}
