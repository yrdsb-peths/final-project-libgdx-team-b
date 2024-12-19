package ca.codepet.sample;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class Menu implements Screen {
    private final GameRoot game;

    Sprite buttonSprite;
    Texture menuTexture;
    Button button;

    Sound sound = Gdx.audio.newSound(Gdx.files.internal("sounds/menuMusic.mp3"));

    public Menu(GameRoot game) {
        this.game = game;
        sound.play(1.0f);
    }

    @Override
    public void show() {
        game.assetManager.finishLoading(); 

        menuTexture = game.assetManager.get("images/menu.png");

        // Initialize the button with the texture path
        button = new Button("images/button.png");
    }

    @Override
    public void render(float delta) {
        game.batch.begin();
        game.batch.draw(menuTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        game.batch.end();

        // Render the button
        button.render();
    }

    @Override
    public void resize(int width, int height) {
        // Resize your screen here. The parameters represent the new window size.
    }

    @Override
    public void pause() {
        // Invoked when your application is paused.
    }

    @Override
    public void resume() {
        // Invoked when your application is resumed after pause.
    }

    @Override
    public void hide() {
        // This method is called when another screen replaces this one.
    }

    @Override
    public void dispose() {
        // Destroy screen's assets here.
        sound.dispose();
        button.dispose();
    }
}