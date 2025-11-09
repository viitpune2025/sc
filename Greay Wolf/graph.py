import numpy as np
import matplotlib.pyplot as plt

# ---------------------------
# Objective Function
# ---------------------------
def fitness(point):
    x1, x2 = point
    return (x1 * x1) - (x1 * x2) + (x2 * x2) + 2 * x1 + 4 * x2 + 3

# ---------------------------
# Initialize Positions
# ---------------------------
def initialize_positions(pop_size, lb, ub):
    return np.random.uniform(lb, ub, (pop_size, 2))

# ---------------------------
# Best Wolves (alpha, beta, delta)
# ---------------------------
def best_values(positions):
    fitness_vals = np.array([fitness(p) for p in positions])
    sorted_idx = np.argsort(fitness_vals)
    return positions[sorted_idx[:3]]

# ---------------------------
# GWO Update Rule
# ---------------------------
def gwo_update(best, curr_pos, a, lb, ub):
    new_pos = np.zeros(2)
    for d in range(2):
        sum_val = 0
        for i in range(3):  # alpha, beta, delta
            C = 2 * np.random.rand()
            D = abs(C * best[i][d] - curr_pos[d])
            A = 2 * a * np.random.rand() - a
            sum_val += best[i][d] - A * D
        new_pos[d] = sum_val / 3.0
        new_pos[d] = np.clip(new_pos[d], lb, ub)  # boundary check
    return new_pos

# ---------------------------
# Run GWO (with position history)
# ---------------------------
def run_gwo(pop_size=10, max_iter=5, lb=-5, ub=5):
    positions = initialize_positions(pop_size, lb, ub)
    history = []  # store positions per iteration

    for iter in range(1, max_iter + 1):
        a = 2 * (1 - iter / max_iter)
        best = best_values(positions)

        new_positions = []
        for j in range(pop_size):
            new_pos = gwo_update(best, positions[j], a, lb, ub)
            if fitness(new_pos) < fitness(positions[j]):
                positions[j] = new_pos
            new_positions.append([positions[j][0], positions[j][1], fitness(positions[j])])
        history.append(np.array(new_positions))

    return history

# ---------------------------
# Main
# ---------------------------
if __name__ == "__main__":
    pop_size = 8
    max_iter = 5
    lb, ub = -5, 5

    # Run GWO for 5 iterations
    history = run_gwo(pop_size, max_iter, lb, ub)

    # Meshgrid for surface plot
    x = np.linspace(lb, ub, 100)
    y = np.linspace(lb, ub, 100)
    X, Y = np.meshgrid(x, y)
    Z = (X * X) - (X * Y) + (Y * Y) + 2 * X + 4 * Y + 3

    # Plot each iteration separately
    fig = plt.figure(figsize=(16, 10))

    for i in range(max_iter):
        ax = fig.add_subplot(2, 3, i+1, projection="3d")
        ax.plot_surface(X, Y, Z, cmap="viridis", alpha=0.6)

        points = history[i]
        ax.scatter(points[:, 0], points[:, 1], points[:, 2], c="red", s=40, label=f"Iter {i+1}")

        ax.set_title(f"Iteration {i+1}")
        ax.set_xlabel("x1")
        ax.set_ylabel("x2")
        ax.set_zlabel("Fitness")
        ax.legend()

    plt.tight_layout()
    plt.show()
