package ca.codepet.Plants;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;

import ca.codepet.characters.Sun;
import ca.codepet.worlds.DayWorld;

public class Sunflower extends Plant {
    static int IDLE_FRAMES = 6;
    private float sunProductionTimer = INITIAL_SUN_PRODUCTION_INTERVAL;
    private static final float INITIAL_SUN_PRODUCTION_INTERVAL = 8f;
    private static final float AFTER_SUN_PRODUCTION_INTERVAL = 24f;

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

    @Override
    public void update(float delta) {
        super.update(delta);
        imageIndex += delta;

        sunProductionTimer = Math.max(0, sunProductionTimer - delta);

        if (sunProductionTimer <= 0) {
            produceSun();
            sunProductionTimer = AFTER_SUN_PRODUCTION_INTERVAL;
        }
    }

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
}
