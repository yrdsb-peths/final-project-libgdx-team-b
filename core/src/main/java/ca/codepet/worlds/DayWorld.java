package ca.codepet.worlds;

import com.badlogic.gdx.math.MathUtils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;

import ca.codepet.Plant;
import ca.codepet.Plants.Peashooter;
import ca.codepet.Plants.Sunflower;
import ca.codepet.Zombies.BasicZombie;
import ca.codepet.Zombies.BucketheadZombie;
import ca.codepet.Zombies.Zombie;
import ca.codepet.ui.PlantBar;
import ca.codepet.GameRoot;
import ca.codepet.characters.PlantCard;
import ca.codepet.characters.Sun;
import ca.codepet.ui.PlantPicker;
import ca.codepet.ui.Shovel;

import java.util.Random;

public class DayWorld implements Screen {
    private Texture backgroundTexture;
    private SpriteBatch batch;
    private PlantBar plantBar;
    private Plant[][] plants;
    private ShapeRenderer shape = new ShapeRenderer();
    private PlantPicker plantPicker;
    private boolean gameStarted = false;

    
    // "final" denotes that this is a constant and cannot be reassigned
    final private int LAWN_WIDTH = 9;
    final private int LAWN_HEIGHT = 5;
    final private int LAWN_TILEWIDTH = 80;
    final private int LAWN_TILEHEIGHT = 96;
    final private int LAWN_TILEX = 56;
    final private int LAWN_TILEY = 416;

    private static final float SUN_SPAWN_RATE = 1f; // seconds
    private float sunSpawnTimer = 0f;
    // Add array to track suns
    private Array<Sun> suns = new Array<>();

    private int sunBalance = 0;

    private final GameRoot game; 

    // private Zombie zombie = new BasicZombie(this);
    private Array<Zombie> zombies = new Array<>();

    private float waveTimer = 0f;
    private float timeBetweenWaves = 10f; // seconds

    private PlantCard draggedCard = null;
    private Plant draggedPlant = null;
    private Plant ghostPlant = null;
    private boolean isValidPlacement = false;

    private Array<Zombie> randomZombies = new Array<>();

    private Shovel shovel;
    private boolean isShovelDragging = false;

    public DayWorld(GameRoot game) {
        this.game = game;

    }
    @Override
    public void show() {
        addZombie(new BasicZombie(this));

        // Load the background texture
        backgroundTexture = new Texture("backgrounds/day.png");
        batch = new SpriteBatch();
        plantBar = new PlantBar(sunBalance);
        plants = new Plant[LAWN_HEIGHT][LAWN_WIDTH];
        for (int y = 0; y < LAWN_HEIGHT; y++) {
            for (int x = 0; x < LAWN_WIDTH; x++) {
                plants[y][x] = null;
            }
        }
        plantPicker = new PlantPicker(plantBar);

        shovel = new Shovel(650, Gdx.graphics.getHeight() - 64);
    }


    // public void spawnRandomZombie() {
    //     Random random = new Random();
    //     int randomRow = random.nextInt(LAWN_HEIGHT);
    //     int x = Gdx.graphics.getWidth(); // zombies spawn off the screen
    //     int y = LAWN_TILEY - randomRow * LAWN_TILEHEIGHT;
    //     int randomZombie = random.nextInt(3);
    //     if(randomZombie == 0)
    //     {
    //         BasicZombie basicZombie = new BasicZombie(x, y);
    //         randomZombies.add(basicZombie);
    //     }
    //     else if(randomZombie == 1)
    //     {
    //         ConeheadZombie coneheadZombie = new ConeheadZombie(x, y);
    //         randomZombies.add(coneheadZombie);
    //     }
    //     else
    //     {
    //         BucketheadZombie bucketheadZombie = new BucketheadZombie(x, y);
    //         randomZombies.add(bucketheadZombie);
    //     }
    // }

    // public void testSpawn() {
    //     // Check for 'E' key press to spawn BungeeZombie
    //     if (Gdx.input.isKeyJustPressed(Keys.E) && plants[0][0] != null) {
    //         spawnBungeeZombie();
    //     }

    //     // Check for 'R' key press to spawn BasicZombie
    //     if (Gdx.input.isKeyJustPressed(Keys.R)) {
    //         spawnRandomZombie();
    //     }
    // }

