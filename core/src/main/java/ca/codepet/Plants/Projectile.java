package ca.codepet.Plants;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.utils.ObjectMap;

import ca.codepet.Zombies.Zombie;
import ca.codepet.Collidable;

import com.badlogic.gdx.math.Rectangle;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.Array;


public class Projectile implements Collidable {
    protected float x, y;
    protected float speed = 300;
    protected int damage;
    protected Rectangle bounds;
    protected ObjectMap<String, Animation<AtlasRegion>> animations;
    protected String currentAnimation = "flying";
    protected float stateTime = 0;
    protected boolean isHit = false;
    protected float scale = 2f;
    private AtlasRegion flyingFrame;
    private int row;

    public String plantType = "shooter";

    private Random rand = new Random();

    private final Sound[] hitSounds = {
            Gdx.audio.newSound(Gdx.files.internal("sounds/zombieHit.mp3")),
            Gdx.audio.newSound(Gdx.files.internal("sounds/zombieHit2.mp3"))
    };

    private final Sound snowPeaSound = Gdx.audio.newSound(Gdx.files.internal("sounds/snow_pea_sparkles.ogg"));

    public Projectile(float x, float y, int damage, String atlasPath, float scale, int row) {
        this.x = x;
        this.y = y;
        this.damage = damage;
        this.scale = scale;
        this.row = row;
        this.bounds = new Rectangle(x, y, 10 * scale, 10 * scale); // Smaller collision box

        animations = new ObjectMap<>();

        try {
            TextureAtlas atlas = new TextureAtlas(Gdx.files.internal(atlasPath));
            // Just get the first frame for flying
            flyingFrame = atlas.findRegion("flying", 0);
            if (flyingFrame == null) {
                System.out.println("Could not find flying frame in atlas: " + atlasPath);
            }

            // Load splat animation frames
            Array<AtlasRegion> splatFrames = atlas.findRegions("splat");
            if (splatFrames.size > 0) {
                Animation<AtlasRegion> splatAnim = new Animation<>(0.1f, splatFrames);
                animations.put("splat", splatAnim);
            }
        } catch (Exception e) {
            System.out.println("Error loading projectile atlas: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void update(float delta) {
        if (!isHit) {
            x += speed * delta;
            // Center the bounds on the projectile
            bounds.setCenter(x, y);
        }
        stateTime += delta;
    }

    public void render(SpriteBatch batch) {
        if (!isHit && flyingFrame != null) {
            // Render static flying frame
            batch.draw(flyingFrame,
                    x - (flyingFrame.getRegionWidth() * scale) / 2,
                    y - (flyingFrame.getRegionHeight() * scale) / 2,
                    flyingFrame.getRegionWidth() * scale,
                    flyingFrame.getRegionHeight() * scale);
        } else if (isHit) {
            // Render splat animation
            Animation<AtlasRegion> splatAnim = animations.get("splat");
            if (splatAnim != null) {
                AtlasRegion currentFrame = splatAnim.getKeyFrame(stateTime, false);
                batch.draw(currentFrame,
                        x - (currentFrame.getRegionWidth() * scale) / 2,
                        y - (currentFrame.getRegionHeight() * scale) / 2,
                        currentFrame.getRegionWidth() * scale,
                        currentFrame.getRegionHeight() * scale);
            }
        }
    }

    public void hit(Zombie zombie) {
        if (animations.containsKey("splat")) {

            if(plantType.equals("snowPea")) {
                snowPeaSound.play(0.6f);
            } else {
                hitSounds[rand.nextInt(hitSounds.length)].play(0.6f);
            }

            isHit = true;
            currentAnimation = "splat";
            stateTime = 0;
        }
    }

    public boolean isFinished() {
        if (!isHit)
            return false;
        return animations.get(currentAnimation).isAnimationFinished(stateTime);
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public int getDamage() {
        return damage;
    }

    public boolean isHit() {
        return isHit;
    }

    public void dispose() {
        for (Animation<AtlasRegion> anim : animations.values()) {
            anim.getKeyFrame(0).getTexture().dispose();
        }

        for (Sound sound : hitSounds) {
            sound.dispose();
        }
    }

    @Override
    public float getX() {
        return (int) x;
    }

    @Override
    public float getWidth() {
        return 10 * scale;
    }

    @Override
    public int getRow() {
        return row;
    }
}
