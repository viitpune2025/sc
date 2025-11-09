import java.util.*;

public class ass8N {

    static Random rand = new Random();

    // Step 1: Fitness function
    static int calculateFitness(String chromosome) {
        int count = 0;
        for (char c : chromosome.toCharArray()) {
            if (c == '1') count++;
        }
        return count;
    }

    // Step 2: Roulette Wheel Selection
    static String rouletteSelection(String[] population, int[] fitness) {
        int total = 0;
        for (int f : fitness) total += f;
        double pick = rand.nextDouble() * total;
        double current = 0;
        for (int i = 0; i < population.length; i++) {
            current += fitness[i];
            if (current >= pick)
                return population[i];
        }
        return population[population.length - 1];
    }

    // ===== CROSSOVER METHODS =====
    static String[] singlePointCrossover(String p1, String p2) {
        int point = rand.nextInt(p1.length() - 1) + 1;
        String c1 = p1.substring(0, point) + p2.substring(point);
        String c2 = p2.substring(0, point) + p1.substring(point);
        System.out.println("Single-point crossover at: " + point);
        return new String[]{c1, c2};
    }

    static String[] doublePointCrossover(String p1, String p2) {
        int point1 = rand.nextInt(p1.length() - 2) + 1;
        int point2 = rand.nextInt(p1.length() - point1 - 1) + point1 + 1;
        String c1 = p1.substring(0, point1) + p2.substring(point1, point2) + p1.substring(point2);
        String c2 = p2.substring(0, point1) + p1.substring(point1, point2) + p2.substring(point2);
        System.out.println("Double-point crossover at: " + point1 + " and " + point2);
        return new String[]{c1, c2};
    }

    static String[] multiPointCrossover(String p1, String p2) {
        int len = p1.length();
        int[] points = {rand.nextInt(len), rand.nextInt(len), rand.nextInt(len)};
        Arrays.sort(points);
        StringBuilder c1 = new StringBuilder();
        StringBuilder c2 = new StringBuilder();
        boolean swap = false;
        int last = 0;

        for (int i = 0; i < points.length; i++) {
            if (!swap) {
                c1.append(p1.substring(last, points[i]));
                c2.append(p2.substring(last, points[i]));
            } else {
                c1.append(p2.substring(last, points[i]));
                c2.append(p1.substring(last, points[i]));
            }
            swap = !swap;
            last = points[i];
        }
        if (!swap) {
            c1.append(p1.substring(last));
            c2.append(p2.substring(last));
        } else {
            c1.append(p2.substring(last));
            c2.append(p1.substring(last));
        }

        System.out.println("Multi-point crossover points: " + Arrays.toString(points));
        return new String[]{c1.toString(), c2.toString()};
    }

    static String[] uniformCrossover(String p1, String p2) {
        StringBuilder c1 = new StringBuilder();
        StringBuilder c2 = new StringBuilder();
        for (int i = 0; i < p1.length(); i++) {
            if (rand.nextBoolean()) {
                c1.append(p1.charAt(i));
                c2.append(p2.charAt(i));
            } else {
                c1.append(p2.charAt(i));
                c2.append(p1.charAt(i));
            }
        }
        System.out.println("Uniform crossover applied.");
        return new String[]{c1.toString(), c2.toString()};
    }

    static String[] crossoverMask(String p1, String p2) {
        StringBuilder mask = new StringBuilder();
        StringBuilder c1 = new StringBuilder();
        StringBuilder c2 = new StringBuilder();

        for (int i = 0; i < p1.length(); i++) {
            int bit = rand.nextInt(2);
            mask.append(bit);
            if (bit == 0) {
                c1.append(p1.charAt(i));
                c2.append(p2.charAt(i));
            } else {
                c1.append(p2.charAt(i));
                c2.append(p1.charAt(i));
            }
        }

        System.out.println("Crossover mask: " + mask);
        return new String[]{c1.toString(), c2.toString()};
    }

    // ===== MUTATION METHODS =====
    static String bitFlipMutation(String chromosome, double mutationRate) {
        char[] genes = chromosome.toCharArray();
        for (int i = 0; i < genes.length; i++) {
            if (rand.nextDouble() < mutationRate) {
                genes[i] = (genes[i] == '0') ? '1' : '0';
            }
        }
        System.out.println("Bit-flip mutation applied.");
        return new String(genes);
    }

    static String interchangeMutation(String chromosome) {
        char[] genes = chromosome.toCharArray();
        int i = rand.nextInt(genes.length);
        int j = rand.nextInt(genes.length);
        char temp = genes[i];
        genes[i] = genes[j];
        genes[j] = temp;
        System.out.println("Interchanged bits at: " + i + " and " + j);
        return new String(genes);
    }

    // Display population
    static void displayPopulation(String[] population, int[] fitness) {
        System.out.println("\nPopulation and Fitness:");
        for (int i = 0; i < population.length; i++) {
            System.out.printf("%s  ->  Fitness: %d%n", population[i], fitness[i]);
        }
    }

    // main
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        double mutationRate = 0.3;

        String[] population = {"1010", "1110", "0101", "0011"};
        int[] fitness = new int[population.length];
        for (int i = 0; i < population.length; i++) {
            fitness[i] = calculateFitness(population[i]);
        }

        System.out.println("=== GENETIC ALGORITHM MENU ===");
        displayPopulation(population, fitness);

        String parent1 = rouletteSelection(population, fitness);
        String parent2 = rouletteSelection(population, fitness);

        System.out.println("\nSelected Parents:");
        System.out.println("Parent 1: " + parent1);
        System.out.println("Parent 2: " + parent2);

        System.out.println("\nChoose a crossover method:");
        System.out.println("1. Single-point crossover");
        System.out.println("2. Double-point crossover");
        System.out.println("3. Multi-point crossover");
        System.out.println("4. Uniform crossover");
        System.out.println("5. Crossover mask");
        System.out.print("Enter your choice: ");
        int choice = sc.nextInt();

        String[] children = null;
        switch (choice) {
            case 1:
                children = singlePointCrossover(parent1, parent2);
                break;
            case 2:
                children = doublePointCrossover(parent1, parent2);
                break;
            case 3:
                children = multiPointCrossover(parent1, parent2);
                break;
            case 4:
                children = uniformCrossover(parent1, parent2);
                break;
            case 5:
                children = crossoverMask(parent1, parent2);
                break;
            default:
                System.out.println("Invalid choice! Using single-point crossover by default.");
                children = singlePointCrossover(parent1, parent2);
        }

        System.out.println("\nChildren after crossover: " + Arrays.toString(children));

        System.out.println("\nChoose a mutation method:");
        System.out.println("1. Bit-flip mutation");
        System.out.println("2. Interchange mutation");
        System.out.print("Enter your choice: ");
        int mutChoice = sc.nextInt();

        for (int i = 0; i < children.length; i++) {
            switch (mutChoice) {
                case 1:
                    children[i] = bitFlipMutation(children[i], mutationRate);
                    break;
                case 2:
                    children[i] = interchangeMutation(children[i]);
                    break;
                default:
                    System.out.println("Invalid choice! Applying bit-flip mutation.");
                    children[i] = bitFlipMutation(children[i], mutationRate);
            }
        }

        System.out.println("\nChildren after mutation: " + Arrays.toString(children));

        System.out.println("\n=== FINAL RESULTS ===");
        for (String child : children) {
            System.out.printf("Chromosome: %s  ->  Fitness: %d%n", child, calculateFitness(child));
        }

        sc.close();
    }
}
