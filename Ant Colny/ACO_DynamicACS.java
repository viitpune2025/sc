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

    public static void main(String[] args) {
        initializePheromone();

        int[] bestTour = null;
        int bestLength = Integer.MAX_VALUE;

        for (int it = 1; it <= iterations; it++) {
            System.out.println("\n=== Iteration " + it + " ===");

            int[][] antTours = new int[ants][N + 1];
            int[] tourLengths = new int[ants];

            // Step 1: Each ant builds its tour dynamically
            for (int k = 0; k < ants; k++) {
                antTours[k] = constructTour();
                tourLengths[k] = calculateTourLength(antTours[k]);

                System.out.print("Ant " + (k + 1) + " tour: ");
                printTour(antTours[k]);
                System.out.println(" Length = " + tourLengths[k]);
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
            printPheromone();
            System.out.print("Current Best Tour: ");
            printTour(bestTour);
            System.out.println(" Length = " + bestLength);
        }

        System.out.println("\n=== Final Best Tour ===");
        printTour(bestTour);
        System.out.println(" Length = " + bestLength);
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
        int startCity = 0; // all ants start from same city (A)
        boolean[] visited = new boolean[N];
        int[] tour = new int[N + 1];

        int current = startCity;
        visited[current] = true;
        tour[0] = current;

        for (int step = 1; step < N; step++) {
            int next = selectNextCity(current, visited, rand);
            tour[step] = next;

            // Local pheromone update (immediate)
            localPheromoneUpdate(current, next);

            visited[next] = true;
            current = next;
        }

        tour[N] = startCity; // return to start
        localPheromoneUpdate(current, startCity);
        return tour;
    }

    // Choose next city based on probability
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
                if (r <= cumulative) {
                    return j;
                }
            }
        }

        // fallback (should not happen often)
        for (int j = 0; j < N; j++) {
            if (!visited[j]) return j;
        }
        return -1;
    }

    // Local pheromone update after each move
    static void localPheromoneUpdate(int i, int j) {
        pheromone[i][j] = (1 - xi) * pheromone[i][j] + xi * tau0;
        pheromone[j][i] = pheromone[i][j]; // symmetric
    }

    // Global pheromone update (on best tour)
    static void globalPheromoneUpdate(int[] bestTour, int bestLength) {
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                pheromone[i][j] *= (1 - rho); // evaporation
            }
        }

        double delta = 1.0 / bestLength;
        for (int i = 0; i < N; i++) {
            int from = bestTour[i];
            int to = bestTour[i + 1];
            pheromone[from][to] += rho * delta;
            pheromone[to][from] = pheromone[from][to];
        }
    }

    // Calculate total length of tour
    static int calculateTourLength(int[] tour) {
        int len = 0;
        for (int i = 0; i < N; i++) {
            len += distance[tour[i]][tour[i + 1]];
        }
        return len;
    }

    // Print pheromone matrix
    static void printPheromone() {
        System.out.println("\nPheromone Matrix:");
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                System.out.printf("%.2f\t", pheromone[i][j]);
            }
            System.out.println();
        }
    }

    // Print tour path (A, B, C, D)
    static void printTour(int[] tour) {
        for (int i = 0; i < tour.length; i++) {
            System.out.print((char) ('A' + tour[i]));
            if (i < tour.length - 1) System.out.print(" -> ");
        }
        System.out.println();
    }
}
