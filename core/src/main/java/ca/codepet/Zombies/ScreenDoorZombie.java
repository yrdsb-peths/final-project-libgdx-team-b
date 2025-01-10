package ca.codepet.Zombies;

import ca.codepet.Zombie;
import ca.codepet.Plant;

// this zombie only blocks peas
// lobbed plants and piercing plants can bypass the shield
public class ScreenDoorZombie extends Zombie {
    public ScreenDoorZombie(String spritePath) {
        super("spritePath", "screen door", 200, 1, 10, 1370);
    }
}
