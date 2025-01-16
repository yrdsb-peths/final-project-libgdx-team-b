package ca.codepet;

import com.badlogic.gdx.utils.Array;
import ca.codepet.Zombies.*;
import ca.codepet.worlds.DayWorld;

public class WaveManager {
    private DayWorld world;
    private float timeBetweenWaves = 2f; // seconds
    private float waveTimer = 0f;
    private int currentWave = 0;
    private boolean waveInProgress = false;
    private float zombieSpawnInterval = 2f; // seconds between zombie spawns
    private float zombieTimer = 0f;
    private Array<Zombie> waveZombies = new Array<>();
    private int zombiesSpawned = 0;

    public WaveManager(DayWorld world) {
        this.world = world;
    }

    public void update(float delta) {
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

    private void startNextWave() {
        currentWave++;
        waveInProgress = true;
        createWaveZombies();
    }

    private void createWaveZombies() {
        int baseZombies = 3 + (currentWave / 2);
        int bucketheadZombies = currentWave / 3;
        int coneheadZombies = currentWave / 2; // Add coneheads

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
    }

    private void spawnNextZombie() {
        if (zombiesSpawned < waveZombies.size) {
            world.addZombie(waveZombies.get(zombiesSpawned));
            zombiesSpawned++;
        }
    }

    public int getCurrentWave() {
        return currentWave;
    }

    public float getWaveProgress() {
        return waveTimer / timeBetweenWaves;
    }
}