    @Override
    public void render(float delta) {
        // Draw the background texture
        batch.begin();
        batch.draw(backgroundTexture, -200, 0);
        batch.end();

        // Draw the plant bar first (at the bottom layer)
        plantBar.render();

        // After plantBar.render(), add shovel handling
        if (Gdx.input.justTouched()) {
            float mouseX = Gdx.input.getX();
            float mouseY = Gdx.graphics.getHeight() - Gdx.input.getY();
            
            if (shovel.isClicked(mouseX, mouseY)) {
                isShovelDragging = true;
                shovel.setDragging(true);
            }
        }
        
        if (isShovelDragging && !Gdx.input.isTouched()) {
            float mouseX = Gdx.input.getX();
            float mouseY = Gdx.graphics.getHeight() - Gdx.input.getY();
            int tileX = MathUtils.floor((mouseX - LAWN_TILEX) / LAWN_TILEWIDTH);
            int tileY = -MathUtils.floor((mouseY - LAWN_TILEY) / LAWN_TILEHEIGHT);
            
            if (tileX >= 0 && tileX < LAWN_WIDTH && tileY >= 0 && tileY < LAWN_HEIGHT) {
                if (plants[tileY][tileX] != null) {
                    plants[tileY][tileX].dispose();
                    plants[tileY][tileX] = null;
                }
            }
            
            isShovelDragging = false;
            shovel.setDragging(false);
        }

        shovel.render();

        if (!gameStarted) {
            // Render plant picker if game hasn't started
            plantPicker.render();
            if (plantPicker.isPicked()) {
                gameStarted = true;
                // Start cooldowns for all plant cards when game starts
                plantBar.startAllCardCooldowns();
                plantBar.startGame();  // Add this line to enable affordability checks
            }

            if (Gdx.input.justTouched()) {
                float mouseX = Gdx.input.getX();
                float mouseY = Gdx.graphics.getHeight() - Gdx.input.getY();
                PlantCard clickedCard = plantBar.checkCardClick(mouseX, mouseY);
                if (clickedCard != null) {
                    plantPicker.returnCard(clickedCard.getPlantType());
                }
            }
            
            // Don't continue the game until picked
            return;
        }

        batch.begin();
        // Draw plants first
        float mouseX = Gdx.input.getX();
        float mouseY = Gdx.graphics.getHeight() - Gdx.input.getY();
        int clickedTileX = MathUtils.floor((mouseX - LAWN_TILEX) / LAWN_TILEWIDTH);
        int clickedTileY = -MathUtils.floor((mouseY - LAWN_TILEY) / LAWN_TILEHEIGHT);

        // Handle dragging
        if (Gdx.input.justTouched()) {
            draggedCard = plantBar.checkCardDragStart(mouseX, mouseY);
            if (draggedCard != null && plantBar.canAffordCard(draggedCard)) {
                // Create actual plant and ghost plant
                if (draggedCard.getPlantType().equals("Peashooter")) {
                    draggedPlant = new Peashooter(mouseX, mouseY);
                    ghostPlant = new Peashooter(0, 0);
                } else if (draggedCard.getPlantType().equals("Sunflower")) {
                    draggedPlant = new Sunflower(mouseX, mouseY, this);
                    ghostPlant = new Sunflower(0, 0, this);
                }
            } else {
                draggedCard = null;
            }
        } else if (draggedCard != null) {
            // Update dragged plant position
            if (draggedPlant != null) {
                draggedPlant.setPosition(mouseX, mouseY);
            }
            
            // Update ghost plant position and validity
            isValidPlacement = false;
            if (clickedTileX >= 0 && clickedTileX < LAWN_WIDTH && 
                clickedTileY >= 0 && clickedTileY < LAWN_HEIGHT) {
                if (ghostPlant != null) {
                    float plantX = LAWN_TILEX + (clickedTileX * LAWN_TILEWIDTH) + (LAWN_TILEWIDTH / 2);
                    float plantY = LAWN_TILEY - (clickedTileY * LAWN_TILEHEIGHT) + (LAWN_TILEHEIGHT / 2);
                    ghostPlant.setPosition(plantX, plantY);
                }
                isValidPlacement = plants[clickedTileY][clickedTileX] == null;
            }
            
            if (!Gdx.input.isTouched()) {
                // Only try to place plant if we're in valid bounds and placement is valid
                if (isValidPlacement && clickedTileX >= 0 && clickedTileX < LAWN_WIDTH && 
                    clickedTileY >= 0 && clickedTileY < LAWN_HEIGHT) {
                    
                    if (plantBar.deductSun(draggedCard.getCost())) {
                        float plantX = LAWN_TILEX + (clickedTileX * LAWN_TILEWIDTH) + (LAWN_TILEWIDTH / 2);
                        float plantY = LAWN_TILEY - (clickedTileY * LAWN_TILEHEIGHT) + (LAWN_TILEHEIGHT / 2);
                        draggedPlant.setPosition(plantX, plantY);
                        plants[clickedTileY][clickedTileX] = draggedPlant;
                        draggedCard.startCooldown();
                    }
                }
                
                // Clean up
                if (draggedPlant != null) {
                    if (clickedTileX < 0 || clickedTileX >= LAWN_WIDTH || 
                        clickedTileY < 0 || clickedTileY >= LAWN_HEIGHT ||
                        !isValidPlacement ||
                        plants[clickedTileY][clickedTileX] != draggedPlant) {
                        draggedPlant.dispose();
                    }
                }
                if (ghostPlant != null) {
                    ghostPlant.dispose();
                }
                draggedPlant = null;
                ghostPlant = null;
                plantBar.resetCardPosition(draggedCard);
                draggedCard.stopDragging();
                draggedCard = null;
                isValidPlacement = false;
            }
        }

        for (int y = 0; y < LAWN_HEIGHT; y++) {
            for (int x = 0; x < LAWN_WIDTH; x++) {
                Plant p = plants[y][x];
                if (p != null) {
                    p.update(delta);
                    p.render(batch);
                } else if (x == clickedTileX && y == clickedTileY) {
                    // Draw "ghost" of plant here
                }
            }
        }

        // Render dragged plant
        if (draggedPlant != null) {
            draggedPlant.render(batch);
        }

        // Render ghost plant
        if (ghostPlant != null && isValidPlacement) {
            ghostPlant.setAlpha(0.5f);  // Make translucent
            ghostPlant.render(batch);
            ghostPlant.setAlpha(1.0f);  // Reset alpha
        }

        // Draw zombies second (on top of plants)
        for(Zombie zombie : zombies) {

            if(zombie.getCol() < 0) {
                removeZombie(zombie);
                goEndscreen();
                break;
                // TODO end screen go there
            }
            
            // System.out.println(zombie.getRow());
            
            batch.draw(zombie.getTextureRegion(), 
                      zombie.getX() + zombie.getXOffset(), 
                      (LAWN_HEIGHT - zombie.getRow()) * LAWN_TILEHEIGHT - (zombie.getHeight() - LAWN_TILEHEIGHT)/2 + zombie.getYOffset(),
                      zombie.getWidth(),
                      zombie.getHeight());


            Plant plant = plants[zombie.getRow()][zombie.getCol()];


            zombie.update(delta);
            
            if(plant != null) {
                if(zombie.canAttack()) {  // Only attack if cooldown is ready
                    zombie.attack(plant);
                    if(plant.isDead()) plants[zombie.getRow()][zombie.getCol()] = null;
                }
            } else {
                zombie.move();
            }
        }

        // Draw suns last (on top of zombies)
        // Update sun spawning
        sunSpawnTimer += delta;
        if (sunSpawnTimer >= SUN_SPAWN_RATE) {
            sunSpawnTimer = 0f;
            // Create new sun and add to array
            suns.add(new Sun());
        }

        // Render all suns and check for collection
        // Loop through suns
        for (int i = 0; i < suns.size; i++) {
            Sun sun = suns.get(i);
            // Check if the sun was collected
            if (sun.checkClick()) {
                // Add 25 balance
                sunBalance += 25;
                plantBar.setSunDisplay(sunBalance);
                // Remove sun from array and dispose
                suns.removeIndex(i);
                sun.dispose();
                // Since we've removed our current index, decrement i
                i--;
            } else if (!sun.isAlive()) {
                // If the sun is dead (expired), remove and dispose
                suns.removeIndex(i);
                sun.dispose();
                // Since we've removed our current index, decrement i
                i--;
            } else {
                // Render the sun
                sun.render(batch);
            }
        }

        batch.end();

        // Draw the plant bar
        plantBar.render();
    }

