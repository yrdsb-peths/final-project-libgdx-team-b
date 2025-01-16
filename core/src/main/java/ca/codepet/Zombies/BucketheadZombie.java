package ca.codepet.Zombies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;

import ca.codepet.Plants.Plant;
import ca.codepet.worlds.DayWorld;

public class BucketheadZombie extends Zombie {
    static int WALK_FRAMES = 7;
    static int ATTACK_FRAMES = 7;
    static int FRAMES_DEATH = 9;

    public BucketheadZombie(DayWorld theWorld) {
        super(theWorld, new Texture("zombies/bucketheadZombie/BucketHeadZombie.png"), 300, 50, 2.0f);

        // Load walk animation
        TextureAtlas walkAtlas = new TextureAtlas(
                Gdx.files.internal("zombies/bucketheadZombie/BucketheadZombieWalk.atlas"));
        AtlasRegion[] walk = new AtlasRegion[WALK_FRAMES];
        for (int i = 0; i < WALK_FRAMES; i++) {
            walk[i] = walkAtlas.findRegion("tile00" + i);
        }
        Animation<AtlasRegion> walkanim = new Animation<>(0.2f, walk);
        animations.put("walk", walkanim);

        // Load attack animation
        TextureAtlas attackAtlas = new TextureAtlas(
                Gdx.files.internal("zombies/bucketheadZombie/BucketheadZombieAttack.atlas"));
        AtlasRegion[] attack = new AtlasRegion[ATTACK_FRAMES];
        for (int i = 0; i < ATTACK_FRAMES; i++) {
            attack[i] = attackAtlas.findRegion("tile00" + i);
        }
        Animation<AtlasRegion> attackanim = new Animation<>(0.2f, attack);
        animations.put("attack", attackanim);

        TextureAtlas deathAtlas = new TextureAtlas(Gdx.files.internal("zombies/zombie-death.atlas"));
        AtlasRegion[] death = new AtlasRegion[FRAMES_DEATH];
        for (int i = 0; i < FRAMES_DEATH; i++) {
            death[i] = deathAtlas.findRegion("tile00" + i);
        }
        Animation<AtlasRegion> deathanim = new Animation<>(0.2f, death);
        animations.put("death", deathanim);

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

    @Override
    public void die() {
        currentAnimation = "death";
        super.die();
    }
}
