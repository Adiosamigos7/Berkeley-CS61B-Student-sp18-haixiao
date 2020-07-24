import static org.junit.Assert.*;
import org.junit.Test;


public class TestArrayDequeGold {

    @Test
    public void testStudentArray() {
        StudentArrayDeque<Integer> studentDeque = new StudentArrayDeque<Integer>();
        ArrayDequeSolution<Integer> solutionDeque = new ArrayDequeSolution<Integer>();
        String message = "";
        for (int i = 0; i < 1000; i += 1) {
            int randomNum = StdRandom.uniform(1001);
            if (randomNum % 4 == 0) {
                studentDeque.addFirst(i);
                solutionDeque.addFirst(i);
                message += "addFirst(" + i + ")\n";
            } else if (randomNum % 4 == 1) {
                studentDeque.addLast(i);
                solutionDeque.addLast(i);
                message += "addLast(" + i + ")\n";
            } else if (randomNum % 4 == 2 && !studentDeque.isEmpty() && !solutionDeque.isEmpty()) {
                message += "removeFirst()";
                assertEquals(message,
                        solutionDeque.removeFirst(), studentDeque.removeFirst());
                message += "\n";
            } else if (randomNum % 4 == 3 && !studentDeque.isEmpty() && !solutionDeque.isEmpty()) {
                message += "removeLast()";
                assertEquals(message,
                        solutionDeque.removeLast(), studentDeque.removeLast());
                message += "\n";
            }
        }
    }
}
