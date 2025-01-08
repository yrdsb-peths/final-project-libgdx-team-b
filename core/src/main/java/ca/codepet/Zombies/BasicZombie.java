package ca.codepet.Zombies;

import ca.codepet.Plant;

public class BasicZombie extends Zombie {
    //basic zombie moves 1 tile in 15 seconds
    public BasicZombie(String spritePath) {
        super(spritePath, "basic", 200, 1, 10, 0);
    }

    @Override
    public void damage(Plant plant, int dmg) {
        // zombie damages a plant
        plant.damage(dmg);
    }
    
}
