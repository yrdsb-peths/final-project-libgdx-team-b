package ca.codepet;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import ca.codepet.worlds.DayWorld;

public class Lawnmower {
    private Texture texture;
    private TextureRegion textureRegion;
    private int row;
    private int x, y;

    int width = 50;
    int height = 80;

    int offSetY = 30;

    boolean isActivated = false;

    public Lawnmower(DayWorld world, int row) {
        texture = new Texture("images/lawnmower.png");
        this.row = row;


        textureRegion = new TextureRegion(texture);

        x = 0;
        y = row * world.getLawnTileHeight() + offSetY;
    }

    public void activate() {
        isActivated = true;
    }

    public boolean getIsActivated() {
        return isActivated;
    }

    public void move() {
        x += 5;
    }

    public TextureRegion getTextureRegion() {
        return textureRegion;
    }

    public Texture getTexture() {
        return texture;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
