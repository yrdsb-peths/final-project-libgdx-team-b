package ca.codepet.Plants;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.utils.Array;

import ca.codepet.Collidable;
import ca.codepet.Zombies.Zombie;
import ca.codepet.worlds.DayWorld;

/**
 * The Spikeweed class for the Spikeweed plant.
 * Damages zombies that walk over it.
 */
public class Spikeweed extends Plant implements Collidable {
    private static final int DEFAULT_HEALTH = 600;

    private static final float DEFAULT_ATTACK_COOLDOWN = 1f; 
    private static final int DEFAULT_DAMAGE = 10;
    private static final int COLLIDE_MARGIN = 4;

    protected float attackTimer = 0;
    protected boolean startAttack = false;
    protected boolean isAttacking = false;
    protected int row = -1;
    
    /**
     * Constructor for the Spikeweed class.
     * @param world The world the Spikeweed is in
     * @param x The x position of the Spikeweed
     * @param y The y position of the Spikeweed
     */
    public Spikeweed(DayWorld world, float x, float y) {
        super(world, x, y, DEFAULT_HEALTH);
        health = DEFAULT_HEALTH;
        setScale(2.4f);

        TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("plants/spikeweed.atlas"));
        // Load idle animation
        Array<AtlasRegion> idle = atlas.findRegions("spikeweed_idle");
        Animation<AtlasRegion> idleAnim = new Animation<>(0.5f, idle);
        idleAnim.setPlayMode(PlayMode.LOOP);
        animations.put("idle", idleAnim);

        // Load attack animation
        Array<AtlasRegion> attack = atlas.findRegions("spikeweed_attack");
        Animation<AtlasRegion> attackAnim = new Animation<>(0.5f, attack);
        attackAnim.setPlayMode(PlayMode.NORMAL);
        animations.put("attack", attackAnim);

        setAnimation("idle");
    }

    /**
     * Update the Spikeweed plant. Check if it is attacking and if the attack animation is done.
     * @param delta The time since the last frame in seconds
     */
    @Override
    public void update(float delta) {
        super.update(delta);
        imageIndex += delta;
        if (startAttack) {
            startAttack = false;
            attackTimer = DEFAULT_ATTACK_COOLDOWN;
        }
        attackTimer = Math.max(0, attackTimer - delta);

        // If attack animation is done, return to idle
        if (isAttacking && animations.get("attack").isAnimationFinished(imageIndex)) {
            isAttacking = false;
            setAnimationUnique("idle");
        }
    }

    /**
     * Attack a zombie.
     * @param zombie The zombie to attack
     */
    public void attack(Zombie zombie) {
        if (attackTimer <= 0f) {
            startAttack = true;
            isAttacking = true;
            setAnimationUnique("attack");
            zombie.damage(DEFAULT_DAMAGE);
        }
    }

    /**
     * Get the x position of the Spikeweed.
     * @return The x position
     */
    public float getX() {
        return x - COLLIDE_MARGIN;
    }

    /**
     * Get the width of the Spikeweed.
     * @return The width
     */
    public float getWidth() {
        return getWorld().getLawnTileWidth() + COLLIDE_MARGIN * 2;
    }

    /**
     * Get the row the Spikeweed is in.
     * @return The row
     */
    public int getRow() {
        return row;
    }
    
    /**
     * Set the row the Spikeweed is in.
     * @param row The row
     */
    public void setRow(int row) {
        this.row = row;
    }

    /**
     * Dispose of the Spikeweed.
     */
    public void dispose() {
        super.dispose();
    }
}
