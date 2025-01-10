package ca.codepet.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.utils.Array;

import ca.codepet.characters.PlantCard;

public class PlantBar {
    private Texture barTexture;
    private SpriteBatch batch;
    private BitmapFont font;

    private Array<PlantCard> selectedCards;
    private static final int MAX_CARDS = 6;
    private static final float CARD_START_X = 100;
    private static final float CARD_SPACING = 65;

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
        selectedCards = new Array<>();
    }

    public void setSunDisplay(int sun) {
        sunDisplay = sun;
    }

    public boolean addCard(PlantCard card) {
        if (selectedCards.size < MAX_CARDS) {
            float x = CARD_START_X + (selectedCards.size * CARD_SPACING);
            float y = Gdx.graphics.getHeight() - 90;
            card.setPosition(x, y);
            selectedCards.add(card);
            return true;
        }
        return false;
    }

    public PlantCard checkCardClick(float x, float y) {
        for (PlantCard card : selectedCards) {
            if (card.contains(x, y)) {
                selectedCards.removeValue(card, true);
                return card;
            }
        }
        return null;
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

        // Draw selected cards
        for (PlantCard card : selectedCards) {
            card.render(batch);
        }

        batch.end();
    }

    public void dispose() {
        barTexture.dispose();
        batch.dispose();
        font.dispose();
    }
}