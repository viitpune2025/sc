import java.io.FileWriter;
import java.io.IOException;

public class PSON {

    // Rosenbrock Objective Function
    public static double function(double x1, double x2) {
        double first = x2 - (x1 * x1);
        double second = (1 - x1) * (1 - x1);
        double third = first * first;
        double answer = (100 * third) + second;
        return answer;
    }

    // Find index of particle with best (minimum) fitness
    public static int pgb_index(double[] fitness) {
        double min = Double.MAX_VALUE;
        int index = -1;
        for (int i = 0; i < fitness.length; i++) {
            if (fitness[i] < min) {
                min = fitness[i];
                index = i;
            }
        }
        return index;
    }

    public static void main(String[] args) {
        double lower_limit = -5.0;
        double upper_limit = 5.0;
        double gbest = 14.222;
        double c1 = 1.5;
        double c2 = 2.0;
        double w = 0.75;

        double[][] data = {
                {2.212, 3.009, 0.0, 0.0},
                {-2.289, -2.396, 0.0, 0.0},
                {-2.393, -4.790, 0.0, 0.0},
                {-0.639, 1.692, 0.0, 0.0},
                {-3.168, 0.706, 0.0, 0.0},
                {0.215, -2.350, 0.0, 0.0},
                {-0.742, 1.934, 0.0, 0.0},
                {-4.563, 4.791, 0.0, 0.0}
        };

        double[] fitness = new double[8];
        for (int i = 0; i < 8; i++) {
            fitness[i] = Double.MAX_VALUE;
        }

        double[][] local_best = new double[8][2];
        for (int i = 0; i < 8; i++) {
            local_best[i][0] = data[i][0];
            local_best[i][1] = data[i][1];
        }

        double[] r1 = {0.661, 0.919, 0.782, 0.299, 0.874, 0.133, 0.031, 0.366};
        double[] r2 = {0.312, 0.271, 0.824, 0.055, 0.595, 0.582, 0.736, 0.954};

        int iteration = 0;

        try (FileWriter fout = new FileWriter("particles.txt")) {

            while (gbest >= 14.222) {

                // Fitness calculation and local_best update
                for (int i = 0; i < 8; i++) {
                    double last = fitness[i];
                    double current = function(data[i][0], data[i][1]);
                    if (current < last) {
                        fitness[i] = current;
                        local_best[i][0] = data[i][0];
                        local_best[i][1] = data[i][1];
                    }
                    if (fitness[i] < gbest) {
                        gbest = fitness[i];
                    }
                }

                // Velocity update
                System.out.println("Printing Particle Velocity");
                System.out.println("------------------------------------------------------------------------------------------------------------------------------");

                for (int i = 0; i < 8; i++) {
                    int g_index = pgb_index(fitness);

                    data[i][2] = w * data[i][2]
                            + c1 * r1[i] * (local_best[i][0] - data[i][0])
                            + c2 * r2[i] * (data[g_index][0] - data[i][0]);

                    data[i][3] = w * data[i][3]
                            + c1 * r1[i] * (local_best[i][1] - data[i][1])
                            + c2 * r2[i] * (data[g_index][1] - data[i][1]);

                    System.out.println(data[i][2] + " " + data[i][3]);
                }

                // Position update
                System.out.println("Printing Particles:");
                System.out.println("------------------------------------------------------------------------------------------------------------------------------");

                for (int i = 0; i < 8; i++) {
                    data[i][0] = data[i][0] + data[i][2];
                    if (data[i][0] < lower_limit) data[i][0] = -5.0;
                    else if (data[i][0] > upper_limit) data[i][0] = 5.0;

                    data[i][1] = data[i][1] + data[i][3];
                    if (data[i][1] < lower_limit) data[i][1] = -5.0;
                    else if (data[i][1] > upper_limit) data[i][1] = 5.0;

                    System.out.println(data[i][0] + " " + data[i][1]);

                    fout.write(iteration + " " + i + " "
                            + data[i][0] + " " + data[i][1] + " "
                            + function(data[i][0], data[i][1]) + "\n");
                }

                int g_index = pgb_index(fitness);
                fout.write("GLOBAL " + iteration + " "
                        + g_index + " "
                        + data[g_index][0] + " "
                        + data[g_index][1] + " "
                        + fitness[g_index] + "\n");

                iteration++;

                System.out.println("Printing Final Fitness:");
                System.out.println("------------------------------------------------------------------------------------------------------------------------------");
                for (int i = 0; i < 8; i++) {
                    System.out.println(fitness[i]);
                }

                // To avoid infinite loop in demo (since condition may never fail)
                if (iteration > 10) break;
            }

            System.out.println("------------------------------------------------------------------------------------------------------------------------------");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
