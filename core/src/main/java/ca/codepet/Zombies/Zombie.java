package ca.codepet.Zombies;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import ca.codepet.Plant;
import ca.codepet.worlds.DayWorld;

// info
// gargantuar 3000 hp, instakill any permanent plant except spikerock which takes 9 hits
// basic zombie 200 hp
// https://plantsvszombies.fandom.com/wiki/Plants_(PvZ)#List_of_plants
// https://pvzstrategy.fandom.com/wiki/Plant_Stats

public abstract class Zombie {

    private int hp;
    private int x = Gdx.graphics.getWidth();
    private int y;

    Random rand = new Random();
    private Texture zombieTexture;
    private TextureRegion textureRegion;
    private Sound[] chompSounds;

    private int row, col;
    
    private int damage = 10;

    private float atkDelay; // 1 second between attacks
    private float attackTimer = 0.0f;

    private int width = 64;
    private int height = 120;
    
    DayWorld world;

  
    public Zombie(DayWorld theWorld, Texture zombieTexture, int hp, int damage, float atkDelay)
    {
        this.hp = hp;
        this.damage = damage;
        this.atkDelay = atkDelay;

        this.zombieTexture = zombieTexture;
        
        textureRegion = new TextureRegion(zombieTexture);

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

    public TextureRegion getTextureRegion() {
        return textureRegion;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
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
        if (attackTimer >= atkDelay) {
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
