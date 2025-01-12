package ca.codepet.Zombies;

import java.util.Random;

import ca.codepet.Plant;
import ca.codepet.Zombie;

public class BungeeZombie extends Zombie {
    public BungeeZombie(int x, int y) {
        super(x, y, "zombies/bungee.png", "bungee", 450, 0, 0, 0);
    }

    public void mark(Plant[][] plants) {
        Random random = new Random();
        int x, y;
        Plant selectedPlant = null;

        // Find a random plant
        while (selectedPlant == null) {
            x = random.nextInt(plants.length);
            y = random.nextInt(plants[0].length);
            selectedPlant = plants[x][y];
        }

        // Mark the plant

        // 3 seconds
        
        drop(selectedPlant);
        
        // 3.5 seconds

        steal(selectedPlant);
    }

    public void drop(Plant plant) {
        // Set the position of the BungeeZombie to the plant's position
        this.setPosition(plant.getRect().getX(), plant.getRect().getY());
    }

    public void steal(Plant plant) {
        // Zombie gets pulled up along with the plant
        this.setPosition(getX(), getY() + 100);
        plant.setPosition(getX(), getY() + 100);
    }
}
