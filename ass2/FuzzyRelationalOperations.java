import java.util.Scanner;

public class FuzzyRelationalOperations {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // Input size of relations
        System.out.print("Enter number of rows for Relation R: ");
        int r = sc.nextInt();
        System.out.print("Enter number of columns for Relation R / rows for Relation S: ");
        int c = sc.nextInt();
        System.out.print("Enter number of columns for Relation S: ");
        int s = sc.nextInt();

        double[][] R = new double[r][c];
        double[][] S = new double[c][s];

        // Input Relation R
        System.out.println("Enter elements of Relation R (values between 0 and 1):");
        for (int i = 0; i < r; i++)
            for (int j = 0; j < c; j++)
                R[i][j] = sc.nextDouble();

        // Input Relation S
        System.out.println("Enter elements of Relation S (values between 0 and 1):");
        for (int i = 0; i < c; i++)
            for (int j = 0; j < s; j++)
                S[i][j] = sc.nextDouble();

        // Max-Min Composition of R and S
        double[][] maxMinComposition = new double[r][s];
        for (int i = 0; i < r; i++) {
            for (int j = 0; j < s; j++) {
                double maxMin = 0;
                for (int k = 0; k < c; k++) {
                    double minVal = Math.min(R[i][k], S[k][j]);
                    if (minVal > maxMin) maxMin = minVal;
                }
                maxMinComposition[i][j] = maxMin;
            }
        }

        // Display Max-Min Composition
        System.out.println("\nMax-Min Composition of R and S:");
        displayMatrix(maxMinComposition);

        // Fuzzy Relation Union of R and S (if same size)
        if (r == c && c == s) {
            double[][] union = new double[r][c];
            for (int i = 0; i < r; i++)
                for (int j = 0; j < c; j++)
                    union[i][j] = Math.max(R[i][j], S[i][j]);

            System.out.println("\nUnion of R and S:");
            displayMatrix(union);

            // Fuzzy Relation Intersection
            double[][] intersection = new double[r][c];
            for (int i = 0; i < r; i++)
                for (int j = 0; j < c; j++)
                    intersection[i][j] = Math.min(R[i][j], S[i][j]);

            System.out.println("\nIntersection of R and S:");
            displayMatrix(intersection);

            // Complement of R
            double[][] complementR = new double[r][c];
            for (int i = 0; i < r; i++)
                for (int j = 0; j < c; j++)
                    complementR[i][j] = 1 - R[i][j];

            System.out.println("\nComplement of R:");
            displayMatrix(complementR);
        } else {
            System.out.println("\nUnion, Intersection, and Complement skipped: R and S are not same size.");
        }

        sc.close();
    }

    static void displayMatrix(double[][] matrix) {
        for (double[] row : matrix) {
            for (double val : row) {
                System.out.printf("%.2f ", val);
            }
            System.out.println();
        }
    }
}
