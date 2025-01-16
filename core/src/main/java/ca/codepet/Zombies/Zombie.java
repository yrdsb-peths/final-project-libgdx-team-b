package ca.codepet.Zombies;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.ObjectMap;

import ca.codepet.Collidable;
import ca.codepet.Plants.Plant;
import ca.codepet.worlds.DayWorld;

// info
// gargantuar 3000 hp, instakill any permanent plant except spikerock which takes 9 hits
// basic zombie 200 hp
// https://plantsvszombies.fandom.com/wiki/Plants_(PvZ)#List_of_plants
// https://pvzstrategy.fandom.com/wiki/Plant_Stats

public abstract class Zombie implements Collidable {

    protected int hp;
    private float x = Gdx.graphics.getWidth();
    private float y;

    Random rand = new Random();
    private Texture zombieTexture;
    private TextureRegion textureRegion;
    private Sound[] chompSounds;
    private Sound[] groanSounds;
    private Sound[] deathSounds;
    private float groanTimer = 0f;
    private static final float GROAN_INTERVAL = 9f; 
    private boolean hasGroanedOnSpawn = false;
    private static final float INITIAL_GROAN_DELAY = 0.5f;

    private int row;
    private int col = 8;

    private int damage = 30;

    private float atkDelay; // 1 second between attacks
    private float attackTimer = 0.0f;

    private float width = 135;
    private float height = 160;

    private float slowTimer = 0.0f;

    protected int deathWidth = 51; // Default death animation width
    protected int deathHeight = 40; // Default death animation height
    protected int deathXOffset = -80;  // Add death animation offset
    protected int deathYOffset = -60;  // Add death animation offset

    DayWorld world;

    protected ObjectMap<String, Animation<AtlasRegion>> animations;
    protected String currentAnimation = "walk";
    protected float stateTime = 0;

    private int xOffset = -40; // Adjust this value as needed
    private int yOffset = -40; // Adjust this value as needed

    private float rotation = 0;
    private float scaleY = 1;
    private boolean isSquashed = false;
    public static final float SQUASH_DURATION = 0.5f;
    private float squashTimer = 0;
    private float flashTimer = 0;

    private boolean isDying = false;
    private float deathTimer = 0f;
    private static final float DEATH_SOUND_DURATION = 1.5f;

    private boolean isDeathSoundPlaying = false;


    private static final float MOVE_SPEED = 15f; 


    private boolean isDeathAnimationComplete = false;
    private static final float DEATH_ANIMATION_DURATION = 1.8f; // 9 frames * 0.2s per frame

    protected boolean isAttacking;


    public Zombie(DayWorld theWorld, Texture zombieTexture, int hp, int damage, float atkDelay) {
        this.hp = hp;
        this.damage = damage;
        this.atkDelay = atkDelay;

        this.zombieTexture = zombieTexture;

        textureRegion = new TextureRegion(zombieTexture);

        // Load multiple chomp sounds
        chompSounds = new Sound[] {
                Gdx.audio.newSound(Gdx.files.internal("sounds/chomp.ogg")),
                Gdx.audio.newSound(Gdx.files.internal("sounds/chomp2.ogg")),
                Gdx.audio.newSound(Gdx.files.internal("sounds/chomp3.ogg"))
        };

        // Load multiple groan sounds
        groanSounds = new Sound[] {
                Gdx.audio.newSound(Gdx.files.internal("sounds/groan.ogg")),
                Gdx.audio.newSound(Gdx.files.internal("sounds/groan2.ogg")),
                Gdx.audio.newSound(Gdx.files.internal("sounds/groan3.ogg")),
                Gdx.audio.newSound(Gdx.files.internal("sounds/groan4.ogg")),
                Gdx.audio.newSound(Gdx.files.internal("sounds/groan5.ogg")),
                Gdx.audio.newSound(Gdx.files.internal("sounds/groan6.ogg"))
        };

        // load death sound
        deathSounds = new Sound[] {
                Gdx.audio.newSound(Gdx.files.internal("sounds/zombieDeath1.ogg")),
                Gdx.audio.newSound(Gdx.files.internal("sounds/zombieDeath2.ogg"))
        };

        world = theWorld;

        row = rand.nextInt(world.getLawnHeight());
        this.animations = new ObjectMap<>();
        currentAnimation = "walk"; // Set default animation

        // Play initial groan with delay
        groanTimer = INITIAL_GROAN_DELAY;
    }

    public Texture getTexture() {
        return zombieTexture;
    }

    public boolean isDead() {
        return isDying;
    }

    public TextureRegion getTextureRegion() {
        Animation<AtlasRegion> currentAnim = animations.get(currentAnimation);
        if (currentAnim != null) {
            // Make death animation non-looping
            boolean shouldLoop = !currentAnimation.equals("death");
            return currentAnim.getKeyFrame(stateTime, shouldLoop);
        }
        return textureRegion; // Fallback to static texture if no animation
    }

    public float getWidth() {
        if (isDying) {
            return deathWidth;
        }
        return width;
    }

    public float getHeight() {
        if (isDying) {
            return deathHeight;
        }
        return height;
    }

