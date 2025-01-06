package ca.codepet.sample;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public abstract class Zombie {
    protected Sprite sprite;
    protected String type;
    protected int hp;
    protected int atk;
    protected int spd;
    protected int armor;

    public Zombie(String spritePath, String type, int hp, int atk, int spd, int armor)
    {
        this.sprite = new Sprite(new Texture(spritePath));
        this.type = type;
        this.hp = hp;
        this.atk = atk;
        this.spd = spd;
        this.armor = armor;
    }

    public void move()
    {
        float deltaTime = Gdx.graphics.getDeltaTime();
        // move right
        sprite.setX(sprite.getX() - spd * deltaTime);
    }

    // zombie takes damage
    public void takeDamage(int dmg)
    {
        hp -= dmg;
    }

    // zombie damages a plant
    public void damage(int dmg)
    {
        
    }

    public void die()
    {
        // if zombie's hp is equal to or lower than 0
        if(hp <= 0)
        {
            // remove zombie sprite
        }
    }

    public void draw(SpriteBatch batch) {
        sprite.draw(batch);
    }
}
