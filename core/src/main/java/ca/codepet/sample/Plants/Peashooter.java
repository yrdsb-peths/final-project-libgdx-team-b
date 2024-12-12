package ca.codepet.sample.Plants;

import ca.codepet.sample.Plant;

public class Peashooter extends Plant {
    enum State {
        IDLE,
        ATTACKING
    }
    State state = State.IDLE;

    public Peashooter() {
        //sprites.put()
    }

    public void update() {
        switch(state) {
            case IDLE:
                setSpriteUnique("idle");
            break;
            case ATTACKING:
                setSpriteUnique("attack");
            break;
        }
    }

    public void damage(int damage) {
    
    }
}
