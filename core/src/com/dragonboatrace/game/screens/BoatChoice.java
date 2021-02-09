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

/**
 * Represents the screen that allows the user to choose a boat for the race.
 *
 * @author Jacob Turner
 */
public class BoatChoice extends ScreenAdapter {

    /**
     * The instance of DragonBoatRace to be used across the whole game.
     */
    DragonBoatRace game;
    /**
     * The list of instantiated boats to be presented to the user as choices.
     */
    Boat[] boats;
    /**
     * The list of the types of boats the user can choose from.
     */
    ArrayList<BoatType> BoatTypes;
    /**
     * The index of the current selection the user is looking at.
     */
    int selection;
    /**
     * The size to scale the boat up to in the preview.
     */
    int boatScale;
    /**
     * The font used to render the statistics and other UI elements.
     */
    BitmapFont font;
    /**
     * The background texture.
     */
    Texture background;

    /**
     * Creates a new screen for the boat choice of the user.
     *
     * @param game The instance of DragonBoatRace to be used across the whole game.
     */
    public BoatChoice(DragonBoatRace game) {
        this.game = game;
        this.selection = 0;
        this.boatScale = 7;
        this.BoatTypes = availableBoats(null);
        this.background = new Texture("menus/boatSelection.png");
        this.game.toDispose.add(this);

        // Initialise the list of boats to display as options to the user.
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
            // Render the boats at a larger scale.
            this.boats[i].getSize().scl(this.boatScale);
            this.boats[i].loadTexture();
        }

        // Initialise the font.
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/FreeMono.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size *= 3;
        parameter.color = Color.BLACK;
        this.font = generator.generateFont(parameter);
    }

    // THIS IS NEW
    public static ArrayList<BoatType> availableBoats(BoatType remove) {
        // Make an array of the available boattypes
        ArrayList<BoatType> types = new ArrayList<>(Arrays.asList(BoatType.values()));
        types.remove(BoatType.TESTING);
        if (remove != null) types.remove(remove);
        return types;
    }

    // THIS IS CHANGED
    /**
     * Start the game once the user has made their choice with a specific difficulty.
     *
     * @param difficulty The difficulty the user has chosen.
     */
    public void startGame(int difficulty) {

        // The number of lanes.
        int laneCount = 7;

        // Make n-1 cpu boats as one lane is for the player.
        CPUBoat[] CPUs = new CPUBoat[laneCount - 1];
        ArrayList<BoatType> cpuBoatTypes = availableBoats(BoatTypes.get(selection));
        // Width of each lane.
        float laneWidth = Gdx.graphics.getWidth() / (float) laneCount;

        for (int i = 0; i < laneCount - 1; i++) {
            int xpos = i;
            // Don't choose the middle lane.
            if (i >= (laneCount - 1) / 2) {
                xpos += 1;
            }

            // Choose a random boat type for a given cpu boat.
            BoatType cpuBoatType = cpuBoatTypes.get((int) (Math.random() * cpuBoatTypes.size()));
            CPUs[i] = new CPUBoat(
                    cpuBoatType,
                    new Vector2(
                            (int) (0.5 + xpos) * (Gdx.graphics.getWidth() / (float) laneCount),
                            10
                    ),
                    new Tuple<>(
                            (xpos + 0) * laneWidth,
                            (xpos + 1) * laneWidth
                    )
            );
            CPUs[i].loadTexture();
        }

        // Make the player's boat
        PlayerBoat pb = new PlayerBoat(
                BoatTypes.get(selection),
                new Vector2(
                        Gdx.graphics.getWidth() / 2f,
                        10
                ), new Tuple<>(
                ((laneCount - 1) / 2) * laneWidth,
                ((laneCount + 1) / 2) * laneWidth
        )
        );
        pb.loadTexture();

        // Make the lanes for the boats.
        // Order here doesn't matter as the position of the lane is defined separately
        Lane[] lanes = new Lane[laneCount];
        for (int i = 0; i < laneCount - 1; i++) {
            lanes[i] = new Lane(CPUs[i], pb);
        }
        lanes[laneCount - 1] = new Lane(pb, pb);

        game.setScreen(new GameScreen(game, 0, lanes, pb, difficulty));
    }

    // THIS IS CHANGED
    /**
     * Show the screen.
     */
    @Override
    public void show() {
        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean keyDown(int keyCode) {

                // Create the key processor to capture the users input.
                if (keyCode >= Input.Keys.F1 && keyCode <= Input.Keys.F3) {
                    // Catch one of the F keys to load the respective file.
                    loadFile(keyCode);
                } else if (keyCode == Input.Keys.NUM_1) {
                    // Choose difficulty 1
                    startGame(1);
                } else if (keyCode == Input.Keys.NUM_2) {
                    // Choose difficulty 2
                    startGame(2);
                } else if (keyCode == Input.Keys.NUM_3) {
                    // Choose difficulty 3
                    startGame(3);
                } else if (keyCode == Input.Keys.LEFT) {
                    // Select the previous boat
                    selection += boats.length - 1;
                    selection %= boats.length;
                } else if (keyCode == Input.Keys.RIGHT) {
                    // Select the next boat
                    selection++;
                    selection %= boats.length;
                }
                return true;
            }
        });
    }

    // THIS IS NEW
    /**
     * Try to load a save file given a keycode, but if no file present do nothing.
     *
     * @param keyCode The keycode of the key being pressed.
     */
    protected void loadFile(int keyCode) {
        // Offset is the start F key - 1 as they are indexed from 1
        int keyCodeOffset = Input.Keys.F1 - 1;
        int fileNum = keyCode - keyCodeOffset;
        // Catch if no such save file
        try {
            FileHandle file = Gdx.files.local("bin/save" + fileNum + ".json");
            JsonValue jsonString = new JsonReader().parse(file);
            game.setScreen(new GameScreen(game, jsonString));
        } catch (NullPointerException e) {
            System.out.println("No Such file");
        }
    }

    /**
     * Render the screen.
     *
     * @param deltaTime The time since the previous frame.
     */
    @Override
    public void render(float deltaTime) {
        game.batch.begin();
        game.batch.draw(this.background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        game.batch.end();

        this.boats[this.selection].render(game.batch);

        game.batch.begin();
        // Render the boat information for the currently selected boat.
        this.font.draw(game.batch,
                String.format("Current Selection: %s BOAT",
                        this.BoatTypes.get(this.selection).name()),
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
        this.font.draw(game.batch, "Easy (1), Normal (2), Hard (3)", Gdx.graphics.getWidth() * 0.03f, Gdx.graphics.getHeight() * 0.05f);
        game.batch.end();

    }

    /**
     * Hide the screen.
     */
    @Override
    public void hide() {
        //Removed the input processor
        Gdx.input.setInputProcessor(null);
    }

    /**
     * Dispose the screen's background and font.
     */
    public void dispose() {
        this.background.dispose();
        this.font.dispose();
    }
}
