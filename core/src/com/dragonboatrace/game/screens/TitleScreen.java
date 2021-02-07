package com.dragonboatrace.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.dragonboatrace.game.DragonBoatRace;

/**
 * The screen that shows at the launch of the game.
 *
 * @author Jacob Turner
 */
public class TitleScreen extends ScreenAdapter {

    /**
     * The instance of the DragonBoatRace.
     */
    DragonBoatRace game;

    /**
     * Creates a new screen.
     * @param game The instance of DragonBoatRace.
     */
    public TitleScreen(DragonBoatRace game) {
        this.game = game;
    }

    /**
     * Show the screen.
     */
    @Override
    public void show() {
        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean keyDown(int keyCode) {
                if (keyCode == Input.Keys.SPACE) {
                    game.setScreen(new BoatChoice(game));
                }
                if (keyCode == Input.Keys.ESCAPE) {
                    game.dispose();
                    Gdx.app.exit();
                }
                return true;
            }
        });
    }

    /**
     * Show the text on the screen.
     * @param deltaTime The time since the previous frame.
     */
    @Override
    public void render(float deltaTime) {
        Gdx.gl.glClearColor(0, 0, 1, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.batch.begin();
        game.batch.draw(new Texture("menus/title.png"), 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        game.font.draw(game.batch, "Title Screen!", Gdx.graphics.getWidth() * .1f, Gdx.graphics.getHeight() * .5f);
        game.font.draw(game.batch, "Press SPACE to choose a boat.", Gdx.graphics.getWidth() * .1f, Gdx.graphics.getHeight() * .4f);
        game.font.draw(game.batch, "Press ESC to exit.", Gdx.graphics.getWidth() * .1f, Gdx.graphics.getHeight() * .3f);
        game.batch.end();
    }

    /**
     * Hide the screen.
     */
    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }
}
