package ca.codepet.Zombies;

import ca.codepet.Zombie;
import ca.codepet.Plant;

public class PoleVaultingZombie extends Zombie {
    public PoleVaultingZombie(int x, int y) {
        super(x, y, "spritePath", 340, 18, 0);
    }

    public void jump()
    {
        // jump over plant
        // speed decreases to walk speed
    }
}
