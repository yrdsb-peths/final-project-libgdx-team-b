package ca.codepet.Plants;

import ca.codepet.Zombies.Zombie;

/**
 * The SnowProjectile class for the Snow Pea plant.
 * Slows zombies when it hits them.
 */
public class SnowProjectile extends Projectile {
    /**
     * Constructor for the SnowProjectile class.
     * @param x The x position of the projectile
     * @param y The y position of the projectile
     * @param damage The damage the projectile does
     * @param atlasPath The path to the atlas file for the projectile
     * @param scale The scale of the projectile
     * @param row The row the projectile is in
     */
    public SnowProjectile(float x, float y, int damage, String atlasPath, float scale, int row) {
        super(x, y, damage, atlasPath, scale, row);
        plantType = "snowPea";
    }

    /**
     * Hit a zombie with the projectile.
     * @param zombie The zombie to hit
     */
    @Override
    public void hit(Zombie zombie) {
        super.hit(zombie);
        zombie.doSlow();
    }
}
