package ca.codepet.Zombies;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import ca.codepet.Plant;
import ca.codepet.worlds.DayWorld;

public class BasicZombie extends Zombie {

    Random rand = new Random();
    //basic zombie moves 1 tile in 15 seconds
    private Texture zombieTexture;
    private SpriteBatch batch;

    private int x = Gdx.graphics.getWidth();
    private int y;
    

    public BasicZombie(DayWorld world) {
        zombieTexture = new Texture("images/zombie.png");
        batch = new SpriteBatch();

        final int lawnRow = rand.nextInt(world.getLawnHeight());
        y = world.getLawnTileHeight() * lawnRow;
    }

    public void render() {
        batch.begin();
        
        batch.draw(zombieTexture, x, y);
        x-=1;
        batch.end();
    }
        
    @Override
    public void damage(Plant plant, int dmg) {
        // zombie damages a plant
        plant.damage(dmg);
    }
    
}
