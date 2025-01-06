package ca.codepet.worlds;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

import ca.codepet.Plant;
import ca.codepet.Plants.Peashooter;
import ca.codepet.ui.PlantBar;
import ca.codepet.characters.Sun;
import com.badlogic.gdx.utils.Array;

public class DayWorld implements Screen {
    private Texture backgroundTexture;
    private SpriteBatch batch;
    private PlantBar plantBar;
    private Plant[][] plants;
    
    // "final" denotes that this is a constant and cannot be reassigned
    final private int LAWN_WIDTH = 9;
    final private int LAWN_HEIGHT = 5;
    final private int LAWN_TILEWIDTH = 80;
    final private int LAWN_TILEHEIGHT = 92;
    final private int LAWN_TILEX = 72;
    final private int LAWN_TILEY = 441;

    private static final float SUN_SPAWN_RATE = 1f; // seconds
    private float sunSpawnTimer = 0f;
    // Add array to track suns
    private Array<Sun> suns = new Array<>();

    private int sunBalance = 0;

    @Override
    public void show() {
        // Load the background texture
        backgroundTexture = new Texture("backgrounds/day.png");
        batch = new SpriteBatch();
        plantBar = new PlantBar(sunBalance);
        plants = new Plant[LAWN_WIDTH][LAWN_HEIGHT];
        for (int x = 0; x < LAWN_WIDTH; x++) {
            for (int y = 0; y < LAWN_HEIGHT; y++) {
                plants[x][y] = new Peashooter();
            }
        }
    }

    @Override
    public void render(float delta) {
        // Draw the background texture
        batch.begin();
        batch.draw(backgroundTexture, -200, 0);
        for (int x = 0; x < LAWN_WIDTH; x++) {
            for (int y = 0; y < LAWN_HEIGHT; y++) {
                Plant p = plants[x][y];
                if (p != null) {
                    p.update();
                    TextureRegion tex = p.getTexture();
                    batch.draw(tex, LAWN_TILEX + x * LAWN_TILEWIDTH, LAWN_TILEY - y * LAWN_TILEHEIGHT);
                }
            }
        }
        batch.end();

        // Update sun spawning
        sunSpawnTimer += delta;
        if (sunSpawnTimer >= SUN_SPAWN_RATE) {
            sunSpawnTimer = 0f;
            // Create new sun and add to array
            suns.add(new Sun());
        }

        // Render all suns and check for collection
        batch.begin();
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
            } else if (!sun.isAlive()) {
                // If the sun is dead (expired), remove and dispose
                suns.removeIndex(i);
                sun.dispose();
            } else {
                // Render the sun
                sun.render(batch);
            }
        }
        
        batch.end();

        // Draw the plant bar
        plantBar.render();
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