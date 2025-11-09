import java.io.FileWriter;
import java.io.IOException;

public class GaussianMF {

    // Gaussian membership function
    public static double gaussian(double x, double mean, double sigma) {
        return Math.exp(-0.5 * Math.pow((x - mean) / sigma, 2));
    }

    public static void main(String[] args) {
        double mean = 5;   // mean
        double sigma = 2;  // standard deviation

        try {
            FileWriter fout = new FileWriter("gaussian.txt");

            for (double x = -2; x <= 12; x += 0.5) {
                double value = gaussian(x, mean, sigma);
                fout.write(x + " " + value + "\n");
            }

            fout.close();
            System.out.println("Gaussian values saved in gaussian.txt");

        } catch (IOException e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
    }
}
