package ca.codepet.Zombies;

import ca.codepet.Zombie;
import ca.codepet.Plant;

public class BasicZombie extends Zombie {
    //basic zombie moves 1 tile in 15 seconds
    public BasicZombie(int x, int y) {
        super(x, y, "zombies/BasicZombie.png", 200, 10, 0);
    }
    
}
