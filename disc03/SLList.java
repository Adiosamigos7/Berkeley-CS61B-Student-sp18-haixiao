public class SLList {
 private class IntNode {
   public int item;
   public IntNode next;
   public IntNode(int item, IntNode next) {
     this.item = item;
     this.next = next;
   }
 }

 private IntNode first;

 //constructor method
 public SLList() {

 }

 public void addFirst(int x) {
   first = new IntNode(x, first);
 }

 public void insert(int item, int position) {
     if (position == 0 || first == null) {
         addFirst(item);
     } else {
         int i = 1;
         IntNode ptr1 = this.first;
         while (i < position && ptr1.next != null) {
             ptr1 = ptr1.next;
             i += 1;
         }
         IntNode ins = new IntNode(item, ptr1.next);
         ptr1.next = ins;
     }

 }

 //reverse the elements - iterative method
 public void reverse() {
     SLList temp = new SLList();
     IntNode ptr = first;
     while (ptr != null) {
         temp.addFirst(ptr.item);
         ptr = ptr.next;
     }
     first = temp.first;
 }

 //reverse the elements - recursive method
 public void ireverse() {
    first = recursivehelper(first);
 }

 //helper method for recursive reverse
 public IntNode recursivehelper(IntNode front) {
     if (front == null || front.next == null) {
         return front;
     } else {
        IntNode newFirst = recursivehelper(front.next);
        front.next.next = front;
        front.next = null;
        return newFirst;

     }
 }
 // Print to test
 public void print() {
   IntNode ptr = first;
   while (ptr != null) {
     System.out.println(ptr.item);
     ptr = ptr.next;
   }
 }


 public static void main(String[] args) {
    SLList L = new SLList();
    L.addFirst(1);
    L.addFirst(2);
    L.addFirst(3);
    L.addFirst(4);
//    L.insert(10, 0);
    L.ireverse();
    L.print();
 }
}
