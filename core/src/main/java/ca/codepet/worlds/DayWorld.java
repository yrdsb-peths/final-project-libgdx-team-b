package ca.codepet.worlds;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;

import ca.codepet.Collidable;
import ca.codepet.GameRoot;
import ca.codepet.Lawnmower;
import ca.codepet.WaveManager;
import ca.codepet.Plants.Peashooter;
import ca.codepet.Plants.Plant;
import ca.codepet.Plants.PotatoMine;
import ca.codepet.Plants.Projectile;
import ca.codepet.Plants.Repeater;
import ca.codepet.Plants.ShooterPlant;
import ca.codepet.Plants.SnowPea;
import ca.codepet.Plants.Squash;
import ca.codepet.Plants.Sunflower;
import ca.codepet.Plants.TallNut;
import ca.codepet.Plants.Threepeater;
import ca.codepet.Plants.Walnut;
import ca.codepet.Zombies.Zombie;
import ca.codepet.characters.PlantCard;
import ca.codepet.characters.Sun;
import ca.codepet.ui.PlantBar;
import ca.codepet.ui.PlantPicker;
import ca.codepet.ui.Shovel;

public class DayWorld implements Screen {
    private Random rand = new Random();

    private Texture backgroundTexture;
    private SpriteBatch batch;
    private PlantBar plantBar;
    private Plant[][] plants;
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
    private static final float END_GAME_TIMER = 5f; // seconds
    private static final float GAME_OVER_DELAY = 5f; // seconds
    private static final float ZOMBIE_DISPOSE_DELAY = 7f;

    private boolean isGameOver = false;
    private float gameOverDelayTimer = 0f;
    private boolean gameOverPending = false;

    private float sunSpawnTimer = 0f;
    private float endGameTimer = 0f;
    private float zombieDisposeTimer = 0f;

    // Add array to track suns
    private Array<Sun> suns = new Array<>();

    private int sunBalance = 0;

    private final GameRoot game;

    // private Zombie zombie = new BasicZombie(this);
    private Array<Zombie> zombies = new Array<>();
    private Array<Lawnmower> lawnmowers = new Array<>();

    private PlantCard draggedCard = null;
    private Plant draggedPlant = null;
    private Plant ghostPlant = null;
    private boolean isValidPlacement = false;

    private Shovel shovel;
    private boolean isShovelDragging = false;

    private WaveManager waveManager;

    private boolean loseSoundPlayed = false;

    private final Sound loseSound = Gdx.audio.newSound(Gdx.files.internal("sounds/loseMusic.ogg"));
    private final Sound shovelSound = Gdx.audio.newSound(Gdx.files.internal("sounds/shovel.ogg"));
    private final Sound sunPickupSound = Gdx.audio.newSound(Gdx.files.internal("sounds/sunPickup.mp3"));
    private final Sound buttonClickSound = Gdx.audio.newSound(Gdx.files.internal("sounds/buttonClick.ogg"));
    private final Sound[] plantDeathSounds = new Sound[] {
            Gdx.audio.newSound(Gdx.files.internal("sounds/plantDie.mp3")),
            Gdx.audio.newSound(Gdx.files.internal("sounds/plantDie2.ogg"))
    };

    private Sound[] plantSpawnSound;

    private final Sound backgroundMusic = Gdx.audio.newSound(Gdx.files.internal("sounds/dayMusic.mp3"));

    private float gameOverScale = 0f;
    private Texture gameOverTexture;

    public DayWorld(GameRoot game) {
        this.game = game;
    }

    @Override
    public void show() {

        // Load plant spawn sounds
        plantSpawnSound = new Sound[] {
                Gdx.audio.newSound(Gdx.files.internal("sounds/plant_spawn.ogg")),
                Gdx.audio.newSound(Gdx.files.internal("sounds/plant_spawn2.ogg"))
        };

        for (int i = LAWN_HEIGHT - 1; i >= 0; i--) {
            lawnmowers.add(new Lawnmower(this, i));
        }

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

        waveManager = new WaveManager(this);

        // Start playing background music on loop
        backgroundMusic.loop(0.2f);

        gameOverTexture = new Texture("images/gameOver.png");
    }

    @Override
    public void render(float delta) {
        if (gameOverPending) {
            gameOverDelayTimer += delta;
            if (gameOverDelayTimer >= GAME_OVER_DELAY) {
                isGameOver = true;
                gameOverPending = false;
            }
        }

        if (isGameOver) {
            gameOver(delta);
        } else {
            renderGame(delta);
        }
    }

