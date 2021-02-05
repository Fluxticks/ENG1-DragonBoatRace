package com.dragonboatrace.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.dragonboatrace.game.screens.TitleScreen;

import java.util.ArrayList;

/**
 * Represents the game as a class.
 *
 * @author Jacob Turner
 */
public class DragonBoatRace extends Game {

    /**
     * The spritebatch to be used across all the screens.
     */
    public SpriteBatch batch;
    /**
     * The shape renderer to be used across all the screen.s
     */
    public ShapeRenderer shapeRenderer;
    /**
     * The font used to display the text on screen.
     */
    public BitmapFont font;
    /**
     * A list of currently active screens that need to be disposed when the game closes.
     */
    public ArrayList<ScreenAdapter> toDispose;

    /**
     * Create the screen.
     */
    @Override
    public void create() {
        this.batch = new SpriteBatch();
        this.shapeRenderer = new ShapeRenderer();
        this.font = new BitmapFont();
        this.toDispose = new ArrayList<>();
        setScreen(new TitleScreen(this));
    }

    /**
     * Dispose this screen and any other active screens.
     */
    @Override
    public void dispose() {
        this.batch.dispose();
        this.shapeRenderer.dispose();
        this.font.dispose();
        for (ScreenAdapter s : this.toDispose) {
            s.dispose();
        }
    }

}
