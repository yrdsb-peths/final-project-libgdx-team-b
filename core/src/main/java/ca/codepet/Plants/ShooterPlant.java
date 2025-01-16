package ca.codepet.Plants;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;

import ca.codepet.Zombies.Zombie;
import ca.codepet.worlds.DayWorld;

public abstract class ShooterPlant extends Plant {
    protected Array<Projectile> projectiles = new Array<>();
    protected int range = 800; // Shooting range in pixels
    protected float attackTimer = 0;
    protected float attackCooldown = 1.5f;
    protected int currentRow;

    private final Sound[] shootSounds = {
            Gdx.audio.newSound(Gdx.files.internal("sounds/shootSound.mp3")),
            Gdx.audio.newSound(Gdx.files.internal("sounds/shootSound2.mp3"))
    };

    public ShooterPlant(DayWorld world, float x, float y) {
        super(world, x, y, 300);
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        imageIndex += delta;
        attackTimer += delta;

        // Update existing projectiles
        for (int i = projectiles.size - 1; i >= 0; i--) {
            Projectile proj = projectiles.get(i);
            proj.update(delta);

            // Remove finished projectiles
            if (proj.isFinished()) {
                proj.dispose();
                projectiles.removeIndex(i);
            }
        }

        // If attack animation is done, return to idle
        if (isAttacking && animations.get("attack").isAnimationFinished(imageIndex)) {
            stopAttack();
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        super.render(batch);

        // Render projectiles
        for (Projectile proj : projectiles) {
            proj.render(batch);
        }
    }

    public void tryAttack(Array<Zombie> zombies, int row) {
        this.currentRow = row; // Store current row
        if (attackTimer >= attackCooldown) {
            // Look for zombies in range and same row
            boolean foundTarget = false;
            for (Zombie zombie : zombies) {
                if (zombie.getRow() == row && !zombie.isSquashed()) {
                    float distance = zombie.getX() - this.x;
                    if (distance > 0 && distance <= range) {
                        foundTarget = true;
                        break;
                    }
                }
            }

            if (foundTarget && !isAttacking) {
                startAttack();
                attackTimer = 0;
                Projectile proj = createProjectile();

                shootSounds[rand.nextInt(shootSounds.length)].play(0.5f);

                if (proj != null) {
                    projectiles.add(proj);
                }
            } else if (!foundTarget && isAttacking) {
                stopAttack();
            }
        }
    }

    @Override
    public void dispose() {
        super.dispose();
        for (Projectile proj : projectiles) {
            proj.dispose();
        }
        projectiles.clear();

        for (Sound sound : shootSounds) {
            sound.dispose();
        }

    }

    protected abstract Projectile createProjectile();

    public Array<Projectile> getProjectiles() {
        return projectiles;
    }
}
