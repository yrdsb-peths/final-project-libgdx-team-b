package ca.codepet.Plants;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;

import ca.codepet.Zombies.Zombie;
import ca.codepet.worlds.DayWorld;

/**
 * The ShooterPlant class for the shooter plants in the game.
 * Contains the base functionality for all shooter plants.
 */
public abstract class ShooterPlant extends Plant {
    protected Array<Projectile> projectiles = new Array<>();
    protected int range = 800; // Shooting range in pixels
    protected float attackTimer = 0;
    protected float attackCooldown = 1.5f;
    protected boolean isAttacking = false;
    protected int currentRow;

    private final Sound[] shootSounds = {
            Gdx.audio.newSound(Gdx.files.internal("sounds/shootSound.mp3")),
            Gdx.audio.newSound(Gdx.files.internal("sounds/shootSound2.mp3"))
    };

    /**
     * Constructor for the ShooterPlant class.
     * @param world The world the ShooterPlant is in
     * @param x The x position of the ShooterPlant
     * @param y The y position of the ShooterPlant
     * @param health The health of the ShooterPlant
     */
    public ShooterPlant(DayWorld world, float x, float y, int health) {
        super(world, x, y, health);
    }

    /**
     * Update the ShooterPlant. Updates the animation and projectiles.
     * @param delta The time since the last frame in seconds
     */
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

    /**
     * Render the ShooterPlant. Renders the plant and projectiles.
     * @param batch The SpriteBatch to render to
     */
    @Override
    public void render(SpriteBatch batch) {
        super.render(batch);

        // Render projectiles
        for (Projectile proj : projectiles) {
            proj.render(batch);
        }
    }

    /**
     * Start the attack animation.
     */
    public void startAttack() {
        isAttacking = true;
        setAnimationUnique("attack");
    }

    /**
     * Stop the attack animation.
     */
    public void stopAttack() {
        isAttacking = false;
        setAnimationUnique("idle");
    }

    /**
     * Try to attack a zombie in the same row as the plant.
     * @param zombies The zombies to check for
     * @param row The row to check for zombies
     */
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

    /**
     * Dispose of the ShooterPlant and its projectiles.
     */
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

    /**
     * Create a projectile for the ShooterPlant.
     * @return The projectile
     */
    protected abstract Projectile createProjectile();

    /**
     * Get the projectiles of the ShooterPlant.
     * @return The projectiles
     */
    public Array<Projectile> getProjectiles() {
        return projectiles;
    }
}
