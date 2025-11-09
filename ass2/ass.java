import java.util.Scanner;

public class ass {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // Input sizes
        System.out.print("Enter number of elements in set X (rows of R): ");
        int r = sc.nextInt();
        System.out.print("Enter number of elements in set Y (columns of R / rows of S): ");
        int c = sc.nextInt();
        System.out.print("Enter number of elements in set Z (columns of S): ");
        int s = sc.nextInt();

        sc.nextLine(); // consume newline

        // Input element names
        String[] setX = new String[r];
        String[] setY = new String[c];
        String[] setZ = new String[s];

        System.out.println("\nEnter elements of Set X:");
        for (int i = 0; i < r; i++) {
            System.out.print("X[" + i + "]: ");
            setX[i] = sc.nextLine();
        }

        System.out.println("\nEnter elements of Set Y:");
        for (int i = 0; i < c; i++) {
            System.out.print("Y[" + i + "]: ");
            setY[i] = sc.nextLine();
        }

        System.out.println("\nEnter elements of Set Z:");
        for (int i = 0; i < s; i++) {
            System.out.print("Z[" + i + "]: ");
            setZ[i] = sc.nextLine();
        }

        // Initialize relations
        double[][] R1 = new double[r][c];
        double[][] R2 = new double[r][c]; // second relation for union/intersection
        double[][] S = new double[c][s];

        // Input Relation R1 (X→Y)
        System.out.println("\nEnter membership values for Relation R1 (X→Y):");
        for (int i = 0; i < r; i++) {
            for (int j = 0; j < c; j++) {
                System.out.print("R1(" + setX[i] + "," + setY[j] + "): ");
                R1[i][j] = sc.nextDouble();
            }
        }

        // Input Relation R2 (X→Y)
        System.out.println("\nEnter membership values for Relation R2 (X→Y):");
        for (int i = 0; i < r; i++) {
            for (int j = 0; j < c; j++) {
                System.out.print("R2(" + setX[i] + "," + setY[j] + "): ");
                R2[i][j] = sc.nextDouble();
            }
        }

        // Input Relation S (Y→Z)
        System.out.println("\nEnter membership values for Relation S (Y→Z):");
        for (int i = 0; i < c; i++) {
            for (int j = 0; j < s; j++) {
                System.out.print("S(" + setY[i] + "," + setZ[j] + "): ");
                S[i][j] = sc.nextDouble();
            }
        }

        // Union and Intersection of R1 and R2
        double[][] unionR = new double[r][c];
        double[][] intersectionR = new double[r][c];
        double[][] complementR1 = new double[r][c];
        double[][] complementR2 = new double[r][c];

        for (int i = 0; i < r; i++) {
            for (int j = 0; j < c; j++) {
                unionR[i][j] = Math.max(R1[i][j], R2[i][j]);
                intersectionR[i][j] = Math.min(R1[i][j], R2[i][j]);
                complementR1[i][j] = 1 - R1[i][j];
                complementR2[i][j] = 1 - R2[i][j];
            }
        }

        // Max-Min Composition of R1 and S
        double[][] maxMinComposition = new double[r][s];
        for (int i = 0; i < r; i++) {
            for (int j = 0; j < s; j++) {
                double maxMin = 0;
                for (int k = 0; k < c; k++) {
                    double minVal = Math.min(R1[i][k], S[k][j]);
                    if (minVal > maxMin) maxMin = minVal;
                }
                maxMinComposition[i][j] = maxMin;
            }
        }

        // Display results
        System.out.println("\nUnion of R1 and R2:");
        displayMatrix(unionR, setX, setY);

        System.out.println("\nIntersection of R1 and R2:");
        displayMatrix(intersectionR, setX, setY);

        System.out.println("\nComplement of R1:");
        displayMatrix(complementR1, setX, setY);

        System.out.println("\nComplement of R2:");
        displayMatrix(complementR2, setX, setY);

        System.out.println("\nMax-Min Composition of R1 and S (X→Z):");
        displayMatrix(maxMinComposition, setX, setZ);

        sc.close();
    }

    // Display matrix with row and column labels
    static void displayMatrix(double[][] matrix, String[] rowLabels, String[] colLabels) {
        System.out.print("\t");
        for (String col : colLabels) {
            System.out.print(col + "\t");
        }
        System.out.println();

        for (int i = 0; i < matrix.length; i++) {
            System.out.print(rowLabels[i] + "\t");
            for (int j = 0; j < matrix[i].length; j++) {
                System.out.printf("%.2f\t", matrix[i][j]);
            }
            System.out.println();
        }
    }
}
