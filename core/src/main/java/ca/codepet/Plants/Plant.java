package ca.codepet.Plants;

import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectMap.Entry;

import ca.codepet.worlds.DayWorld;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.audio.Sound;
import java.util.Random;

public abstract class Plant {
    private DayWorld world;
    protected float x, y;
    protected int health = 100;
    protected Rectangle rect = new Rectangle(0, 0, 32, 32);
    protected ObjectMap<String, Animation<AtlasRegion>> animations = new ObjectMap<>();
    protected String currentAnimation = null;
    protected float imageIndex = 0f;
    protected float alpha = 1.0f;
    protected float flash = 0f;
    protected float scale = 1f; // Add this line
    protected Sound[] hitSounds;
    protected Sound[] deathSounds;
    protected Random rand = new Random();
    protected float attackCooldown = 0;
    protected float attackTimer = 0;
    protected boolean isAttacking = false;

    public Plant(DayWorld world, float x, float y, int health) {
        this.world = world;
        this.x = x;
        this.y = y;
        this.health = health;
    }

    protected void setAnimation(String spr) {
        currentAnimation = spr;
    }

    protected void setAnimationUnique(String spr) {
        if (currentAnimation == null || !currentAnimation.equals(spr))
            imageIndex = 0f;
        setAnimation(spr);
    }

    public DayWorld getWorld() {
        return world;
    }

    public AtlasRegion getTexture() {
        return animations.get(currentAnimation).getKeyFrame(imageIndex, true);
    }

    public Rectangle getRect() {
        return rect;
    }

    public boolean damage(int dmg) {
        flash = 0.2f;
        health -= dmg;
        if (health <= 0) {
            // playDeathSound();
            dispose();
            return true;
        }
        // playHitSound();
        return false;
    }

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
        if (tex == null || health <= 0)
            return; // Add this check

        float pX = x + tex.offsetX - (tex.originalWidth * scale) / 2;
        float pY = y + tex.offsetY - (tex.originalHeight * scale) / 2;

        float oldAlpha = batch.getColor().a;
        batch.setColor(batch.getColor().r, batch.getColor().g, batch.getColor().b, alpha);
        batch.draw(tex, pX, pY, tex.originalWidth * scale, tex.originalHeight * scale); // Modified to use scale

        batch.setShader(world.getGame().getFlashShader());
        batch.setColor(1f, 1f, 1f, flash / 0.2f * alpha);
        batch.draw(tex, pX, pY, tex.originalWidth * scale, tex.originalHeight * scale); // Modified to use scale
        batch.setShader(null);
        batch.setColor(batch.getColor().r, batch.getColor().g, batch.getColor().b, oldAlpha);
    }

    public void update(float delta) {
        flash = Math.max(0f, flash - delta);
    }

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