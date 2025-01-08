package ca.codepet.Zombies;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import ca.codepet.Plant;

public class BasicZombie extends Zombie {
    //basic zombie moves 1 tile in 15 seconds
    private Texture zombieTexture;
    private SpriteBatch batch;

    public BasicZombie() {
        zombieTexture = new Texture("images/zombie.png");
        batch = new SpriteBatch();
    }

    public void render() {
        batch.begin();
        batch.draw(zombieTexture, 400, 300);
        batch.end();
    }
        
    @Override
    public void damage(Plant plant, int dmg) {
        // zombie damages a plant
        plant.damage(dmg);
    }
    
}
