import java.io.FileWriter;
import java.io.IOException;

public class SingletonMF {

    // Singleton membership function
    public static double singleton(double x, double a) {
        return (x == a) ? 1.0 : 0.0;
    }

    public static void main(String[] args) {
        double a = 5; // singleton point

        try {
            FileWriter fout = new FileWriter("singleton.txt");

            for (double x = -2; x <= 12; x += 0.5) {
                double value = singleton(x, a);
                fout.write(x + " " + value + "\n");
            }

            fout.close();
            System.out.println("Singleton values saved in singleton.txt");

        } catch (IOException e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
    }
}
