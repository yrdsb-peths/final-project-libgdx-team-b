package ca.codepet.Zombies;

import ca.codepet.Zombie;
import ca.codepet.Plant;

public class ConeheadZombie extends Zombie {
    public ConeheadZombie(int x, int y) {
        super(x, y, "zombies/ConeheadZombie.png", 200, 0.26f, 380);
        setSize(80, 140);
    }
}
