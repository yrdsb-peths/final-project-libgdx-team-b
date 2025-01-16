package ca.codepet.Plants;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;

import ca.codepet.worlds.DayWorld;

public class SnowPea extends ShooterPlant {
    static int IDLE_FRAMES = 8;
    static int ATTACK_FRAMES = 3;
    static int DEFAULT_HEALTH = 100;
    static float DEFAULT_ATTACK_COOLDOWN = 1.5f;
    static int DEFAULT_DAMAGE = 20;
    public static final String PROJECTILE_ATLAS = "projectiles/snowpeaproj.atlas";
    public static final float PROJECTILE_SCALE = 3f;

    public SnowPea(DayWorld world, float x, float y) {
        super(world, x, y);

        setScale(2.2f);

        attackCooldown = DEFAULT_ATTACK_COOLDOWN;

        TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("plants/snowpea/snowpea-idle.atlas"));

        // Load idle animation (looping)
        AtlasRegion[] idle = new AtlasRegion[IDLE_FRAMES];
        for (int i = 0; i < IDLE_FRAMES; i++) {
            // Change to match atlas naming
            idle[i] = atlas.findRegion("snowpea_idle" + (i + 1));
        }
        Animation<AtlasRegion> idleanim = new Animation<>(0.2f, idle);
        idleanim.setPlayMode(Animation.PlayMode.LOOP);
        animations.put("idle", idleanim);

        // Attack animation (non-looping)
        TextureAtlas attackAtlas = new TextureAtlas(Gdx.files.internal("plants/snowpea/snowpea-attack.atlas"));
        AtlasRegion[] attack = new AtlasRegion[ATTACK_FRAMES];
        for (int i = 0; i < ATTACK_FRAMES; i++) {
            attack[i] = attackAtlas.findRegion("snowpea_shoot" + (i + 1));
        }
        Animation<AtlasRegion> attackAnim = new Animation<>(0.1f, attack);
        attackAnim.setPlayMode(Animation.PlayMode.NORMAL);
        animations.put("attack", attackAnim);

        setAnimation("idle"); // Set initial animation
    }

    @Override
    protected SnowProjectile createProjectile() {
        try {
            // Pass the row number to the projectile
            SnowProjectile proj = new SnowProjectile(x + 30, y + 15, DEFAULT_DAMAGE, PROJECTILE_ATLAS, PROJECTILE_SCALE,
                    currentRow);
            System.out.println("Created peashooter projectile in row: " + currentRow);
            return proj;
        } catch (Exception e) {
            System.out.println("Error creating projectile: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}
