import java.io.FileWriter;
import java.io.IOException;

public class TrapezoidalMF {

    // Trapezoidal membership function
    public static double trapezoidal(double x, double a, double b, double c, double d) {
        if (x <= a || x >= d) return 0.0;
        else if (x >= b && x <= c) return 1.0;
        else if (x > a && x < b) return (x - a) / (b - a);
        else return (d - x) / (d - c);
    }

    public static void main(String[] args) {
        double a = 0, b = 3, c = 7, d = 10;  // trapezoid parameters

        try {
            FileWriter fout = new FileWriter("trapezoidal.txt");

            for (double x = -2; x <= 12; x += 0.5) {
                double value = trapezoidal(x, a, b, c, d);
                fout.write(x + " " + value + "\n");
            }

            fout.close();
            System.out.println("Trapezoidal values saved in trapezoidal.txt");

        } catch (IOException e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
    }
}
