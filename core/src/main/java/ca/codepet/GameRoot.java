package ca.codepet;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.assets.AssetManager;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class GameRoot extends Game {
    public SpriteBatch batch;
    public AssetManager assetManager;

    @Override
    public void create() {
        batch = new SpriteBatch();
        assetManager = new AssetManager();

        assetManager.load("images/menu.png", Texture.class);


        setScreen(new Menu(this));
    }
}