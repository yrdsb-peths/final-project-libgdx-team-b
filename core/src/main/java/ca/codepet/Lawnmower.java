package ca.codepet;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Lawnmower {
    private Texture texture;
    private TextureRegion textureRegion;
    private int row;
    private int x, y;

    int width = 50;
    int height = 80;

    public Lawnmower(int row) {
        texture = new Texture("images/lawnmower.png");
        this.row = row;


        textureRegion = new TextureRegion(texture);

        int x = 0;
        int y = 500;
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
