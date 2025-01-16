package ca.codepet.Plants;

import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectMap.Entry;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.Gdx;
import java.util.Random;

public abstract class Plant {
    protected float x, y;
    protected int health = 100;
    protected Rectangle rect = new Rectangle(0, 0, 32, 32);
    protected ObjectMap<String, Animation<AtlasRegion>> animations = new ObjectMap<>();
    protected String currentAnimation = null;
    protected float imageIndex = 0f;
    protected float alpha = 1.0f;
    protected float scale = 1f; // Add this line
    protected Sound[] hitSounds;
    protected Sound[] deathSounds;
    protected Random rand = new Random();
    protected float attackCooldown = 0;
    protected float attackTimer = 0;
    protected boolean isAttacking = false;

    public Plant(float x, float y) {
        this.x = x;
        this.y = y;
        

        

        // // Load hit sounds
        // hitSounds = new Sound[] {
        //     Gdx.audio.newSound(Gdx.files.internal("sounds/plant_hit1.ogg")),
        //     Gdx.audio.newSound(Gdx.files.internal("sounds/plant_hit2.ogg"))
        // };
        
        // // Load death sounds
        // deathSounds = new Sound[] {
        //     Gdx.audio.newSound(Gdx.files.internal("sounds/plant_death1.ogg")),
        //     Gdx.audio.newSound(Gdx.files.internal("sounds/plant_death2.ogg"))
        // };
    }

    protected void setAnimation(String spr) {
        currentAnimation = spr;
    }

    protected void setAnimationUnique(String spr) {
        if (currentAnimation == null || !currentAnimation.equals(spr))
            imageIndex = 0f;
        setAnimation(spr);
    }

    public AtlasRegion getTexture() {
        return animations.get(currentAnimation).getKeyFrame(imageIndex, true);
    }

    public Rectangle getRect() {
        return rect;
    }

    public boolean damage(int dmg) {
        health -= dmg;
        if(health <= 0) {
            // playDeathSound();
            dispose();
            return true;
        }
        // playHitSound();
        return false;
    }

    // protected void playHitSound() {
    //     hitSounds[rand.nextInt(hitSounds.length)].play(0.4f);
    // }

    // protected void playDeathSound() {
    //     deathSounds[rand.nextInt(deathSounds.length)].play(0.5f);
    // }

    public boolean isDead() {
        return health <= 0;
    }

    public void dispose() {
        for (Entry<String, Animation<AtlasRegion>> entry : animations.entries()) {
            for (AtlasRegion atlas : entry.value.getKeyFrames()) {
                if (atlas != null) {
                    Texture tex = atlas.getTexture();
                    if (tex != null)
                        tex.dispose();
                }
            }
        }
        // for(Sound sound : hitSounds) {
        //     sound.dispose();
        // }
        // for(Sound sound : deathSounds) {
        //     sound.dispose();
        // }

    }

    public void setAlpha(float alpha) {
        this.alpha = alpha;
    }

    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

    public void render(SpriteBatch batch) {
        AtlasRegion tex = getTexture();
        if (tex == null || health <= 0) return; // Add this check
        
        float pX = x + tex.offsetX - (tex.originalWidth * scale) / 2;
        float pY = y + tex.offsetY - (tex.originalHeight * scale) / 2;
        
        float oldAlpha = batch.getColor().a;
        batch.setColor(batch.getColor().r, batch.getColor().g, batch.getColor().b, alpha);
        batch.draw(tex, pX, pY, tex.originalWidth * scale, tex.originalHeight * scale); // Modified to use scale
        batch.setColor(batch.getColor().r, batch.getColor().g, batch.getColor().b, oldAlpha);
    }

    public abstract void update(float delta);

    public void startAttack() {
        isAttacking = true;
        setAnimationUnique("attack");
    }

    public void stopAttack() {
        isAttacking = false;
        setAnimationUnique("idle");
    }

    public boolean isAttacking() {
        return isAttacking;
    }

    public boolean canAttack() {
        return attackTimer >= attackCooldown;
    }

    public void resetAttackTimer() {
        attackTimer = 0;
    }
}