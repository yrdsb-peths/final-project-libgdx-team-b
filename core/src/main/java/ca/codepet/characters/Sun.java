package ca.codepet.characters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Sun {
    private Texture sunTexture;
    private SpriteBatch batch;

    public Sun() {
        sunTexture = new Texture("characters/sun.png");
        batch = new SpriteBatch();
    }

    public void render() {
        // Resize the bar to be 100px tall; maintain aspect ratio
        float aspectRatio = (float) sunTexture.getWidth() / sunTexture.getHeight();
        float newHeight = 25;
        float newWidth = newHeight * aspectRatio;

        batch.begin();
        batch.draw(sunTexture, 0, Gdx.graphics.getHeight() - newHeight, newWidth, newHeight);
        batch.end();
    }

    public void dispose() {
        sunTexture.dispose();
        batch.dispose();
    }
}