package ca.codepet.Plants;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;

import ca.codepet.worlds.DayWorld;

public class TallNut extends Plant {
    private static final int FRAMES = 4;
    private static final int DEFAULT_HEALTH = 400;
    private static final int STAGE2_HEALTH = 300;
    private static final int STAGE3_HEALTH = 200;

    private static final int TOTAL_HP = DEFAULT_HEALTH + STAGE2_HEALTH + STAGE3_HEALTH;

    public TallNut(DayWorld world, float x, float y) {
        super(world, x, y, TOTAL_HP);
        health = DEFAULT_HEALTH;
        setScale(2.3f);

        // Load stage 1 animation (full health)
        TextureAtlas stage1Atlas = new TextureAtlas(Gdx.files.internal("plants/tall-nut/tall-nut-1.atlas"));
        AtlasRegion[] stage1 = new AtlasRegion[FRAMES];
        for (int i = 0; i < FRAMES; i++) {
            stage1[i] = stage1Atlas.findRegion("120244-" + i);
        }
        animations.put("stage1", new Animation<>(0.2f, stage1));

        // Load stage 2 animation (medium damage)
        TextureAtlas stage2Atlas = new TextureAtlas(Gdx.files.internal("plants/tall-nut/tall-nut-2.atlas"));
        AtlasRegion[] stage2 = new AtlasRegion[FRAMES];
        for (int i = 0; i < FRAMES; i++) {
            stage2[i] = stage2Atlas.findRegion("120244-" + (i + 18));
        }
        animations.put("stage2", new Animation<>(0.2f, stage2));

        // Load stage 3 animation (heavy damage)
        TextureAtlas stage3Atlas = new TextureAtlas(Gdx.files.internal("plants/tall-nut/tall-nut-3.atlas"));
        AtlasRegion[] stage3 = new AtlasRegion[FRAMES];
        for (int i = 0; i < FRAMES; i++) {
            stage3[i] = stage3Atlas.findRegion("120244-" + (i + 27));
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
