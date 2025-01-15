package ca.codepet.characters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

public class BungeeTarget {
    private Texture texture;
    private Rectangle rect;
    
    public BungeeTarget(float x, float y) {
        this.texture = new Texture(Gdx.files.internal("zombies/BungeeTarget.png"));
        this.rect = new Rectangle(x, y, texture.getWidth(), texture.getHeight());
    }
}
