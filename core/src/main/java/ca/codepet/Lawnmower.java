package ca.codepet;

import com.badlogic.gdx.graphics.Texture;

public class Lawnmower {
    private Texture texture;
    private int row;

    public Lawnmower(int row) {
        texture = new Texture("images/lawnmower.png");
        this.row = row;
    }

    public Texture getTexture() {
        return texture;
    }


}
