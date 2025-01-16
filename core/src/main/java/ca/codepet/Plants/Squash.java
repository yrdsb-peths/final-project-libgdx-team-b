package ca.codepet.Plants;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.utils.Array;

import ca.codepet.Zombies.Zombie;
import ca.codepet.worlds.DayWorld;

public class Squash extends Plant {
    static int IDLE_FRAMES = 4;
    static int ATTACK_FRAMES = 4;
    static int DEFAULT_HEALTH = 100000;
    static float DEFAULT_ATTACK_COOLDOWN = 0f; // Only attacks once
    static int SQUASH_DAMAGE = 1800;
    static float JUMP_HEIGHT = 80f; // Add constant for consistent jump height
    static float ATTACK_RANGE = 120f; // Increased detection range
    static float SQUASH_RANGE = 80f; // Range for squash damage
    static float NOTICE_DELAY = 1.3f; // Time between noticing zombie and attacking

    private boolean hasSquashed = false;
    private float attackTimer = 0;
    private float jumpHeight = 0;
    private float originalY;
    private int row; // Add row variable
    private Zombie targetZombie = null;
    private float noticeTimer = 0f;
    private boolean hasNoticed = false;

    private boolean isHmmPlayed = false;

    private Random rand = new Random();

    private final Sound[] splatSounds = {
            Gdx.audio.newSound(Gdx.files.internal("sounds/splat.ogg")),
            Gdx.audio.newSound(Gdx.files.internal("sounds/splat2.ogg")),
            Gdx.audio.newSound(Gdx.files.internal("sounds/splat3.ogg"))
    };

    private final Sound[] hmmSounds = {
            Gdx.audio.newSound(Gdx.files.internal("sounds/squash_hmm.ogg")),
            Gdx.audio.newSound(Gdx.files.internal("sounds/squash_hmm2.ogg"))
    };

    public Squash(DayWorld world, float x, float y) {
        super(world, x, y, DEFAULT_HEALTH);
        originalY = y;
        setScale(2.5f);

        TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("plants/squash/squash-idle.atlas"));
        AtlasRegion[] idle = new AtlasRegion[IDLE_FRAMES];
        for (int i = 0; i < IDLE_FRAMES; i++) {
            idle[i] = atlas.findRegion("tile00" + i);
        }
        Animation<AtlasRegion> idleAnim = new Animation<>(0.2f, idle);
        idleAnim.setPlayMode(Animation.PlayMode.LOOP);
        animations.put("idle", idleAnim);

        TextureAtlas attackAtlas = new TextureAtlas(Gdx.files.internal("plants/squash/squash-attack.atlas"));
        AtlasRegion[] attack = new AtlasRegion[ATTACK_FRAMES];
        for (int i = 0; i < ATTACK_FRAMES; i++) {
            attack[i] = attackAtlas.findRegion("tile00" + i);
        }
        Animation<AtlasRegion> attackAnim = new Animation<>(0.15f, attack);
        attackAnim.setPlayMode(Animation.PlayMode.NORMAL);
        animations.put("attack", attackAnim);

        setAnimation("idle");
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        
        if (!hasSquashed) {
            Array<Zombie> zombies = getWorld().getZombies();
            
            for (Zombie zombie : zombies) {
                if (zombie.getRow() == row && !zombie.isSquashed() && !zombie.isDying() 
                    && Math.abs(zombie.getX() - x) < ATTACK_RANGE) {

                    if(!isHmmPlayed) {
                        hmmSounds[rand.nextInt(hmmSounds.length)].play(0.8f);
                        isHmmPlayed = true;
                        hasNoticed = true;
                        targetZombie = zombie;
                    }
                    break;
                }
            }

            if (hasNoticed) {
                noticeTimer += delta;
                if (noticeTimer >= NOTICE_DELAY) {
                    startAttack();
                }
            }
        }

        if (isAttacking) {
            attackTimer += delta;
            
            if (attackTimer < 0.45f) {
                // Rising animation with fixed height
                jumpHeight = Math.min(JUMP_HEIGHT, attackTimer * JUMP_HEIGHT * 4);
            } else if (attackTimer < 0.6f) {
                // Keep maximum height while moving
                jumpHeight = JUMP_HEIGHT;
                // Move towards zombie if we have a target
                if (targetZombie != null && !targetZombie.isDying()) {
                    float targetX = targetZombie.getX() + 20; // Offset to hit center of zombie
                    x += (targetX - x) * delta * 12;
                }
            } else {
                // Squashing - fast drop
                jumpHeight = Math.max(0, jumpHeight - delta * 800);
                if (jumpHeight <= 0 && !hasSquashed) {
                    squash();
                }
            }
            
            // Calculate y position based on row and add jumpHeight
            y = getWorld().getLawnTileY() - (row * getWorld().getLawnTileHeight()) + (getWorld().getLawnTileHeight() / 2) + jumpHeight;
        }
    }

    private void squash() {
        Array<Zombie> zombies = getWorld().getZombies();
        
        splatSounds[rand.nextInt(splatSounds.length)].play(0.5f);

        // Deal damage to any zombies in range
        for (Zombie zombie : zombies) {
            if (zombie.getRow() == row && !zombie.isSquashed() && !zombie.isDying() 
                && Math.abs(zombie.getX() - x) < SQUASH_RANGE) {
                zombie.damage(SQUASH_DAMAGE);
            }
        }
        
        hasSquashed = true;
        health = 0;
    }

    // Add setter for row
    public void setRow(int row) {
        this.row = row;
    }

    @Override
    public void render(SpriteBatch batch) {
        super.render(batch);
        imageIndex += Gdx.graphics.getDeltaTime();
    }

    public void dipose() {
        for (Sound sound : splatSounds) {
            sound.dispose();
        }
        for (Sound sound : hmmSounds) {
            sound.dispose();
        }
    }
}
