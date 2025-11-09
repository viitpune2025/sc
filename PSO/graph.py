import numpy as np
import matplotlib.pyplot as plt
from mpl_toolkits.mplot3d import Axes3D

# Fitness function (same as Java)
def function(x1, x2):
    first = x2 - (x1 * x1)
    second = (1 - x1) * (1 - x1)
    third = first * first
    return (100 * third) + second

# --------------------------------------------------------------------------------
# Step 1: Create fitness surface
x1 = np.linspace(-5, 5, 100)
x2 = np.linspace(-5, 5, 100)
X1, X2 = np.meshgrid(x1, x2)
Z = function(X1, X2)

# --------------------------------------------------------------------------------
# Step 2: Put Java particle outputs here (copy from System.out in Java)
# Example: first iteration particle positions
particles = np.array([
    [2.212, 3.009],
    [-2.289, -2.396],
    [-2.393, -4.790],
    [-0.639, 1.692],
    [-3.168, 0.706],
    [0.215, -2.350],
    [-0.742, 1.934],
    [-4.563, 4.791]
])

# Compute their fitness values
particle_fitness = [function(p[0], p[1]) for p in particles]

# --------------------------------------------------------------------------------
# Step 3: Plot
fig = plt.figure(figsize=(10, 7))
ax = fig.add_subplot(111, projection='3d')

# Surface
surf = ax.plot_surface(X1, X2, Z, cmap='viridis', edgecolor='none', alpha=0.6)

# Particles
ax.scatter(particles[:, 0], particles[:, 1], particle_fitness,
           color='red', s=70, label='Particles')

# Labels
ax.set_xlabel('x1')
ax.set_ylabel('x2')
ax.set_zlabel('Fitness Value')
ax.set_title('3D Fitness Landscape with Particles')
ax.legend()

# Color bar
fig.colorbar(surf, shrink=0.5, aspect=10)

plt.show()
