package ca.codepet.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class PlantPicker {
    private Texture pickerTexture;
    private Texture buttonEnabledTexture;
    private Texture buttonDisabledTexture;
    private SpriteBatch batch;
    private boolean picked = false;
    private Rectangle buttonBounds;
    private boolean buttonEnabled = true;

    public PlantPicker() {
        pickerTexture = new Texture("ui-components/plant-picker.png");
        buttonEnabledTexture = new Texture("ui-components/lets-rock-enabled.png");
        buttonDisabledTexture = new Texture("ui-components/lets-rock-disabled.png");
        batch = new SpriteBatch();
        
        // Initialize button bounds
        float buttonWidth = 150;
        float buttonHeight = 45;
        float buttonX = 150;
        float buttonY = 110; // Position from bottom
        buttonBounds = new Rectangle(buttonX, buttonY, buttonWidth, buttonHeight);
    }

    public boolean isPicked() {
        return picked;
    }

    public void render() {
        float aspectRatio = (float) pickerTexture.getWidth() / pickerTexture.getHeight();
        float newHeight = 500;
        float newWidth = newHeight * aspectRatio;
        float pickerY = Gdx.graphics.getHeight() - newHeight;

        batch.begin();
        // Draw picker background
        batch.draw(pickerTexture, 0, pickerY, newWidth, newHeight);
        
        // Draw button
        Texture buttonTexture = buttonEnabled ? buttonEnabledTexture : buttonDisabledTexture;
        batch.draw(buttonTexture, buttonBounds.x, buttonBounds.y, 
                  buttonBounds.width, buttonBounds.height);
        batch.end();

        // Check for button click
        if (Gdx.input.justTouched() && buttonEnabled) {
            float mouseX = Gdx.input.getX();
            float mouseY = Gdx.graphics.getHeight() - Gdx.input.getY(); // Flip Y coordinate
            if (buttonBounds.contains(mouseX, mouseY)) {
                picked = true;
            }
        }
    }

    public void dispose() {
        pickerTexture.dispose();
        buttonEnabledTexture.dispose();
        buttonDisabledTexture.dispose();
        batch.dispose();
    }
}