import matplotlib.pyplot as plt
import numpy as np

# ✅ Define your fitness function
def fitness(x1, x2):
    return (100 * (x2 - x1**2)**2) + (1 - x1)**2

# ✅ Example iterations (list of tables: each iteration has multiple points)
iterations = [
    [(0.5, 0.2), (1.0, 0.5)],                 # Iteration 1
    [(1.5, 2.0), (2.0, 4.0)],                 # Iteration 2
    [(2.5, 6.5), (2.0, 2.5), (1.8, 1.2)],     # Iteration 3
    [(1.2, 0.8), (0.9, 0.5), (2.1, 4.5)],     # Iteration 4
]

# ✅ Create a smooth surface for background
X1 = np.linspace(-3, 3, 100)
X2 = np.linspace(-1, 10, 100)
X1, X2 = np.meshgrid(X1, X2)
Z = fitness(X1, X2)

# ✅ Plot subplots for each iteration
fig = plt.figure(figsize=(16, 12))

for i, points in enumerate(iterations):
    ax = fig.add_subplot(2, 2, i+1, projection='3d')
    ax.plot_surface(X1, X2, Z, cmap='viridis', alpha=0.6, edgecolor='none')
    
    x1_vals = [p[0] for p in points]
    x2_vals = [p[1] for p in points]
    f_vals = [fitness(p[0], p[1]) for p in points]
    
    ax.scatter(x1_vals, x2_vals, f_vals, color='red', s=70, marker='o', label=f"Iteration {i+1}")
    
    # Label points
    for j, (x1, x2) in enumerate(points):
        ax.text(x1, x2, fitness(x1, x2), f"P{j+1}", color='black')
    
    ax.set_title(f"Iteration {i+1}", fontsize=14)
    ax.set_xlabel("X1")
    ax.set_ylabel("X2")
    ax.set_zlabel("Fitness")
    ax.legend()

plt.tight_layout()
plt.show()
