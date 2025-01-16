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

    static int STAGE_1_HP = 100;
    static int STAGE_2_HP = 90;

    public BasicZombie(DayWorld theWorld) {
        super(theWorld, new Texture("zombies/basicZombie/basiczombie-idle-1.png"), 190, 100, 1f, 18.8f);

        // Load walk 1 animation
        TextureAtlas walkAtlas = new TextureAtlas(Gdx.files.internal("zombies/basicZombie/basiczombie-idle-1.atlas"));
        AtlasRegion[] walk = new AtlasRegion[WALK_FRAMES];
        for (int i = 0; i < WALK_FRAMES; i++) {
            walk[i] = walkAtlas.findRegion("tile00" + i);
        }
        Animation<AtlasRegion> walkanim = new Animation<>(FRAME_DURATION, walk);
        animations.put("walk1", walkanim);

        // Load walk 2 animation
        TextureAtlas walkAtlas2 = new TextureAtlas(Gdx.files.internal("zombies/basicZombie/basiczombie-idle-2.atlas"));
        AtlasRegion[] walk2 = new AtlasRegion[WALK_FRAMES];
        for (int i = 0; i < WALK_FRAMES; i++) {
            walk2[i] = walkAtlas2.findRegion("tile00" + i);
        }
        Animation<AtlasRegion> walkanim2 = new Animation<>(FRAME_DURATION, walk2);
        animations.put("walk2", walkanim2);

        // Load attack animation
        TextureAtlas attackAtlas1 = new TextureAtlas(Gdx.files.internal("zombies/basicZombie/basiczombie-attack-1.atlas"));
        AtlasRegion[] attack1 = new AtlasRegion[ATTACK_FRAMES];
        for (int i = 0; i < ATTACK_FRAMES; i++) {
            attack1[i] = attackAtlas1.findRegion("tile00" + i);
        }
        Animation<AtlasRegion> attackanim1 = new Animation<>(FRAME_DURATION, attack1);
        animations.put("attack1", attackanim1);

        // Load attack animation
        TextureAtlas attackAtlas2 = new TextureAtlas(Gdx.files.internal("zombies/basicZombie/basiczombie-attack-2.atlas"));
        AtlasRegion[] attack2 = new AtlasRegion[ATTACK_FRAMES];
        for (int i = 0; i < ATTACK_FRAMES; i++) {
            attack2[i] = attackAtlas2.findRegion("tile00" + i);
        }
        Animation<AtlasRegion> attackanim2 = new Animation<>(FRAME_DURATION, attack2);
        animations.put("attack2", attackanim2);




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

    private void updateAnimation() {
        if (currentAnimation.equals("death")) {
            return;
        }

        String baseAnim = isAttacking ? "attack" : "walk";
        if (hp > STAGE_1_HP) {
            currentAnimation = baseAnim + "1";
        } else {
            currentAnimation = baseAnim + "2";
        }   
    }

    @Override
    public void attack(Plant plant) {
        if (plant != null) {
            currentAnimation = "attack";
            updateAnimation();
            super.attack(plant);
        }
    }

    @Override
    public void move(float delta) {
        currentAnimation = "walk";
        updateAnimation();
        super.move(delta);
    }
}
