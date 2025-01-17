package ca.codepet;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import ca.codepet.worlds.DayWorld;

/**
 * The Lawnmower class for the lawnmowers in the game.
 */
public class Lawnmower implements Collidable {
    private Texture texture;
    private TextureRegion textureRegion;
    private int row;
    private float x, y;

    private final Sound sound = Gdx.audio.newSound(Gdx.files.internal("sounds/lawnmower.ogg"));

    float width = 80;
    float height = 80;

    int offSetY = 60;

    boolean isActivated = false;

    /**
     * Constructor for the Lawnmower class.
     * @param world The world the lawnmower is in
     * @param row The row the lawnmower is in
     */
    public Lawnmower(DayWorld world, int row) {
        texture = new Texture("images/lawnmower.png");
        this.row = row; // Store the actual row number directly

        textureRegion = new TextureRegion(texture);

        x = -15;
        y = (world.getLawnHeight() - row - 1) * world.getLawnTileHeight() + offSetY;
    }

    /**
     * Activate the lawnmower.
     */
    public void activate() {
        sound.play(0.1f);
        isActivated = true;
    }

    /**
     * Check if the lawnmower is activated.
     * @return True if the lawnmower is activated, false otherwise
     */
    public boolean getIsActivated() {
        return isActivated;
    }

    /**
     * Move the lawnmower.
     */
    public void move() {
        x += 5;
    }

    /**
     * Get the row the lawnmower is in.
     * @return The row the lawnmower is in
     */
    public int getRow() {
        return row;
    }

    /**
     * Get the lawnmower's texture region.
     * @return The lawnmower's texture region
     */
    public TextureRegion getTextureRegion() {
        return textureRegion;
    }

    /**
     * Get the texture of the lawnmower.
     * @return The texture of the lawnmower
     */
    public Texture getTexture() {
        return texture;
    }

    /**
     * Get the width of the lawnmower.
     * @return The width of the lawnmower
     */
    public float getWidth() {
        return width;
    }

    /**
     * Get the height of the lawnmower.
     * @return The height of the lawnmower
     */
    public float getHeight() {
        return height;
    }

    /**
     * Get the x position of the lawnmower.
     * @return The x position of the lawnmower
     */
    public float getX() {
        return x;
    }

    /**
     * Get the y position of the lawnmower.
     * @return The y position of the lawnmower
     */
    public float getY() {
        return y;
    }

    /**
     * Dispose of the lawnmower.
     */
    public void dispose() {
        texture.dispose();
    }
}
