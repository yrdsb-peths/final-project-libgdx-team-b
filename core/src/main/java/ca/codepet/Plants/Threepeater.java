package ca.codepet.Plants;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.utils.Array;

import ca.codepet.worlds.DayWorld;
import ca.codepet.Zombies.Zombie;

public class Threepeater extends ShooterPlant {
    static int IDLE_FRAMES = 8;
    static int ATTACK_FRAMES = 2;
    static int DEFAULT_HEALTH = 100;
    static float DEFAULT_ATTACK_COOLDOWN = 1.5f;
    static int DEFAULT_DAMAGE = 20;
    public static final String PROJECTILE_ATLAS = "projectiles/pea.atlas";
    public static final float PROJECTILE_SCALE = 3f;

    private boolean firedShot = false;
    private float shotDelay = 0.1f;
    private float shotTimer = 0;

    public Threepeater(DayWorld world, float x, float y) {
        super(world, x, y + 50);

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

    @Override
    protected Projectile createProjectile() {
        return new Projectile(x + 30, y + 15, DEFAULT_DAMAGE, PROJECTILE_ATLAS, PROJECTILE_SCALE, currentRow);
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        if (isAttacking && !firedShot) {
            shotTimer += delta;
            if (shotTimer >= shotDelay) {
                // Create three projectiles, one for each row, using the plant's current row as center
                for (int i = -1; i <= 1; i++) {
                    int targetRow = currentRow + i;
                    if (targetRow >= 0 && targetRow < getWorld().getLawnHeight()) {
                        float rowOffset = i * getWorld().getLawnTileHeight();
                        Projectile proj = new Projectile(
                            x + 30, 
                            y + rowOffset, // Adjust Y position based on row offset
                            DEFAULT_DAMAGE, 
                            PROJECTILE_ATLAS, 
                            PROJECTILE_SCALE, 
                            targetRow    // Important: use the correct target row for each projectile
                        );
                        projectiles.add(proj);
                    }
                }
                firedShot = true;
            }
        }
    }

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
                        if (zombie.getRow() == targetRow && !zombie.isDying() && !zombie.isSquashed() && zombie.getX() > x) {
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

    @Override
    public void startAttack() {
        super.startAttack();
        setAnimation("attack");
        firedShot = false;
        shotTimer = 0;
    }

    @Override
    public void stopAttack() {
        super.stopAttack();
        firedShot = false;
        shotTimer = 0;
    }
}
