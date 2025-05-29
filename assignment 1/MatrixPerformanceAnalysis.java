import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class MatrixPerformanceAnalysis {

    public static void main(String[] args) {
        StringBuilder csv = new StringBuilder();
        csv.append("MatrisBoyutu,CalismaSuresi_us\n");

        // N değerlerini 10'dan 1000'e kadar 50 adımlarla arttırarak test et
        for (int N = 10; N <= 1000; N += 50) {
            int[][] matrix = generateRandomMatrix(N);
            Thread[] threads = new Thread[N];

            long startTime = System.nanoTime();

            for (int i = 0; i < N; i++) {
                threads[i] = new Thread(new MatrixRowAnalyzer(matrix[i]));
                threads[i].start();
            }

            for (int i = 0; i < N; i++) {
                try {
                    threads[i].join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            long endTime = System.nanoTime();
            long durationMicroseconds = (endTime - startTime) / 1000;

            System.out.println("N = " + N + " → Süre: " + durationMicroseconds + " µs");
            csv.append(N).append(",").append(durationMicroseconds).append("\n");
        }

        try {
            FileWriter writer = new FileWriter("performance_b.csv");
            writer.write(csv.toString());
            writer.close();
            System.out.println("\nCSV dosyası oluşturuldu: performance_b.csv");
        } catch (IOException e) {
            System.out.println("CSV yazma hatası: " + e.getMessage());
        }
    }

    public static int[][] generateRandomMatrix(int size) {
        Random rand = new Random();
        int[][] matrix = new int[size][size];
        for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++)
                matrix[i][j] = rand.nextInt(10) + 1; // 1–10 arasında
        return matrix;
    }
}

class MatrixRowAnalyzer implements Runnable {
    private int[] row;

    public MatrixRowAnalyzer(int[] row) {
        this.row = row;
    }

    @Override
    public void run() {
        int positive = 0;
        int negative = 0;

        for (int val : row) {
            if (val > 0) positive++;
            else if (val < 0) negative++;
        }
    }
}
