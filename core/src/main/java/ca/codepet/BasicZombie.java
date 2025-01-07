package ca.codepet;

public class BasicZombie extends Zombie {
    //basic zombie moves 1 tile in 15 seconds
    public BasicZombie(String spritePath) {
        super(spritePath, basic, 200, 1, 10, 0);
    }

    // 1 bite = 1 dmg
    // gargantuar instantly destroy any plant except spikerock
    @Override
    public void damage(Plant plant, int dmg) {
        // zombie damages a plant
    }
    
}
