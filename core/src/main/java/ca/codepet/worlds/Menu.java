package ca.codepet.worlds;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;

import ca.codepet.GameRoot;
import ca.codepet.ui.Button;

public class Menu implements Screen {
    private final GameRoot game;

    Sprite buttonSprite;
    Texture menuTexture;
    Button button;

    float xRatio = 360f / Gdx.graphics.getWidth();
    float yRatio = 300f / Gdx.graphics.getHeight();
    int buttonX = (int) (Gdx.graphics.getWidth() * xRatio);
    int buttonY = (int) (Gdx.graphics.getHeight() * yRatio);

    float widthRatio = 394f / Gdx.graphics.getWidth();
    float heightRatio = 200f / Gdx.graphics.getHeight();
    int buttonWidth = (int) (Gdx.graphics.getWidth() * widthRatio);
    int buttonHeight = (int) (Gdx.graphics.getHeight() * heightRatio);

    Sound sound = Gdx.audio.newSound(Gdx.files.internal("sounds/menuMusic.mp3"));

    Sound buttonClick = Gdx.audio.newSound(Gdx.files.internal("sounds/buttonClick.ogg"));

    private boolean buttonClicked = false;
    private float buttonClickTimer = 0f;
    private static final float CLICK_SOUND_DURATION = 0.5f; // Adjust this value based on your sound length

    public Menu(GameRoot game) {

        System.out.println(buttonX);
        System.out.println(buttonY);

        this.game = game;
        sound.play(0.5f);

        // Initialize the button with the texture path
        button = new Button("images/button.png", buttonX, buttonY, buttonWidth, buttonHeight);

        // Add a listener to the button
        button.setButtonListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                buttonClicked = true;
                return true;
            }
        });
    }

    @Override
    public void show() {
        game.assetManager.finishLoading();

        menuTexture = game.assetManager.get("images/menu.png");
    }

    @Override
    public void render(float delta) {
        if (buttonClicked) {
            buttonClickTimer += delta;
            if (buttonClickTimer >= CLICK_SOUND_DURATION) {
                dispose();
                game.setScreen(new DayWorld(game));
                return;
            }
        }

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
        buttonClick.dispose();
        sound.dispose();
        button.dispose();
        if (!buttonClicked) {
            buttonClick.dispose(); // Only dispose if button wasn't clicked
        }
    }
}