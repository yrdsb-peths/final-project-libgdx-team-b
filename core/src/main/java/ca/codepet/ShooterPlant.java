package ca.codepet;

public abstract class ShooterPlant extends Plant {
    enum State {
        IDLE,
        ATTACKING
    }
    State state = State.IDLE;
    int range = 5;

    @Override
    public void update() {
        imageIndex += 0.35;
        switch(state) {
            case IDLE:
                setAnimationUnique("idle");
            break;
            case ATTACKING:
                setAnimationUnique("attack");
            break;
        }
    }
}
