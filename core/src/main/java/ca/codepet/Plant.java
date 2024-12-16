package ca.codepet;

import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;

public abstract class Plant {
    protected int hp = 100;
    protected Rectangle rect = new Rectangle(0, 0, 32, 32);
    protected ObjectMap<String, Sprite> sprites = new ObjectMap<>();
    protected String currentSprite = null;
    protected float imageIndex = 0f;

    protected void setSprite(String spr) {
        currentSprite = spr;
    }

    protected void setSpriteUnique(String spr) {
        if (!currentSprite.equals(spr))
            imageIndex = 0f;
        setSprite(spr);
    }

    public Sprite getCurrentSprite() {
        return sprites.get(currentSprite);
    }

    public Rectangle getRect() {
        return rect;
    }

    public boolean damage(int damage) {
        hp -= damage;
        return hp <= 0;
    }

    public abstract void update();
}