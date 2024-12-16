package ca.codepet.sample;
import com.badlogic.gdx.Game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import com.badlogic.gdx.graphics.g2d.Sprite;

public class Button extends Game{

    // Stage stage;W
    // TextButton button;
    // TextButtonStyle textButtonStyle;
    // TextureAtlas buttonAtlas;

    private SpriteBatch buttonBatch;
    private Sprite buttonSprite;
    private Texture buttonTexture;

    public Button(String texture) {
        buttonTexture = new Texture(texture);
        buttonSprite = new Sprite(buttonTexture);

        buttonSprite.setSize(199, 399);
        buttonSprite.setPosition(33, 344);
    }

    public void act() {

    }

    @Override
    public void create() {      
        
    }

    @Override
    public void render() {      
        float height = 100;
        float width = 500;
        
        buttonBatch.begin();
        buttonSprite.draw(buttonBatch);
        buttonBatch.end();
    }

    public void dispose() {
        buttonTexture.dispose();
    }
}

//  button.addListener(new ChangeListener() {
//         @Override
//         public void changed (ChangeEvent event, Actor actor) {
//             System.out.println("Button Pressed");
//         }
//     });
// }
