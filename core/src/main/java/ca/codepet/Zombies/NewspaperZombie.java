package ca.codepet.Zombies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;

import ca.codepet.Plants.Plant;
import ca.codepet.worlds.DayWorld;

public class NewspaperZombie extends Zombie {
    static int WALK_FRAMES = 7;
    static int ATTACK_FRAMES = 7;
    static int FRAMES_DEATH = 9;

    static int STAGE_1_HP = 160;
    static int STAGE_2_HP = 200;

    static int TOTAL_HP = STAGE_1_HP + STAGE_2_HP;

    static float FRAME_DURATION = 0.7f;

    private static final float NORMAL_SPEED = 15f;
    private static final float FAST_SPEED = 25f;

    public NewspaperZombie(DayWorld theWorld) {
        super(theWorld, new Texture("zombies/newspaperZombie/NewspaperZombie.png"), TOTAL_HP, 0, 50, 2.0f, NORMAL_SPEED);

        // Adjust scale
        setScaleX(0.95f);
        setScaleY(0.83f);

        // Load walk animation 1
        TextureAtlas walkAtlas = new TextureAtlas(
                Gdx.files.internal("zombies/newspaperZombie/newspaper-idle-1.atlas"));

        AtlasRegion[] walk = new AtlasRegion[WALK_FRAMES];
        for (int i = 0; i < WALK_FRAMES; i++) {
            walk[i] = walkAtlas.findRegion("tile00" + i);
        }
        Animation<AtlasRegion> walkanim = new Animation<>(FRAME_DURATION, walk);
        animations.put("walk1", walkanim);

        // Load walk animation 2
        TextureAtlas walkAtlas2 = new TextureAtlas(
                Gdx.files.internal("zombies/newspaperZombie/newspaper-idle-2.atlas"));
        AtlasRegion[] walk2 = new AtlasRegion[WALK_FRAMES];
        for (int i = 0; i < WALK_FRAMES; i++) {
            walk2[i] = walkAtlas2.findRegion("tile00" + i);
        }
        Animation<AtlasRegion> walkanim2 = new Animation<>(FRAME_DURATION, walk2);
        animations.put("walk2", walkanim2);

        // Load attack animation 1
        TextureAtlas attackAtlas1 = new TextureAtlas(
                Gdx.files.internal("zombies/newspaperZombie/newspaper-attack-1.atlas"));
        AtlasRegion[] attack1 = new AtlasRegion[ATTACK_FRAMES];
        for (int i = 0; i < ATTACK_FRAMES; i++) {
            attack1[i] = attackAtlas1.findRegion("tile00" + i);
        }
        Animation<AtlasRegion> attackanim1 = new Animation<>(0.2f, attack1);
        animations.put("attack1", attackanim1);

        // Load attack animation 2
        TextureAtlas attackAtlas2 = new TextureAtlas(
                Gdx.files.internal("zombies/newspaperZombie/newspaper-attack-2.atlas"));
        AtlasRegion[] attack2 = new AtlasRegion[ATTACK_FRAMES];
        for (int i = 0; i < ATTACK_FRAMES; i++) {
            attack2[i] = attackAtlas2.findRegion("tile00" + i);
        }
        Animation<AtlasRegion> attackanim2 = new Animation<>(0.2f, attack2);
        animations.put("attack2", attackanim2);

        // Load death animation
        TextureAtlas deathAtlas = new TextureAtlas(Gdx.files.internal("zombies/zombie-death.atlas"));
        AtlasRegion[] death = new AtlasRegion[FRAMES_DEATH];
        for (int i = 0; i < FRAMES_DEATH; i++) {
            death[i] = deathAtlas.findRegion("tile00" + i);
        }
        Animation<AtlasRegion> deathanim = new Animation<>(0.2f, death);
        animations.put("death", deathanim);

        currentAnimation = "walk";
    }

    private void updateAnimation() {
        if (currentAnimation.equals("death")) {
            return;
        }

        String baseAnim = isAttacking ? "attack" : "walk";
        if (hp > STAGE_2_HP) {
            currentAnimation = baseAnim + "1";
        } else {
            currentAnimation = baseAnim + "2";
            setSpeed(FAST_SPEED); // Increase speed once newspaper broken
        }
    }

    @Override
    public void attack(Plant plant) {
        if (plant != null) {
            isAttacking = true;
            updateAnimation();
            super.attack(plant);
        }
    }

    @Override
    public void move(float delta) {
        isAttacking = false;
        updateAnimation();
        super.move(delta);
    }

    @Override
    public void die() {
        currentAnimation = "death";
        super.die();
    }
}
