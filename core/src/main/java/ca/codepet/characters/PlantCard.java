package ca.codepet.characters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.math.Rectangle;

/**
 * PlantCard class for the plant bar cards.
 * Each card represents a plant that can be placed in the game.
 * The card displays the plant's cost, cooldown, and icon.
 */
public class PlantCard {
    private static Texture cardBackground;
    private static BitmapFont font;
    private static Texture cooldownOverlay;
    private TextureRegion cardTexture;
    private Rectangle bounds;
    private int cost;
    private float cooldown;
    private float cooldownTimer;
    private boolean isSelected;
    private String plantType;
    private boolean isDragging = false;
    private float dragOffsetX, dragOffsetY;
    private float originalX, originalY;
    private static final float COOLDOWN_DARKEN = 0.5f; // Was 0.3f
    private static final float UNAFFORDABLE_DARKEN = 0.7f; // Was 0.5f
    private static final float SELECTED_DARKEN = 0.7f; // New constant
    private static final float NORMAL_DARKEN = 1.0f;
    private boolean isAffordable = true;
    private float iconScale = 1.0f; // Add this field

    /**
     * Constructor for PlantCard.
     * @param texture The texture of the plant card background.
     * @param cost The cost of the plant.
     * @param cooldown The cooldown of the plant.
     * @param plantType The type of plant.
     * @param x The x-coordinate of the plant card.
     * @param y The y-coordinate of the plant card.
     * @param iconScale The scale of the plant icon.
     */
    public PlantCard(TextureRegion texture, int cost, float cooldown, String plantType, float x, float y,
            float iconScale) {
        if (cardBackground == null) {
            cardBackground = new Texture(Gdx.files.internal("characters/card.png"));
        }
        if (font == null) {
            FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/pico12.ttf"));
            FreeTypeFontParameter parameter = new FreeTypeFontParameter();
            parameter.size = 15;
            font = generator.generateFont(parameter);
            generator.dispose();
        }
        if (cooldownOverlay == null) {
            // Create 1x1 grey pixel texture for cooldown overlay
            com.badlogic.gdx.graphics.Pixmap pixmap = new com.badlogic.gdx.graphics.Pixmap(1, 1,
                    com.badlogic.gdx.graphics.Pixmap.Format.RGBA8888);
            pixmap.setColor(0.2f, 0.2f, 0.2f, 0.7f);
            pixmap.fill();
            cooldownOverlay = new Texture(pixmap);
            pixmap.dispose();
        }
        this.cardTexture = texture;
        this.cost = cost;
        this.cooldown = cooldown;
        this.cooldownTimer = 0;
        this.isSelected = false;
        this.plantType = plantType;
        this.bounds = new Rectangle(x, y, 70, 80);
        this.originalX = x;
        this.originalY = y;
        this.iconScale = iconScale;
    }

    /**
     * Copy constructor for PlantCard.
     * @param other The PlantCard to copy.
     * @param x The x-coordinate of the new PlantCard.
     * @param y The y-coordinate of the new PlantCard.
     */
    public PlantCard(PlantCard other, float x, float y) {
        this(other.cardTexture, other.cost, other.cooldown, other.plantType, x, y, other.iconScale);
    }

    /**
     * Get the scale of the plant icon.
     * @return The scale of the plant icon.
     */
    public float getIconScale() {
        return iconScale;
    }

    /**
     * Check if the coordinates are within the bounds of the card.
     * @param x The x-coordinate to check.
     * @param y The y-coordinate to check.
     * @return True if the coordinates are within the bounds of the card, false otherwise.
     */
    public boolean contains(float x, float y) {
        return bounds.contains(x, y);
    }

    /**
     * Set the selected state of the card.
     * @param selected The new selected state.
     */
    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    /**
     * Check if the card is selected.
     * @return True if the card is selected, false otherwise.
     */
    public boolean isSelected() {
        return isSelected;
    }

    /**
     * Set the position of the card.
     * @param x The new x-coordinate.
     * @param y The new y-coordinate.
     */
    public void setPosition(float x, float y) {
        bounds.setPosition(x, y);
    }

    /**
     * Get the TextureRegion of the card.
     * @return The card's TextureRegion.
     */
    public TextureRegion getCardTexture() {
        return cardTexture;
    }

    /**
     * Get the cost of the plant.
     * @return The cost of the plant.
     */
    public int getCost() {
        return cost;
    }

    /**
     * Get the cooldown of the plant.
     * @return The cooldown of the plant.
     */
    public float getCooldown() {
        return cooldown;
    }

    /**
     * Get the type of plant.
     * @return The type of plant.
     */
    public String getPlantType() {
        return plantType;
    }

    /**
     * Get the bounds of the card.
     * @return The bounds of the card as a Rectangle.
     */
    public Rectangle getBounds() {
        return bounds;
    }

