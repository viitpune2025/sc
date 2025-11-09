import matplotlib.pyplot as plt

# Read the data from the file
iterations = []
fitness_values = []

with open("particles.txt", "r") as file:
    for line in file:
        # Skip global best lines separately
        if line.startswith("GLOBAL"):
            parts = line.strip().split()
            iteration = int(parts[1])
            fitness = float(parts[-1])
            iterations.append(iteration)
            fitness_values.append(fitness)

# Plotting the Global Best Fitness vs Iteration
plt.figure(figsize=(8, 5))
plt.plot(iterations, fitness_values, marker='o', linestyle='-', linewidth=2, markersize=6)

plt.title("Global Best Fitness vs Iteration")
plt.xlabel("Iteration")
plt.ylabel("Global Best Fitness (gbest)")
plt.grid(True)
plt.tight_layout()
plt.show()
