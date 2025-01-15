package ca.codepet.Plants;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import ca.codepet.Plant;

public class PotatoMine extends Plant {
    private static final float ARMING_TIME = 15f; // seconds to arm
    private float armingTimer = 0f;
    private boolean isArmed = false;
    private boolean hasExploded = false;
    private float explosionDamage = 1800f;
    
    public PotatoMine(float x, float y) {
        super(x, y);
        
        // Load animations from atlas files
        TextureAtlas popupAtlas = new TextureAtlas(Gdx.files.internal("plants/potato-popup.atlas"));
        TextureAtlas idleAtlas = new TextureAtlas(Gdx.files.internal("plants/potato-idle.atlas"));
        TextureAtlas explodeAtlas = new TextureAtlas(Gdx.files.internal("plants/potato-explode.atlas"));
        
        // Create animations
        animations.put("popup", new Animation<>(0.1f, popupAtlas.getRegions()));
        animations.put("idle", new Animation<>(0.1f, idleAtlas.getRegions()));
        animations.put("explode", new Animation<>(0.1f, explodeAtlas.getRegions()));
        
        // Set initial animation
        setAnimation("popup");
        
        // Set health
        health = 300;
    }
    
    @Override
    public void update(float delta) {
        if (hasExploded) {
            if (animations.get("explode").isAnimationFinished(imageIndex)) {
                health = 0; // Mark for removal
            }
            imageIndex += delta;
            return;
        }
        
        if (!isArmed) {
            armingTimer += delta;
            if (armingTimer >= ARMING_TIME) {
                isArmed = true;
                setAnimation("idle");
                imageIndex = 0;
            }
        }
        
        imageIndex += delta;
    }
    
    public boolean isArmed() {
        return isArmed;
    }
    
    public void explode() {
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
}
