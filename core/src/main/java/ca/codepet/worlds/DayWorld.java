package ca.codepet.worlds;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import ca.codepet.ui.PlantBar;
import ca.codepet.ui.PlantPicker;
import ca.codepet.characters.Sun;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;

public class DayWorld implements Screen {
    private Texture backgroundTexture;
    private SpriteBatch batch;
    private PlantBar plantBar;
    private PlantPicker plantPicker;

    private static final float SUN_SPAWN_RATE = 1f; // seconds
    private float sunSpawnTimer = 0f;
    // Add array to track suns
    private Array<Sun> suns = new Array<>();
    private int sunBalance = 0;
    private boolean gameStarted = false;

    private Texture pickerTexture;
    private Texture buttonEnabledTexture;
    private Texture buttonDisabledTexture;
    private Rectangle buttonBounds;
    private boolean buttonEnabled;
    private boolean picked;

    @Override
    public void show() {
        // Load the background texture
        backgroundTexture = new Texture("backgrounds/day.png");
        batch = new SpriteBatch();
        plantBar = new PlantBar(sunBalance);
        plantPicker = new PlantPicker();
        pickerTexture = new Texture("ui-components/plant-picker.png");
        buttonEnabledTexture = new Texture("ui-components/lets-rock-enabled.png");
        buttonDisabledTexture = new Texture("ui-components/lets-rock-disabled.png");
        buttonBounds = new Rectangle(50, 50, 100, 50);
        buttonEnabled = true;
        picked = false;
    }

    @Override
    public void render(float delta) {
        // Draw the background texture
        batch.begin();
        batch.draw(backgroundTexture, -200, 0);
        batch.end();

        // Always draw the plant bar first (underneath)
        plantBar.render();

        if (!gameStarted) {
            // Show plant picker above plant bar
            plantPicker.render();
            if (plantPicker.isPicked()) {
                gameStarted = true;
            }
        } else {
            // Update sun spawning
            sunSpawnTimer += delta;
            if (sunSpawnTimer >= SUN_SPAWN_RATE) {
                sunSpawnTimer = 0f;
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

        // Check for button click
        if (Gdx.input.justTouched() && buttonEnabled) {
            float mouseX = Gdx.input.getX();
            float mouseY = Gdx.graphics.getHeight() - Gdx.input.getY(); // Flip Y coordinate
            if (buttonBounds.contains(mouseX, mouseY)) {
                picked = true;
            }
        }
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
        pickerTexture.dispose();
        buttonEnabledTexture.dispose();
        buttonDisabledTexture.dispose();
    }
}