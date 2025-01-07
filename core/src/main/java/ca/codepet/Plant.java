package ca.codepet;

import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectMap.Entry;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public abstract class Plant {
    protected int hp = 100;
    protected Rectangle rect = new Rectangle(0, 0, 32, 32);
    protected ObjectMap<String, Animation<TextureRegion>> animations = new ObjectMap<>();
    protected String currentAnimation = null;
    protected float imageIndex = 0f;

    protected void setAnimation(String spr) {
        currentAnimation = spr;
    }

    protected void setAnimationUnique(String spr) {
        if (currentAnimation == null || !currentAnimation.equals(spr))
            imageIndex = 0f;
        setAnimation(spr);
    }

    public TextureRegion getTexture() {
        return animations.get(currentAnimation).getKeyFrame(imageIndex, true);
    }

    public Rectangle getRect() {
        return rect;
    }

    public boolean damage(int damage) {
        hp -= damage;
        return hp <= 0;
    }

    public void dispose() {
        for (Entry<String, Animation<TextureRegion>> e : animations) {
            for (TextureRegion t : e.value.getKeyFrames()) {
                t.getTexture().dispose();
            }
        } 
    }

    public abstract void update();
}