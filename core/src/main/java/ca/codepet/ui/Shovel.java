package ca.codepet.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class Shovel {
    private Texture iconTexture;
    private Texture dragTexture;
    private float x, y;
    private Rectangle bounds;
    private boolean isDragging = false;
    private SpriteBatch batch;
    private static final float SCALE = 0.5f; // Add scale constant
    private final Sound shovelSound;

    public Shovel(float x, float y) {
        this.x = x;
        this.y = y;
        iconTexture = new Texture("ui-components/shovel-icon.png");
        dragTexture = new Texture("ui-components/shovel-drag.png");
        bounds = new Rectangle(x, y, iconTexture.getWidth() * SCALE, iconTexture.getHeight() * SCALE);
        batch = new SpriteBatch();
        shovelSound = Gdx.audio.newSound(Gdx.files.internal("sounds/shovel.ogg"));
    }

    public void render() {
        batch.begin();
        if (isDragging) {
            float mouseX = Gdx.input.getX() - (dragTexture.getWidth() * SCALE) / 2f;
            float mouseY = Gdx.graphics.getHeight() - Gdx.input.getY() - (dragTexture.getHeight() * SCALE) / 2f;
            batch.draw(dragTexture,
                    mouseX,
                    mouseY,
                    dragTexture.getWidth() * SCALE,
                    dragTexture.getHeight() * SCALE);
        }
        batch.draw(iconTexture,
                x,
                y,
                iconTexture.getWidth() * SCALE,
                iconTexture.getHeight() * SCALE);
        batch.end();
    }

    public boolean isClicked(float touchX, float touchY) {
        return bounds.contains(touchX, touchY);
    }

    public void setDragging(boolean dragging) {
        isDragging = dragging;
    }

    public boolean isDragging() {
        return isDragging;
    }

    public void playShovelSound() {
        shovelSound.play(0.5f);
    }

    public void dispose() {
        iconTexture.dispose();
        dragTexture.dispose();
        batch.dispose();
        shovelSound.dispose();
    }
}
