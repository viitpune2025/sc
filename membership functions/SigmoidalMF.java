import java.io.FileWriter;
import java.io.IOException;

public class SigmoidalMF {

    // Sigmoidal membership function
    public static double sigmoid(double x, double a, double c) {
        return 1.0 / (1.0 + Math.exp(-a * (x - c)));
    }

    public static void main(String[] args) {
        double a = 1; // slope parameter
        double c = 5; // center parameter

        try {
            FileWriter fout = new FileWriter("sigmoidal.txt");

            for (double x = -2; x <= 12; x += 0.5) {
                double value = sigmoid(x, a, c);
                fout.write(x + " " + value + "\n");
            }

            fout.close();
            System.out.println("Sigmoidal values saved in sigmoidal.txt");

        } catch (IOException e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
    }
}
