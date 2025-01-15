package ca.codepet.Zombies;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.utils.ObjectMap;

import ca.codepet.Collidable;
import ca.codepet.Plant;
import ca.codepet.worlds.DayWorld;

// info
// gargantuar 3000 hp, instakill any permanent plant except spikerock which takes 9 hits
// basic zombie 200 hp
// https://plantsvszombies.fandom.com/wiki/Plants_(PvZ)#List_of_plants
// https://pvzstrategy.fandom.com/wiki/Plant_Stats

public abstract class Zombie implements Collidable {

    private int hp;
    private int x = Gdx.graphics.getWidth();
    private int y;

    Random rand = new Random();
    private Texture zombieTexture;
    private TextureRegion textureRegion;
    private Sound[] chompSounds;
    private Sound[] groanSounds;
    private Sound[] deathSounds;
    private float groanTimer = 0f;
    private static final float GROAN_INTERVAL = 5f; // Groan every 5 seconds
    private boolean hasGroanedOnSpawn = false;
    private static final float INITIAL_GROAN_DELAY = 0.5f;

    private int row;
    private int col = 8;
    
    private int damage = 30;

    private float atkDelay; // 1 second between attacks
    private float attackTimer = 0.0f;

    private int width = 135;
    private int height = 160;

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

    private boolean isDying = false;
    private float deathTimer = 0f;
    private static final float DEATH_SOUND_DURATION = 1.5f; // Adjust based on your sound length

    private boolean isDeathSoundPlaying = false;
    private long deathSoundId;

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
                Gdx.audio.newSound(Gdx.files.internal("sounds/chomp3.ogg")),
                Gdx.audio.newSound(Gdx.files.internal("sounds/chomp4.ogg"))
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

        //load death sound
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

    public TextureRegion getTextureRegion() {
        Animation<AtlasRegion> currentAnim = animations.get(currentAnimation);
        if (currentAnim != null) {
            return currentAnim.getKeyFrame(stateTime, true);
        }
        return textureRegion; // Fallback to static texture if no animation
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getX() {
        return x;
    }

    public void move() {
        x -= 0.5; // Slower movement speed
        // Add bounds checking for column calculation
        int newCol = (int) (x / world.getLawnTileWidth()) - 1;
        col = Math.min(Math.max(newCol, 0), 8); // Clamp between 0 and 8

    }

    public boolean hasReachedHouse() {
        return x <= world.getLawnTileWidth(); // Add this method
    }

    public void update(float delta) {
        attackTimer += delta;
        stateTime += delta; // Update animation state time

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
            if (deathTimer >= DEATH_SOUND_DURATION) {
                world.removeZombie(this);
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

    public void playChompSound() {
        // Play random chomp sound
        chompSounds[rand.nextInt(chompSounds.length)].play(0.4f);
    }

    private void playGroanSound() {
        if (!isSquashed && !isDying) {
            groanSounds[rand.nextInt(groanSounds.length)].play(0.4f);
        }
    } 

    public void damage(int dmg) {
        hp -= dmg;
        if (hp <= 0 && !isDying) {
            isDying = true;
            deathSounds[rand.nextInt(deathSounds.length)].play(0.5f);
        }
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public int getXOffset() {
        return xOffset;
    }

    public int getYOffset() {
        return yOffset;
    }

    public void dispose() {
        if (!isDeathSoundPlaying) {
            // Play death sound and set flag
            deathSoundId = deathSounds[rand.nextInt(deathSounds.length)].play(0.5f);
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

    public Rectangle getBounds() {
        return new Rectangle(x + xOffset, y, width/2, height); // Adjust collision box
    }
}
