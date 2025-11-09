import java.util.*;

public class GreyWolfOptimizerDetailed {

    static final int NUM_WOLVES = 4, MAX_ITER = 5;
    static final double LB = -10, UB = 10;
    static final Random rand = new Random(42);

    // position class - f(x1,x2) = x1^2 + x2^2
    static class Position {
        double x1, x2;
        Position(double x1, double x2) { this.x1 = x1; this.x2 = x2; }
        Position(Position p) { this.x1 = p.x1; this.x2 = p.x2; }
        double fitness() { return x1*x1 + x2*x2; }
        public String toString() { return String.format("(%.2f, %.2f)", x1, x2); }
    }

    // init population
    static Position[] initializePopulation() {
        Position[] wolves = new Position[NUM_WOLVES];
        for (int i=0;i<NUM_WOLVES;i++)
            wolves[i] = new Position(LB + (UB-LB)*rand.nextDouble(), LB + (UB-LB)*rand.nextDouble());
        return wolves;
    }

    // clamp to bounds
    static double clamp(double val) { return Math.max(LB, Math.min(UB, val)); }

    // sort by fitness
    static Position[] sortByFitness(Position[] wolves) {
        return Arrays.stream(wolves)
                .sorted(Comparator.comparingDouble(Position::fitness))
                .toArray(Position[]::new);
    }

    // get leaders α,β,δ
    static Position[] getLeaders(Position[] wolves) {
        Position[] sorted = sortByFitness(wolves);
        return new Position[]{sorted[0], sorted[1], sorted[2]};
    }

    // update wolf position
    static Position updatePosition(Position wolf, Position alpha, Position beta, Position delta, double a) {
        double[] coords = {wolf.x1, wolf.x2}, newCoords = new double[2];
        double[] Acoords = {alpha.x1, alpha.x2}, Bcoords = {beta.x1, beta.x2}, Dcoords = {delta.x1, delta.x2};

        for(int i=0;i<2;i++){
            // alpha
            double r1=rand.nextDouble(), r2=rand.nextDouble();
            double A1=2*a*r1-a, C1=2*r2;
            double D_alpha=Math.abs(C1*Acoords[i]-coords[i]);
            double X1=Acoords[i]-A1*D_alpha;
            // beta
            r1=rand.nextDouble(); r2=rand.nextDouble();
            double A2=2*a*r1-a, C2=2*r2;
            double D_beta=Math.abs(C2*Bcoords[i]-coords[i]);
            double X2=Bcoords[i]-A2*D_beta;
            // delta
            r1=rand.nextDouble(); r2=rand.nextDouble();
            double A3=2*a*r1-a, C3=2*r2;
            double D_delta=Math.abs(C3*Dcoords[i]-coords[i]);
            double X3=Dcoords[i]-A3*D_delta;

            newCoords[i]=clamp((X1+X2+X3)/3);
        }
        return new Position(newCoords[0], newCoords[1]);
    }

    // print table header
    static void printHeader(int iter){
        System.out.println("\n=== Iteration "+iter+" ===");
        System.out.printf("%-7s | %-15s | %-10s%n","Wolf","New Position","f(x1,x2)");
        System.out.println("------------------------------------------");
    }

    // print wolves table
    static void printTable(Position[] wolves, Position alpha, Position beta, Position delta){
        for(int i=0;i<wolves.length;i++){
            String mark="";
            if(wolves[i]==alpha) mark="(α)";
            else if(wolves[i]==beta) mark="(β)";
            else if(wolves[i]==delta) mark="(δ)";
            System.out.printf("| W%-5d %-3s| %-15s | %-10.2f |%n",i+1,mark,wolves[i].toString(),wolves[i].fitness());
        }
        System.out.println("------------------------------------------");
        System.out.printf("Alpha = %s  f = %.2f%n",alpha,alpha.fitness());
        System.out.printf("Beta  = %s  f = %.2f%n",beta,beta.fitness());
        System.out.printf("Delta = %s  f = %.2f%n",delta,delta.fitness());
    }

    // main GWO loop
    static void runGWO(){
        Position[] wolves = initializePopulation();

        for(int iter=1;iter<=MAX_ITER;iter++){
            double a=2.0-(2.0*(iter-1)/(MAX_ITER-1));
            Position[] leaders = getLeaders(wolves);
            Position alpha=leaders[0], beta=leaders[1], delta=leaders[2];

            printHeader(iter);

            Position[] newWolves = new Position[NUM_WOLVES];
            for(int i=0;i<NUM_WOLVES;i++)
                newWolves[i]=updatePosition(wolves[i],alpha,beta,delta,a);

            printTable(newWolves, alpha, beta, delta);
            wolves=newWolves;
        }

        Position[] leaders = getLeaders(wolves);
        System.out.println("\n=== Final Result ===");
        System.out.printf("Alpha = %s  f = %.4f%n",leaders[0],leaders[0].fitness());
    }

    public static void main(String[] args){
        runGWO();
    }
}
