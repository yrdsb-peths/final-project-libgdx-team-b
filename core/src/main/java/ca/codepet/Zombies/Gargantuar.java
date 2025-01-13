package ca.codepet.Zombies;

import ca.codepet.Zombie;
import ca.codepet.Plant;

public class Gargantuar extends Zombie {
    public Gargantuar(int x, int y) {
        super(x, y, "spritePath", 3000, 10, 0);
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
