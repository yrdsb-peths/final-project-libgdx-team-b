package ca.codepet.Zombies;

import ca.codepet.Zombie;
import ca.codepet.Plant;

public class BasicZombie extends Zombie {
    //basic zombie moves 1 tile in 15 seconds
    public BasicZombie(String spritePath) {
        super("spritePath", "basic", 200, 1, 10, 0);
    }
    
}
