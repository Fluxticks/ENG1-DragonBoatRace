package com.dragonboatrace.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.dragonboatrace.game.DragonBoatRace;

/**
 * The screen that shows when the player boat has run out of health.
 *
 * @author Jacob Turner
 */
public class BoatDeathScreen extends ScreenAdapter {

    /**
     * The instance of the DragonBoatRace.
     */
    DragonBoatRace game;

    /**
     * Creates a new screen to display.
     *
     * @param game The instance of the game.
     */
    public BoatDeathScreen(DragonBoatRace game) {
        this.game = game;
    }

    /**
     * Display the screen.
     */
    @Override
    public void show() {
        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean keyDown(int keyCode) {
                if (keyCode == Input.Keys.SPACE) {
                    game.setScreen(new TitleScreen(game));
                }
                return true;
            }
        });
    }

    /**
     * Render the text to tell the user that the game is over.
     *
     * @param deltaTime The time since the previous frame.
     */
    @Override
    public void render(float deltaTime) {
        game.batch.begin();
        game.batch.draw(new Texture("menus/boat broke.png"), 0, 0);
        game.font.draw(game.batch, "Press SPACE to restart :(", Gdx.graphics.getWidth() * .1f, Gdx.graphics.getHeight() * .5f);
        game.batch.end();
    }
}
