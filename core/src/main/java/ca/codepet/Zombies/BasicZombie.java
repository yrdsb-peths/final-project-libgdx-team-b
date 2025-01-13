package ca.codepet.Zombies;

import ca.codepet.Zombie;
import ca.codepet.Plant;

public class BasicZombie extends Zombie {
    //basic zombie moves 1 tile in 15 seconds
    public BasicZombie(int x, int y) {
        super(x, y, "zombies/BasicZombie.png", 200, 0.25f, 0);
        setSize(80, 110); // Set the size of the sprite
    }
    
}
