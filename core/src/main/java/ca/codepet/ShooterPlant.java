package ca.codepet;

public abstract class ShooterPlant extends Plant {
    enum State {
        IDLE,
        ATTACKING
    }
    State state = State.IDLE;
    int range = 5;

    @Override
    public void update(float delta) {
        imageIndex += delta;
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
