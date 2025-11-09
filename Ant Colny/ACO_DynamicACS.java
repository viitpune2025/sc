import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class ACO_DynamicACS {

    // Problem setup
    static int N = 4;             // Number of cities
    static int ants = 3;          // Number of ants
    static int iterations = 5;    // Number of iterations

    // Parameters
    static double alpha = 1;      // Pheromone influence
    static double beta = 2;       // Heuristic influence
    static double rho = 0.1;      // Global pheromone evaporation rate
    static double xi = 0.1;       // Local pheromone decay (for ACS)
    static double tau0 = 1.0;     // Initial pheromone level

    static int[][] distance = {
        {0, 1, 2, 2},
        {1, 0, 1, 2},
        {2, 1, 0, 1},
        {2, 2, 1, 0}
    };

    static double[][] pheromone = new double[N][N];
    static FileWriter writer;

    public static void main(String[] args) {
        try {
            writer = new FileWriter("ACO_Output.txt");
            runACO();
            writer.close();
            System.out.println("\n✅ Output saved to ACO_Output.txt");
        } catch (IOException e) {
            System.out.println("Error writing file: " + e.getMessage());
        }
    }

    static void runACO() throws IOException {
        initializePheromone();

        int[] bestTour = null;
        int bestLength = Integer.MAX_VALUE;

        writer.write("=== Ant Colony Optimization (ACS) ===\n");

        for (int it = 1; it <= iterations; it++) {
            writer.write("\n=== Iteration " + it + " ===\n");
            System.out.println("\n=== Iteration " + it + " ===");

            int[][] antTours = new int[ants][N + 1];
            int[] tourLengths = new int[ants];

            // Step 1: Each ant builds its tour dynamically
            for (int k = 0; k < ants; k++) {
                antTours[k] = constructTour();
                tourLengths[k] = calculateTourLength(antTours[k]);

                String line = "Ant " + (k + 1) + " tour: " + getTourString(antTours[k]) +
                        " Length = " + tourLengths[k];
                System.out.println(line);
                writer.write(line + "\n");
            }

            // Step 2: Find the best tour in this iteration
            for (int k = 0; k < ants; k++) {
                if (tourLengths[k] < bestLength) {
                    bestLength = tourLengths[k];
                    bestTour = Arrays.copyOf(antTours[k], N + 1);
                }
            }

            // Step 3: Apply global pheromone update
            globalPheromoneUpdate(bestTour, bestLength);

            // Step 4: Show pheromone and best tour
            writer.write("\nPheromone Matrix:\n");
            System.out.println("\nPheromone Matrix:");
            for (int i = 0; i < N; i++) {
                for (int j = 0; j < N; j++) {
                    String val = String.format("%.2f\t", pheromone[i][j]);
                    System.out.print(val);
                    writer.write(val);
                }
                System.out.println();
                writer.write("\n");
            }

            String bestLine = "Current Best Tour: " + getTourString(bestTour) +
                    " Length = " + bestLength + "\n";
            System.out.print(bestLine);
            writer.write(bestLine);
            writer.write("BestLength=" + bestLength + "\n"); // For Python plotting
        }

        String finalRes = "\n=== Final Best Tour ===\n" +
                getTourString(bestTour) + "\nLength = " + bestLength + "\n";
        System.out.println(finalRes);
        writer.write(finalRes);
    }

    // Initialize pheromone levels
    static void initializePheromone() {
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                pheromone[i][j] = (i == j) ? 0 : tau0;
            }
        }
    }

    // Each ant constructs a tour dynamically with local pheromone updates
    static int[] constructTour() {
        Random rand = new Random();
        int startCity = 0; // all ants start from city A
        boolean[] visited = new boolean[N];
        int[] tour = new int[N + 1];

        int current = startCity;
        visited[current] = true;
        tour[0] = current;

        for (int step = 1; step < N; step++) {
            int next = selectNextCity(current, visited, rand);
            tour[step] = next;
            localPheromoneUpdate(current, next);
            visited[next] = true;
            current = next;
        }

        tour[N] = startCity; // return to start
        localPheromoneUpdate(current, startCity);
        return tour;
    }

    // Select next city
    static int selectNextCity(int current, boolean[] visited, Random rand) {
        double[] prob = new double[N];
        double sum = 0;

        for (int j = 0; j < N; j++) {
            if (!visited[j]) {
                double eta = 1.0 / distance[current][j];
                prob[j] = Math.pow(pheromone[current][j], alpha) * Math.pow(eta, beta);
                sum += prob[j];
            }
        }

        double r = rand.nextDouble();
        double cumulative = 0;

        for (int j = 0; j < N; j++) {
            if (!visited[j]) {
                cumulative += prob[j] / sum;
                if (r <= cumulative) return j;
            }
        }

        for (int j = 0; j < N; j++) if (!visited[j]) return j;
        return -1;
    }

    // Local pheromone update
    static void localPheromoneUpdate(int i, int j) {
        pheromone[i][j] = (1 - xi) * pheromone[i][j] + xi * tau0;
        pheromone[j][i] = pheromone[i][j];
    }

    // Global pheromone update
    static void globalPheromoneUpdate(int[] bestTour, int bestLength) {
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) pheromone[i][j] *= (1 - rho);
        }

        double delta = 1.0 / bestLength;
        for (int i = 0; i < N; i++) {
            int from = bestTour[i];
            int to = bestTour[i + 1];
            pheromone[from][to] += rho * delta;
            pheromone[to][from] = pheromone[from][to];
        }
    }

    static int calculateTourLength(int[] tour) {
        int len = 0;
        for (int i = 0; i < N; i++)
            len += distance[tour[i]][tour[i + 1]];
        return len;
    }

    static String getTourString(int[] tour) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < tour.length; i++) {
            sb.append((char) ('A' + tour[i]));
            if (i < tour.length - 1) sb.append(" -> ");
        }
        return sb.toString();
    }
}
