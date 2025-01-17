package ca.codepet.Plants;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.utils.Array;

import ca.codepet.worlds.DayWorld;
import ca.codepet.Zombies.Zombie;

/**
 * The Threepeater class for the Threepeater plant.
 * A plant that shoots peas in three lanes.
 */
public class Threepeater extends ShooterPlant {
    static int IDLE_FRAMES = 8;
    static int ATTACK_FRAMES = 2;
    static int DEFAULT_HEALTH = 600;
    static float DEFAULT_ATTACK_COOLDOWN = 1.5f;
    static int DEFAULT_DAMAGE = 20;
    public static final String PROJECTILE_ATLAS = "projectiles/pea.atlas";
    public static final float PROJECTILE_SCALE = 3f;

    private boolean firedShot = false;
    private float shotDelay = 0.1f;
    private float shotTimer = 0;

    /**
     * Constructor for the Threepeater class.
     * @param world The world the Threepeater is in
     * @param x The x position of the Threepeater
     * @param y The y position of the Threepeater
     */
    public Threepeater(DayWorld world, float x, float y) {
        super(world, x, y + 50, DEFAULT_HEALTH);

        setScale(3f);
        attackCooldown = DEFAULT_ATTACK_COOLDOWN;

        TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("plants/threepeater/threepeater-idle.atlas"));

        // Load idle animation (looping)
        AtlasRegion[] idle = new AtlasRegion[IDLE_FRAMES];
        for (int i = 0; i < IDLE_FRAMES; i++) {
            idle[i] = atlas.findRegion("tile00" + i);
        }
        Animation<AtlasRegion> idleAnim = new Animation<>(0.2f, idle);
        idleAnim.setPlayMode(Animation.PlayMode.LOOP);
        animations.put("idle", idleAnim);

        TextureAtlas attackAtlas = new TextureAtlas(Gdx.files.internal("plants/threepeater/threepeater-attack.atlas"));
        AtlasRegion[] attack = new AtlasRegion[ATTACK_FRAMES];
        for (int i = 0; i < ATTACK_FRAMES; i++) {
            attack[i] = attackAtlas.findRegion("tile00" + i);
        }
        Animation<AtlasRegion> attackAnim = new Animation<>(0.2f, attack);
        attackAnim.setPlayMode(Animation.PlayMode.NORMAL);
        animations.put("attack", attackAnim);

        setAnimation("idle");
    }

    /**
     * Create a new projectile for the Threepeater.
     * @return The new projectile
     */
    @Override
    protected Projectile createProjectile() {
        return new Projectile(x + 30, y + 15, DEFAULT_DAMAGE, PROJECTILE_ATLAS, PROJECTILE_SCALE, currentRow);
    }

    /**
     * Update the Threepeater plant. Check if it is attacking and if it is time to fire the three shots.
     * @param delta The time since the last frame in seconds
     */
    @Override
    public void update(float delta) {
        super.update(delta);
        if (isAttacking && !firedShot) {
            shotTimer += delta;
            if (shotTimer >= shotDelay) {
                // Create three projectiles, one for each row, but invert the row offset
                for (int i = 1; i >= -1; i--) {  // Changed iteration order
                    int targetRow = currentRow + i;
                    if (targetRow >= 0 && targetRow < getWorld().getLawnHeight()) {
                        float rowOffset = -i * getWorld().getLawnTileHeight();  // Negated the offset
                        Projectile proj = new Projectile(
                            x + 30, 
                            y + rowOffset,
                            DEFAULT_DAMAGE, 
                            PROJECTILE_ATLAS, 
                            PROJECTILE_SCALE, 
                            targetRow
                        );
                        projectiles.add(proj);
                    }
                }
                firedShot = true;
            }
        }
    }

    /**
     * Try to attack a zombie in the current row and the rows above and below.
     * @param zombies The list of zombies
     * @param row The row of the plant
     */
    @Override
    public void tryAttack(Array<Zombie> zombies, int row) {
        if (attackTimer >= attackCooldown) {
            // Store the current row before checking for zombies
            currentRow = row;
            
            // Check all three rows for zombies (above, current, and below)
            boolean zombieFound = false;
            for (int i = -1; i <= 1; i++) {
                int targetRow = row + i;
                if (targetRow >= 0 && targetRow < getWorld().getLawnHeight()) {
                    for (Zombie zombie : zombies) {
                        if (zombie.getRow() == targetRow && !zombie.isDead() && !zombie.isSquashed() && zombie.getX() > x) {
                            zombieFound = true;
                            break;
                        }
                    }
                }
            }

            if (zombieFound) {
                startAttack();
                attackTimer = 0;
            }
        }
    }

    /**
     * Start the attack animation and create the first projectile.
     */
    @Override
    public void startAttack() {
        super.startAttack();
        setAnimation("attack");
        firedShot = false;
        shotTimer = 0;
    }

    /**
     * Stop the attack animation and reset the shot timer.
     */
    @Override
    public void stopAttack() {
        super.stopAttack();
        firedShot = false;
        shotTimer = 0;
    }
}
