package ca.codepet.Plants;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;

import ca.codepet.worlds.DayWorld;

public class Repeater extends ShooterPlant {
    static int IDLE_FRAMES = 5;
    static int ATTACK_FRAMES = 2;
    static int DEFAULT_HEALTH = 100;
    static float DEFAULT_ATTACK_COOLDOWN = 2.0f; // Increased to account for double shot
    static int DEFAULT_DAMAGE = 20;

    public static final String PROJECTILE_ATLAS = "projectiles/pea.atlas";
    public static final float PROJECTILE_SCALE = 3f;
    private boolean firedSecondShot = false;
    private float secondShotDelay = 0.15f; // Reduced delay between shots
    private float secondShotTimer = 0;

    public Repeater(DayWorld world, float x, float y) {
        super(world, x, y);

        setScale(2.2f);

        TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("plants/repeater/repeater-idle.atlas"));
        AtlasRegion[] idle = new AtlasRegion[IDLE_FRAMES];
        for (int i = 0; i < IDLE_FRAMES; i++) {
            idle[i] = atlas.findRegion("tile00" + i);
        }
        Animation<AtlasRegion> idleanim = new Animation<>(0.2f, idle);
        idleanim.setPlayMode(Animation.PlayMode.LOOP);
        animations.put("idle", idleanim);

        TextureAtlas attackAtlas = new TextureAtlas(Gdx.files.internal("plants/repeater/repeater-attack.atlas"));
        AtlasRegion[] attack = new AtlasRegion[ATTACK_FRAMES];
        for (int i = 0; i < ATTACK_FRAMES; i++) {
            attack[i] = attackAtlas.findRegion("tile00" + i);
        }
        Animation<AtlasRegion> attackAnim = new Animation<>(0.1f, attack);
        attackAnim.setPlayMode(Animation.PlayMode.NORMAL);
        animations.put("attack", attackAnim);

        setAnimation("idle"); // Set initial animation
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        if (isAttacking && !firedSecondShot) {
            secondShotTimer += delta;
            if (secondShotTimer >= secondShotDelay) {
                Projectile proj = createProjectile();
                if (proj != null) {
                    projectiles.add(proj);
                }
                firedSecondShot = true;
            }
        }
    }

    @Override
    protected Projectile createProjectile() {
        return new Projectile(x + 30, y + 15, DEFAULT_DAMAGE, PROJECTILE_ATLAS, PROJECTILE_SCALE, currentRow);
    }

    @Override
    public void startAttack() {
        super.startAttack();
        setAnimation("attack");
        Projectile firstProj = createProjectile(); // Create first projectile immediately
        if (firstProj != null) {
            projectiles.add(firstProj);
        }
        firedSecondShot = false;
        secondShotTimer = 0;
    }

    @Override
    public void stopAttack() {
        super.stopAttack();
        firedSecondShot = false;
        secondShotTimer = 0;
    }
}