    /**
     * Start the dragging behaviour of the card.
     * @param mouseX The x-coordinate of the mouse.
     * @param mouseY The y-coordinate of the mouse.
     */
    public void startDragging(float mouseX, float mouseY) {
        isDragging = true;
        dragOffsetX = mouseX - bounds.x;
        dragOffsetY = mouseY - bounds.y;
    }

    /**
     * Update the position of the card while dragging.
     * @param mouseX The x-coordinate of the mouse.
     * @param mouseY The y-coordinate of the mouse.
     */
    public void updateDragPosition(float mouseX, float mouseY) {
        if (isDragging) {
            bounds.x = mouseX - dragOffsetX;
            bounds.y = mouseY - dragOffsetY;
        }
    }

    /**
     * Stop the dragging behaviour of the card.
     */
    public void stopDragging() {
        isDragging = false;
    }

    /**
     * Check if the card is being dragged.
     * @return True if the card is being dragged, false otherwise.
     */
    public boolean isDragging() {
        return isDragging;
    }

    /**
     * Reset the position of the card to its original position.
     */
    public void resetPosition() {
        bounds.x = originalX;
        bounds.y = originalY;
    }

    /**
     * Update the original position of the card.
     * @param x The new x-coordinate.
     * @param y The new y-coordinate.
     */
    public void updateOriginalPosition(float x, float y) {
        this.originalX = x;
        this.originalY = y;
    }

    /**
     * Check if the card is on cooldown.
     * @return True if the card is on cooldown, false otherwise.
     */
    public boolean isOnCooldown() {
        return cooldownTimer > 0;
    }

    /**
     * Start the cooldown of the card.
     */
    public void startCooldown() {
        cooldownTimer = cooldown;
    }

    /**
     * Update the cooldown of the card.
     * @param delta The time since the last update.
     */
    public void updateCooldown(float delta) {
        if (cooldownTimer > 0) {
            cooldownTimer = Math.max(0, cooldownTimer - delta);
        }
    }

    /**
     * Set the affordability of the card.
     * @param affordable The new affordability of the card.
     */
    public void setAffordable(boolean affordable) {
        this.isAffordable = affordable;
    }

    /**
     * Render the card.
     * @param batch The SpriteBatch to render to.
     */
    public void render(SpriteBatch batch) {
        float originalR = batch.getColor().r;
        float originalG = batch.getColor().g;
        float originalB = batch.getColor().b;
        float originalA = batch.getColor().a;

        // Determine darkening level - prioritize cooldown over affordability
        float darkenAmount;
        if (isOnCooldown()) {
            darkenAmount = COOLDOWN_DARKEN;
        } else if (!isAffordable) {
            darkenAmount = UNAFFORDABLE_DARKEN;
        } else if (isSelected) {
            darkenAmount = SELECTED_DARKEN;
        } else {
            darkenAmount = NORMAL_DARKEN;
        }

        batch.setColor(
                originalR * darkenAmount,
                originalG * darkenAmount,
                originalB * darkenAmount,
                originalA);

        // Draw card background
        batch.draw(cardBackground, bounds.x, bounds.y, bounds.width, bounds.height);

        // Center plant sprite on card with scaling
        float plantWidth = Math.min(bounds.width * 0.6f, cardTexture.getRegionWidth()) * iconScale;
        float plantHeight = Math.min(bounds.height * 0.6f, cardTexture.getRegionHeight()) * iconScale;
        float plantX = bounds.x + (bounds.width - plantWidth) / 2;
        float plantY = bounds.y + (bounds.height - plantHeight) / 2;

        // Draw plant sprite
        batch.draw(cardTexture, plantX, plantY, plantWidth, plantHeight);

        // Draw cooldown overlay
        if (isOnCooldown()) {
            float progress = cooldownTimer / cooldown;
            float overlayHeight = bounds.height * progress;
            batch.draw(cooldownOverlay,
                    bounds.x, bounds.y,
                    bounds.width, overlayHeight);
        }

        // Draw cost with same darkening effect
        font.setColor(batch.getColor());
        font.draw(batch, String.valueOf(cost),
                bounds.x + 13,
                bounds.y + 13);

        // Restore original color
        batch.setColor(originalR, originalG, originalB, originalA);
        font.setColor(originalR, originalG, originalB, originalA);
    }

    /**
     * Dispose of resources used by PlantCard.
     */
    public void dispose() {
        if (cardBackground != null) {
            cardBackground.dispose();
            cardBackground = null;
        }
        if (font != null) {
            font.dispose();
            font = null;
        }
        if (cooldownOverlay != null) {
            cooldownOverlay.dispose();
            cooldownOverlay = null;
        }
    }
}