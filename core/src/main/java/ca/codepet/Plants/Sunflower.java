package ca.codepet.Plants;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;

import ca.codepet.characters.Sun;
import ca.codepet.worlds.DayWorld;

/**
 * The Sunflower class for the Sun flower plant.
 * Produces suns over time.
 */
public class Sunflower extends Plant {
    static int IDLE_FRAMES = 6;
    private float sunProductionTimer = INITIAL_SUN_PRODUCTION_INTERVAL;
    private float sunFlashTimer = 0f;
    private boolean sunFlash = false;
    private static final float INITIAL_SUN_PRODUCTION_INTERVAL = 8f;
    private static final float AFTER_SUN_PRODUCTION_INTERVAL = 24f;

    /**
     * Constructor for the Sunflower class.
     * @param world The world the Sunflower is in
     * @param x The x position of the Sunflower
     * @param y The y position of the Sunflower
     */
    public Sunflower(DayWorld world, float x, float y) {
        super(world, x, y, 600);
        this.scale = 1.1f; // Make sunflower 50% bigger
        TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("plants/sunflower/Sunflower.atlas"));
        AtlasRegion[] idle = new AtlasRegion[IDLE_FRAMES];
        for (int i = 0; i < IDLE_FRAMES; i++) {
            idle[i] = atlas.findRegion("sunflower_idle" + (i + 1));
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
        Animation<AtlasRegion> idleAnim = new Animation<>(0.2f, boomerangFrames);
        animations.put("idle", idleAnim);
        setAnimation("idle");
    }

    /**
     * Update the Sunflower plant. Check if it is time to produce suns.
     * @param delta The time since the last frame in seconds
     */
    @Override
    public void update(float delta) {
        super.update(delta);
        imageIndex += delta;

        sunProductionTimer = Math.max(0f, sunProductionTimer - delta);
        sunFlashTimer = Math.max(0f, sunFlashTimer - delta);
        if (sunFlashTimer <= 0f)
            sunFlash = false;

        if (sunProductionTimer <= 1f && !sunFlash) {
            sunFlash = true;
            sunFlashTimer = 2.5f;
        }

        if (sunProductionTimer <= 0) {
            produceSun();
            sunProductionTimer = AFTER_SUN_PRODUCTION_INTERVAL;
        }
    }

    /**
     * Produce a sun at a random location around the Sunflower.
     */
    private void produceSun() {
        if (getWorld() != null) {
            // Generate random angle in radians
            float angle = (float) (Math.random() * 2 * Math.PI);
            // Generate random radius between 0 and 50
            float radius = (float) (Math.random() * 50);
            // Calculate offset using polar coordinates
            float offsetX = radius * (float) Math.cos(angle);
            float offsetY = radius * (float) Math.sin(angle);

            Sun sun = new Sun(x + offsetX, y + offsetY);
            getWorld().addSun(sun);
        }
    }

    /**
     * Render the Sunflower plant. Add a flash effect when producing suns.
     * @param batch The SpriteBatch to render to
     */
    @Override
    public void render(SpriteBatch batch) {
        super.render(batch);
        AtlasRegion tex = getTexture();
        if (tex == null || health <= 0 || sunFlashTimer <= 0)
            return; // Add this check

        float pX = x + tex.offsetX - (tex.originalWidth * scale) / 2;
        float pY = y + tex.offsetY - (tex.originalHeight * scale) / 2;

        float oldAlpha = batch.getColor().a;
        batch.setShader(getWorld().getGame().getFlashShader());
        batch.setColor(1f, 1f, 1f, 0.25f);
        batch.draw(tex, pX, pY, tex.originalWidth * scale, tex.originalHeight * scale); // Modified to use scale
        batch.setShader(null);
        batch.setColor(batch.getColor().r, batch.getColor().g, batch.getColor().b, oldAlpha);
    }
}
