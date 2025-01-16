package ca.codepet;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import ca.codepet.worlds.DayWorld;

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

    public Lawnmower(DayWorld world, int row) {
        texture = new Texture("images/lawnmower.png");
        this.row = row; // Store the actual row number directly

        textureRegion = new TextureRegion(texture);

        x = -15;
        y = (world.getLawnHeight() - row - 1) * world.getLawnTileHeight() + offSetY;
    }

    public void activate() {
        sound.play(0.2f);
        isActivated = true;
    }

    public boolean getIsActivated() {
        return isActivated;
    }

    public void move() {
        x += 5;
    }

    public int getRow() {
        return row;
    }

    public TextureRegion getTextureRegion() {
        return textureRegion;
    }

    public Texture getTexture() {
        return texture;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public void dispose() {
        texture.dispose();
    }
}
