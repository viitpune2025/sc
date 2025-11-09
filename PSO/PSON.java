import java.util.Random;

public class PSON {

    // ---------- Step 1: Rosenbrock Objective Function ----------
    public static double function(double x1, double x2) {
        double first = x2 - (x1 * x1);
        double second = (1 - x1) * (1 - x1);
        return (100 * first * first) + second;
    }

    // ---------- Step 2: Initialize Local Best ----------
    public static void initializeLocalBest(double[][] data, double[][] local_best) {
        for (int i = 0; i < data.length; i++) {
            local_best[i][0] = data[i][0];
            local_best[i][1] = data[i][1];
        }
    }

    // ---------- Step 3: Calculate Fitness and Update Local Best ----------
    public static void calculateFitness(double[][] data, double[] fitness, double[][] local_best) {
        for (int i = 0; i < data.length; i++) {
            double current = function(data[i][0], data[i][1]);
            if (current < fitness[i]) {
                fitness[i] = current;
                local_best[i][0] = data[i][0];
                local_best[i][1] = data[i][1];
            }
        }
    }

    // ---------- Step 4: Get Global Best Index ----------
    public static int getGlobalBestIndex(double[] fitness) {
        double min = Double.MAX_VALUE;
        int index = 0;
        for (int i = 0; i < fitness.length; i++) {
            if (fitness[i] < min) {
                min = fitness[i];
                index = i;
            }
        }
        return index;
    }

    // ---------- Step 5: Update Velocity ----------
    public static void updateVelocity(double[][] data, double[][] local_best, double[] fitness,
                                      double c1, double c2, double w) {
        Random rand = new Random();

        int gIndex = getGlobalBestIndex(fitness);

        for (int i = 0; i < data.length; i++) {
            double r1 = rand.nextDouble();
            double r2 = rand.nextDouble();

            // Velocity update equations
            data[i][2] = w * data[i][2] +
                         c1 * r1 * (local_best[i][0] - data[i][0]) +
                         c2 * r2 * (data[gIndex][0] - data[i][0]);

            data[i][3] = w * data[i][3] +
                         c1 * r1 * (local_best[i][1] - data[i][1]) +
                         c2 * r2 * (data[gIndex][1] - data[i][1]);
        }
    }

    // ---------- Step 6: Update Position ----------
    public static void updatePosition(double[][] data, double lower_limit, double upper_limit) {
        for (int i = 0; i < data.length; i++) {
            data[i][0] += data[i][2];
            data[i][1] += data[i][3];

            // Boundary check for x1
            if (data[i][0] < lower_limit)
                data[i][0] = lower_limit;
            else if (data[i][0] > upper_limit)
                data[i][0] = upper_limit;

            // Boundary check for x2
            if (data[i][1] < lower_limit)
                data[i][1] = lower_limit;
            else if (data[i][1] > upper_limit)
                data[i][1] = upper_limit;
        }
    }

    // ---------- Step 7: Main Program ----------
    public static void main(String[] args) {
        int swarmSize = 8;
        int maxIterations = 50;
        double lower_limit = -5.0;
        double upper_limit = 5.0;

        double c1 = 1.5;   // Cognitive coefficient
        double c2 = 2.0;   // Social coefficient
        double wMax = 0.9; // Inertia max
        double wMin = 0.4; // Inertia min

        double[][] data = new double[swarmSize][4];
        double[][] local_best = new double[swarmSize][2];
        double[] fitness = new double[swarmSize];

        Random rand = new Random();

        // Initialize particles
        for (int i = 0; i < swarmSize; i++) {
            data[i][0] = lower_limit + (upper_limit - lower_limit) * rand.nextDouble(); // x1
            data[i][1] = lower_limit + (upper_limit - lower_limit) * rand.nextDouble(); // x2
            data[i][2] = 0; // velocity x
            data[i][3] = 0; // velocity y
            fitness[i] = Double.MAX_VALUE;
        }

        initializeLocalBest(data, local_best);

        double gbest = Double.MAX_VALUE;

        // Iteration loop
        for (int iter = 1; iter <= maxIterations; iter++) {

            // Dynamically reduce inertia weight (w)
            double w = wMax - ((wMax - wMin) * iter / maxIterations);

            // Step 3: Evaluate fitness
            calculateFitness(data, fitness, local_best);

            // Step 4: Update global best
            gbest = fitness[getGlobalBestIndex(fitness)];

            // Step 5: Update velocity
            updateVelocity(data, local_best, fitness, c1, c2, w);

            // Step 6: Update position
            updatePosition(data, lower_limit, upper_limit);

            System.out.printf("Iteration %d -> Global Best Fitness: %.6f%n", iter, gbest);
        }

        // Final output
        int bestIndex = getGlobalBestIndex(fitness);
        System.out.println("\n=== FINAL RESULTS ===");
        System.out.println("Best Fitness: " + fitness[bestIndex]);
        System.out.println("Best X1: " + data[bestIndex][0]);
        System.out.println("Best X2: " + data[bestIndex][1]);
    }
}
