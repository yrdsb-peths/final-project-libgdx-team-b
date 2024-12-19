package ca.codepet.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

public class PlantBar {
    private Texture barTexture;
    private SpriteBatch batch;
    private BitmapFont font;

    private int sunDisplay = 0;

    public PlantBar(int sun) {
        barTexture = new Texture("ui-components/bar.png");
        batch = new SpriteBatch();
        
        // Load font
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(
            Gdx.files.internal("fonts/fbUsv8C5eIBlack.ttf")
        );
        FreeTypeFontParameter parameter = new FreeTypeFontParameter();
        parameter.size = 24;
        font = generator.generateFont(parameter);
        // dispose generator
        generator.dispose();

        sunDisplay = sun;
    }

    public void setSunDisplay(int sun) {
        sunDisplay = sun;
    }

    public void render() {
        float aspectRatio = (float) barTexture.getWidth() / barTexture.getHeight();
        float newHeight = 100;
        float newWidth = newHeight * aspectRatio;
        float barY = Gdx.graphics.getHeight() - newHeight;

        batch.begin();
        // Draw bar
        batch.draw(barTexture, 0, barY, newWidth, newHeight);
        
        // Draw sun count
        String sunText = String.valueOf(sunDisplay);
        float textX = 10;
        float textY = barY + newHeight - 10;
        font.draw(batch, sunText, textX, textY);
        
        batch.end();
    }

    public void dispose() {
        barTexture.dispose();
        batch.dispose();
        font.dispose();
    }
}