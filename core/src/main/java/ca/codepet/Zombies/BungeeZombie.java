package ca.codepet.Zombies;

import ca.codepet.Zombie;
import ca.codepet.Plant;

public class BungeeZombie extends Zombie {
    public BungeeZombie(String spritePath) {
        super("zombies/bungee.png", "bungee", 450, 0, 0, 0);
    }

    public void mark(Plant plant)
    {
        // mark random plant in the plant array
    }

    public void drop(Plant plant)
    {
        // zombie drops down and hangs for 3.5 seconds
    }

    public void steal(Plant plant)
    {
        // steal the plant
    }
}
