package ca.codepet;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.assets.AssetManager;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

import ca.codepet.worlds.Menu;

/**
 * {@link com.badlogic.gdx.ApplicationListener} implementation shared by all
 * platforms.
 */
public class GameRoot extends Game {
    public SpriteBatch batch;
    public AssetManager assetManager;

    final String flashVertex =   "attribute vec4 a_position;\n" +
                                        "attribute vec4 a_color;\n" +
                                        "attribute vec2 a_texCoord0;\n" +
                                        "uniform mat4 u_projTrans;\n" +
                                        "varying vec4 v_color;\n" +
                                        "varying vec2 v_texCoords;\n" +
                                        "void main() {\n" +
                                            "v_color = a_color;\n" +
                                            "v_texCoords = a_texCoord0;\n" +
                                            "gl_Position =  u_projTrans * a_position;\n" +
                                        "}";
    final String flashFragment = "#ifdef GL_ES\n" +
                                        "precision mediump float;\n" +
                                        "#endif\n" +
                                        "varying vec4 v_color;\n" +
                                        "varying vec2 v_texCoords;\n" +
                                        "uniform sampler2D u_texture;\n" +
                                        "void main() {\n" +
                                            "gl_FragColor = vec4(1., 1., 1., texture2D(u_texture, v_texCoords).a * v_color.a);\n" +
                                        "}";
    private ShaderProgram flashShader;

    @Override
    public void create() {
        batch = new SpriteBatch();
        assetManager = new AssetManager();

        assetManager.load("images/menu.png", Texture.class);

        flashShader = new ShaderProgram(flashVertex, flashFragment);

        setScreen(new Menu(this));
    }

    public ShaderProgram getFlashShader() {
        return flashShader;
    }
}