package ca.codepet.worlds;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

import ca.codepet.Plant;
import ca.codepet.Plants.Peashooter;
import ca.codepet.ui.PlantBar;

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

    @Override
    public void show() {
        // Load the background texture
        backgroundTexture = new Texture("backgrounds/day.png");
        batch = new SpriteBatch();
        plantBar = new PlantBar();
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
    }
}