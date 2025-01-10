package ca.codepet.Zombies;

import ca.codepet.Zombie;
import ca.codepet.Plant;

public class Gargantuar extends Zombie {
    public Gargantuar(String spritePath) {
        super("spritePath", "gargantuar", 3000, 0, 10, 0);
    }

    public void smash(Plant plant, int dmg)
    {
        // takes a sec to swing  kill the plant instantly
    }

    public void throwImp()
    {
        // throws an imp when half hp
    }
}
