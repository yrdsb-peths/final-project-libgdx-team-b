package ca.codepet.Zombies;

import ca.codepet.Zombie;
import ca.codepet.Plant;

public class BungeeZombie extends Zombie {
    public BungeeZombie(String spritePath) {
        super("spritePath", "bungee", 450, 0, 0, 0);
    }

    public void mark(Plant plant)
    {
        // mark the plant
    }

    public void drop(Plant plant)
    {
        // drop the zombie
    }

    // hangs for 3.5 seconds before stealing plant
    public void steal(Plant plant)
    {
        // steal the plant
    }
}
