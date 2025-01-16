package ca.codepet.Zombies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;

import ca.codepet.Plants.Plant;
import ca.codepet.worlds.DayWorld;

public class BasicZombie extends Zombie {
    static int WALK_FRAMES = 7; // Based on the atlas file which has 7 frames (tile000 to tile006)
    static int ATTACK_FRAMES = 7;
    static int FRAMES_DEATH = 9;
    static float FRAME_DURATION = 0.7f;

    public BasicZombie(DayWorld theWorld) {
        super(theWorld, new Texture("zombies/basicZombie/BasicZombieWalk.png"), 100, 10, 2.0f);

        // Load walk animation
        TextureAtlas walkAtlas = new TextureAtlas(Gdx.files.internal("zombies/basicZombie/BasicZombieWalk.atlas"));
        AtlasRegion[] walk = new AtlasRegion[WALK_FRAMES];
        for (int i = 0; i < WALK_FRAMES; i++) {
            walk[i] = walkAtlas.findRegion("tile00" + i);
        }
        Animation<AtlasRegion> walkanim = new Animation<>(FRAME_DURATION, walk);
        animations.put("walk", walkanim);

        // Load attack animation
        TextureAtlas attackAtlas = new TextureAtlas(Gdx.files.internal("zombies/basicZombie/BasicZombieAttack.atlas"));
        AtlasRegion[] attack = new AtlasRegion[ATTACK_FRAMES];
        for (int i = 0; i < ATTACK_FRAMES; i++) {
            attack[i] = attackAtlas.findRegion("tile00" + i);
        }
        Animation<AtlasRegion> attackanim = new Animation<>(FRAME_DURATION, attack);
        animations.put("attack", attackanim);

        // Fix death animation with proper size and timing
        TextureAtlas deathAtlas = new TextureAtlas(Gdx.files.internal("zombies/zombie-death.atlas"));
        AtlasRegion[] death = new AtlasRegion[FRAMES_DEATH];
        for (int i = 0; i < FRAMES_DEATH; i++) {
            death[i] = deathAtlas.findRegion("tile00" + i);
        }
        // Slow down death animation and make it non-looping

        Animation<AtlasRegion> deathanim = new Animation<>(0.4f, death);
        deathanim.setPlayMode(Animation.PlayMode.NORMAL);  // Make it play only once

        animations.put("death", deathanim);

        // Set custom size for death animation frames
        deathWidth = 51 * 3; // Match original width from atlas
        deathHeight = 40 * 3; // Match original height from atlas

        currentAnimation = "walk";
    }

    @Override
    public void attack(Plant plant) {
        if (plant != null) {
            currentAnimation = "attack";
            super.attack(plant);
        }
    }

    @Override
    public void move(float delta) {
        currentAnimation = "walk";
        super.move(delta);
    }
}
