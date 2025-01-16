package ca.codepet;

import ca.codepet.Plants.Projectile;
import ca.codepet.Zombies.Zombie;

public class SnowProjectile extends Projectile {
    public SnowProjectile(float x, float y, int damage, String atlasPath, float scale, int row) {
        super(x, y, damage, atlasPath, scale, row);
    }

    @Override
    public void hit(Zombie zombie) {
        super.hit(zombie);
        zombie.doSlow();
    }
}
