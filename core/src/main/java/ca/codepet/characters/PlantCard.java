package ca.codepet.characters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.math.Rectangle;

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

    public PlantCard(TextureRegion texture, int cost, float cooldown, String plantType, float x, float y) {
        this(texture, cost, cooldown, plantType, x, y, 1.0f);
    }

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

    // Add this new constructor
    public PlantCard(PlantCard other, float x, float y) {
        this(other.cardTexture, other.cost, other.cooldown, other.plantType, x, y, other.iconScale);
    }

    // Add getter for iconScale
    public float getIconScale() {
        return iconScale;
    }

    public boolean contains(float x, float y) {
        return bounds.contains(x, y);
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setPosition(float x, float y) {
        bounds.setPosition(x, y);
    }

    public TextureRegion getCardTexture() {
        return cardTexture;
    }

    public int getCost() {
        return cost;
    }

    public float getCooldown() {
        return cooldown;
    }

    public String getPlantType() {
        return plantType;
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public void startDragging(float mouseX, float mouseY) {
        isDragging = true;
        dragOffsetX = mouseX - bounds.x;
        dragOffsetY = mouseY - bounds.y;
    }

    public void updateDragPosition(float mouseX, float mouseY) {
        if (isDragging) {
            bounds.x = mouseX - dragOffsetX;
            bounds.y = mouseY - dragOffsetY;
        }
    }

    public void stopDragging() {
        isDragging = false;
    }

    public boolean isDragging() {
        return isDragging;
    }

    public void resetPosition() {
        bounds.x = originalX;
        bounds.y = originalY;
    }

    public void updateOriginalPosition(float x, float y) {
        this.originalX = x;
        this.originalY = y;
    }

    public boolean isOnCooldown() {
        return cooldownTimer > 0;
    }

    public void startCooldown() {
        cooldownTimer = cooldown;
    }

    public void updateCooldown(float delta) {
        if (cooldownTimer > 0) {
            cooldownTimer = Math.max(0, cooldownTimer - delta);
        }
    }

    public void setAffordable(boolean affordable) {
        this.isAffordable = affordable;
    }

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