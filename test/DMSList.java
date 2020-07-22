public class DMSList {
    private IntNode sentinel;
    public DMSList() {
        sentinel = new IntNode(-1000, new EndNode(0, null));
    }

    public void addFirst(int x) {
        IntNode s = new IntNode(x, null);
        s.next = sentinel.next;
        sentinel.next = s;
    }
    public class IntNode {
        public int item;
        public IntNode next;
        public IntNode(int i, IntNode h) {
            item = i;
            next = h;
        }
        public int max() {
            return Math.max(item, next.max());
        }
    }
    public class EndNode extends IntNode {
        public EndNode(int i, IntNode h) {
            super(i, h);
        }

        @Override
        public int max() {
            return item;
        }
    }

    public int max() {
        return sentinel.next.max();
    }

    public static void main(String[] args) {
        DMSList test = new DMSList();
        System.out.println(test.max());
        test.addFirst(1);
        test.addFirst(2);
        test.addFirst(3);
        test.addFirst(19);
        System.out.println(test.max());
    }
}