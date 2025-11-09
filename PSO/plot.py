import matplotlib.pyplot as plt
from mpl_toolkits.mplot3d import Axes3D

# Particle positions and fitness values from Java output
x1 = [2.8874, 0.0314, 2.3273, -0.3856, 2.8920, 1.7690, 2.6429, 0.5408]
x2 = [3.1106, 0.7726, 2.4949, 1.7493, 2.7913, 3.4221, 2.7145, 5.0]
fitness = [356.39, 457.50, 1796.63, 165.97, 1.56, 308.70, 119.55, 1420.81]

# Create 3D scatter plot
fig = plt.figure(figsize=(8, 6))
ax = fig.add_subplot(111, projection="3d")

ax.scatter(x1, x2, fitness, c=fitness, cmap="viridis", s=80, edgecolor="k")

ax.set_xlabel("X1")
ax.set_ylabel("X2")
ax.set_zlabel("Fitness")
ax.set_title("Particle Positions vs Fitness")

plt.show()
