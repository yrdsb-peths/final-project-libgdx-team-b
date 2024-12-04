package ca.codepet.characters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Sun {
    private Texture sunTexture;
    private float x, y;
    private float width, height;

    private float velocity = 100f; // pixels per second
    private float timeTillDespawn = 5f; // seconds

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
        // Move sun
        y -= velocity * Gdx.graphics.getDeltaTime();
        // If the sun is at the bottom 5% of the screen
        if (y < Gdx.graphics.getHeight() * 0.05f) {
            // Set the velocity to 0 so it stops moving
            velocity = 0f;

            // Begin despawn timer
            timeTillDespawn -= Gdx.graphics.getDeltaTime();
            if (timeTillDespawn <= 0) {
                // Remove the sun
                return;
            }
        }

        batch.draw(sunTexture, x, y, width, height);
    }

    public void dispose() {
        sunTexture.dispose();
    }
}