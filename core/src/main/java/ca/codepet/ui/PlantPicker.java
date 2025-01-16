package ca.codepet.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

import ca.codepet.characters.PlantCard;

public class PlantPicker {
    private Texture pickerTexture;
    private Texture buttonEnabledTexture;
    private Texture buttonDisabledTexture;
    private SpriteBatch batch;
    private boolean picked = false;
    private Rectangle buttonBounds;
    private boolean buttonEnabled = true;
    private Array<PlantCard> plantCards;

    // Constants for card layout
    private static final int CARD_SPACING = 20;
    private static final int CARDS_PER_ROW = 4;
    private static final float CARD_START_X = 15;
    private static final float CARD_START_Y = 375;

    private PlantBar plantBar;

    private final Sound buttonClickSound = Gdx.audio.newSound(Gdx.files.internal("sounds/buttonClick.ogg"));

    public PlantPicker(PlantBar plantBar) {
        this.plantBar = plantBar;

        pickerTexture = new Texture("ui-components/plant-picker.png");
        buttonEnabledTexture = new Texture("ui-components/lets-rock-enabled.png");
        buttonDisabledTexture = new Texture("ui-components/lets-rock-disabled.png");
        batch = new SpriteBatch();
        
        // Initialize button bounds
        float buttonWidth = 150;
        float buttonHeight = 45;
        float buttonX = 150;
        float buttonY = 10; // Position from bottom
        buttonBounds = new Rectangle(buttonX, buttonY, buttonWidth, buttonHeight);

        // Initialize plant cards
        plantCards = new Array<>();
        initializePlantCards();
    }

    private void initializePlantCards() {
        int row = 0;
        int col = 0;
        
        // Peashooter card
        TextureAtlas peashooterAtlas = new TextureAtlas(Gdx.files.internal("plants/peashooter-idle.atlas"));
        float x = CARD_START_X + (col * (50 + CARD_SPACING));
        float y = CARD_START_Y - (row * (70 + CARD_SPACING));
        PlantCard peashooterCard = new PlantCard(
            peashooterAtlas.findRegion("tile000"),
            100,
            7.5f,
            "Peashooter",
            x,
            y,
            1.4f
        );
        plantCards.add(peashooterCard);

        // Sunflower card
        col++;
        TextureAtlas sunflowerAtlas = new TextureAtlas(Gdx.files.internal("plants/Sunflower.atlas"));
        x = CARD_START_X + (col * (50 + CARD_SPACING));
        PlantCard sunflowerCard = new PlantCard(
            sunflowerAtlas.findRegion("sunflower_idle1"),
            50,
            7.5f,
            "Sunflower",
            x,
            y,
            1.1f
        );
        plantCards.add(sunflowerCard);

        // PotatoMine card
        col++;
        TextureAtlas potatoAtlas = new TextureAtlas(Gdx.files.internal("plants/potato-idle.atlas"));
        x = CARD_START_X + (col * (50 + CARD_SPACING));
        PlantCard potatoCard = new PlantCard(
            potatoAtlas.findRegion("tile000"),
            25,
            10f,
            "PotatoMine",
            x,
            y,
            1.4f
        );
        plantCards.add(potatoCard);

        // Add Walnut card
        col++;
        TextureAtlas walnutAtlas = new TextureAtlas(Gdx.files.internal("plants/walnut-idle-1.atlas"));
        x = CARD_START_X + (col * (50 + CARD_SPACING));
        PlantCard walnutCard = new PlantCard(
            walnutAtlas.findRegion("tile000"),
            50,
            10f,
            "Walnut",
            x,
            y,
            1.3f
        );
        plantCards.add(walnutCard);

        // Add TallNut card
        col++;
        TextureAtlas tallnutAtlas = new TextureAtlas(Gdx.files.internal("plants/tall-nut-1.atlas"));
        x = CARD_START_X + (col * (50 + CARD_SPACING));
        PlantCard tallnutCard = new PlantCard(
            tallnutAtlas.findRegion("120244-0"),
            125,
            15f,
            "TallNut",
            x,
            y,
            1f
        );
        plantCards.add(tallnutCard);

        // Add Repeater card
        col++;
        TextureAtlas repeaterAtlas = new TextureAtlas(Gdx.files.internal("plants/repeater-idle.atlas"));
        x = CARD_START_X + (col * (50 + CARD_SPACING));
        PlantCard repeaterCard = new PlantCard(
            repeaterAtlas.findRegion("tile000"),
            200,
            7.5f,
            "Repeater",
            x,
            y,
            1.4f  // Added scale parameter to make the icon larger
        );
        plantCards.add(repeaterCard);
    }

    public void render() {
        float aspectRatio = (float) pickerTexture.getWidth() / pickerTexture.getHeight();
        float newHeight = 500;
        float newWidth = newHeight * aspectRatio;
        float pickerY = 0; // Changed from Gdx.graphics.getHeight() - newHeight to 0

        batch.begin();
        // Draw picker background
        batch.draw(pickerTexture, 0, pickerY, newWidth, newHeight);
        
        // Draw plant cards
        for (PlantCard card : plantCards) {
            card.render(batch);
        }
        
        // Draw button
        Texture buttonTexture = buttonEnabled ? buttonEnabledTexture : buttonDisabledTexture;
        batch.draw(buttonTexture, buttonBounds.x, buttonBounds.y, 
                  buttonBounds.width, buttonBounds.height);
        
        // Check for card clicks
        if (Gdx.input.justTouched()) {
            float mouseX = Gdx.input.getX();
            float mouseY = Gdx.graphics.getHeight() - Gdx.input.getY();
            
            for (PlantCard card : plantCards) {
                if (card.contains(mouseX, mouseY)) {
                    buttonClickSound.play(0.5f);
                    handleCardSelection(card);
                    break;
                }
            }
        }

        batch.end();

        // Check for button click
        if (Gdx.input.justTouched() && buttonEnabled) {
            float mouseX = Gdx.input.getX();
            float mouseY = Gdx.graphics.getHeight() - Gdx.input.getY(); // Flip Y coordinate
            if (buttonBounds.contains(mouseX, mouseY)) {
                picked = true;
                buttonClickSound.play(0.5f);
            }
        }
    }

    private void handleCardSelection(PlantCard card) {
        if (!card.isSelected()) {
            card.setSelected(true);
            
            // Create new card instance using copy constructor
            PlantCard barCard = new PlantCard(card, card.getBounds().x, card.getBounds().y);
            
            if (plantBar.addCard(barCard)) {
                card.setDarkened(true);
            } else {
                card.setSelected(false);
            }
        }
    }

    public void returnCard(String plantType) {
        for (PlantCard card : plantCards) {
            if (card.getPlantType().equals(plantType)) {
                card.setSelected(false);
                card.setDarkened(false);
                break;
            }
        }
    }

    public boolean isPicked() {
        return picked;
    }

    public void dispose() {
        pickerTexture.dispose();
        buttonEnabledTexture.dispose();
        buttonDisabledTexture.dispose();
        batch.dispose();
        for (PlantCard card : plantCards) {
            card.dispose();
        }

        buttonClickSound.dispose();
    }
}