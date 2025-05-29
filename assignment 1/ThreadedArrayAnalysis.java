import java.util.Random;

public class ThreadedArrayAnalysis {

    public static void main(String[] args) {
        int N = 100; // Dizi uzunluğu
        int M = 10;  // Alt küme (thread) sayısı

        int[] numbers = generateRandomArray(N);

        int subsetSize = N / M;
        Thread[] threads = new Thread[M];

        long startTime = System.nanoTime();

        for (int i = 0; i < M; i++) {
            int start = i * subsetSize;
            int end = (i == M - 1) ? N : (i + 1) * subsetSize;

            threads[i] = new Thread(new SubArrayAnalyzer(numbers, start, end, i));
            threads[i].start();
        }

        for (int i = 0; i < M; i++) {
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        long endTime = System.nanoTime();
        long durationMicroseconds = (endTime - startTime) / 1000;

        System.out.println("\nToplam çalışma süresi: " + durationMicroseconds + " mikro saniye");
    }

    public static int[] generateRandomArray(int size) {
        Random rand = new Random();
        int[] array = new int[size];
        for (int i = 0; i < size; i++) {
            array[i] = rand.nextInt(20) - 10; // -10 ile +9 arası değerler
        }
        return array;
    }
}

class SubArrayAnalyzer implements Runnable {
    private int[] array;
    private int start;
    private int end;
    private int threadId;

    public SubArrayAnalyzer(int[] array, int start, int end, int threadId) {
        this.array = array;
        this.start = start;
        this.end = end;
        this.threadId = threadId;
    }

    @Override
    public void run() {
        int positiveCount = 0;
        int negativeCount = 0;

        for (int i = start; i < end; i++) {
            if (array[i] > 0) {
                positiveCount++;
            } else if (array[i] < 0) {
                negativeCount++;
            }
        }

        boolean hasPositive = positiveCount > 0;
        boolean hasNegative = negativeCount > 0;

        // Düzenli ekran çıktısı
        StringBuilder sb = new StringBuilder();
        sb.append("Thread ").append(threadId).append(" → [").append(start).append(",").append(end - 1).append("]\n");
        sb.append("Pozitif: ").append(positiveCount).append(", Negatif: ").append(negativeCount).append("\n");
        sb.append("Pozitif var mı? ").append(hasPositive).append(", Negatif var mı? ").append(hasNegative).append("\n");
        sb.append("------------------------------------------\n");

        System.out.print(sb.toString());
    }
}
