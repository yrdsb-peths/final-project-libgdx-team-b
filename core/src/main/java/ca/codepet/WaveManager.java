package ca.codepet;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.Array;
import ca.codepet.Zombies.*;
import ca.codepet.worlds.DayWorld;

/**
 * The WaveManager class for managing the waves of zombies in the game.
 */
public class WaveManager {
    private static final float INITIAL_WAVE_DELAY = 35f;
    private static final float MIN_WAVE_DELAY = 25f;
    private static final float WAVE_DELAY_DECREASE = 0.5f;
    private static final float ANNOUNCEMENT_DURATION = 2f;

    private DayWorld world;
    private float timeBetweenWaves;
    private float waveTimer = 0f;
    private int currentWave = 0;
    private boolean waveInProgress = false;
    private float zombieSpawnInterval = 2f; // seconds between zombie spawns
    private float zombieTimer = 0f;
    private Array<Zombie> waveZombies = new Array<>();
    private int zombiesSpawned = 0;
    private float announcementTimer;
    private boolean announcing;

    private Sound newWaveSound = Gdx.audio.newSound(Gdx.files.internal("sounds/newWave.mp3"));

    /**
     * Constructor for the WaveManager class.
     * @param world The world the WaveManager is in
     */
    public WaveManager(DayWorld world) {
        this.world = world;
        this.timeBetweenWaves = INITIAL_WAVE_DELAY;
        this.announcing = false;
    }

    /**
     * Update the WaveManager.
     * @param delta The time since the last frame in seconds
     */
    public void update(float delta) {
        if (announcing) {
            announcementTimer -= delta;
            if (announcementTimer <= 0) {
                announcing = false;
                waveInProgress = true;
            }
            return;
        }

        if (!waveInProgress) {
            waveTimer += delta;
            if (waveTimer >= timeBetweenWaves) {
                startNextWave();
            }
        } else {
            zombieTimer += delta;
            if (zombieTimer >= zombieSpawnInterval && zombiesSpawned < waveZombies.size) {
                spawnNextZombie();
                zombieTimer = 0f;
            }

            if (zombiesSpawned >= waveZombies.size) {
                waveInProgress = false;
                waveTimer = 0f;
                zombiesSpawned = 0;
                waveZombies.clear();
            }
        }
    }

    /**
     * Start the next wave of zombies.
     */
    private void startNextWave() {
        currentWave++;
        announcing = true;
        announcementTimer = ANNOUNCEMENT_DURATION;

        // Decrease time between waves but don't go below minimum
        timeBetweenWaves = Math.max(MIN_WAVE_DELAY, 
            INITIAL_WAVE_DELAY - (currentWave * WAVE_DELAY_DECREASE));

        createWaveZombies();
    }

    /**
     * Create the zombies for the current wave.
     */
    private void createWaveZombies() {
        int baseZombies = 2 + (currentWave / 2);
        int bucketheadZombies = currentWave / 3;
        int coneheadZombies = currentWave / 2;
        int screenDoorZombies = Math.max(0, (currentWave - 4) / 2); // Introduce after wave 4

        newWaveSound.play(0.5f);

        // Add basic zombies
        for (int i = 0; i < baseZombies; i++) {
            waveZombies.add(new BasicZombie(world));
        }

        // Add conehead zombies
        for (int i = 0; i < coneheadZombies; i++) {
            waveZombies.add(new ConeheadZombie(world));
        }

        // Add buckethead zombies
        for (int i = 0; i < bucketheadZombies; i++) {
            waveZombies.add(new BucketheadZombie(world));
        }

        // Add screen door zombies
        for (int i = 0; i < screenDoorZombies; i++) {
            waveZombies.add(new ScreenDoorZombie(world));
        }
    }

    /**
     * Spawn the next zombie in the wave.
     */
    private void spawnNextZombie() {
        if (zombiesSpawned < waveZombies.size) {
            world.addZombie(waveZombies.get(zombiesSpawned));
            zombiesSpawned++;
        }
    }

    /**
     * Check if is currently announcing the next wave.
     * @return True if announcing, false otherwise
     */
    public boolean isAnnouncing() {
        return announcing;
    }

    /**
     * Get the current wave number.
     * @return The current wave number
     */
    public int getCurrentWave() {
        return currentWave;
    }

    /**
     * Get the progress of the current wave.
     * @return The progress of the current wave
     */
    public float getWaveProgress() {
        return waveTimer / timeBetweenWaves;
    }
}
