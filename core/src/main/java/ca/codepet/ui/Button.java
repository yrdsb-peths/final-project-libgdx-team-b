package ca.codepet.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

/**
 * The Button class for the buttons in the game.
 * Contains the base functionality the buttons.
 */
public class Button {
    private Stage stage;
    private ImageButton button;
    private SpriteBatch buttonBatch;
    private Texture buttonTexture;

    /**
     * Constructor for the Button class.
     * @param texturePath The path to the texture of the button
     * @param x The x position of the button
     * @param y The y position of the button
     * @param width The width of the button
     * @param height The height of the button
     */
    public Button(String texturePath, int x, int y, int width, int height) {
        buttonTexture = new Texture(Gdx.files.internal(texturePath));
        Drawable drawable = new TextureRegionDrawable(buttonTexture);

        button = new ImageButton(drawable);

        button.setSize(width, height);
        button.setPosition(x, y);

        buttonBatch = new SpriteBatch();
        stage = new Stage();
        stage.addActor(button);

        Gdx.input.setInputProcessor(stage);
    }

    /**
     * Set the listener for the button.
     * @param listener The listener for the button
     */
    public void setButtonListener(InputListener listener) {
        button.addListener(listener);
    }

    /**
     * Render the button.
     */
    public void render() {
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }

    /**
     * Dispose of the button.
     */
    public void dispose() {
        buttonTexture.dispose();
        stage.dispose();
        buttonBatch.dispose();
    }
}
