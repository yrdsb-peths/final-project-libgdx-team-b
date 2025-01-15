package ca.codepet.Plants;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;

import ca.codepet.Plant;

public class PotatoMine extends Plant {
    private static final int POPUP_FRAMES = 3;
    private static final int IDLE_FRAMES = 5;
    private static final int EXPLODE_FRAMES = 8;
    private static final int DEFAULT_HEALTH = 300;
    
    private static final float ARMING_TIME = 1f;
    private float armingTimer = 0f;
    private boolean isArmed = false;
    private boolean hasExploded = false;
    private boolean hasPopped = false;
    private float explosionDamage = 1800f;

    private final Sound explodeSound = Gdx.audio.newSound(Gdx.files.internal("sounds/potato_mine.ogg"));
    
    public PotatoMine(float x, float y) {
        super(x, y);
        health = DEFAULT_HEALTH;
        setScale(2.3f);  // Add this line to scale the potato mine
        
        // Load popup animation
        TextureAtlas popupAtlas = new TextureAtlas(Gdx.files.internal("plants/potato-popup.atlas"));
        AtlasRegion[] popup = new AtlasRegion[POPUP_FRAMES];
        for (int i = 0; i < POPUP_FRAMES; i++) {
            popup[i] = popupAtlas.findRegion(String.valueOf(i + 1));
        }
        animations.put("popup", new Animation<>(0.2f, popup));

        // Load idle animation
        TextureAtlas idleAtlas = new TextureAtlas(Gdx.files.internal("plants/potato-idle.atlas"));
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
        TextureAtlas explodeAtlas = new TextureAtlas(Gdx.files.internal("plants/potato-explode.atlas"));
        AtlasRegion[] explode = new AtlasRegion[EXPLODE_FRAMES];
        for (int i = 0; i < EXPLODE_FRAMES; i++) {
            explode[i] = explodeAtlas.findRegion(String.valueOf(i + 10));
        }
        animations.put("explode", new Animation<>(0.1f, explode));
        
        setAnimation("popup");
    }
    
    @Override
    public void update(float delta) {
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
        } else if (hasPopped && animations.get("popup").isAnimationFinished(imageIndex)) {
            setAnimation("idle");
            imageIndex = 0;
        }
        
        if (isArmed) { // Only advance animation after arming
            imageIndex += delta;
        }
    }
    
    public boolean isArmed() {
        return isArmed;
    }
    
    public void explode() {
        explodeSound.play(0.5f);

        if (!hasExploded && isArmed) {
            hasExploded = true;
            setAnimation("explode");
            imageIndex = 0;
        }
    }
    
    public float getExplosionDamage() {
        return explosionDamage;
    }
    
    public boolean hasExploded() {
        return hasExploded;
    }

    public void dispose() {
        super.dispose();
        explodeSound.dispose();
    }
}
