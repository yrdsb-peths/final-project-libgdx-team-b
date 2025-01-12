package ca.codepet.Zombies;

import ca.codepet.Zombie;
import ca.codepet.Plant;

public class BasicZombie extends Zombie {
    //basic zombie moves 1 tile in 15 seconds
    public BasicZombie(int x, int y) {
        super(x, y, "spritePath", "basic", 200, 1, 10, 0);
    }
    
}
