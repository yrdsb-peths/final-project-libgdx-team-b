package ca.codepet;

import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectMap.Entry;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.math.Rectangle;

public abstract class Plant {
    protected float x, y;
    protected int health = 100;
    protected Rectangle rect = new Rectangle(0, 0, 32, 32);
    protected ObjectMap<String, Animation<AtlasRegion>> animations = new ObjectMap<>();
    protected String currentAnimation = null;
    protected float imageIndex = 0f;

    public Plant(float x, float y) {
        this.x = x;
        this.y = y;
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
        if(health <= 0){
            dispose();
            return true;
        }
        return false;
    }

    public boolean isDead() {
        return health <= 0;
    }

    public void dispose() {
        for (Entry<String, Animation<AtlasRegion>> entry : animations.entries()) {
            entry.value.getKeyFrames()[0].getTexture().dispose();
        }
        
    }

    public void render(SpriteBatch batch) {
        AtlasRegion tex = getTexture();
        float pX = x + tex.offsetX - tex.originalWidth / 2;
        float pY = y + tex.offsetY - tex.originalHeight / 2;
        batch.draw(tex, pX, pY);
    }

    public abstract void update(float delta);

}