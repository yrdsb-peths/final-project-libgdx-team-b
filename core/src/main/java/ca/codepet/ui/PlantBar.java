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
    private static final float CARD_START_X = 90;
    private static final float CARD_SPACING = 70;

    private int sunBalance = 0;
    private boolean gameStarted = false;

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

        sunBalance = sun;
        selectedCards = new Array<>();
    }

    public void startGame() {
        gameStarted = true;
        // Update initial affordability when game starts
        for (PlantCard card : selectedCards) {
            card.setAffordable(sunBalance >= card.getCost());
        }
    }

    public void updateSunDisplay() {
        // Update affordability of all cards
        for (PlantCard card : selectedCards) {
            card.setAffordable(card.getCost() <= sunBalance);
        }
    }

    public boolean addCard(PlantCard card) {
        if (selectedCards.size < MAX_CARDS) {
            float x = CARD_START_X + (selectedCards.size * CARD_SPACING);
            float y = Gdx.graphics.getHeight() - 90;
            card.setPosition(x, y);
            card.updateOriginalPosition(x, y); // Store original position when adding
            card.setAffordable(sunBalance >= card.getCost()); // Set initial affordability
            selectedCards.add(card);
            return true;
        }
        return false;
    }

    public PlantCard checkCardClick(float x, float y) {
        for (PlantCard card : selectedCards) {
            if (card.contains(x, y)) {
                selectedCards.removeValue(card, true);
                repositionRemainingCards(); // Add this line
                return card;
            }
        }
        return null;
    }

    public boolean canAffordCard(PlantCard card) {
        return !gameStarted || sunBalance >= card.getCost();
    }

    public PlantCard checkCardDragStart(float x, float y) {
        for (PlantCard card : selectedCards) {
            if (card.contains(x, y) && !card.isOnCooldown() && canAffordCard(card)) {
                card.startDragging(x, y);
                return card;
            }
        }
        return null;
    }

    // Add new method to start all cooldowns
    public void startAllCardCooldowns() {
        for (PlantCard card : selectedCards) {
            card.startCooldown();
        }
    }

    public void resetCardPosition(PlantCard card) {
        // Find card index in selected cards
        int index = selectedCards.indexOf(card, true);
        if (index != -1) {
            float x = CARD_START_X + (index * CARD_SPACING);
            float y = Gdx.graphics.getHeight() - 90;
            card.setPosition(x, y);
            card.updateOriginalPosition(x, y); // Update the original position
        }
    }

    public void addSun(int amount) {
        sunBalance += amount;
        updateSunDisplay();
    }

    public boolean deductSun(int amount) {
        if (sunBalance >= amount) {
            sunBalance -= amount;
            updateSunDisplay();
            return true;
        }
        return false;
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
        String sunText = String.valueOf(sunBalance);
        float centerX = 45; // Original x position
        GlyphLayout layout = new GlyphLayout(font, sunText); // To measure the text
        float textWidth = layout.width;
        float textX = centerX - (textWidth / 2); // Center around the original x position
        float textY = barY + newHeight - 75;
        font.draw(batch, sunText, textX, textY);

        // Update cooldowns and affordability before rendering cards
        for (PlantCard card : selectedCards) {
            card.updateCooldown(Gdx.graphics.getDeltaTime());
            if (gameStarted) {
                card.setAffordable(sunBalance >= card.getCost()); // Only check affordability after game starts
            } else {
                card.setAffordable(true); // Always affordable during picking phase
            }
        }

        // Draw selected cards
        for (PlantCard card : selectedCards) {
            if (!card.isDragging()) {
                card.render(batch);
            }
        }

        // Draw dragged card last to appear on top
        for (PlantCard card : selectedCards) {
            if (card.isDragging()) {
                card.render(batch);
                break;
            }
        }

        batch.end();
    }

    public void dispose() {
        barTexture.dispose();
        batch.dispose();
        font.dispose();
    }

    // Add this new method
    private void repositionRemainingCards() {
        for (int i = 0; i < selectedCards.size; i++) {
            PlantCard card = selectedCards.get(i);
            float x = CARD_START_X + (i * CARD_SPACING);
            float y = Gdx.graphics.getHeight() - 90;
            card.setPosition(x, y);
            card.updateOriginalPosition(x, y);
        }
    }
}