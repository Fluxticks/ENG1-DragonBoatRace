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
 * The screen that shows when either all the rounds are over, or if the user didn't make it into the final.
 *
 * @author Jacob Turner
 */
public class Finale extends ScreenAdapter {

    /**
     * The instance of the DragonBoatRace.
     */
    DragonBoatRace game;
    /**
     * The array of lanes that hold the boats.
     */
    Lane[] lanes;
    /**
     * The instance of the players boat.
     */
    PlayerBoat pb;
    /**
     * The race positions of all the boats.
     */
    int[] playerPositions;

    /**
     * Creates a new screen to display.
     *
     * @param game  The instance of the DragonBoatRace.
     * @param lanes The array of lanes of the boats.
     * @param pb    The instance of the player boat.
     */
    public Finale(DragonBoatRace game, Lane[] lanes, PlayerBoat pb) {
        this.game = game;
        this.lanes = lanes;
        this.pb = pb;
        this.playerPositions = getPlayerPositions();
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
                if(keyCode == Input.Keys.ESCAPE){
                    Gdx.app.exit();
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
        if (playerPositions[1] > 3) {
            game.batch.draw(new Texture("menus/didnt qualify.png"), 0, 0);
            game.font.draw(game.batch, "You didn't win a medal :(", Gdx.graphics.getWidth() * .1f, Gdx.graphics.getHeight() * .4f);
        } else {
            game.batch.draw(new Texture("menus/victory.png"), 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            String medal = getPlayerMedal(playerPositions[1]);
            String text = "Congratulations you won a " + medal + " medal!";
            game.font.draw(game.batch, text, Gdx.graphics.getWidth() * .1f, Gdx.graphics.getHeight() * .4f);
        }
        game.font.draw(game.batch, "You came #" + playerPositions[1] + " in the dragon boat race! Your total time was " + pb.getTotalTimeString(), Gdx.graphics.getWidth() * .1f, Gdx.graphics.getHeight() * .5f);
        game.font.draw(game.batch, "Press Space to restart!", Gdx.graphics.getWidth() * .1f, Gdx.graphics.getHeight() * .3f);
        game.batch.end();


    }

    public static String getPlayerMedal(int playerPosition){
        switch (playerPosition) {
            case 1:
                return "Gold";
            case 2:
                return "Silver";
            case 3:
                return "Bronze";
            default:
                return "";
        }
    }

    /**
     * Get the positions of the cpu boats.
     * @return An array indicating which position each of the cpu boats came, ignoring the player boat.
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
                if (lane.getBoatTotalTimeLong() < pb.getTotalTimeLong()) {
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
