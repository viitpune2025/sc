import re
import matplotlib.pyplot as plt

# Read the GWO output file
file_path = "GWO_Output.txt"

# Store best (alpha) fitness per iteration
iterations = []
fitness_values = []

with open(file_path, "r") as file:
    lines = file.readlines()
    current_iter = None
    for line in lines:
        # Detect iteration header
        iter_match = re.search(r"=== Iteration (\d+) ===", line)
        if iter_match:
            current_iter = int(iter_match.group(1))
        
        # Detect Alpha line (fitness)
        alpha_match = re.search(r"Alpha = .* f = ([\d\.]+)", line)
        if alpha_match and current_iter is not None:
            fitness = float(alpha_match.group(1))
            iterations.append(current_iter)
            fitness_values.append(fitness)

# Plot
plt.figure(figsize=(8,5))
plt.plot(iterations, fitness_values, marker='o', linestyle='-', linewidth=2)
plt.title("Grey Wolf Optimizer - Alpha Fitness per Iteration")
plt.xlabel("Iteration")
plt.ylabel("Best Fitness (Alpha)")
plt.grid(True)
plt.tight_layout()
plt.show()