    public void setScaleX(float scaleX) {
        width *= scaleX;
    }

    public void setScaleY(float scaleY) {
        height *= scaleY;
    }

    public float getX() {
        return x;
    }

    public void move(float delta) {
        float speed = MOVE_SPEED * delta;
        if (slowTimer > 0.0f)
            speed /= 2f;
        x -= speed; // Slower movement speed
        // Add bounds checking for column calculation
        int newCol = (int) (x / world.getLawnTileWidth()) - 1;
        col = Math.min(Math.max(newCol, 0), 8); // Clamp between 0 and 8

    }

    public boolean hasReachedHouse() {
        return x <= world.getLawnTileWidth(); // Add this method
    }

    public void update(float delta) {
        slowTimer = Math.max(0, slowTimer - delta);
        float modDelta = delta;
        if (slowTimer > 0f)
            modDelta /= 2f;
        attackTimer += modDelta;
        stateTime += modDelta; // Update animation state time

        flashTimer = Math.max(0, flashTimer - delta);
        attackTimer += delta;
        stateTime += delta; // Update animation state time

        // Handle death animation and timing
        if (isDying) {
            Animation<AtlasRegion> deathAnim = animations.get("death");
            if (deathAnim != null && deathAnim.isAnimationFinished(stateTime)) {
                isDeathAnimationComplete = true;
            }
        }

        // Add spawn groan
        if (!hasGroanedOnSpawn) {
            groanTimer -= delta;
            if (groanTimer <= 0) {
                playGroanSound();
                hasGroanedOnSpawn = true;
                groanTimer = GROAN_INTERVAL;
            }
        } else {
            // Regular groaning
            groanTimer += delta;
            if (groanTimer >= GROAN_INTERVAL) {
                playGroanSound();
                groanTimer = 0f;
            }
        }

        if (isSquashed) {
            squashTimer += delta;
            rotation = Math.min(90, squashTimer * (90 / SQUASH_DURATION));
            scaleY = Math.max(0.3f, 1 - (squashTimer / SQUASH_DURATION));

            groanTimer += delta;
            if (groanTimer >= GROAN_INTERVAL) {
                playGroanSound();
                groanTimer = 0f;
            }
        }

        if (isDying) {
            deathTimer += delta;
            if (deathTimer >= DEATH_ANIMATION_DURATION) {
                isDeathAnimationComplete = true;
            }
            if (deathTimer >= DEATH_SOUND_DURATION) {
                world.removeZombie(this, delta);
            }
        }

        if (isDeathSoundPlaying) {
            deathTimer += delta;
            if (deathTimer >= DEATH_SOUND_DURATION) {
                // Only dispose death sounds after they finish playing
                for (Sound sound : deathSounds) {
                    sound.dispose();
                }
            }
        }
    }

    public boolean canAttack() {
        if (attackTimer >= atkDelay) {
            attackTimer = 0;
            return true;
        }
        return false;
    }

    public void attack(Plant plant) {
        if (plant != null) {
            plant.damage(damage);
            playChompSound();
        }
    }

    public void die() {
        if (!isDying) {
            isDying = true;
            currentAnimation = "death";
            stateTime = 0; // Reset animation time when starting death animation
            deathTimer = 0; // Reset death timer
            deathSounds[rand.nextInt(deathSounds.length)].play(0.5f);
        }
    }

    public void playChompSound() {
        // Play random chomp sound
        chompSounds[rand.nextInt(chompSounds.length)].play(0.4f);
    }

    private void playGroanSound() {
        if (!isSquashed && !isDying) {
            groanSounds[rand.nextInt(groanSounds.length)].play(0.2f);
        }
    }

    public void doSlow() {
        slowTimer = 10f;
    }

    public void damage(int dmg) {
        hp -= dmg;
        flashTimer = 0.2f;
        if (hp <= 0 && !isDying) {
            die();
        }
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public int getXOffset() {
        if (isDying) {
            return deathXOffset;
        }
        return xOffset;
    }

    public int getYOffset() {
        if (isDying) {
            return deathYOffset;
        }
        return yOffset;
    }

    public void dispose() {
        if (!isDeathSoundPlaying) {
            // Play death sound and set flag
            isDeathSoundPlaying = true;
        }

        zombieTexture.dispose();
        for (Sound sound : chompSounds) {
            sound.dispose();
        }
        for (Sound sound : groanSounds) {
            sound.dispose();
        }
        // Death sounds will be disposed after they finish playing
    }

    public void squash() {
        isSquashed = true;
        squashTimer = 0;
    }

    public float getRotation() {
        return rotation;
    }

    public float getScaleY() {
        return scaleY;
    }

    public boolean isSquashed() {
        return isSquashed;
    }

    public float getSquashTimer() {
        return squashTimer;
    }

    public float getSlowTimer() {
        return slowTimer;
    }

    public float getFlashTimer() {
        return flashTimer;
    }

    public Rectangle getBounds() {
        return new Rectangle(x + xOffset, y, width / 2, height); // Adjust collision box
    }

    public boolean isDeathAnimationComplete() {
        return isDeathAnimationComplete;
    }

    public boolean isDying() {
        return isDying;
    }

}
