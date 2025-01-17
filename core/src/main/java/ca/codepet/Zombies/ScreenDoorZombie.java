package ca.codepet.Zombies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;

import ca.codepet.Plants.Plant;
import ca.codepet.worlds.DayWorld;

public class ScreenDoorZombie extends Zombie {
    static int WALK_FRAMES = 7;
    static int ATTACK_FRAMES = 7;
    static int FRAMES_DEATH = 9;

    static int SHIELD_1_HP = 380; // Screendoor fresh
    static int SHIELD_2_HP = 360; // Screendoor damaged
    static int SHIELD_3_HP = 360; // Screendoor very damaged
    static int STAGE_1_HP = 270; // Basic zombie form
    static int STAGE_2_HP = 90; // Basic zombie form

    static int TOTAL_HP = STAGE_1_HP + STAGE_2_HP;
    static int TOTAL_SHIELD = SHIELD_1_HP + SHIELD_2_HP + SHIELD_3_HP;

    static float FRAME_DURATION = 0.7f;

    public ScreenDoorZombie(DayWorld theWorld) {
        super(theWorld, new Texture("zombies/screenDoorZombie/ScreenDoorZombie.png"), TOTAL_HP, TOTAL_SHIELD, 100, 1f, 18.8f);
        shield = TOTAL_SHIELD;

        // Adjust scale
        setScaleX(0.95f);
        setScaleY(0.82f);

        // Load walk animations for each stage
        loadWalkAnimation(1, "zombies/screenDoorZombie/screendoor-idle-1.atlas");
        loadWalkAnimation(2, "zombies/screenDoorZombie/screendoor-idle-2.atlas");
        loadWalkAnimation(3, "zombies/screenDoorZombie/screendoor-idle-3.atlas");
        loadWalkAnimation(4, "zombies/basicZombie/basiczombie-idle-1.atlas");
        loadWalkAnimation(5, "zombies/basicZombie/basiczombie-idle-2.atlas");


        // Load attack animations for each stage
        loadAttackAnimation(1, "zombies/screendoorZombie/screendoor-attack-1.atlas");
        loadAttackAnimation(2, "zombies/screendoorZombie/screendoor-attack-2.atlas");
        loadAttackAnimation(3, "zombies/screendoorZombie/screendoor-attack-3.atlas");
        loadAttackAnimation(4, "zombies/basicZombie/basiczombie-attack-1.atlas");
        loadAttackAnimation(5, "zombies/basicZombie/basiczombie-attack-2.atlas");

        // Load death animation
        TextureAtlas deathAtlas = new TextureAtlas(Gdx.files.internal("zombies/zombie-death.atlas"));
        AtlasRegion[] death = new AtlasRegion[FRAMES_DEATH];
        for (int i = 0; i < FRAMES_DEATH; i++) {
            death[i] = deathAtlas.findRegion("tile00" + i);
        }
        Animation<AtlasRegion> deathanim = new Animation<>(0.4f, death);
        deathanim.setPlayMode(Animation.PlayMode.NORMAL);
        animations.put("death", deathanim);

        // Set death animation dimensions
        deathWidth = 51 * 3;
        deathHeight = 40 * 3;

        currentAnimation = "walk1";
    }

    private void loadAttackAnimation(int stage, String atlasPath) {
        TextureAtlas attackAtlas = new TextureAtlas(Gdx.files.internal(atlasPath));
        AtlasRegion[] attack = new AtlasRegion[ATTACK_FRAMES];
        for (int i = 0; i < ATTACK_FRAMES; i++) {
            attack[i] = attackAtlas.findRegion("tile00" + i);
        }
        Animation<AtlasRegion> attackanim = new Animation<>(0.2f, attack);
        animations.put("attack" + stage, attackanim);
    }

    private void loadWalkAnimation(int stage, String atlasPath) {
        TextureAtlas walkAtlas = new TextureAtlas(Gdx.files.internal(atlasPath));
        AtlasRegion[] walk = new AtlasRegion[WALK_FRAMES];
        for (int i = 0; i < WALK_FRAMES; i++) {
            walk[i] = walkAtlas.findRegion("tile00" + i);
        }
        Animation<AtlasRegion> walkanim = new Animation<>(FRAME_DURATION, walk);
        animations.put("walk" + stage, walkanim);
    }

    private void updateAnimation() {
        if (currentAnimation.equals("death")) {
            return;
        }

        String baseAnim = isAttacking ? "attack" : "walk";
        if (shield > SHIELD_2_HP + SHIELD_3_HP) {
            currentAnimation = baseAnim + "1";
        } else if (shield > SHIELD_3_HP) {
            currentAnimation = baseAnim + "2";
        } else if (hp > STAGE_1_HP + STAGE_2_HP) {
            currentAnimation = baseAnim + "3";
        } else if (hp > STAGE_1_HP) {
            currentAnimation = baseAnim + "4";
        } else {
            currentAnimation = baseAnim + "5";
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
