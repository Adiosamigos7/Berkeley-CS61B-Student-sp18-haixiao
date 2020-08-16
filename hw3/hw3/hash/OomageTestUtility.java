package hw3.hash;

import java.util.List;

public class OomageTestUtility {
    public static boolean haveNiceHashCodeSpread(List<Oomage> oomages, int M) {
        int[] buckets = new int[M];
        int N = oomages.size();
        for (int i = 0; i < N; i++) {
            int hash = oomages.get(i).hashCode();
            int bucketNum = (hash & 0x7FFFFFFF) % M;
            buckets[bucketNum] += 1;
        }
        for(int bucket : buckets) {
            if ((double) bucket < (double) N / 50 || (double) bucket > (double) N / 2.5) {
                return false;
            }
        }
        return true;
    }
}
