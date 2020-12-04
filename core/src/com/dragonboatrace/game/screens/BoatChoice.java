package com.dragonboatrace.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Vector2;
import com.dragonboatrace.game.DragonBoatRace;
import com.dragonboatrace.game.Tuple;
import com.dragonboatrace.game.entities.Boat;
import com.dragonboatrace.game.entities.BoatType;
import com.dragonboatrace.game.entities.CPUBoat;
import com.dragonboatrace.game.entities.PlayerBoat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;

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

    @Override
    public void show() {
        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean keyDown(int keyCode) {
                if (keyCode == Input.Keys.SPACE) {

                    int laneCount = 7;

                    CPUBoat[] CPUs = new CPUBoat[laneCount - 1];

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
                                        (xpos + 0) * (Gdx.graphics.getWidth() / (laneCount)) - cpuBoatType.getSize().x / 2,
                                        (xpos + 1) * (Gdx.graphics.getWidth() / (laneCount)) - cpuBoatType.getSize().x / 2
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
                            ((laneCount - 1) / 2) * (Gdx.graphics.getWidth() / (laneCount)) - BoatTypes.get(selection).getSize().x / 2,
                            ((laneCount + 1) / 2) * (Gdx.graphics.getWidth() / (laneCount)) - BoatTypes.get(selection).getSize().x / 2
                    )
                    );    // Creating the players boat
                    pb.saveStartPos();

                    game.setScreen(new GameScreen(game, 0, CPUs, pb));
                } else if (keyCode == Input.Keys.LEFT) {
                    selection += boats.length - 1;
                    selection %= boats.length;
                } else if (keyCode == Input.Keys.RIGHT) {
                    selection++;
                    selection %= boats.length;
                }
                return true;
            }
        });
    }

    @Override
    public void render(float delta) {
        game.batch.begin();
        game.batch.draw(this.background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        this.font.draw(game.batch,
                String.format("Current Selection: %s",
                        this.BoatTypes.get(this.selection).getID()),
                Gdx.graphics.getWidth() * 0.05f, Gdx.graphics.getHeight() * .9f
        );
        this.font.draw(game.batch,
                String.format("Speed: %s",
                        String.join("",
                                Collections.nCopies((int) this.BoatTypes.get(this.selection).getSpeed() / 5, "█"))),
                Gdx.graphics.getWidth() * 0.05f, Gdx.graphics.getHeight() * .8f
        );
        this.font.draw(game.batch,
                String.format("Acceleration: %s",
                        String.join("",
                                Collections.nCopies((int) this.BoatTypes.get(this.selection).getAcceleration() / 5, "█"))),
                Gdx.graphics.getWidth() * 0.05f, Gdx.graphics.getHeight() * .7f
        );
        this.font.draw(game.batch,
                String.format("Handling: %s",
                        String.join("",
                                Collections.nCopies((int) this.BoatTypes.get(this.selection).getHandling(), "█"))),
                Gdx.graphics.getWidth() * 0.05f, Gdx.graphics.getHeight() * .6f
        );
        this.font.draw(game.batch,
                String.format("Strength: %s",
                        String.join("",
                                Collections.nCopies((int) this.BoatTypes.get(this.selection).getMaxHealth() / 100, "█"))),
                Gdx.graphics.getWidth() * 0.05f, Gdx.graphics.getHeight() * .5f
        );
        this.font.draw(game.batch,
                String.format("Weight: %s",
                        String.join("",
                                Collections.nCopies((int) this.BoatTypes.get(this.selection).getWeight(), "█"))),
                Gdx.graphics.getWidth() * 0.05f, Gdx.graphics.getHeight() * .4f
        );

        this.font.draw(game.batch, "Use arrow keys to select and space to confirm", Gdx.graphics.getWidth() * 0.05f, Gdx.graphics.getHeight() * 0.15f);
        game.batch.end();

        this.boats[this.selection].render(game.batch);
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
