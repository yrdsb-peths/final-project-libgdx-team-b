package ca.codepet.Zombies;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import ca.codepet.Plant;
import ca.codepet.Zombie;
import ca.codepet.worlds.DayWorld;



public class BasicZombie extends Zombie {

    Random rand = new Random();
    private Texture zombieTexture;

    private int x = Gdx.graphics.getWidth();
    private int y;
    
    private int row, col;
    
    private int health = 100; 
    private int damage = 10;

    DayWorld world;
    public BasicZombie(int x, int y) {
        super(x, y, "zombies/BasicZombie.png", 200, 0.26f, 0);
        setSize(80, 110);
    }

    public BasicZombie(DayWorld theWorld) {
        super("images/zombie.png", 100, 10, 0);
        zombieTexture = new Texture("images/zombie.png");

        world = theWorld;

        row = rand.nextInt(world.getLawnHeight());
        
    }

    public Texture getTexture() {
        return zombieTexture;
    }

    public float getX() {
        return x;
    }

    public void move() {
        x -= 1;
        col = (int) (x/world.getLawnTileWidth()) - 1 ;
        if(col > 9) col = 8;
        
    }

    

    public int getAttack() {
        return damage;
    }
        

    // @Override
    // public void damage(Plant plant, int dmg) {
    //     // zombie damages a plant
    //     // plant.damage(dmg);
    // }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }
    
    public void dispose() {
        zombieTexture.dispose();
    }
    
}
