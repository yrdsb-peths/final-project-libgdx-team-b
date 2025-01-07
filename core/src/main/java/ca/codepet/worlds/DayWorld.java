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
import ca.codepet.ui.PlantBar;
import ca.codepet.GameRoot;
import ca.codepet.characters.Sun;
import ca.codepet.ui.PlantPicker;
import com.badlogic.gdx.math.Rectangle;

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

    private final GameRoot game;

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
        
        shape.begin(ShapeType.Filled);
        for (int x = 0; x < LAWN_WIDTH; x++) {
            for (int y = 0; y < LAWN_HEIGHT; y++) {
                shape.setColor((float) x / LAWN_WIDTH, (float) y / LAWN_HEIGHT, 0, 1);
                shape.rect(LAWN_TILEX + x * LAWN_TILEWIDTH, LAWN_TILEY + y * -LAWN_TILEHEIGHT, LAWN_TILEWIDTH, LAWN_TILEHEIGHT);
                Plant p = plants[x][y];
                if (p != null) {
                    TextureRegion tex = p.getTexture();
                    shape.setColor((float) x / LAWN_WIDTH, (float) y / LAWN_HEIGHT, 1, 1);
                    shape.rect(LAWN_TILEX + x * LAWN_TILEWIDTH + tex.getRegionWidth() / 2, LAWN_TILEY - y * LAWN_TILEHEIGHT + tex.getRegionHeight() / 2, tex.getRegionWidth(), tex.getRegionHeight());
                }
            }
        }
        shape.end();

        batch.begin();
        // Draw plants
        // To-do: align plants better
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
                    batch.draw(tex, LAWN_TILEX + x * LAWN_TILEWIDTH + tex.getRegionWidth() / 2, LAWN_TILEY - y * LAWN_TILEHEIGHT + tex.getRegionHeight() / 2);
                } else if (x == clickedTileX && y == clickedTileY) {
                    // Draw "ghost" of plant here
                }
            }
        }

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