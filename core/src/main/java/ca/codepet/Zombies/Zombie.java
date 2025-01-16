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
    private float groanTimer = 0f;
    private static final float GROAN_INTERVAL = 5f; // Groan every 5 seconds

    private int row, col;

    private int damage = 10;

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
    private boolean isDead = false;

    private static final float DEATH_SCALE_X = 4.0f;
    private static final float DEATH_SCALE_Y = 4.0f;
    private float currentScaleX = 1.0f;
    private float currentScaleY = 1.0f;

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

        world = theWorld;

        row = rand.nextInt(world.getLawnHeight());
        this.animations = new ObjectMap<>();
        currentAnimation = "walk"; // Set default animation
    }

    public Texture getTexture() {
        return zombieTexture;
    }

    public TextureRegion getTextureRegion() {
        Animation<AtlasRegion> currentAnim = animations.get(currentAnimation);
        if (currentAnim != null) {
            if (currentAnimation.equals("death")) {
                currentScaleX = DEATH_SCALE_X;
                currentScaleY = DEATH_SCALE_Y;
                return currentAnim.getKeyFrame(stateTime, false);
            }
            currentScaleX = 1.0f;
            currentScaleY = 1.0f;
            return currentAnim.getKeyFrame(stateTime, true);
        }
        return textureRegion;
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
        if (!isDying && !isSquashed) {  // Don't move while dying or squashed
            x -= 0.5;
            int newCol = (int) (x / world.getLawnTileWidth()) - 1;
            col = Math.min(Math.max(newCol, 0), 8);
        }
    }

    public boolean hasReachedHouse() {
        return x <= world.getLawnTileWidth(); // Add this method
    }

    public void update(float delta) {
        attackTimer += delta;
        stateTime += delta;

        if (isDying) {
            Animation<AtlasRegion> deathAnim = animations.get("death");
            if (deathAnim != null && stateTime >= deathAnim.getAnimationDuration()) {
                isDead = true;
                world.removeZombie(this);
                return;
            }
        } else if (isSquashed) {
            squashTimer += delta;
            rotation = Math.min(90, squashTimer * (90 / SQUASH_DURATION));
            scaleY = Math.max(0.3f, 1 - (squashTimer / SQUASH_DURATION));

            groanTimer += delta;
            if (groanTimer >= GROAN_INTERVAL) {
                playGroanSound();
                groanTimer = 0f;
            }
        }

        groanTimer += delta;
        if (groanTimer >= GROAN_INTERVAL) {
            playGroanSound();
            groanTimer = 0f;
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
        if (!isDying && plant != null) {  // Don't attack while dying
            plant.damage(damage);
            playChompSound();
        }
    }

    public void playChompSound() {
        // Play random chomp sound
        chompSounds[rand.nextInt(chompSounds.length)].play(0.4f);
    }

    private void playGroanSound() {
        groanSounds[rand.nextInt(groanSounds.length)].play(0.5f);
    } 

    public void damage(int dmg) {
        if (!isDying) {  // Only take damage if not already dying
            hp -= dmg;
            if (hp <= 0) {
                isDying = true;
                currentAnimation = "death";
                stateTime = 0;
            }
        }
    }

    public boolean isDying() {
        return isDying;
    }

    public boolean isDead() {
        return isDead;
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
        zombieTexture.dispose();
        for (Sound sound : chompSounds) {
            sound.dispose();
        }
        for (Sound sound : groanSounds) {
            sound.dispose();
        }
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

    public float getCurrentScaleX() {
        return currentScaleX;
    }

    public float getCurrentScaleY() {
        return currentScaleY;
    }

}
