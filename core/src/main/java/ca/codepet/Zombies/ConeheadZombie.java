package ca.codepet.Zombies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;

import ca.codepet.Plants.Plant;
import ca.codepet.worlds.DayWorld;

public class ConeheadZombie extends Zombie {
    static int WALK_FRAMES = 7;
    static int ATTACK_FRAMES = 7;
    static int FRAMES_DEATH = 9;

    static int STAGE_0_HP = 100;
    static int STAGE_1_HP = 130;
    static int STAGE_2_HP = 130;
    static int STAGE_3_HP = 110;
    static int STAGE_4_HP = 270;

    static int TOTAL_HP = STAGE_1_HP + STAGE_2_HP + STAGE_3_HP + STAGE_4_HP;

    static float FRAME_DURATION = 0.7f;
    

    public ConeheadZombie(DayWorld theWorld) {
        super(theWorld, new Texture("zombies/coneZombie/ConeheadZombie.png"), TOTAL_HP, 100, 1f, 18.8f);

        // Adjust scale

        setScaleX(1f);
        setScaleY(0.9f);


        // Initialize hp
        this.hp = TOTAL_HP;

        // Set initial animation state
        currentAnimation = "walk1";
        isAttacking = false;

        // Load walk animation
        TextureAtlas walkAtlas = new TextureAtlas(Gdx.files.internal("zombies/coneZombie/conehead-idle-1.atlas"));
        AtlasRegion[] walk = new AtlasRegion[WALK_FRAMES];
        for (int i = 0; i < WALK_FRAMES; i++) {
            walk[i] = walkAtlas.findRegion("tile00" + i);
        }
        Animation<AtlasRegion> walkanim = new Animation<>(FRAME_DURATION, walk);
        animations.put("walk1", walkanim);

        // load walk animation 2
        TextureAtlas walkAtlas2 = new TextureAtlas(Gdx.files.internal("zombies/coneZombie/conehead-idle-2.atlas"));
        AtlasRegion[] walk2 = new AtlasRegion[WALK_FRAMES];
        for (int i = 0; i < WALK_FRAMES; i++) {
            walk2[i] = walkAtlas2.findRegion("tile00" + i);
        }
        Animation<AtlasRegion> walkanim2 = new Animation<>(FRAME_DURATION, walk2);
        animations.put("walk2", walkanim2);

        // load walk animation 3
        TextureAtlas walkAtlas3 = new TextureAtlas(Gdx.files.internal("zombies/coneZombie/conehead-idle-3.atlas"));
        AtlasRegion[] walk3 = new AtlasRegion[WALK_FRAMES];
        for (int i = 0; i < WALK_FRAMES; i++) {
            walk3[i] = walkAtlas3.findRegion("tile00" + i);
        }
        Animation<AtlasRegion> walkanim3 = new Animation<>(FRAME_DURATION, walk3);
        animations.put("walk3", walkanim3);

        // Load walk 4 animation
        TextureAtlas walkAtlas4 = new TextureAtlas(Gdx.files.internal("zombies/basicZombie/basiczombie-idle-1.atlas"));
        AtlasRegion[] walk4 = new AtlasRegion[WALK_FRAMES];
        for (int i = 0; i < WALK_FRAMES; i++) {
            walk4[i] = walkAtlas4.findRegion("tile00" + i);
        }
        Animation<AtlasRegion> walkanim4 = new Animation<>(FRAME_DURATION, walk4);
        animations.put("walk4", walkanim4);

        // Load walk 5 animation
        TextureAtlas walkAtlas5 = new TextureAtlas(Gdx.files.internal("zombies/basicZombie/basiczombie-idle-2.atlas"));
        AtlasRegion[] walk5 = new AtlasRegion[WALK_FRAMES];
        for (int i = 0; i < WALK_FRAMES; i++) {
            walk5[i] = walkAtlas5.findRegion("tile00" + i);
        }
        Animation<AtlasRegion> walkanim5 = new Animation<>(FRAME_DURATION, walk5);
        animations.put("walk5", walkanim5);

        // Load attack animation
        TextureAtlas attackAtlas = new TextureAtlas(Gdx.files.internal("zombies/coneZombie/conehead-attack-1.atlas"));
        AtlasRegion[] attack = new AtlasRegion[ATTACK_FRAMES];
        for (int i = 0; i < ATTACK_FRAMES; i++) {
            attack[i] = attackAtlas.findRegion("tile00" + i);
        }
        Animation<AtlasRegion> attackanim = new Animation<>(FRAME_DURATION, attack);
        animations.put("attack1", attackanim);

        // Load attack animation 2
        TextureAtlas attackAtlas2 = new TextureAtlas(Gdx.files.internal("zombies/coneZombie/conehead-attack-2.atlas"));
        AtlasRegion[] attack2 = new AtlasRegion[ATTACK_FRAMES];
        for (int i = 0; i < ATTACK_FRAMES; i++) {
            attack2[i] = attackAtlas2.findRegion("tile00" + i);
        }
        Animation<AtlasRegion> attackanim2 = new Animation<>(FRAME_DURATION, attack2);
        animations.put("attack2", attackanim2);

        // Load attack animation 3
        TextureAtlas attackAtlas3 = new TextureAtlas(Gdx.files.internal("zombies/coneZombie/conehead-attack-3.atlas"));
        AtlasRegion[] attack3 = new AtlasRegion[ATTACK_FRAMES];
        for (int i = 0; i < ATTACK_FRAMES; i++) {
            attack3[i] = attackAtlas3.findRegion("tile00" + i);
        }
        Animation<AtlasRegion> attackanim3 = new Animation<>(FRAME_DURATION, attack3);
        animations.put("attack3", attackanim3);

        // Load attack animation 4
        TextureAtlas attackAtlas4 = new TextureAtlas(Gdx.files.internal("zombies/basicZombie/basiczombie-attack-1.atlas"));
        AtlasRegion[] attack4 = new AtlasRegion[ATTACK_FRAMES];
        for (int i = 0; i < ATTACK_FRAMES; i++) {
            attack4[i] = attackAtlas4.findRegion("tile00" + i);
        }
        Animation<AtlasRegion> attackanim4 = new Animation<>(FRAME_DURATION, attack4);
        animations.put("attack4", attackanim4);

        // Load attack animation 5
        TextureAtlas attackAtlas5 = new TextureAtlas(Gdx.files.internal("zombies/basicZombie/basiczombie-attack-2.atlas"));
        AtlasRegion[] attack5 = new AtlasRegion[ATTACK_FRAMES];
        for (int i = 0; i < ATTACK_FRAMES; i++) {
            attack5[i] = attackAtlas5.findRegion("tile00" + i);
        }
        Animation<AtlasRegion> attackanim5 = new Animation<>(FRAME_DURATION, attack5);
        animations.put("attack5", attackanim5);


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
        if(currentAnimation.equals("death")) {
            return;
        }


        String baseAnim = isAttacking ? "attack" : "walk";
        if (hp > STAGE_1_HP + STAGE_2_HP + STAGE_3_HP) {
            currentAnimation = baseAnim + "1";
        } else if (hp > STAGE_1_HP + STAGE_2_HP) {
            currentAnimation = baseAnim + "2";
        } else if (hp > STAGE_1_HP + STAGE_0_HP) {
            currentAnimation = baseAnim + "3";
        } else if (hp >  STAGE_0_HP){
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
    public void damage(int dmg) {
        int oldHp = hp;
        super.damage(dmg);

        // Check if we crossed a health threshold
        if ((oldHp > STAGE_1_HP + STAGE_2_HP && hp <= STAGE_1_HP + STAGE_2_HP) ||
                (oldHp > STAGE_1_HP && hp <= STAGE_1_HP)) {
            updateAnimation();
        }
    }

    @Override
    public void die() {
        currentAnimation = "death";
        super.die();
    }
}
