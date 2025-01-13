package ca.codepet.Zombies;

import ca.codepet.Zombie;
import ca.codepet.Plant;

// this zombie only blocks peas
// lobbed plants and piercing plants can bypass the shield
public class NewspaperZombie extends Zombie {
    public NewspaperZombie(int x, int y) {
        super(x, y, "spritePath", 200, 0.35f, 160);
    }

    public void charge()
    {
        // moves faster when newspaper is lost
    }
}
