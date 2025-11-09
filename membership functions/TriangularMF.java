import java.io.FileWriter;
import java.io.IOException;

public class TriangularMF {

    // Triangular membership function
    public static double triangular(double x, double a, double b, double c) {
        if (x <= a || x >= c) return 0.0;
        else if (x == b) return 1.0;
        else if (x > a && x < b) return (x - a) / (b - a);
        else return (c - x) / (c - b);
    }

    public static void main(String[] args) {
        double a = 0, b = 5, c = 10; // triangular parameters

        try {
            FileWriter fout = new FileWriter("triangular.txt");

            for (double x = -2; x <= 12; x += 0.5) {
                double value = triangular(x, a, b, c);
                fout.write(x + " " + value + "\n");
            }

            fout.close();
            System.out.println("Triangular values saved in triangular.txt");

        } catch (IOException e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
    }
}
