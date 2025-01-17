package ca.codepet.Plants;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import ca.codepet.worlds.DayWorld;

/**
 * The PotatoMine class for the Potato Mine plant.
 * Explodes when a zombie steps on it.
 */
public class PotatoMine extends Plant {
    private static final int POPUP_FRAMES = 3;
    private static final int IDLE_FRAMES = 5;
    private static final int EXPLODE_FRAMES = 8;
    private static final int DEFAULT_HEALTH = 600;

    private static final float ARMING_TIME = 15f;
    private float armingTimer = 0f;
    private boolean isArmed = false;
    private boolean hasExploded = false;
    private boolean hasPopped = false;
    private int explosionDamage = 1800;
    private boolean hasCheckedAfterArming = false;

    private final Sound explodeSound = Gdx.audio.newSound(Gdx.files.internal("sounds/potato_mine.ogg"));

    private static final float EXPLOSION_Y_OFFSET = 40f; // Add this constant

    /**
     * Constructor for the PotatoMine class.
     * @param world The world the PotatoMine is in
     * @param x The x position of the PotatoMine
     * @param y The y position of the PotatoMine
     */
    public PotatoMine(DayWorld world, float x, float y) {
        super(world, x, y, 200);
        health = DEFAULT_HEALTH;
        setScale(2.7f); // Add this line to scale the potato mine

        // Load popup animation
        TextureAtlas popupAtlas = new TextureAtlas(Gdx.files.internal("plants/potato/potato-popup.atlas"));
        AtlasRegion[] popup = new AtlasRegion[POPUP_FRAMES];
        for (int i = 0; i < POPUP_FRAMES; i++) {
            popup[i] = popupAtlas.findRegion(String.valueOf(i + 1));
        }
        animations.put("popup", new Animation<>(0.2f, popup));

        // Load idle animation
        TextureAtlas idleAtlas = new TextureAtlas(Gdx.files.internal("plants/potato/potato-idle.atlas"));
        AtlasRegion[] idle = new AtlasRegion[IDLE_FRAMES];
        for (int i = 0; i < IDLE_FRAMES; i++) {
            idle[i] = idleAtlas.findRegion("tile00" + i);
        }
        // Create boomerang animation by adding reversed frames
        AtlasRegion[] boomerangFrames = new AtlasRegion[IDLE_FRAMES * 2 - 2];
        // Forward frames
        for (int i = 0; i < IDLE_FRAMES; i++) {
            boomerangFrames[i] = idle[i];
        }
        // Reverse frames (excluding first and last to avoid duplicates)
        for (int i = IDLE_FRAMES - 2; i > 0; i--) {
            boomerangFrames[IDLE_FRAMES * 2 - 2 - i] = idle[i];
        }
        animations.put("idle", new Animation<>(0.2f, boomerangFrames));

        // Load explode animation
        TextureAtlas explodeAtlas = new TextureAtlas(Gdx.files.internal("plants/potato/potato-explode.atlas"));
        AtlasRegion[] explode = new AtlasRegion[EXPLODE_FRAMES];
        for (int i = 0; i < EXPLODE_FRAMES; i++) {
            explode[i] = explodeAtlas.findRegion(String.valueOf(i + 10));
        }
        animations.put("explode", new Animation<>(0.1f, explode));

        setAnimation("popup");
    }

    /**
     * Update the PotatoMine, checking for arming and explosion.
     * @param delta The time since the last frame
     */
    @Override
    public void update(float delta) {
        super.update(delta);
        if (hasExploded) {
            if (animations.get("explode").isAnimationFinished(imageIndex)) {
                health = 0; // Mark for removal
                return; // Don't increment imageIndex anymore
            }
            imageIndex += delta;
            return;
        }

        if (!isArmed) {
            imageIndex = 0; // Keep showing first frame while arming
            armingTimer += delta;
            if (armingTimer >= ARMING_TIME) {
                isArmed = true;
                if (!hasPopped) {
                    setAnimation("popup");
                    imageIndex = 0;
                    hasPopped = true;
                }
            }
        } else if (hasPopped && !hasCheckedAfterArming && animations.get("popup").isAnimationFinished(imageIndex)) {
            setAnimation("idle");
            imageIndex = 0;
            hasCheckedAfterArming = true;
        }

        if (isArmed) { // Only advance animation after arming
            health = 10000; // once armed, becomes instant use plant, therefore unkillable
            imageIndex += delta;
        }
    }

    /**
     * Render the PotatoMine.
     * @param batch The SpriteBatch to render the PotatoMine
     */
    @Override
    public void render(SpriteBatch batch) {
        if (hasExploded) {
            // Temporarily adjust y position up for explosion animation
            float originalY = y;
            y += EXPLOSION_Y_OFFSET;
            super.render(batch);
            y = originalY;
        } else {
            super.render(batch);
        }
    }

    /**
     * Check if the PotatoMine is armed.
     * @return True if the PotatoMine is armed
     */
    public boolean isArmed() {
        return isArmed;
    }

    /**
     * Explode the PotatoMine.
     */
    public void explode() {
        explodeSound.play(0.8f);

        if (!hasExploded && isArmed) {
            hasExploded = true;
            setAnimation("explode");
            imageIndex = 0;
        }
    }

    /**
     * Get the explosion damage of the PotatoMine.
     * @return The explosion damage
     */
    public int getExplosionDamage() {
        return explosionDamage;
    }

    /**
     * Check if the PotatoMine has exploded.
     * @return True if the PotatoMine has exploded
     */
    public boolean hasExploded() {
        return hasExploded;
    }

    /**
     * Dispose of the PotatoMine.
     */
    public void dispose() {
        super.dispose();
        explodeSound.dispose();
    }
}
