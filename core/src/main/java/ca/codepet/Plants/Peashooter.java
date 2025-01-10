package ca.codepet.Plants;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import ca.codepet.ShooterPlant;

public class Peashooter extends ShooterPlant {

    private int health = 100;
    private int damage = 10;

    public Peashooter() {
        Texture tex = new Texture(Gdx.files.internal("plants/plants.png"));
        TextureRegion[] frames = {new TextureRegion(tex, 0, 0, 36, 32)};
        Animation<TextureRegion> anim = new Animation<>(1f, frames);
        animations.put("idle", anim);
        animations.put("attack", anim);
    }

}
