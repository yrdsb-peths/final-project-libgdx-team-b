package ca.codepet.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

/**
 * The Shovel class for the shovel in the game.
 */
public class Shovel {
    private Texture iconTexture;
    private Texture dragTexture;
    private float x, y;
    private Rectangle bounds;
    private boolean isDragging = false;
    private SpriteBatch batch;
    private static final float SCALE = 0.5f; // Add scale constant
    private final Sound shovelSound;

    /**
     * Constructor for the Shovel class.
     * @param x The x position of the shovel
     * @param y The y position of the shovel
     */
    public Shovel(float x, float y) {
        this.x = x;
        this.y = y;
        iconTexture = new Texture("ui-components/shovel-icon.png");
        dragTexture = new Texture("ui-components/shovel-drag.png");
        bounds = new Rectangle(x, y, iconTexture.getWidth() * SCALE, iconTexture.getHeight() * SCALE);
        batch = new SpriteBatch();
        shovelSound = Gdx.audio.newSound(Gdx.files.internal("sounds/shovel.ogg"));
    }

    /**
     * Render the shovel.
     */
    public void render() {
        batch.begin();
        if (isDragging) {
            float mouseX = Gdx.input.getX() - (dragTexture.getWidth() * SCALE) / 2f;
            float mouseY = Gdx.graphics.getHeight() - Gdx.input.getY() - (dragTexture.getHeight() * SCALE) / 2f;
            batch.draw(dragTexture,
                    mouseX,
                    mouseY,
                    dragTexture.getWidth() * SCALE,
                    dragTexture.getHeight() * SCALE);
        }
        batch.draw(iconTexture,
                x,
                y,
                iconTexture.getWidth() * SCALE,
                iconTexture.getHeight() * SCALE);
        batch.end();
    }

    /**
     * Check if the shovel was clicked.
     * @param touchX The x position of the touch
     * @param touchY The y position of the touch
     * @return True if the shovel was clicked
     */
    public boolean isClicked(float touchX, float touchY) {
        return bounds.contains(touchX, touchY);
    }

    /**
     * Set the dragging state of the shovel.
     * @param dragging The dragging state of the shovel
     */
    public void setDragging(boolean dragging) {
        isDragging = dragging;
    }

    /**
     * Get the dragging state of the shovel.
     * @return The dragging state of the shovel
     */
    public boolean isDragging() {
        return isDragging;
    }

    /**
     * Play the shovel sound.
     */
    public void playShovelSound() {
        shovelSound.play(0.5f);
    }

    /**
     * Dispose of the shovel.
     */
    public void dispose() {
        iconTexture.dispose();
        dragTexture.dispose();
        batch.dispose();
        shovelSound.dispose();
    }
}
