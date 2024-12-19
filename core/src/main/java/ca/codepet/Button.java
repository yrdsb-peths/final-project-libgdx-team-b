package ca.codepet.sample;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class Button {

    private Stage stage;
    private ImageButton button;
    private SpriteBatch buttonBatch;
    private Texture buttonTexture;
    private Skin skin;

    public Button(String texturePath) {
        buttonTexture = new Texture(Gdx.files.internal(texturePath));
        Drawable drawable = new TextureRegionDrawable(buttonTexture);
        button = new ImageButton(drawable);

        button.setSize(199, 399);
        button.setPosition(33, 344);

        buttonBatch = new SpriteBatch();
        stage = new Stage();
        stage.addActor(button);

        Gdx.input.setInputProcessor(stage);

        button.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                System.out.println("Button Pressed");
                return true;
            }
        });
    }

    public void render() {
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }

    public void dispose() {
        buttonTexture.dispose();
        stage.dispose();
        buttonBatch.dispose();
    }
}
