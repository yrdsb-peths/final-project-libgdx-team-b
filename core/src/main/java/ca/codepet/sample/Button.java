package ca.codepet.sample;
import com.badlogic.gdx.Game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class Button extends Game{

    // Stage stage;W
    // TextButton button;
    // TextButtonStyle textButtonStyle;
    // TextureAtlas buttonAtlas;

    private float x, y;

    private SpriteBatch buttonBatch;
    private Sprite buttonSprite;
    private Texture buttonTexture;

    private Rectangle hitBox;


    public Button(String texture) {
        buttonTexture = new Texture(texture);
        buttonSprite = new Sprite(buttonTexture);

        buttonSprite.setSize(199, 399);
        buttonSprite.setPosition(33, 344);

        buttonBatch = new SpriteBatch();

        hitBox = new Rectangle(250, 250, 500, 600);
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
        
        // hitBox.setPosition(0, 0);

        buttonBatch.begin();
        buttonSprite.draw(buttonBatch);
        buttonBatch.end();

        if(detectTouch()) System.out.println(121323);
    }

    private boolean detectTouch() {
        if (Gdx.input.justTouched()) {
            float mouseX = Gdx.input.getX();
            float mouseY = Gdx.graphics.getHeight() - Gdx.input.getY();

            if(hitBox.contains(mouseX, mouseY)) {
             
                return true;
            }

        }

        return false;
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
