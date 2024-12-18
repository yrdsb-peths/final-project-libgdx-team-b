package ca.codepet.sample;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

/** First screen of the application. Displayed after the application is created. */
public class Menu implements Screen {
    private final GameRoot game;

    Sprite buttonSprite;
    Texture menuTexture;

    Sound sound = Gdx.audio.newSound(Gdx.files.internal("sounds/menuMusic.mp3"));

    public Menu(GameRoot game) {
        this.game = game;
        sound.play(1.0f);
    }

    @Override
    public void show() {
        // Prepare your screen here.
        game.assetManager.finishLoading(); 

        menuTexture = game.assetManager.get("images/menu.png");
        // buttonSprite = new Sprite(buttonTexture);
        
        int x = Gdx.graphics.getWidth();
        int y = Gdx.graphics.getHeight();

        // buttonSprite.setSize(x, y);

        // buttonSprite.setPosition(0, 0);
    }
    

    @Override
    public void render(float delta) {
        // Draw your screen here. "delta" is the time since last render in seconds.
        game.batch.begin();
        game.batch.draw(menuTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        // buttonSprite.draw(game.batch);
        game.batch.end();
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
    }
}