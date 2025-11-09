import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

import javax.swing.text.Position;

public class GreyWolf {

    static final int NUM_WOLVES = 4, MAX_ITER = 5;
    static final double LB = -10, UB = 10;
    static final Random rand = new Random(42);

    static FileWriter writer;

    static class Position {
        double x1, x2;
        Position(double x1, double x2) { this.x1 = x1; this.x2 = x2; }
        Position(Position p) { this.x1 = p.x1; this.x2 = p.x2; }
        double fitness() { return x1*x1 + x2*x2; }
        public String toString() { return String.format("(%.2f, %.2f)", x1, x2); }
    }

    static Position[] initializePopulation() {
        Position[] wolves = new Position[NUM_WOLVES];
        for (int i = 0; i < NUM_WOLVES; i++)
            wolves[i] = new Position(LB + (UB - LB) * rand.nextDouble(), LB + (UB - LB) * rand.nextDouble());
        return wolves;
    }

    static double clamp(double val) { return Math.max(LB, Math.min(UB, val)); }

    static Position[] sortByFitness(Position[] wolves) {
        return Arrays.stream(wolves)
                .sorted(Comparator.comparingDouble(Position::fitness))
                .toArray(Position[]::new);
    }

    static Position[] getLeaders(Position[] wolves) {
        Position[] sorted = sortByFitness(wolves);
        return new Position[]{sorted[0], sorted[1], sorted[2]};
    }

    static Position updatePosition(Position wolf, Position alpha, Position beta, Position delta, double a) {
        double[] coords = {wolf.x1, wolf.x2}, newCoords = new double[2];
        double[] Acoords = {alpha.x1, alpha.x2}, Bcoords = {beta.x1, beta.x2}, Dcoords = {delta.x1, delta.x2};

        for (int i = 0; i < 2; i++) {
            double r1 = rand.nextDouble(), r2 = rand.nextDouble();
            double A1 = 2 * a * r1 - a, C1 = 2 * r2;
            double D_alpha = Math.abs(C1 * Acoords[i] - coords[i]);
            double X1 = Acoords[i] - A1 * D_alpha;

            r1 = rand.nextDouble(); r2 = rand.nextDouble();
            double A2 = 2 * a * r1 - a, C2 = 2 * r2;
            double D_beta = Math.abs(C2 * Bcoords[i] - coords[i]);
            double X2 = Bcoords[i] - A2 * D_beta;

            r1 = rand.nextDouble(); r2 = rand.nextDouble();
            double A3 = 2 * a * r1 - a, C3 = 2 * r2;
            double D_delta = Math.abs(C3 * Dcoords[i] - coords[i]);
            double X3 = Dcoords[i] - A3 * D_delta;

            newCoords[i] = clamp((X1 + X2 + X3) / 3);
        }
        return new Position(newCoords[0], newCoords[1]);
    }

    static void printHeader(int iter) throws IOException {
        String header = "\n=== Iteration " + iter + " ===\n" +
                String.format("%-7s | %-15s | %-10s%n", "Wolf", "New Position", "f(x1,x2)") +
                "------------------------------------------";
        System.out.println(header);
        writer.write(header + "\n");
    }

    static void printTable(Position[] wolves, Position alpha, Position beta, Position delta) throws IOException {
        for (int i = 0; i < wolves.length; i++) {
            String mark = "";
            if (wolves[i] == alpha) mark = "(α)";
            else if (wolves[i] == beta) mark = "(β)";
            else if (wolves[i] == delta) mark = "(δ)";

            String line = String.format("| W%-5d %-3s| %-15s | %-10.2f |", i + 1, mark, wolves[i].toString(), wolves[i].fitness());
            System.out.println(line);
            writer.write(line + "\n");
        }
        String leadersInfo = "------------------------------------------\n" +
                String.format("Alpha = %s  f = %.2f%n", alpha, alpha.fitness()) +
                String.format("Beta  = %s  f = %.2f%n", beta, beta.fitness()) +
                String.format("Delta = %s  f = %.2f%n", delta, delta.fitness());
        System.out.println(leadersInfo);
        writer.write(leadersInfo + "\n");
    }

   static void runGWO() throws IOException {
    Position[] wolves = initializePopulation();

    for (int iter = 1; iter <= MAX_ITER; iter++) {
        double a = 2.0 - (2.0 * (iter - 1) / (MAX_ITER - 1));

        printHeader(iter);

        // Step 1: Get current leaders
        Position[] leaders = getLeaders(wolves);
        Position alpha = leaders[0], beta = leaders[1], delta = leaders[2];

        // Step 2: Update positions using current leaders
        Position[] newWolves = new Position[NUM_WOLVES];
        for (int i = 0; i < NUM_WOLVES; i++)
            newWolves[i] = updatePosition(wolves[i], alpha, beta, delta, a);

        // Step 3: Replace old population
        wolves = newWolves;

        // Step 4: Recalculate leaders *after* update
        Position[] newLeaders = getLeaders(wolves);
        Position newAlpha = newLeaders[0], newBeta = newLeaders[1], newDelta = newLeaders[2];

        // Step 5: Print the updated table + new leaders
        printTable(wolves, newAlpha, newBeta, newDelta);
    }

    // Final result
    Position[] leaders = getLeaders(wolves);
    String result = "\n=== Final Result ===\n" +
            String.format("Alpha = %s  f = %.4f%n", leaders[0], leaders[0].fitness());
    System.out.println(result);
    writer.write(result);
}

    public static void main(String[] args) {
        try {
            writer = new FileWriter("GWO_Output.txt");
            runGWO();
            writer.close();
            System.out.println("\n✅ Output successfully saved in GWO_Output.txt");
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }
    }
}
