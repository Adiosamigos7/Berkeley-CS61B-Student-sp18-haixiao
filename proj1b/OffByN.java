
public class OffByN implements CharacterComparator {
    int n;
    public OffByN(int n) {
        this.n = n;
    }
    /** Returns true if characters are equal by the rules of the implementing class. */
    @Override
    public boolean equalChars(char x, char y) {
        int diff = x - y;
        if (diff == -n || diff == n) {
            return true;
        }
        return false;
    }
}
