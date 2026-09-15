class MatrixWorker extends Thread {

    private int[][] a;
    private int[][] b;
    private int[][] answer;
    private int row;

    MatrixWorker(int[][] a, int[][] b, int[][] answer, int row) {
        this.a = a;
        this.b = b;
        this.answer = answer;
        this.row = row;
    }

    public void run() {
        for (int col = 0; col < b[0].length; col++) {

            int sum = 0;

            for (int k = 0; k < b.length; k++) {
                sum = sum + a[row][k] * b[k][col];
            }

            answer[row][col] = sum;
        }
    }
}

public class MatrixMultiplication {

    public static void main(String[] args) {

        int n = 100;

        int[][] first = new int[n][n];
        int[][] second = new int[n][n];
        int[][] product = new int[n][n];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                first[i][j] = (i + j) % 10 + 1;
                second[i][j] = (i * 2 + j) % 10 + 1;
            }
        }

        MatrixWorker[] workers = new MatrixWorker[n];

        for (int i = 0; i < n; i++) {
            workers[i] =
                new MatrixWorker(first, second, product, i);

            workers[i].start();
        }

        for (int i = 0; i < n; i++) {
            try {
                workers[i].join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        System.out.println("Matrix multiplication completed.");

        System.out.println("\nFirst 5 x 5 values of result:");

        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                System.out.print(product[i][j] + "\t");
            }
            System.out.println();
        }
    }
}
