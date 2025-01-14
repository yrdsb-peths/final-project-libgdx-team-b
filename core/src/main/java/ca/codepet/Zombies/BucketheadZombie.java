package ca.codepet.Zombies;

import com.badlogic.gdx.graphics.Texture;

import ca.codepet.Plant;
import ca.codepet.worlds.DayWorld;

public class BucketheadZombie extends Zombie {
    public BucketheadZombie(DayWorld theWorld) {
         super(theWorld, new Texture("images/bucketHeadZombie.png"), 300, 50, 2.0f);
    }
}
