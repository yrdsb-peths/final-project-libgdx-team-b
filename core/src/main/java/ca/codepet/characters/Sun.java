package ca.codepet.characters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

/**
 * The Sun class for the sun that falls and the one generated by the sunflower.
 * Clicking on the sun will collect it.
 */
public class Sun {
    private Texture sunTexture;
    private float x, y;
    private float width, height;
    private Rectangle bounds;
    private boolean isAlive = true;

    private float velocity = 100f; // pixels per second
    private float timeTillDespawn = 5f; // seconds

    /**
     * Constructor for the Sun class. Random position.
     */
    public Sun() {
        sunTexture = new Texture("characters/sun.png");
        // Calculate size
        float aspectRatio = (float) sunTexture.getWidth() / sunTexture.getHeight();
        height = 100;
        width = height * aspectRatio;
        // Set random x position
        x = (float) Math.random() * Gdx.graphics.getWidth() - width / 2;
        // Set y position clamp to above 50% of the screen
        y = (float) Math.random() * Gdx.graphics.getHeight() / 2 + Gdx.graphics.getHeight() / 2 - height / 2;
        bounds = new Rectangle(x, y, width, height);
    }

    /**
     * Constructor for the Sun class. Specific position.
     * 
     * @param startX The x position of the sun
     * @param startY The y position of the sun
     */
    public Sun(float startX, float startY) {
        sunTexture = new Texture("characters/sun.png");
        float aspectRatio = (float) sunTexture.getWidth() / sunTexture.getHeight();
        height = 100;
        width = height * aspectRatio;
        x = startX;
        y = startY;
        bounds = new Rectangle(x, y, width, height);
        velocity = 0; // Stationary sun
        timeTillDespawn = 5f;
    }

    /**
     * Check if the sun is alive.
     * @return True if the sun is alive
     */
    public boolean isAlive() {
        return isAlive;
    }

    /**
     * Check if the sun was clicked.
     * @return True if the sun was clicked
     */
    public boolean checkClick() {
        if (Gdx.input.justTouched()) {
            float mouseX = Gdx.input.getX();
            float mouseY = Gdx.graphics.getHeight() - Gdx.input.getY(); // Flip Y coordinate
            if (bounds.contains(mouseX, mouseY)) {
                isAlive = false;
                return true; // Return true if sun was collected
            }
        }
        return false;
    }

    /**
     * Render the sun.
     * @param batch The SpriteBatch to render the sun
     */
    public void render(SpriteBatch batch) {
        if (!isAlive)
            return;

        // Move sun
        y -= velocity * Gdx.graphics.getDeltaTime();
        // Update bounds
        bounds.setPosition(x, y);

        checkClick();

        // If the sun is at the bottom 5% of the screen
        if (y < Gdx.graphics.getHeight() * 0.05f) {
            // Set the velocity to 0 so it stops moving
            velocity = 0f;

            // Begin despawn timer
            timeTillDespawn -= Gdx.graphics.getDeltaTime();
            if (timeTillDespawn <= 0) {
                isAlive = false;
            }
        }

        batch.draw(sunTexture, x, y, width, height);
    }

    /**
     * Dispose of the sun texture.
     */
    public void dispose() {
        sunTexture.dispose();
    }
}