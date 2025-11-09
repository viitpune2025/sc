import java.util.Scanner;

public class FuzzyOperations {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // Input size of matrices
        System.out.print("Enter number of rows in R: ");
        int rRows = sc.nextInt();
        System.out.print("Enter number of columns in R (and rows in S): ");
        int rCols = sc.nextInt();
        System.out.print("Enter number of columns in S: ");
        int sCols = sc.nextInt();

        double[][] R = new double[rRows][rCols];
        double[][] S = new double[rCols][sCols];
        double[][] T = new double[rRows][sCols];

        // Input R matrix
        System.out.println("Enter elements of matrix R:");
        for (int i = 0; i < rRows; i++) {
            for (int j = 0; j < rCols; j++) {
                R[i][j] = sc.nextDouble();
            }
        }

        // Input S matrix
        System.out.println("Enter elements of matrix S:");
        for (int i = 0; i < rCols; i++) {
            for (int j = 0; j < sCols; j++) {
                S[i][j] = sc.nextDouble();
            }
        }

        // Min-Max Composition
        for (int i = 0; i < rRows; i++) {
            for (int j = 0; j < sCols; j++) {
                double maxVal = 0;
                for (int k = 0; k < rCols; k++) {
                    double minVal = Math.min(R[i][k], S[k][j]);
                    if (minVal > maxVal)
                        maxVal = minVal;
                }
                T[i][j] = maxVal;
            }
        }

        // Display Result
        System.out.println("Resultant matrix T (Min-Max Composition):");
        for (int i = 0; i < rRows; i++) {
            for (int j = 0; j < sCols; j++) {
                System.out.print(T[i][j] + " ");
            }
            System.out.println();
        }

        sc.close();
    }
}
