package ca.codepet;

import com.badlogic.gdx.Game;

import ca.codepet.worlds.DayWorld;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class GameRoot extends Game {
    @Override
    public void create() {
        setScreen(new DayWorld());
    }
}