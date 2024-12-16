package ca.codepet.sample;

public abstract class Zombie {
    private String type;
    private int hp;
    private int atk;
    private int spd;
    private int armor;

    public Zombie(String type, int hp, int atk, int spd, int armor)
    {
        this.type = type;
        this.hp = hp;
        this.atk = atk;
        this.spd = spd;
        this.armor = armor;
    }

    public void move()
    {
        // move left based on zombie's speed
    }

    public void takeDamage()
    {
        // lower the zombie's hp
    }

    public void damage()
    {
        // damage the plants
    }

    public void die()
    {
        // if zombie's hp is equal to or lower than 0
        // remove zombie's sprite
    }
}
