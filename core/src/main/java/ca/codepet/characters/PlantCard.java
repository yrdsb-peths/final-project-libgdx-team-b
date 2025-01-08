package ca.codepet.characters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class PlantCard {
    private Texture cardTexture;
    private Rectangle bounds;
    private int cost;
    private float cooldown;
    private float cooldownTimer;
    private boolean isSelected;
    private String plantType;
    
    public PlantCard(String texturePath, int cost, float cooldown, String plantType, float x, float y) {
        this.cardTexture = new Texture(Gdx.files.internal(texturePath));
        this.cost = cost;
        this.cooldown = cooldown;
        this.cooldownTimer = 0;
        this.isSelected = false;
        this.plantType = plantType;
        this.bounds = new Rectangle(x, y, cardTexture.getWidth(), cardTexture.getHeight());
    }

    public void render(SpriteBatch batch) {
        batch.draw(cardTexture, bounds.x, bounds.y);
    }

    public void dispose() {
        cardTexture.dispose();
    }

}