package com.dragonboatrace.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.dragonboatrace.game.DragonBoatRace;
import com.dragonboatrace.game.Lane;
import com.dragonboatrace.game.Tuple;
import com.dragonboatrace.game.entities.Boat;
import com.dragonboatrace.game.entities.BoatType;
import com.dragonboatrace.game.entities.CPUBoat;
import com.dragonboatrace.game.entities.PlayerBoat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;

public class BoatChoice extends ScreenAdapter {
    DragonBoatRace game;
    Boat[] boats;
    ArrayList<BoatType> BoatTypes;
    int selection, boatScale;
    BitmapFont font;
    Texture background;

    public BoatChoice(DragonBoatRace game) {
        this.game = game;
        this.selection = 0;
        this.boatScale = 7;
        this.BoatTypes = new ArrayList<>(EnumSet.allOf(BoatType.class));
        this.background = new Texture("menus/boatSelection.png");
        this.game.toDispose.add(this);

        boats = new Boat[this.BoatTypes.size()];
        for (int i = 0; i < this.BoatTypes.size(); i++) {
            BoatType boatType = this.BoatTypes.get(i);
            this.boats[i] = new PlayerBoat(
                    boatType,
                    new Vector2(
                            (Gdx.graphics.getWidth() / 2f) - (this.boatScale * .5f * boatType.getSize().x),
                            (Gdx.graphics.getHeight() / 2f) - (this.boatScale * .5f * boatType.getSize().y)),
                    null
            );
            this.boats[i].getSize().scl(this.boatScale);
        }

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/FreeMono.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size *= 3;
        parameter.color = Color.BLACK;
        this.font = generator.generateFont(parameter);
    }

    public void startGame(int difficulty){

        int laneCount = 7;

        CPUBoat[] CPUs = new CPUBoat[laneCount - 1];

        float laneWidth = Gdx.graphics.getWidth()/(float)laneCount;

        for (int i = 0; i < laneCount - 1; i++) {
            int xpos = i;
            if (i >= (laneCount - 1) / 2) {
                xpos += 1;
            }
            ArrayList<BoatType> cpuBoatTypes = new ArrayList<>(Arrays.asList(BoatType.values()));
            cpuBoatTypes.remove(BoatTypes.get(selection)); // CPUs can't choose player boat
            BoatType cpuBoatType = cpuBoatTypes.get((int) (Math.random() * cpuBoatTypes.size()));
            CPUs[i] = new CPUBoat(
                    cpuBoatType,
                    new Vector2(
                            (int) (0.5 + xpos) * (Gdx.graphics.getWidth() / (float)laneCount),
                            10
                    ), 0,
                    new Tuple<>(
                            (xpos + 0) * laneWidth,
                            (xpos + 1) * laneWidth
                    )
            );
            CPUs[i].saveStartPos();
        }

        PlayerBoat pb = new PlayerBoat(
                BoatTypes.get(selection),
                new Vector2(
                        Gdx.graphics.getWidth() / 2f,
                        10
                ), new Tuple<>(
                ((laneCount - 1) / 2) * laneWidth,
                ((laneCount + 1) / 2) * laneWidth
        )
        );    // Creating the players boat
        pb.saveStartPos();

        Lane[] lanes = new Lane[laneCount];
        for(int i = 0; i < laneCount - 1; i++){
            lanes[i] = new Lane(CPUs[i], pb);
        }
        lanes[laneCount-1] = new Lane(pb,pb);

        game.setScreen(new GameScreen(game, 0, lanes, pb, difficulty));
    }


    @Override
    public void show() {
        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean keyDown(int keyCode) {
                if (keyCode == Input.Keys.F6) {
                    FileHandle file = Gdx.files.local("bin/save1.json");
                    JsonValue jsonString = new JsonReader().parse(file);
                    game.setScreen(new GameScreen(game, jsonString));

                } else if (keyCode == Input.Keys.LEFT) {

                    if (keyCode == Input.Keys.NUM_1) {
                        startGame(1);
                    } else if (keyCode == Input.Keys.NUM_2) {
                        startGame(2);
                    } else if (keyCode == Input.Keys.NUM_3) {
                        startGame(3);
                    } else if (keyCode == Input.Keys.LEFT) {
                        selection += boats.length - 1;
                        selection %= boats.length;
                    } else if (keyCode == Input.Keys.RIGHT) {
                        selection++;
                        selection %= boats.length;
                    }
                    return true;
                }
                return true;
            }
        });
    }

    @Override
    public void render(float delta) {
        game.batch.begin();
        game.batch.draw(this.background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        game.batch.end();

        this.boats[this.selection].render(game.batch);

        game.batch.begin();
        this.font.draw(game.batch,
                String.format("Current Selection: %s",
                        this.BoatTypes.get(this.selection).getID()),
                Gdx.graphics.getWidth() * 0.03f, Gdx.graphics.getHeight() * .9f
        );
        this.font.draw(game.batch,
                String.format("Speed: %s",
                        String.join("",
                                Collections.nCopies((int) this.BoatTypes.get(this.selection).getSpeed() / 5, "█"))),
                Gdx.graphics.getWidth() * 0.03f, Gdx.graphics.getHeight() * .8f
        );
        this.font.draw(game.batch,
                String.format("Acceleration: %s",
                        String.join("",
                                Collections.nCopies((int) this.BoatTypes.get(this.selection).getAcceleration() / 5, "█"))),
                Gdx.graphics.getWidth() * 0.03f, Gdx.graphics.getHeight() * .7f
        );
        this.font.draw(game.batch,
                String.format("Handling: %s",
                        String.join("",
                                Collections.nCopies((int) this.BoatTypes.get(this.selection).getHandling(), "█"))),
                Gdx.graphics.getWidth() * 0.03f, Gdx.graphics.getHeight() * .6f
        );
        this.font.draw(game.batch,
                String.format("Strength: %s",
                        String.join("",
                                Collections.nCopies((int) this.BoatTypes.get(this.selection).getMaxHealth() / 100, "█"))),
                Gdx.graphics.getWidth() * 0.03f, Gdx.graphics.getHeight() * .5f
        );
        this.font.draw(game.batch,
                String.format("Weight: %s",
                        String.join("",
                                Collections.nCopies((int) this.BoatTypes.get(this.selection).getWeight(), "█"))),
                Gdx.graphics.getWidth() * 0.03f, Gdx.graphics.getHeight() * .4f
        );

        this.font.draw(game.batch, "Use arrow keys to select boat", Gdx.graphics.getWidth() * 0.03f, Gdx.graphics.getHeight() * 0.2f);
        this.font.draw(game.batch, "Select difficulty by pressing the corresponding button:", Gdx.graphics.getWidth() * 0.03f, Gdx.graphics.getHeight() * 0.1f);
        this.font.draw(game.batch, "Easy (1), Normal (2), Hard (3)", Gdx.graphics.getWidth() * 0.03f, Gdx.graphics.getHeight()*0.05f);
        game.batch.end();

    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }

    public void dispose() {
        this.background.dispose();
        this.font.dispose();
    }
}
