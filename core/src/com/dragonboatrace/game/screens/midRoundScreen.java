package com.dragonboatrace.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.dragonboatrace.game.DragonBoatRace;
import com.dragonboatrace.game.Lane;
import com.dragonboatrace.game.entities.PlayerBoat;

/**
 * The screen that shows in-between rounds.
 *
 * @author Jacob Turner
 */
public class midRoundScreen extends ScreenAdapter {

    /**
     * The instance of the DragonBoatRace.
     */
    DragonBoatRace game;
    /**
     * The round that was just completed.
     */
    int round;
    /**
     * The instance of the player boat.
     */
    PlayerBoat pb;
    /**
     * The current positions of the boats in the overall race.
     */
    int[] playerPositions;
    /**
     * The array of lanes.
     */
    Lane[] lanes;
    /**
     * The difficulty chosen by the player.
     */
    int difficulty;

    /**
     * Creates a new screen.
     *
     * @param game       The instance of the DragonBoatRace.
     * @param round      The round that was just completed.
     * @param lanes      The array of lanes.
     * @param playerBoat The instance of the player boat.
     * @param difficulty The difficulty chosen by the player.
     */
    public midRoundScreen(DragonBoatRace game, int round, Lane[] lanes, PlayerBoat playerBoat, int difficulty) {
        this.game = game;
        this.lanes = lanes;
        this.round = round;
        this.pb = playerBoat;
        this.difficulty = difficulty;
        this.game.toDispose.add(this);
        this.playerPositions = getPlayerPositions();

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
                    for (Lane lane : lanes) {
                        lane.moveBoatToStart();
                    }
                    if (round != 3 || playerPositions[1] < 4) {
                        game.setScreen(new GameScreen(game, round + 1, lanes, pb, difficulty));
                    } else {
                        game.setScreen(new Finale(game, lanes, pb));
                    }
                    //this will move every cpu boat to the 'start'
                    //really it just moves them to be at the same position as the player
                }
                return true;
            }
        });
    }

    /**
     * Render the information to the player.
     *
     * @param deltaTime The time since the previous frame.
     */
    @Override
    public void render(float deltaTime) {
        Gdx.gl.glClearColor(0, 0, 1, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


        game.batch.begin();
        game.batch.draw(new Texture("menus/between rounds.png"), 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        game.font.draw(game.batch, "You came #" + playerPositions[0] + " in that leg! You took " + pb.getFinishTimeString(), Gdx.graphics.getWidth() * .1f, Gdx.graphics.getHeight() * .5f);
        game.font.draw(game.batch, "Overall you are #" + playerPositions[1] + " in the dragon boat race!", Gdx.graphics.getWidth() * .1f, Gdx.graphics.getHeight() * .4f);

        if (round != 2) {
            game.font.draw(game.batch, "You can progress to the next round!", Gdx.graphics.getWidth() * .1f, Gdx.graphics.getHeight() * .3f);
        } else {
            game.font.draw(game.batch, "You can progress to the finale!", Gdx.graphics.getWidth() * .1f, Gdx.graphics.getHeight() * .3f);
        }
        if (pb.getPenalty() > 0) {
            game.font.draw(game.batch, "There is a time penalty of " + pb.getPenalty() / 1000 + " seconds for crossing the lane boundaries!", Gdx.graphics.getWidth() * .1f, Gdx.graphics.getHeight() * .2f);
        }
        game.font.draw(game.batch, "Press space to continue!", Gdx.graphics.getWidth() * .1f, Gdx.graphics.getHeight() * .1f);


        game.batch.end();
    }

    /**
     * Get the current positions of the cpu boats ignoring the player boats position.
     *
     * @return An array of the positions of the cpu boats.
     */
    public int[] getPlayerPositions() {
        //element 0 of the output is the players position in the last race
        //element 1 of the output is the players position in all races

        int[] output = {1, 1};
        for (Lane lane : lanes) {
            if (!lane.isPlayerLane) {
                if (lane.getBoatFinishTimeLong() < pb.getFinishTimeLong()) {
                    output[0] += 1;
                }
                if (lane.getBoatTotalTimeLong() + lane.getBoatFinishTimeLong() < pb.getTotalTimeLong() + pb.getFinishTimeLong()) {
                    output[1] += 1;
                }
            }
        }
        return output;
    }

    /**
     * Hide the screen.
     */
    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }

}
