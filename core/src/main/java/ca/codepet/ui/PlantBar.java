package ca.codepet.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class PlantBar {
    private Texture barTexture;
    private SpriteBatch batch;

    public PlantBar() {
        barTexture = new Texture("ui-components/bar.png");
        batch = new SpriteBatch();
    }

    public void render() {
        // Resize the bar to be 100px tall; maintain aspect ratio
        float aspectRatio = (float) barTexture.getWidth() / barTexture.getHeight();
        float newHeight = 100;
        float newWidth = newHeight * aspectRatio;

        batch.begin();
        batch.draw(barTexture, 0, Gdx.graphics.getHeight() - newHeight, newWidth, newHeight);
        batch.end();
    }

    public void dispose() {
        barTexture.dispose();
        batch.dispose();
    }
}