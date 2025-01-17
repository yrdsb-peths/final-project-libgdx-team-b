package ca.codepet.Plants;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.utils.ObjectMap;

import ca.codepet.Zombies.Zombie;
import ca.codepet.Collidable;

import com.badlogic.gdx.math.Rectangle;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.Array;

/**
 * The Projectile class for the projectiles fired by plants.
 * Contains the base functionality for all projectiles.
 */
public class Projectile implements Collidable {
    protected float x, y;
    protected float speed = 300;
    protected int damage;
    protected Rectangle bounds;
    protected ObjectMap<String, Animation<AtlasRegion>> animations;
    protected String currentAnimation = "flying";
    protected float stateTime = 0;
    protected boolean isHit = false;
    protected float scale = 2f;
    private AtlasRegion flyingFrame;
    private int row;

    public String plantType = "shooter";

    private Random rand = new Random();

    private final Sound[] hitSounds = {
            Gdx.audio.newSound(Gdx.files.internal("sounds/zombieHit.mp3")),
            Gdx.audio.newSound(Gdx.files.internal("sounds/zombieHit2.mp3"))
    };

    private final Sound snowPeaSound = Gdx.audio.newSound(Gdx.files.internal("sounds/snow_pea_sparkles.ogg"));

    /**
     * Constructor for the Projectile class.
     * @param x The x position of the projectile
     * @param y The y position of the projectile
     * @param damage The damage the projectile does 
     * @param atlasPath The path to the atlas file for the projectile
     * @param scale The scale of the projectile
     * @param row The row the projectile is in
     */
    public Projectile(float x, float y, int damage, String atlasPath, float scale, int row) {
        this.x = x;
        this.y = y;
        this.damage = damage;
        this.scale = scale;
        this.row = row;
        this.bounds = new Rectangle(x, y, 10 * scale, 10 * scale); // Smaller collision box

        animations = new ObjectMap<>();

        try {
            TextureAtlas atlas = new TextureAtlas(Gdx.files.internal(atlasPath));
            // Just get the first frame for flying
            flyingFrame = atlas.findRegion("flying", 0);
            if (flyingFrame == null) {
                System.out.println("Could not find flying frame in atlas: " + atlasPath);
            }

            // Load splat animation frames
            Array<AtlasRegion> splatFrames = atlas.findRegions("splat");
            if (splatFrames.size > 0) {
                Animation<AtlasRegion> splatAnim = new Animation<>(0.1f, splatFrames);
                animations.put("splat", splatAnim);
            }
        } catch (Exception e) {
            System.out.println("Error loading projectile atlas: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Update the projectile's position.
     * @param delta The time since the last frame
     */
    public void update(float delta) {
        if (!isHit) {
            x += speed * delta;
            // Center the bounds on the projectile
            bounds.setCenter(x, y);
        }
        stateTime += delta;
    }

    /**
     * Render the projectile.
     * @param batch The SpriteBatch to render to
     */
    public void render(SpriteBatch batch) {
        if (!isHit && flyingFrame != null) {
            // Render static flying frame
            batch.draw(flyingFrame,
                    x - (flyingFrame.getRegionWidth() * scale) / 2,
                    y - (flyingFrame.getRegionHeight() * scale) / 2,
                    flyingFrame.getRegionWidth() * scale,
                    flyingFrame.getRegionHeight() * scale);
        } else if (isHit) {
            // Render splat animation
            Animation<AtlasRegion> splatAnim = animations.get("splat");
            if (splatAnim != null) {
                AtlasRegion currentFrame = splatAnim.getKeyFrame(stateTime, false);
                batch.draw(currentFrame,
                        x - (currentFrame.getRegionWidth() * scale) / 2,
                        y - (currentFrame.getRegionHeight() * scale) / 2,
                        currentFrame.getRegionWidth() * scale,
                        currentFrame.getRegionHeight() * scale);
            }
        }
    }

    /**
     * Hit action for the projectile.
     * @param zombie The zombie that was hit
     */
    public void hit(Zombie zombie) {
        if (animations.containsKey("splat")) {

            if(plantType.equals("snowPea")) {
                snowPeaSound.play(0.6f);
            } else {
                hitSounds[rand.nextInt(hitSounds.length)].play(0.6f);
            }

            isHit = true;
            currentAnimation = "splat";
            stateTime = 0;
        }
    }

    /**
     * Check if the projectile is finished.
     * @return True if the projectile is finished
     */
    public boolean isFinished() {
        if (!isHit)
            return false;
        return animations.get(currentAnimation).isAnimationFinished(stateTime);
    }

    /**
     * Get the bounds of the projectile.
     * @return The bounds of the projectile
     */
    public Rectangle getBounds() {
        return bounds;
    }

    /**
     * Get the damage of the projectile.
     * @return The damage of the projectile
     */
    public int getDamage() {
        return damage;
    }

    /**
     * Check if the projectile has hit.
     * @return True if the projectile has hit
     */
    public boolean isHit() {
        return isHit;
    }

    /**
     * Dispose of the projectile.
     */
    public void dispose() {
        for (Animation<AtlasRegion> anim : animations.values()) {
            anim.getKeyFrame(0).getTexture().dispose();
        }

        for (Sound sound : hitSounds) {
            sound.dispose();
        }
    }

    /**
     * Get the x position of the projectile.
     * @return The x position of the projectile
     */
    @Override
    public float getX() {
        return (int) x;
    }

    /**
     * Get the width of the projectile.
     * @return The width of the projectile
    */
    @Override
    public float getWidth() {
        return 10 * scale;
    }

    /**
     * Get the row the projectile is in.
     * @return The row of the projectile
     */
    @Override
    public int getRow() {
        return row;
    }
}
