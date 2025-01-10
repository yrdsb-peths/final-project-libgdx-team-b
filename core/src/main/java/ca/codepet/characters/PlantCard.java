package ca.codepet.characters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public class PlantCard {
    private static Texture cardBackground;
    private TextureRegion cardTexture;
    private Rectangle bounds;
    private int cost;
    private float cooldown;
    private float cooldownTimer;
    private boolean isSelected;
    private String plantType;
    private boolean isDarkened;
    private float darkenAmount = 0.5f; // 0-1 value for darkening
    
    public PlantCard(TextureRegion texture, int cost, float cooldown, String plantType, float x, float y) {
        if (cardBackground == null) {
            cardBackground = new Texture(Gdx.files.internal("characters/card.png"));
        }
        this.cardTexture = texture;
        this.cost = cost;
        this.cooldown = cooldown;
        this.cooldownTimer = 0;
        this.isSelected = false;
        this.plantType = plantType;
        this.bounds = new Rectangle(x, y, 60, 70);
        this.isDarkened = false;
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
        float plantWidth = Math.min(bounds.width * 0.8f, cardTexture.getRegionWidth());
        float plantHeight = Math.min(bounds.height * 0.8f, cardTexture.getRegionHeight());
        float plantX = bounds.x + (bounds.width - plantWidth) / 2;
        float plantY = bounds.y + (bounds.height - plantHeight) / 2;
        
        // Draw plant sprite
        batch.draw(cardTexture, plantX, plantY, plantWidth, plantHeight);

        // Restore original color
        batch.setColor(originalR, originalG, originalB, originalA);
    }

    public void dispose() {
        if (cardBackground != null) {
            cardBackground.dispose();
            cardBackground = null;
        }
    }
}