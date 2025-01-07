package ca.codepet.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;

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
                Gdx.files.internal("fonts/contb.ttf"));
        FreeTypeFontParameter parameter = new FreeTypeFontParameter();
        parameter.size = 20;
        parameter.color.set(0, 0, 0, 1);
        font = generator.generateFont(parameter);
        // dispose generator

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
        float centerX = 45; // Original x position
        GlyphLayout layout = new GlyphLayout(font, sunText); // To measure the text
        float textWidth = layout.width;
        float textX = centerX - (textWidth / 2); // Center around the original x position
        float textY = barY + newHeight - 75;
        font.draw(batch, sunText, textX, textY);

        batch.end();
    }

    public void dispose() {
        barTexture.dispose();
        batch.dispose();
        font.dispose();
    }
}