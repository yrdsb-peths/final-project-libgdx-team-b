package ca.codepet.Plants;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;

import ca.codepet.Plant;
import ca.codepet.characters.Sun;
import ca.codepet.worlds.DayWorld;

public class Sunflower extends Plant {
    static int IDLE_FRAMES = 6;
    private float sunProductionTimer = 0;
    private static final float SUN_PRODUCTION_INTERVAL = 10.0f; // Produces sun every 24 seconds
    private DayWorld world;

    public Sunflower(float x, float y, DayWorld world) {
        super(x, y);
        this.world = world;
        TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("plants/Sunflower.atlas"));
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
    public void update(float deltaTime) {
        imageIndex += deltaTime;

        sunProductionTimer += deltaTime;
        
        if (sunProductionTimer >= SUN_PRODUCTION_INTERVAL) {
            produceSun();
            sunProductionTimer = 0;
        }
    }

    private void produceSun() {
        if (world != null) {
            // Generate random angle in radians
            float angle = (float)(Math.random() * 2 * Math.PI);
            // Generate random radius between 0 and 50
            float radius = (float)(Math.random() * 50);
            // Calculate offset using polar coordinates
            float offsetX = radius * (float)Math.cos(angle);
            float offsetY = radius * (float)Math.sin(angle);
            
            Sun sun = new Sun(x + offsetX, y + offsetY);
            world.addSun(sun);
        }
    }
}
