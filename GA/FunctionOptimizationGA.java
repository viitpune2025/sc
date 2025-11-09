import java.util.*;

public class FunctionOptimizationGA {

    static final int POP_SIZE = 10;
    static final int MAX_GEN = 20;
    static final double CROSS_RATE = 0.8;
    static final double MUTATE_RATE = 0.1;
    static final double X_MIN = -10;
    static final double X_MAX = 10;

    static Random rand = new Random();

    static double fitness(double x) {
        return 1.0 / (1 + x * x);
    }

    static double randomIndividual() {
        return X_MIN + (X_MAX - X_MIN) * rand.nextDouble();
    }

    static double crossover(double p1, double p2) {
        if (rand.nextDouble() < CROSS_RATE)
            return (p1 + p2) / 2;
        return p1;
    }

    static double mutate(double x) {
        if (rand.nextDouble() < MUTATE_RATE) {
            double delta = (rand.nextDouble() - 0.5);
            x += delta;
            if (x < X_MIN) x = X_MIN;
            if (x > X_MAX) x = X_MAX;
        }
        return x;
    }

    static double tournamentSelection(double[] population, double[] fitness) {
        int i1 = rand.nextInt(POP_SIZE);
        int i2 = rand.nextInt(POP_SIZE);
        return (fitness[i1] > fitness[i2]) ? population[i1] : population[i2];
    }

    static double rouletteSelection(double[] population, double[] fitness) {
        double sum = 0;
        for (double f : fitness) sum += f;
        double pick = rand.nextDouble() * sum;
        double current = 0;
        for (int i = 0; i < POP_SIZE; i++) {
            current += fitness[i];
            if (current >= pick)
                return population[i];
        }
        return population[POP_SIZE - 1];
    }

    static double rankSelection(double[] population, double[] fitness) {
        int[] rank = new int[POP_SIZE];
        Integer[] indices = new Integer[POP_SIZE];
        for (int i = 0; i < POP_SIZE; i++) indices[i] = i;

        Arrays.sort(indices, (a, b) -> Double.compare(fitness[a], fitness[b]));

        for (int i = 0; i < POP_SIZE; i++) rank[indices[i]] = i + 1;

        double totalRank = (POP_SIZE * (POP_SIZE + 1)) / 2.0;
        double pick = rand.nextDouble() * totalRank;
        double current = 0;
        for (int i = 0; i < POP_SIZE; i++) {
            current += rank[i];
            if (current >= pick)
                return population[i];
        }
        return population[POP_SIZE - 1];
    }

    static double canonicalSelection(double[] population, double[] fitness) {
        double best = population[0];
        double bestFit = fitness[0];
        for (int i = 1; i < POP_SIZE; i++) {
            if (fitness[i] > bestFit) {
                best = population[i];
                bestFit = fitness[i];
            }
        }
        return best;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        boolean continueRun = true;

        while (continueRun) {
            System.out.println("\nSelect Selection Method:");
            System.out.println("1. Tournament Selection");
            System.out.println("2. Roulette Wheel Selection");
            System.out.println("3. Rank Based Selection");
            System.out.println("4. Canonical Selection");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");
            int choice = sc.nextInt();

            if (choice == 5) {
                System.out.println("Exiting program...");
                break;
            }

            double[] population = new double[POP_SIZE];
            for (int i = 0; i < POP_SIZE; i++) {
                population[i] = randomIndividual();
            }

            for (int gen = 0; gen < MAX_GEN; gen++) {
                double[] fitness = new double[POP_SIZE];
                for (int i = 0; i < POP_SIZE; i++)
                    fitness[i] = fitness(population[i]);

                double[] newPop = new double[POP_SIZE];

                for (int i = 0; i < POP_SIZE; i++) {
                    double parent1 = 0, parent2 = 0;
                    switch (choice) {
                        case 1 -> {
                            parent1 = tournamentSelection(population, fitness);
                            parent2 = tournamentSelection(population, fitness);
                        }
                        case 2 -> {
                            parent1 = rouletteSelection(population, fitness);
                            parent2 = rouletteSelection(population, fitness);
                        }
                        case 3 -> {
                            parent1 = rankSelection(population, fitness);
                            parent2 = rankSelection(population, fitness);
                        }
                        case 4 -> {
                            parent1 = canonicalSelection(population, fitness);
                            parent2 = rouletteSelection(population, fitness);
                        }
                        default -> {
                            System.out.println("Invalid choice!");
                            continueRun = false;
                        }
                    }

                    double child = crossover(parent1, parent2);
                    newPop[i] = mutate(child);
                }

                population = newPop;

                // Find best solution
                double best = population[0];
                double bestFit = fitness(population[0]);
                for (int i = 1; i < POP_SIZE; i++) {
                    if (fitness(population[i]) > bestFit) {
                        best = population[i];
                        bestFit = fitness(population[i]);
                    }
                }

                System.out.printf("Generation %d -> Best X: %.4f, Fitness: %.4f\n",
                        gen + 1, best, bestFit);
            }

            System.out.print("\nDo you want to run another selection method? (y/n): ");
            String ans = sc.next().toLowerCase();
            if (!ans.equals("y")) {
                continueRun = false;
                System.out.println("Program stopped by user.");
            }
        }

        sc.close();
    }
}
