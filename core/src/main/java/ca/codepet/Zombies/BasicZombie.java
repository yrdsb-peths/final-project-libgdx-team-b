package ca.codepet.Zombies;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import ca.codepet.Plant;
import ca.codepet.worlds.DayWorld;

public class BasicZombie extends Zombie {

    Random rand = new Random();
    private Texture zombieTexture;

    private int x = Gdx.graphics.getWidth();
    private int y;
    
    private int row, col;
    
    DayWorld world;

    public BasicZombie(DayWorld theWorld) {
        zombieTexture = new Texture("images/zombie.png");

        world = theWorld;

        row = rand.nextInt(world.getLawnHeight());
        

        System.out.println("row: " + row);
    }

    public Texture getTexture() {
        return zombieTexture;
    }

    public int getX() {
        return x;
    }

    public void move() {
        x -= 1;
        col = (int) (x/world.getLawnTileWidth()) - 1 ;
        if(col > 9) col = 8;
        
        System.out.println("collll: " + col);
    }

        
    @Override
    public void damage(Plant plant, int dmg) {
        // zombie damages a plant
        plant.damage(dmg);
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }
    
}
