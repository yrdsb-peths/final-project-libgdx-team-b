package ca.codepet.Plants;

import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectMap.Entry;

import ca.codepet.worlds.DayWorld;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.audio.Sound;
import java.util.Random;

/**
 * The Plant class for the plants in the game.
 * Contains the base functionality for all plants.
 */
public abstract class Plant {
    private DayWorld world;
    protected float x, y;
    protected int health = 100;
    protected Rectangle rect = new Rectangle(0, 0, 32, 32);
    protected ObjectMap<String, Animation<AtlasRegion>> animations = new ObjectMap<>();
    protected String currentAnimation = null;
    protected float imageIndex = 0f;
    protected float alpha = 1.0f;
    protected float flash = 0f;
    protected float scale = 1f; // Add this line
    protected Sound[] hitSounds;
    protected Sound[] deathSounds;
    protected Random rand = new Random();

    /**
     * Constructor for the Plant class.
     * @param world
     * @param x
     * @param y
     * @param health
     */
    public Plant(DayWorld world, float x, float y, int health) {
        this.world = world;
        this.x = x;
        this.y = y;
        this.health = health;
    }

    /**
     * Set the current animation.
     * @param spr The name of the animation
     */
    protected void setAnimation(String spr) {
        currentAnimation = spr;
    }

    /**
     * Set the current animation if it is different from the current one.
     * @param spr The name of the animation
     */
    protected void setAnimationUnique(String spr) {
        if (currentAnimation == null || !currentAnimation.equals(spr))
            imageIndex = 0f;
        setAnimation(spr);
    }

    /**
     * Get the world the plant is in.
     * @return The world
     */
    public DayWorld getWorld() {
        return world;
    }

    /**
     * Get the texture of the current animation.
     * @return The texture
     */
    public AtlasRegion getTexture() {
        return animations.get(currentAnimation).getKeyFrame(imageIndex, true);
    }

    /**
     * Get the rectangle of the plant.
     * @return The rectangle
     */
    public Rectangle getRect() {
        return rect;
    }

    /**
     * Damage the plant, play sound, and return if the plant is dead.
     * @param dmg The amount of damage
     * @return True if the plant is dead
     */
    public boolean damage(int dmg) {
        flash = 0.2f;
        health -= dmg;
        if (health <= 0) {
            // playDeathSound();
            dispose();
            return true;
        }
        // playHitSound();
        return false;
    }

    /**
     * Check if the plant is dead.
     * @return True if the plant is dead
     */
    public boolean isDead() {
        return health <= 0;
    }

    /**
     * Dispose of the plant.
     */
    public void dispose() {
        for (Entry<String, Animation<AtlasRegion>> entry : animations.entries()) {
            for (AtlasRegion atlas : entry.value.getKeyFrames()) {
                if (atlas != null) {
                    Texture tex = atlas.getTexture();
                    if (tex != null)
                        tex.dispose();
                }
            }
        }
    }

    /**
     * Set the transparency of the plant. Used for the ghost effect.
     * @param alpha The alpha value
     */
    public void setAlpha(float alpha) {
        this.alpha = alpha;
    }

    /**
     * Set the position of the plant.
     * @param x The x position
     * @param y The y position
     */
    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Set the scale of the plant.
     * @param scale The scale
     */
    public void setScale(float scale) {
        this.scale = scale;
    }

    /**
     * Render the plant.
     * @param batch The SpriteBatch to render the plant
     */
    public void render(SpriteBatch batch) {
        AtlasRegion tex = getTexture();
        if (tex == null || health <= 0)
            return; // Add this check

        float pX = x + tex.offsetX - (tex.originalWidth * scale) / 2;
        float pY = y + tex.offsetY - (tex.originalHeight * scale) / 2;

        float oldAlpha = batch.getColor().a;
        batch.setColor(batch.getColor().r, batch.getColor().g, batch.getColor().b, alpha);
        batch.draw(tex, pX, pY, tex.originalWidth * scale, tex.originalHeight * scale); // Modified to use scale

        batch.setShader(world.getGame().getFlashShader());
        batch.setColor(1f, 1f, 1f, flash / 0.2f * alpha);
        batch.draw(tex, pX, pY, tex.originalWidth * scale, tex.originalHeight * scale); // Modified to use scale
        batch.setShader(null);
        batch.setColor(batch.getColor().r, batch.getColor().g, batch.getColor().b, oldAlpha);
    }

    /**
     * Update the plant. Used for the hit flash effect.
     * @param delta The time since the last update
     */
    public void update(float delta) {
        flash = Math.max(0f, flash - delta);
    }
}