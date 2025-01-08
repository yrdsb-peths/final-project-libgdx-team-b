package ca.codepet.Zombies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import ca.codepet.Plant;

// info
// gargantuar 3000 hp, instakill any permanent plant except spikerock which takes 9 hits
// basic zombie 200 hp
// https://plantsvszombies.fandom.com/wiki/Plants_(PvZ)#List_of_plants
// https://pvzstrategy.fandom.com/wiki/Plant_Stats

public abstract class Zombie {
    private Texture zombieTexture;
    private SpriteBatch batch;

    protected Sprite sprite;
    protected String type;
    protected int hp;
    protected int atk;
    protected int spd;
    protected int armor;

    public Zombie()
    {
        // this.sprite = new Sprite(new Texture(spritePath));
        // this.type = type;
        // this.hp = hp;
        // this.atk = atk;
        // this.spd = spd;
        // this.armor = armor;

        zombieTexture = new Texture("images/menu.png");
        batch = new SpriteBatch();
    }

    public void render() {
        batch.begin();
        batch.draw(zombieTexture, 0, 0);
        batch.end();
    }

    public void move()
    {
        float deltaTime = Gdx.graphics.getDeltaTime();
        // move left
        sprite.setX(sprite.getX() - spd * deltaTime);
    }

    // zombie takes damage
    public void takeDamage(int dmg)
    {
        hp -= dmg;
    }

    // zombie damages a plant
    // 1 bite = 1 dmg
    // typical plants have 6 hp
    public abstract void damage(Plant plant, int dmg);

    public void die()
    {
        // once zombie hp reaches 0, remove zombie sprite
        if(hp <= 0)
        {
            sprite = null;
        }
    }

    // draw zombie sprite
    public void draw(SpriteBatch batch) {
        sprite.draw(batch);
    }

    //dispose 
    public void dispose(){
        sprite.getTexture().dispose();
    }
}
