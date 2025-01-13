package ca.codepet.Plants;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;

import ca.codepet.ShooterPlant;

public class Peashooter extends ShooterPlant {
    static int IDLE_FRAMES = 8;
    public Peashooter(float x, float y) {
        super(x, y);
        TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("plants/Peashooter.atlas"));
        AtlasRegion[] idle = new AtlasRegion[IDLE_FRAMES];
        for (int i = 0; i < IDLE_FRAMES; i++) {
            idle[i] = atlas.findRegion("peashooter_idle" + (i + 1));
        }
        Animation<AtlasRegion> idleanim = new Animation<>(0.2f, idle);
        animations.put("idle", idleanim);
        animations.put("attack", idleanim);
    }

}