    public void renderGame(float delta) {

        // Draw the background texture
        batch.begin();
        batch.draw(backgroundTexture, -200, 0);
        batch.end();

        // Draw the plant bar first (at the bottom layer)
        plantBar.render();

        renderShovel();

        renderLawnmower();

        if (!gameStarted) {
            // Render plant picker if game hasn't started
            plantPicker.render();
            if (plantPicker.isPicked()) {
                gameStarted = true;
                // Start cooldowns for all plant cards when game starts
                plantBar.startAllCardCooldowns();
                plantBar.startGame(); // Add this line to enable affordability checks
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

        if (gameStarted) {
            waveManager.update(delta);
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
                switch (draggedCard.getPlantType()) {
                    case "Peashooter":
                        draggedPlant = new Peashooter(this, mouseX, mouseY);
                        ghostPlant = new Peashooter(this, 0, 0);
                        break;
                    case "Sunflower":
                        draggedPlant = new Sunflower(this, mouseX, mouseY);
                        ghostPlant = new Sunflower(this, 0, 0);
                        break;
                    case "PotatoMine":
                        draggedPlant = new PotatoMine(this, mouseX, mouseY);
                        ghostPlant = new PotatoMine(this, 0, 0);
                        break;
                    case "Walnut":
                        draggedPlant = new Walnut(this, mouseX, mouseY);
                        ghostPlant = new Walnut(this, 0, 0);
                        break;
                    case "TallNut":
                        draggedPlant = new TallNut(this, mouseX, mouseY);
                        ghostPlant = new TallNut(this, 0, 0);
                        break;
                    case "Repeater":
                        draggedPlant = new Repeater(this, mouseX, mouseY);
                        ghostPlant = new Repeater(this, 0, 0);
                        break;
                    case "SnowPea":
                        draggedPlant = new SnowPea(this, mouseX, mouseY);
                        ghostPlant = new SnowPea(this, 0, 0);
                        break;
                    case "Squash":
                        draggedPlant = new Squash(this, mouseX, mouseY);
                        ghostPlant = new Squash(this, 0, 0);
                        break;
                    case "Threepeater":
                        draggedPlant = new Threepeater(this, mouseX, mouseY);
                        ghostPlant = new Threepeater(this, 0, 0);
                        break;
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
                        if (draggedPlant instanceof Squash) {
                            ((Squash)draggedPlant).setRow(clickedTileY);
                        }
                        plants[clickedTileY][clickedTileX] = draggedPlant;
                        plantSpawnSound[rand.nextInt(plantSpawnSound.length)].play(0.5f);
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

        updatePlants(delta);

        for (int y = 0; y < LAWN_HEIGHT; y++) {
            for (int x = 0; x < LAWN_WIDTH; x++) {
                Plant p = plants[y][x];
                if (p != null) {
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
            ghostPlant.setAlpha(0.5f); // Make translucent
            ghostPlant.render(batch);
            ghostPlant.setAlpha(1.0f); // Reset alpha
        }

        // Render the shovel last (on top of everything)
        shovel.render();

        renderZombie(delta);

        renderSun(delta);
    }

    private void renderLawnmower() {
        batch.begin();

        Array<Lawnmower> lawnmowersToRemove = new Array<>();

        for (Lawnmower lawnmower : lawnmowers) {
            if (lawnmower.getIsActivated()) {
                lawnmower.move();

                for (Zombie zombie : zombies) {
                    if (checkCollision(lawnmower, zombie) && !zombie.isSquashed()) {
                        zombie.squash(); // Only squash if not already squashed
                    }
                }
            }

            batch.draw(lawnmower.getTextureRegion(),
                    lawnmower.getX(),
                    lawnmower.getY(),
                    lawnmower.getWidth(),
                    lawnmower.getHeight());

            if (lawnmower.getX() > Gdx.graphics.getWidth()) {
                lawnmowersToRemove.add(lawnmower);
            }
        }

        // Check for zombies that have completed their squash animation
        Array<Zombie> zombiesToRemove = new Array<>();
        for (Zombie zombie : zombies) {
            if (zombie.isSquashed() && zombie.getSquashTimer() >= Zombie.SQUASH_DURATION) {
                zombiesToRemove.add(zombie);
            }
        }

        // Clean up zombies and lawnmowers after iteration

        for(Zombie zombie : zombiesToRemove) {
            removeZombie(zombie, 7f);
        }

        for (Lawnmower lawnmower : lawnmowersToRemove) {
            lawnmower.dispose();
            lawnmowers.removeValue(lawnmower, true);
        }

        batch.end();
    }

    private void renderZombie(float delta) {
        Array<Zombie> zombiesToRemove = new Array<>();
      
        for (Zombie zombie : zombies) {
            // Check if zombie reached the house
            if (zombie.hasReachedHouse() && !zombie.isSquashed()) {
                Lawnmower lawnmower = null;
                for (Lawnmower lm : lawnmowers) {
                    if (lm.getRow() == zombie.getRow()) {
                        lawnmower = lm;
                        break;
                    }
                }
                
                if (lawnmower == null) {
                    gameOverPending = true;
                } else {
                    lawnmower.activate();
                }
            }

            zombie.update(delta);

            // Render the zombie no matter what state it's in
            if (zombie.isSquashed() && zombie.getSquashTimer() >= Zombie.SQUASH_DURATION) {
                zombiesToRemove.add(zombie);
                continue;
            }

            // Render zombie
            if (zombie.getSlowTimer() > 0f)
                batch.setColor(0f, 0f, 1f, 1f);
            batch.draw(zombie.getTextureRegion(),
                    zombie.getX() + zombie.getXOffset(),
                    (LAWN_HEIGHT - zombie.getRow()) * LAWN_TILEHEIGHT - (zombie.getHeight() - LAWN_TILEHEIGHT) / 2
                            + zombie.getYOffset(),
                    zombie.getWidth() / 2,
                    zombie.getHeight() / 2,
                    zombie.getWidth(),
                    zombie.getHeight(),
                    1,
                    zombie.getScaleY(),
                    zombie.getRotation());
            batch.setShader(game.getFlashShader());
            batch.setColor(1f, 1f, 1f, zombie.getFlashTimer() / 0.2f);
            batch.draw(zombie.getTextureRegion(),
                    zombie.getX() + zombie.getXOffset(),
                    (LAWN_HEIGHT - zombie.getRow()) * LAWN_TILEHEIGHT - (zombie.getHeight() - LAWN_TILEHEIGHT) / 2
                            + zombie.getYOffset(),
                    zombie.getWidth() / 2,
                    zombie.getHeight() / 2,
                    zombie.getWidth(),
                    zombie.getHeight(),
                    1,
                    zombie.getScaleY(),
                    zombie.getRotation());
            batch.setShader(null);
            batch.setColor(1f, 1f, 1f, 1f);

            // Only mark for removal if death animation is complete
            if (zombie.isDying() && zombie.isDeathAnimationComplete()) {
                zombiesToRemove.add(zombie);
                continue;
            }

            // Only move or attack if not dying or squashed
            if (!zombie.isSquashed() && !zombie.isDying()) {
                // Check if zombie reached the house
                if (zombie.hasReachedHouse() && !zombie.isSquashed()) {
                    Lawnmower lawnmower = null;
                    for (Lawnmower lm : lawnmowers) {
                        if (lm.getRow() == zombie.getRow()) {
                            lawnmower = lm;
                            break;
                        }
                    }

                    if (lawnmower == null) {
                        isGameOver = true;
                    } else {
                        lawnmower.activate();
                    }
                }

                // Add bounds checking before accessing plants array
                int row = zombie.getRow();
                int col = zombie.getCol();
                if (row >= 0 && row < LAWN_HEIGHT && col >= 0 && col < LAWN_WIDTH) {
                    Plant plant = plants[row][col];
                    if (plant != null) {
                        // Check if it's a potato mine and handle explosion
                        if (plant instanceof PotatoMine) {
                            PotatoMine mine = (PotatoMine) plant;
                            if (mine.isArmed() && !mine.hasExploded()) {
                                mine.explode();
                                zombie.damage(mine.getExplosionDamage());
                                continue;
                            }
                        }

                        if (zombie.canAttack()) {
                            zombie.attack(plant);
                            if (plant.isDead()) {
                                plantDeathSounds[rand.nextInt(plantDeathSounds.length)].play(0.7f);
                                plant.dispose();
                                plants[row][col] = null;
                            }
                        }
                    } else {
                        zombie.move(delta);
                    }
                } else {
                    zombie.move(delta);
                }
            }
        }

        // Remove zombies after rendering everything
        for(Zombie zombie : zombiesToRemove) {
            removeZombie(zombie, delta);
        }

        batch.end();
    }

    public void renderSun(float delta) {
        batch.begin();

        // Render all suns and check for collection
        // Loop through suns
        for (int i = 0; i < suns.size; i++) {
            Sun sun = suns.get(i);
            // Check if the sun was collected
            if (sun.checkClick()) {
                // play sun pick up sound
                sunPickupSound.play(0.5f);

                // Add 25 balance
                plantBar.addSun(25);
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

        // Draw suns last (on top of zombies)
        // Update sun spawning
        sunSpawnTimer += delta;
        if (sunSpawnTimer >= SUN_SPAWN_RATE) {
            sunSpawnTimer = 0f;
            // Create new sun and add to array
            suns.add(new Sun());
        }

        batch.end();
    }

    public void renderShovel() {

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
                    shovelSound.play(0.5f); // play shovel sound

                    plants[tileY][tileX].dispose();
                    plants[tileY][tileX] = null;
                }
            }

            isShovelDragging = false;
            shovel.setDragging(false);
        }
    }

    public void addZombie(Zombie zombie) {
        zombies.add(zombie);
    }
    public void removeZombie(Zombie zombime, float delta) {
        zombieDisposeTimer += delta;
        if(zombieDisposeTimer >= ZOMBIE_DISPOSE_DELAY) {
            zombieDisposeTimer = 0f;
            zombime.dispose();
            zombies.removeValue(zombime, true);
        }
    }

    public void gameOver(float delta) {
        if (!loseSoundPlayed) {
            backgroundMusic.stop(); // Stop background music when game over
            loseSound.play();
            loseSoundPlayed = true;
        }

        // Update scale
        gameOverScale = Math.min(1.0f, gameOverScale + delta * 2); // Adjust the multiplication factor to control
                                                                   // animation speed

        endGameTimer += delta;
        if (endGameTimer >= END_GAME_TIMER) {
            endGameTimer = 0f;
            gameOverTexture.dispose();
            game.setScreen(new Menu(game));
            return;
        }

        batch.begin();
        // Draw with scaling from center
        float width = gameOverTexture.getWidth() * gameOverScale;
        float height = gameOverTexture.getHeight() * gameOverScale;
        float x = (Gdx.graphics.getWidth() - width) / 2;
        float y = (Gdx.graphics.getHeight() - height) / 2;

        batch.draw(gameOverTexture,
                x, y, // Position
                width, height // Size
        );
        batch.end();
    }

    private boolean checkCollision(Collidable left, Collidable right) {
        return (left.getRow() == right.getRow()) && (left.getX() + left.getWidth()) > right.getX();
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

    public int getLawnTileY() {
        return LAWN_TILEY;
    }

    public GameRoot getGame() {
        return game;
    }

    @Override
    public void dispose() {
        // Destroy screen's assets here.
        backgroundTexture.dispose();
        batch.dispose();
        plantBar.dispose();
        // Dispose suns
        for (Sun sun : suns) {
            sun.dispose();
        }

        for (Zombie zombie : zombies) {
            zombie.dispose();
        }
        zombies.clear();

        // Add to existing dispose method
        shovel.dispose();

        for (Lawnmower lawnmower : lawnmowers) {
            lawnmower.dispose();
        }

        loseSound.dispose();

        for (Sound sound : plantSpawnSound) {
            sound.dispose();
        }

        for (Sound sound : plantDeathSounds) {
            sound.dispose();
        }

        sunPickupSound.dispose();
        backgroundMusic.stop();
        backgroundMusic.dispose();
        buttonClickSound.dispose();

        if (gameOverTexture != null) {
            gameOverTexture.dispose();
        }
    }

    private void updatePlants(float delta) {
        for (int y = 0; y < LAWN_HEIGHT; y++) {
            for (int x = 0; x < LAWN_WIDTH; x++) {
                Plant p = plants[y][x];
                if (p != null) {
                    p.update(delta);
                    
                    // Remove dead plants
                    if (p.isDead()) {
                        p.dispose();
                        plants[y][x] = null;
                        continue;
                    }

                    // Handle shooter plant attacks
                    if (p instanceof ShooterPlant) {
                        ShooterPlant shooter = (ShooterPlant) p;
                        shooter.tryAttack(zombies, y);

                        // Check projectile collisions
                        Array<Projectile> projectiles = shooter.getProjectiles();
                        for (int i = projectiles.size - 1; i >= 0; i--) {
                            Projectile proj = projectiles.get(i);

                            // Use Collidable interface for collision detection
                            for (Zombie zombie : zombies) {
                                if (!zombie.isSquashed() && !proj.isHit() && checkCollision(proj, zombie) && !zombie.isDead()) {
                                    zombie.damage(proj.getDamage());
                                    proj.hit(zombie);
                                    break;
                                }
                            }

                            // Remove projectiles that are off screen
                            if (proj.getX() > Gdx.graphics.getWidth()) {
                                proj.dispose();
                                projectiles.removeIndex(i);
                            }
                        }
                    }
                }
            }
        }
    }

    // Add getter for zombies array
    public Array<Zombie> getZombies() {
        return zombies;
    }
}