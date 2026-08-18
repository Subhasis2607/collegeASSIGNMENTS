import java.util.Random;

public class linear_search {

    static class SearchResult {
        int index;
        long comparisons;

        SearchResult(int index, long comparisons) {
            this.index = index;
            this.comparisons = comparisons;
        }
    }

    // Linear search recording comparisons
    public static SearchResult linearSearchWithComparisons(int[] arr, int target) {
        long comparisons = 0;
        for (int i = 0; i < arr.length; i++) {
            comparisons++;
            if (arr[i] == target) {
                return new SearchResult(i, comparisons);
            }
        }
        return new SearchResult(-1, comparisons);
    }

    // Simple linear search for pure time measurement (avoiding object creation overhead)
    public static int linearSearchTimed(int[] arr, int target) {
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == target) {
                return i;
            }
        }
        return -1;
    }

    public static void main(String[] args) {
        int[] sizes = {1000, 5000, 10000, 50000, 100000, 200000};
        int numTrials = 1000;
        Random rand = new Random(42);

        System.out.printf("%-8s | %-12s | %-12s | %-16s | %-16s%n",
                "N", "Avg Comp", "Worst Comp", "Avg Time (ms)", "Worst Time (ms)");
        System.out.println("-----------------------------------------------------------------------------");

        for (int n : sizes) {
            int[] arr = new int[n];
            for (int i = 0; i < n; i++) {
                arr[i] = i + 1;
            }

            // 1. Worst Case: Target is absent (N + 1)
            int targetAbsent = n + 1;
            SearchResult worstResult = linearSearchWithComparisons(arr, targetAbsent);

            // Time measurement for Worst Case (averaged over 100 runs)
            long start = System.nanoTime();
            for (int t = 0; t < 100; t++) {
                linearSearchTimed(arr, targetAbsent);
            }
            long end = System.nanoTime();
            double worstTimeMs = ((end - start) / 100.0) / 1e6;

            // 2. Average Case: Search for random existing elements
            long totalComparisons = 0;
            int[] queryKeys = new int[numTrials];
            for (int t = 0; t < numTrials; t++) {
                queryKeys[t] = arr[rand.nextInt(n)];
                totalComparisons += linearSearchWithComparisons(arr, queryKeys[t]).comparisons;
            }
            double avgComparisons = (double) totalComparisons / numTrials;

            // Time measurement for Average Case over numTrials
            start = System.nanoTime();
            for (int t = 0; t < numTrials; t++) {
                linearSearchTimed(arr, queryKeys[t]);
            }
            end = System.nanoTime();
            double avgTimeMs = ((end - start) / (double) numTrials) / 1e6;

            System.out.printf("%-8d | %-12.2f | %-12d | %-16.6f | %-16.6f%n",
                    n, avgComparisons, worstResult.comparisons, avgTimeMs, worstTimeMs);
        }
    }
}