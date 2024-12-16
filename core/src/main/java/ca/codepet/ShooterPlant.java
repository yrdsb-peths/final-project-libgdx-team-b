package ca.codepet;

public class ShooterPlant extends Plant {
    enum State {
        IDLE,
        ATTACKING
    }
    State state = State.IDLE;
    int range = 5;

    public ShooterPlant() {
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
}