    public void addZombie(Zombie zombie) {
        System.out.println(3333);
        zombies.add(zombie);
    }
    
    public void removeZombie(Zombie zombie) {
        zombie.dispose();
        zombies.removeValue(zombie, true);
    }

    public void goEndscreen() {
        // game.setScreen(new EndScreen(game));
    }

    private void spawnWave() {
        int numberOfZombies = 5; // Number of zombies per wave
        for (int i = 0; i < numberOfZombies; i++) {
            addZombie(new BucketheadZombie(this));
        }
    }

    public void addSun(Sun sun) {
        suns.add(sun);
    }

    @Override
    public void resize(int width, int height) {
        // Resize your screen here. The parameters represent the new window size.
    }

    @Override
    public void pause() {
        // Invoked when your application is paused.
    }

    @Override
    public void resume() {
        // Invoked when your application is resumed after pause.
    }

    @Override
    public void hide() {
        // This method is called when another screen replaces this one.
    }

    public int getLawnHeight() {
        return LAWN_HEIGHT;
    }

    public int getLawnTileHeight() {
        return LAWN_TILEHEIGHT;
    }

    public int getLawnTileWidth() {
        return LAWN_TILEWIDTH;
    }

    @Override
    public void dispose() {
        // Destroy screen's assets here.
        backgroundTexture.dispose();
        batch.dispose();
        plantBar.dispose();
        // Dispose suns
        for(Sun sun : suns) {
            sun.dispose();
        }

        for(Zombie zombie : zombies) {
            zombie.dispose();
        }
        zombies.clear();

        // Add to existing dispose method
        shovel.dispose();
    }
}