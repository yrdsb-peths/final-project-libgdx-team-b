package ca.codepet.Zombies;

import ca.codepet.Zombie;
import ca.codepet.Plant;

public class BucketheadZombie extends Zombie {
    public BucketheadZombie(int x, int y) {
        super(x, y, "zombies/BucketheadZombie.png", 200, 0.26f, 1100);
        setSize(80, 140);
    }
}
