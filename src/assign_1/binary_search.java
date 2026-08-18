import java.util.Random;

public class binary_search {

    static class SearchResult {
        int index;
        long comparisons;

        SearchResult(int index, long comparisons) {
            this.index = index;
            this.comparisons = comparisons;
        }
    }

    // Binary search recording key comparisons
    public static SearchResult binarySearchWithComparisons(int[] arr, int target) {
        int low = 0;
        int high = arr.length - 1;
        long comparisons = 0;

        while (low <= high) {
            comparisons++;
            int mid = low + (high - low) / 2;

            if (arr[mid] == target) {
                return new SearchResult(mid, comparisons);
            } else if (arr[mid] < target) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }
        return new SearchResult(-1, comparisons);
    }

    // Binary search for pure execution time measurement
    public static int binarySearchTimed(int[] arr, int target) {
        int low = 0;
        int high = arr.length - 1;

        while (low <= high) {
            int mid = low + (high - low) / 2;

            if (arr[mid] == target) {
                return mid;
            } else if (arr[mid] < target) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }
        return -1;
    }

    public static void main(String[] args) {
        int[] sizes = {1000, 5000, 10000, 50000, 100000, 200000};
        int numTrials = 10000;
        Random rand = new Random(42);

        System.out.printf("%-8s | %-12s | %-12s | %-16s | %-16s%n",
                "N", "Avg Comp", "Worst Comp", "Avg Time (ms)", "Worst Time (ms)");
        System.out.println("-----------------------------------------------------------------------------");

        for (int n : sizes) {
            // Sorted array of even numbers
            int[] sortedArr = new int[n];
            for (int i = 0; i < n; i++) {
                sortedArr[i] = (i + 1) * 2;
            }

            // 1. Worst Case: Target is absent (odd number)
            int targetAbsent = sortedArr[n - 1] + 1;
            SearchResult worstResult = binarySearchWithComparisons(sortedArr, targetAbsent);

            // Time measurement for Worst Case (averaged over numTrials)
            long start = System.nanoTime();
            for (int t = 0; t < numTrials; t++) {
                binarySearchTimed(sortedArr, targetAbsent);
            }
            long end = System.nanoTime();
            double worstTimeMs = ((end - start) / (double) numTrials) / 1e6;

            // 2. Average Case: Random present elements
            long totalComparisons = 0;
            int[] queryKeys = new int[numTrials];
            for (int t = 0; t < numTrials; t++) {
                queryKeys[t] = sortedArr[rand.nextInt(n)];
                totalComparisons += binarySearchWithComparisons(sortedArr, queryKeys[t]).comparisons;
            }
            double avgComparisons = (double) totalComparisons / numTrials;

            // Time measurement for Average Case over numTrials
            start = System.nanoTime();
            for (int t = 0; t < numTrials; t++) {
                binarySearchTimed(sortedArr, queryKeys[t]);
            }
            end = System.nanoTime();
            double avgTimeMs = ((end - start) / (double) numTrials) / 1e6;

            System.out.printf("%-8d | %-12.2f | %-12d | %-16.6f | %-16.6f%n",
                    n, avgComparisons, worstResult.comparisons, avgTimeMs, worstTimeMs);
        }
    }
}