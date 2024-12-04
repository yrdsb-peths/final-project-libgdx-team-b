import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;

public class Button extends Game{

    Stage stage;
    TextButton button;
    TextButtonStyle textButtonStyle;
    TextureAtlas buttonAtlas;

    @Override
    public void create() {      
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        buttonAtlas = new TextureAtlas(Gdx.files.internal("buttons/buttons.pack"));
     
        textButtonStyle = new TextButtonStyle();
        button = new TextButton("Button1", textButtonStyle);
        stage.addActor(button);
    }

    @Override
    public void render() {      
        super.render();
        stage.draw();
    }
}

//  button.addListener(new ChangeListener() {
//         @Override
//         public void changed (ChangeEvent event, Actor actor) {
//             System.out.println("Button Pressed");
//         }
//     });
// }
