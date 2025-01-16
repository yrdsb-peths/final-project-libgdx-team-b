package ca.codepet.Plants;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;

import ca.codepet.worlds.DayWorld;

public class Walnut extends Plant {
    private static final int FRAMES = 5;
    private static final int DEFAULT_HEALTH = 300;
    private static final int STAGE2_HEALTH = 250;
    private static final int STAGE3_HEALTH = 200;
    private static final int TOTAL_HP = DEFAULT_HEALTH + STAGE2_HEALTH + STAGE3_HEALTH;

    public Walnut(DayWorld world, float x, float y) {
        super(world, x, y, TOTAL_HP);
        setScale(2.3f);

        // Load stage 1 animation (full health)
        TextureAtlas stage1Atlas = new TextureAtlas(Gdx.files.internal("plants/walnut/walnut-idle-1.atlas"));
        AtlasRegion[] stage1 = new AtlasRegion[FRAMES];
        for (int i = 0; i < FRAMES; i++) {
            stage1[i] = stage1Atlas.findRegion("tile00" + i);
        }
        animations.put("stage1", new Animation<>(0.2f, stage1));

        // Load stage 2 animation (medium damage)
        TextureAtlas stage2Atlas = new TextureAtlas(Gdx.files.internal("plants/walnut/walnut-idle-2.atlas"));
        AtlasRegion[] stage2 = new AtlasRegion[FRAMES];
        for (int i = 0; i < FRAMES; i++) {
            stage2[i] = stage2Atlas.findRegion("tile00" + i);
        }
        animations.put("stage2", new Animation<>(0.2f, stage2));

        // Load stage 3 animation (heavy damage)
        TextureAtlas stage3Atlas = new TextureAtlas(Gdx.files.internal("plants/walnut/walnut-idle-3.atlas"));
        AtlasRegion[] stage3 = new AtlasRegion[FRAMES];
        for (int i = 0; i < FRAMES; i++) {
            stage3[i] = stage3Atlas.findRegion("tile00" + i);
        }
        animations.put("stage3", new Animation<>(0.2f, stage3));

        setAnimation("stage1");
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        // Update animation based on health
        String newStage;
        if (health > STAGE2_HEALTH) {
            newStage = "stage1";
        } else if (health > STAGE3_HEALTH) {
            newStage = "stage2";
        } else {
            newStage = "stage3";
        }

        setAnimation(newStage);
        imageIndex += delta;
    }
}
