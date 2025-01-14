package ca.codepet.Zombies;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import ca.codepet.Plant;
import ca.codepet.Zombie;
import ca.codepet.worlds.DayWorld;



public class BasicZombie {

    Random rand = new Random();
    private Texture zombieTexture;
    private Sound[] chompSounds;

    private int x = Gdx.graphics.getWidth();
    private int y;
    
    private int row, col;
    
    private int health = 100; 
    private int damage = 10;

    private float ATTACK_DELAY = 2.0f; // 1 second between attacks
    private float attackTimer = 0.0f;

    DayWorld world;

    public BasicZombie(DayWorld theWorld) {
        zombieTexture = new Texture("images/zombie.png");
        
        // Load multiple chomp sounds
        chompSounds = new Sound[] {
            Gdx.audio.newSound(Gdx.files.internal("sounds/chomp.ogg")),
            Gdx.audio.newSound(Gdx.files.internal("sounds/chomp2.ogg")),
            Gdx.audio.newSound(Gdx.files.internal("sounds/chomp3.ogg"))
        };

        world = theWorld;

        row = rand.nextInt(world.getLawnHeight());
        
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
        
    }

    public void update(float delta) {
        attackTimer += delta;
    }

    public boolean canAttack() {
        if (attackTimer >= ATTACK_DELAY) {
            attackTimer = 0;
            return true;
        }
        return false;
    }

    public void attack(Plant plant) {
        if (plant != null) {
            plant.damage(damage);
            playChompSound();
        }
    }

    public int getAttack() {
        return damage;
    }
        
    public void playChompSound() {
        // Play random chomp sound
        chompSounds[rand.nextInt(chompSounds.length)].play(0.5f);
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }
    
    public void dispose() {
        zombieTexture.dispose();
        for(Sound sound : chompSounds) {
            sound.dispose();
        }
    }
    
}
