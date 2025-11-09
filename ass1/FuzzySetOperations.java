import java.util.*;

public class FuzzySetOperations {

   
    static Map<String, Double> inputFuzzySet(Scanner sc, String setName) {
        System.out.print("\nEnter number of elements in " + setName + ": ");
        int n = sc.nextInt();
        sc.nextLine(); 

        Map<String, Double> set = new HashMap<>();

        System.out.println("Enter element and its membership value (0 to 1):");
        for (int i = 0; i < n; i++) {
            System.out.print("Element " + (i + 1) + ": ");
            String element = sc.nextLine();

            System.out.print("Membership value for '" + element + "': ");
            double value = sc.nextDouble();
            sc.nextLine(); 

           
            while (value < 0 || value > 1) {
                System.out.println("Invalid! Membership must be between 0 and 1.");
                System.out.print("Re-enter value for '" + element + "': ");
                value = sc.nextDouble();
                sc.nextLine();
            }
            set.put(element, value);
        }
        return set;
    }

   
    static void displayFuzzySet(Map<String, Double> set, String name) {
        System.out.println("\n" + name + ":");
        System.out.println("Element\t\tMembership");
        System.out.println("--------------------------");
        for (String e : set.keySet()) {
            System.out.printf("%s\t\t%.2f\n", e, set.get(e));
        }
    }

  
    static Map<String, Double> union(Map<String, Double> A, Map<String, Double> B) {
        Map<String, Double> result = new HashMap<>();
        Set<String> allElements = new HashSet<>();
        allElements.addAll(A.keySet());
        allElements.addAll(B.keySet());

        for (String e : allElements) {
            double aVal = A.getOrDefault(e, 0.0);
            double bVal = B.getOrDefault(e, 0.0);
            result.put(e, Math.max(aVal, bVal));
        }
        return result;
    }

    // Intersection
    static Map<String, Double> intersection(Map<String, Double> A, Map<String, Double> B) {
        Map<String, Double> result = new HashMap<>();
        Set<String> common = new HashSet<>(A.keySet());
        common.retainAll(B.keySet());

        for (String e : common) {
            result.put(e, Math.min(A.get(e), B.get(e)));
        }
        return result;
    }

    // Complement
    static Map<String, Double> complement(Map<String, Double> A) {
        Map<String, Double> result = new HashMap<>();
        for (String e : A.keySet()) {
            result.put(e, 1.0 - A.get(e));
        }
        return result;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // Input sets
        Map<String, Double> setA = inputFuzzySet(sc, "Set A");
        Map<String, Double> setB = inputFuzzySet(sc, "Set B");

        // Display inputs
        displayFuzzySet(setA, "Set A");
        displayFuzzySet(setB, "Set B");

        // Perform operations
        Map<String, Double> unionSet = union(setA, setB);
        Map<String, Double> intersectionSet = intersection(setA, setB);
        Map<String, Double> complementA = complement(setA);
        Map<String, Double> complementB = complement(setB);

        System.out.println("\n==== RESULTS ====");
        displayFuzzySet(unionSet, "Union of A and B");
        if (intersectionSet.isEmpty()) {
            System.out.println("\nIntersection of A and B: No common elements");
        } else {
            displayFuzzySet(intersectionSet, "Intersection of A and B");
        }
        displayFuzzySet(complementA, "Complement of A");
        displayFuzzySet(complementB, "Complement of B");

        sc.close();
    }
}
