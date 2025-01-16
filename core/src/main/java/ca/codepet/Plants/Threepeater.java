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

    public Threepeater(DayWorld world, float x, float y) {
        super(world, x, y);

        setScale(2.2f);
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
    public void tryAttack(Array<Zombie> zombies, int row) {
        if (attackTimer >= attackCooldown) {
            // Check all three rows for zombies (above, current, and below)
            boolean zombieFound = false;
            for (int i = -1; i <= 1; i++) {
                int targetRow = row + i;
                if (targetRow >= 0 && targetRow < getWorld().getLawnHeight()) {
                    for (Zombie zombie : zombies) {
                        if (zombie.getRow() == targetRow && !zombie.isDying() && !zombie.isSquashed()) {
                            zombieFound = true;
                            break;
                        }
                    }
                }
            }

            if (zombieFound) {
                // Create three projectiles, one for each row
                for (int i = -1; i <= 1; i++) {
                    int targetRow = row + i;
                    if (targetRow >= 0 && targetRow < getWorld().getLawnHeight()) {
                        Projectile proj = new Projectile(x + 30, y + 15 + (i * getWorld().getLawnTileHeight()),
                                DEFAULT_DAMAGE, PROJECTILE_ATLAS, PROJECTILE_SCALE, targetRow);
                        projectiles.add(proj);
                    }
                }
                setAnimation("attack");
                attackTimer = 0;
            }
        }
    }
}
