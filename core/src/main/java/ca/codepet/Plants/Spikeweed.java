package ca.codepet.Plants;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.utils.Array;

import ca.codepet.Zombies.Zombie;
import ca.codepet.worlds.DayWorld;

public class Spikeweed extends Plant {
    private static final int DEFAULT_HEALTH = 300;

    private static final float DEFAULT_ATTACK_COOLDOWN = 1f; 
    private static final int DEFAULT_DAMAGE = 10;

    protected float attackTimer = 0;
    protected boolean isAttacking = false;
    
    public Spikeweed(DayWorld world, float x, float y) {
        super(world, x, y, DEFAULT_HEALTH);
        health = DEFAULT_HEALTH;
        setScale(2f);

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

    @Override
    public void update(float delta) {
        super.update(delta);
        imageIndex += delta;
        attackTimer = Math.max(0, attackTimer - delta);

        // If attack animation is done, return to idle
        if (isAttacking && animations.get("attack").isAnimationFinished(imageIndex)) {
            isAttacking = false;
            setAnimationUnique("idle");
        }
    }

    public void attack(Zombie zombie) {
        if (attackTimer <= 0f) {
            attackTimer = DEFAULT_ATTACK_COOLDOWN;
            isAttacking = true;
            setAnimationUnique("attack");
            zombie.damage(DEFAULT_DAMAGE);
        }
    }

    public void dispose() {
        super.dispose();
    }
}
