package ca.codepet.Zombies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;

import ca.codepet.Plant;
import ca.codepet.worlds.DayWorld;

public class BucketheadZombie extends Zombie {
    static int WALK_FRAMES = 7;
    static int ATTACK_FRAMES = 7;
    static int DEATH_FRAMES = 8;

    public BucketheadZombie(DayWorld theWorld) {
        super(theWorld, new Texture("images/bucketHeadZombie.png"), 300, 50, 2.0f);
        
        // Load walk animation
        TextureAtlas walkAtlas = new TextureAtlas(Gdx.files.internal("zombies/BucketheadZombieWalk.atlas"));
        AtlasRegion[] walk = new AtlasRegion[WALK_FRAMES];
        for (int i = 0; i < WALK_FRAMES; i++) {
            walk[i] = walkAtlas.findRegion("tile00" + i);
        }
        Animation<AtlasRegion> walkanim = new Animation<>(0.2f, walk);
        animations.put("walk", walkanim);

        // Load attack animation
        TextureAtlas attackAtlas = new TextureAtlas(Gdx.files.internal("zombies/BucketheadZombieAttack.atlas"));
        AtlasRegion[] attack = new AtlasRegion[ATTACK_FRAMES];
        for (int i = 0; i < ATTACK_FRAMES; i++) {
            attack[i] = attackAtlas.findRegion("tile00" + i);
        }
        Animation<AtlasRegion> attackanim = new Animation<>(0.2f, attack);
        animations.put("attack", attackanim);

        // Load death animation
        TextureAtlas deathAtlas = new TextureAtlas(Gdx.files.internal("zombies/zombie-death.atlas"));
        AtlasRegion[] death = new AtlasRegion[DEATH_FRAMES];
        for (int i = 1; i <= DEATH_FRAMES; i++) {
            death[i-1] = deathAtlas.findRegion("138563-" + i);
        }
        Animation<AtlasRegion> deathanim = new Animation<>(0.15f, death); // Faster animation
        animations.put("death", deathanim);
        
        currentAnimation = "walk";
    }

    @Override
    public void move() {
        if (!isDying()) {  // Only change animation if not dying
            currentAnimation = "walk";
            super.move();
        }
    }

    @Override
    public void attack(Plant plant) {
        if (!isDying() && plant != null) {  // Only change animation if not dying
            currentAnimation = "attack";
            super.attack(plant);
        }
    }
}
