package com.dragonboatrace.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.dragonboatrace.game.DragonBoatRace;
import com.dragonboatrace.game.entities.CPUBoat;
import com.dragonboatrace.game.entities.PlayerBoat;
import com.dragonboatrace.game.screens.TitleScreen;

public class Finale extends ScreenAdapter {

    DragonBoatRace game;
    CPUBoat[] CPUs;
    PlayerBoat pb;
    int[] playerPositions;

    public Finale(DragonBoatRace game, CPUBoat[] CPUs, PlayerBoat pb) {
        this.game = game;
        this.CPUs = CPUs;
        this.pb = pb;
        this.playerPositions = getPlayerPositions();
    }

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

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 1, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.batch.begin();
        if (playerPositions[1] > 3) {
            game.batch.draw(new Texture("menus/didnt qualify.png"), 0, 0);
            game.font.draw(game.batch, "You didn't win a medal :(", Gdx.graphics.getWidth() * .1f, Gdx.graphics.getHeight() * .4f);
        } else {
            game.batch.draw(new Texture("menus/victory.png"), 0, 0);

            switch (playerPositions[1]) {
                case 1:
                    game.font.draw(game.batch, "Congratulations you won a Gold medal!", Gdx.graphics.getWidth() * .1f, Gdx.graphics.getHeight() * .4f);
                    break;
                case 2:
                    game.font.draw(game.batch, "Congratulations you won a Silver medal!", Gdx.graphics.getWidth() * .1f, Gdx.graphics.getHeight() * .4f);
                    break;
                case 3:
                    game.font.draw(game.batch, "Congratulations you won a Bronze medal!", Gdx.graphics.getWidth() * .1f, Gdx.graphics.getHeight() * .4f);
                    break;
                default:
                    break;
            }


        }
        game.font.draw(game.batch, "You came #" + playerPositions[1] + " in the dragon boat race! Your total time was " + pb.getTotalTimeString(), Gdx.graphics.getWidth() * .1f, Gdx.graphics.getHeight() * .5f);
        game.font.draw(game.batch, "Press Space to restart!", Gdx.graphics.getWidth() * .1f, Gdx.graphics.getHeight() * .3f);
        game.batch.end();


    }

    public int[] getPlayerPositions() {
        //element 0 of the output is the players position in the last race
        //element 1 of the output is the players position in all races

        int[] output = {1, 1};
        for (CPUBoat CPU : CPUs) {
            if (CPU.getFinishTimeLong() < pb.getFinishTimeLong()) {
                output[0] += 1;
            }
            if (CPU.getTotalTimeLong() < pb.getTotalTimeLong()) {
                output[1] += 1;
            }
        }
        return output;
    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }

}
