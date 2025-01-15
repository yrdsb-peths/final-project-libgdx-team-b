package ca.codepet.Plants;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;

import ca.codepet.ShooterPlant;

public class Repeater extends ShooterPlant {
    static int IDLE_FRAMES = 5;
    static int DEFAULT_HEALTH = 100;
    static float DEFAULT_ATTACK_COOLDOWN = 1.5f;
    static int DEFAULT_DAMAGE = 20;

    public Repeater(float x, float y) {
        super(x, y);

        setScale(2.2f);
        
        TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("plants/repeater.atlas"));
        AtlasRegion[] idle = new AtlasRegion[IDLE_FRAMES];
        for (int i = 0; i < IDLE_FRAMES; i++) {
            idle[i] = atlas.findRegion("tile00" + i);
        }
        Animation<AtlasRegion> idleanim = new Animation<>(0.2f, idle);
        animations.put("idle", idleanim);
        animations.put("attack", idleanim);
        
        setAnimation("idle"); // Set initial animation
    }
}
