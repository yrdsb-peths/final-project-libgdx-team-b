package ca.codepet.Zombies;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;

import ca.codepet.Plant;
import ca.codepet.worlds.DayWorld;

public class BasicZombie extends Zombie {
    static int WALK_FRAMES = 7;  // Based on the atlas file which has 7 frames (tile000 to tile006)

    public BasicZombie(DayWorld theWorld) {
        super(theWorld, new Texture("images/zombie.png"), 100, 10, 2.0f);
        
        TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("zombies/BasicZombieWalk.atlas"));
        AtlasRegion[] walk = new AtlasRegion[WALK_FRAMES];
        for (int i = 0; i < WALK_FRAMES; i++) {
            walk[i] = atlas.findRegion("tile00" + i);
        }
        Animation<AtlasRegion> walkanim = new Animation<>(0.2f, walk);
        animations.put("walk", walkanim);
        animations.put("attack", walkanim); // Using walk animation for attack for now
        
        currentAnimation = "walk"; // Ensure initial animation is set
    }
}
