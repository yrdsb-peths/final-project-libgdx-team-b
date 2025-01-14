package ca.codepet.Plants;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;

import ca.codepet.Plant;
import ca.codepet.characters.Sun;

public class Sunflower extends Plant {
    static int IDLE_FRAMES = 6;
    private float sunProductionTimer = 0;
    private static final float SUN_PRODUCTION_INTERVAL = 24.0f; // Produces sun every 24 seconds

    public Sunflower(float x, float y) {
        super(x, y);
        TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("plants/Sunflower.atlas"));
        AtlasRegion[] idle = new AtlasRegion[IDLE_FRAMES];
        for (int i = 0; i < IDLE_FRAMES; i++) {
            idle[i] = atlas.findRegion("sunflower_idle" + (i + 1));
        }
        Animation<AtlasRegion> idleAnim = new Animation<>(0.2f, idle);
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
        // Create a new sun at the plant's position
        Sun sun = new Sun(rect.x, rect.y);
        // TODO: Add sun to the world's sun collection
    }
}
