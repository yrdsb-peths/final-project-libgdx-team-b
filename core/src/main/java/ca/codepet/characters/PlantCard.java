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
    private TextureRegion cardTexture;
    private Rectangle bounds;
    private int cost;
    private float cooldown;
    private float cooldownTimer;
    private boolean isSelected;
    private String plantType;
    private boolean isDarkened;
    private float darkenAmount = 0.5f; // 0-1 value for darkening
    private boolean isDragging = false;
    private float dragOffsetX, dragOffsetY;
    private float originalX, originalY;
    
    public PlantCard(TextureRegion texture, int cost, float cooldown, String plantType, float x, float y) {
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
        this.cardTexture = texture;
        this.cost = cost;
        this.cooldown = cooldown;
        this.cooldownTimer = 0;
        this.isSelected = false;
        this.plantType = plantType;
        this.bounds = new Rectangle(x, y, 60, 70);
        this.isDarkened = false;
        this.originalX = x;
        this.originalY = y;
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

    public void setDarkened(boolean darkened) {
        isDarkened = darkened;
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

    public void render(SpriteBatch batch) {
        // Store original color
        float originalR = batch.getColor().r;
        float originalG = batch.getColor().g; 
        float originalB = batch.getColor().b;
        float originalA = batch.getColor().a;

        if (isDarkened) {
            batch.setColor(
                originalR * darkenAmount,
                originalG * darkenAmount,
                originalB * darkenAmount,
                originalA
            );
        }

        // Draw card background
        batch.draw(cardBackground, bounds.x, bounds.y, bounds.width, bounds.height);
        
        // Center plant sprite on card
        float plantWidth = Math.min(bounds.width * 0.6f, cardTexture.getRegionWidth());
        float plantHeight = Math.min(bounds.height * 0.6f, cardTexture.getRegionHeight());
        float plantX = bounds.x + (bounds.width - plantWidth) / 2;
        float plantY = bounds.y + (bounds.height - plantHeight) / 2;
        
        // Draw plant sprite
        batch.draw(cardTexture, plantX, plantY, plantWidth, plantHeight);

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
    }
}