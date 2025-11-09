import matplotlib.pyplot as plt

# Read the best lengths from the text file
best_lengths = []
with open("ACO_Output.txt", "r") as f:
    for line in f:
        if line.startswith("BestLength="):
            best_lengths.append(int(line.strip().split("=")[1]))

iterations = list(range(1, len(best_lengths) + 1))

plt.figure(figsize=(7, 5))
plt.plot(iterations, best_lengths, marker='o', linestyle='-', linewidth=2)
plt.title("ACO - Best Tour Length per Iteration", fontsize=14)
plt.xlabel("Iteration", fontsize=12)
plt.ylabel("Best Tour Length", fontsize=12)
plt.grid(True, linestyle="--", alpha=0.6)
plt.show()
