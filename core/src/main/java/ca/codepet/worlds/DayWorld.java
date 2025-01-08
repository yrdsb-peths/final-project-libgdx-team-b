package ca.codepet.worlds;

import com.badlogic.gdx.math.MathUtils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.utils.Array;

import ca.codepet.Plant;
import ca.codepet.Plants.Peashooter;
import ca.codepet.Zombies.BasicZombie;
import ca.codepet.Zombies.Zombie;
import ca.codepet.ui.PlantBar;
import ca.codepet.GameRoot;
import ca.codepet.characters.Sun;

public class DayWorld implements Screen {
    private Texture backgroundTexture;
    private SpriteBatch batch;
    private PlantBar plantBar;
    private Plant[][] plants;
    private ShapeRenderer shape = new ShapeRenderer();
    
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

    private Zombie zombie = new BasicZombie();

    public DayWorld(GameRoot game) {
        this.game = game;

    }
    @Override
    public void show() {
        // Load the background texture
        backgroundTexture = new Texture("backgrounds/day.png");
        batch = new SpriteBatch();
        plantBar = new PlantBar(sunBalance);
        plants = new Plant[LAWN_WIDTH][LAWN_HEIGHT];
        for (int x = 0; x < LAWN_WIDTH; x++) {
            for (int y = 0; y < LAWN_HEIGHT; y++) {
                plants[x][y] = null;
            }
        }
    }

    @Override
    public void render(float delta) {
        // Draw the background texture
        batch.begin();
        batch.draw(backgroundTexture, -200, 0);

        // Draw plants
        float mouseX = Gdx.input.getX();
        float mouseY = Gdx.graphics.getHeight() - Gdx.input.getY();
        int clickedTileX = MathUtils.floor((mouseX - LAWN_TILEX) / LAWN_TILEWIDTH);
        int clickedTileY = -MathUtils.floor((mouseY - LAWN_TILEY) / LAWN_TILEHEIGHT);
        if (Gdx.input.justTouched()) {
            if (clickedTileX >= 0 
            && clickedTileX < LAWN_WIDTH 
            && clickedTileY >= 0 
            && clickedTileY < LAWN_HEIGHT) {
                if (plants[clickedTileX][clickedTileY] == null)
                    plants[clickedTileX][clickedTileY] = new Peashooter();
                else {
                    plants[clickedTileX][clickedTileY].dispose();
                    plants[clickedTileX][clickedTileY] = null;
                }
            }
        }
        for (int x = 0; x < LAWN_WIDTH; x++) {
            for (int y = 0; y < LAWN_HEIGHT; y++) {
                Plant p = plants[x][y];
                if (p != null) {
                    p.update();
                    TextureRegion tex = p.getTexture();
                    float pX = LAWN_TILEX + x * LAWN_TILEWIDTH + (LAWN_TILEWIDTH - tex.getRegionWidth()) / 2;
                    float pY = LAWN_TILEY - y * LAWN_TILEHEIGHT + (LAWN_TILEHEIGHT - tex.getRegionHeight()) / 2;
                    batch.draw(tex, pX, pY);
                } else if (x == clickedTileX && y == clickedTileY) {
                    // Draw "ghost" of plant here
                }
            }
        }

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

        zombie.render();
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
    }
}