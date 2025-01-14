package ca.codepet.Zombies;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import ca.codepet.Plant;
import ca.codepet.worlds.DayWorld;



public class BasicZombie extends Zombie {

   

    public BasicZombie(DayWorld theWorld) {
        super(theWorld, new Texture("images/zombie.png"), 100, 10, 2.0f);
        
    }

}
