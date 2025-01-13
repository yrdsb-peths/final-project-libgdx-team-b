package ca.codepet;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public abstract class Zombie {
    protected Texture texture;
    protected Sprite sprite;
    protected Rectangle rect;
    // hit points
    protected int hp;
    // speed
    protected float spd;
    // shield
    protected int shl;

    public Zombie(float x, float y, String spritePath, int hp, float spd, int shl)
    {
        texture = new Texture(spritePath);
        sprite = new Sprite(texture);
        sprite.setPosition(x, y);
        rect = new Rectangle(x, y, sprite.getWidth(), sprite.getHeight());
        this.hp = hp;
        this.spd = spd;
        this.shl = shl;
    }

    public void render()
    {
        
    }

    public void update()
    {
        
    }

    public float getX()
    {
        return sprite.getX();
    }

    public float getY()
    {
        return sprite.getY();
    }

    public Sprite getSprite()
    {
        return sprite;
    }

    public Rectangle getRect()
    {
        return rect;
    }

    public void setPosition(float x, float y)
    {
        sprite.setPosition(x, y);
        rect.setPosition(x, y);
    }

    public void setSize(float width, float height) {
        sprite.setSize(width, height);
        rect.setSize(width, height);
    }

    public void move()
    {
        sprite.translateX(-spd);
        rect.setPosition(sprite.getX(), sprite.getY());
        // the zombie enters the house
        if (sprite.getX() + sprite.getWidth() < 0)
        {
            // game over
        }
    }

    // zombie takes damage
    public void takeDamage(int dmg)
    {
        if(shl > 0)
        {
            shl -= dmg;
        }
        else
        {
            hp -= dmg;
        }
    }

    // change the sprite of the zombie
    public void removeShield()
    {
        
    }

    // zombie damages plant
    public void damage(Plant plant)
    {
        plant.damage(1);
    }

    public void die()
    {
        if(hp <= 0)
        {
            dispose();
        }
    }

    // draw zombie sprite
    public void draw(SpriteBatch batch) {
        sprite.draw(batch);
    }

    public void dispose() {
        sprite = null;
        texture.dispose();
    }
}
