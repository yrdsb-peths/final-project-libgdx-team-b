package ca.codepet.characters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Sun {
    private Texture sunTexture;
    private float x, y;
    private float width, height;

    public Sun() {
        sunTexture = new Texture("characters/sun.png");
        // Calculate size
        float aspectRatio = (float) sunTexture.getWidth() / sunTexture.getHeight();
        height = 100;
        width = height * aspectRatio;
        // Set random x position
        x = (float) Math.random() * Gdx.graphics.getWidth();
        // Set y position clamp to above 50% of the screen
        y = (float) Math.random() * Gdx.graphics.getHeight() / 2 + Gdx.graphics.getHeight() / 2;
    }

    public void render(SpriteBatch batch) {
        batch.draw(sunTexture, x, y, width, height);
    }

    public void dispose() {
        sunTexture.dispose();
    }
}