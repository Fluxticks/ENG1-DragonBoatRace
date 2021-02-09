package com.dragonboatrace.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.dragonboatrace.game.Background;
import com.dragonboatrace.game.DragonBoatRace;
import com.dragonboatrace.game.Lane;
import com.dragonboatrace.game.LaneMarker;
import com.dragonboatrace.game.entities.Obstacle;
import com.dragonboatrace.game.entities.ObstacleType;
import com.dragonboatrace.game.entities.PlayerBoat;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * The main game screen which is showing when the player is actually playing the game.
 *
 * @author Jacob Turner
 */
public class GameScreen extends ScreenAdapter {

    /**
     * The instance of the DragonBoatRace.
     */
    DragonBoatRace game;
    /**
     * The instance of the player boat.
     */
    PlayerBoat pb;
    /**
     * The finish line of that round.
     */
    Obstacle finishLineObstacle;
    /**
     * The y-position of the finish line, ie the length of the race.
     */
    int finishLine;
    /**
     * The font used to display the UI elements.
     */
    BitmapFont font;
    /**
     * The current round number.
     */
    int round;
    /**
     * The array of lane markers that separates each lane.
     */
    LaneMarker[] laneMarkers;
    /**
     * The array of available backgrounds to display.
     */
    Background[] backgrounds; //TODO: Check this.
    /**
     * The array of the lanes that are in the game.
     */
    Lane[] lanes;
    /**
     * The number of lanes, including the player lane.
     */
    int laneCount;
    /**
     * The time at which the race started. Used to calculate the time it took for each boat.
     */
    long raceStartTime;
    /**
     * The difficulty of the race chosen by the player.
     */
    int difficulty;
    /**
     * The multiplier that increases the number of obstacles for a given difficulty.
     */
    float obstacleMultiplier;

    /**
     * Creates a new screen.
     *
     * @param game       The instance of DragonBoatRace.
     * @param round      The current round of the game.
     * @param lanes      The array of lanes to be persistent throughout the whole game.
     * @param playerBoat The instance of the player boat.
     * @param difficulty The difficulty chosen by the player.
     */
    public GameScreen(DragonBoatRace game, int round, Lane[] lanes, PlayerBoat playerBoat, int difficulty) {
        this.game = game;
        this.lanes = lanes;
        this.pb = playerBoat;
        this.difficulty = difficulty;
        this.game.toDispose.add(this);
        this.finishLineObstacle = new Obstacle(ObstacleType.FINISHLINE, new Vector2(0, 0), new Vector2(0, 0));
        this.finishLineObstacle.loadTexture();
        this.round = round;
        this.obstacleMultiplier = 1;
        this.create(round);
    }

    /**
     * Creates a new screen using a json string from a save file.
     *
     * @param game       The instance of the DragonBoatRace.
     * @param jsonString The json string of the game state from a save file.
     */
    public GameScreen(DragonBoatRace game, JsonValue jsonString) {
        this.game = game;
        this.round = jsonString.getInt("round");
        this.difficulty = jsonString.getInt("difficulty");
        this.obstacleMultiplier = 1;
        this.game.toDispose.add(this);
        this.finishLineObstacle = new Obstacle(ObstacleType.FINISHLINE, new Vector2(0, 0), new Vector2(0, 0));
        this.finishLineObstacle.loadTexture();

        // Create a temporary list of lanes to be dynamically loaded in.
        ArrayList<Lane> tempLanes = new ArrayList<>();
        // Each lane has a json string an just needs to be loaded in.
        for (JsonValue lane : jsonString.get("lanes")) {
            // Each lane is able to create the boat, obstacles and its position from the string.
            Lane tempLane = new Lane(lane);
            tempLanes.add(tempLane);
            // Find which one is the player's lane
            if (tempLane.isPlayerLane) {
                this.pb = (PlayerBoat) tempLane.getBoat();
            }
        }
        // Convert to a static length array
        this.lanes = tempLanes.toArray(new Lane[0]);

        // Set the player boat for all the lanes.
        for (Lane lane : lanes) {
            lane.setPb(this.pb);
            lane.loadTexture();
        }
        this.create(this.round);
    }

    /**
     * Show the screen.
     */
    @Override
    public void show() {
        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean keyDown(int keyCode) {
                if (keyCode == Input.Keys.ESCAPE) {
                    game.dispose();
                    Gdx.app.exit();
                }
                if (keyCode == Input.Keys.F1 || keyCode == Input.Keys.F2 || keyCode == Input.Keys.F3) {
                    // 243 is keycode of F1 - 1
                    makeSave(keyCode - 243);
                    Gdx.app.exit();
                }
                return true;
            }
        });
    }

    /**
     * Save the current game state to a file.
     *
     * @param saveSlot The slot to save in.
     */
    public void makeSave(int saveSlot) {
        FileHandle file = Gdx.files.local("bin/save" + saveSlot + ".json");
        String[] laneStrings = new String[this.lanes.length];

        // Convert each lane to a string containing the boat and obstacles.
        for (int i = 0; i < laneStrings.length; i++) {
            laneStrings[i] = this.lanes[i].save();
        }

        // Format the total string with all the lanes and include the elected difficulty and current round
        String saveString = String.format("{round:%d, difficulty:%d, lanes:%s}",
                this.round,
                this.difficulty,
                Arrays.toString(laneStrings)
        );

        saveJSONString(saveString, file);
    }

    /**
     * Save a JSON string to a file.
     *
     * @param jsonString The string to save.
     * @param file       The file to save to.
     * @return A boolean on if the save was successful or not.
     */
    public static boolean saveJSONString(String jsonString, FileHandle file) {
        try {
            Json json = new Json();
            file.writeString(json.prettyPrint(jsonString), false);
            return true;
        } catch (GdxRuntimeException e) {
            return false;
        }

    }

    /**
     * Setup the number of obstacles and other round based parameters before the screen shows.
     *
     * @param round The current round.
     */
    public void create(int round) {

        // Given the difficulty choose a multiplier
        switch (difficulty) {
            case 1:
                this.obstacleMultiplier = (float) 0.5;
                break;
            case 2:
                this.obstacleMultiplier = (float) 1;
                break;
            case 3:
                this.obstacleMultiplier = (float) 2;
                break;
        }

        raceStartTime = System.currentTimeMillis();

        // Setup the lanes
        laneCount = 7;
        laneMarkers = new LaneMarker[laneCount + 1];
        float laneWidth = Gdx.graphics.getWidth() / (float) laneCount;
        for (int i = 0; i < laneCount + 1; i++) {
            laneMarkers[i] = new LaneMarker(new Vector2(i * laneWidth, 0));
        }

        // Setup the backgrounds
        int backgroundCount = 5;
        backgrounds = new Background[backgroundCount];
        for (int i = 0; i < backgroundCount; i++) {
            backgrounds[i] = new Background(new Vector2(Gdx.graphics.getWidth() / 2f, i * 1080));
        }

        // Update each of the lanes with the new multiplier and round number
        for (Lane lane : lanes) {
            lane.updateRound(this.round, this.obstacleMultiplier);
        }

        // Each round has a different length
        switch (round) {
            case 0:
                finishLine = 10000;
                break;
            case 1:
                finishLine = 12000;
                break;
            case 2:
                finishLine = 14000;
                break;
            case 3:
                finishLine = 15000;
                break;
            default:
                finishLine = 500;
                break;
        }

        // Initialise the font
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/FreeMono.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 24;
        parameter.color = Color.WHITE;
        parameter.borderColor = Color.BLACK;
        parameter.borderWidth = 1;
        this.font = generator.generateFont(parameter);
    }

    /**
     * Render the game to the player.
     *
     * @param deltaTime The time since the previous frame.
     */
    public void render(float deltaTime) {
        Gdx.gl.glClearColor(0, 0, 1, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Render the backgrounds
        for (Background b : backgrounds) {
            b.render(game.batch, pb.getInGamePos());
        }

        // Render the lane markers
        for (LaneMarker l : laneMarkers) {
            l.render(game.batch, pb.getInGamePos());
        }

        // Render each lane, which contains the boats and the obstacles
        for (Lane lane : lanes) {
            lane.render(game.batch);
            lane.update(deltaTime);
        }

        // Check the current health of the players boat only
        if (pb.getCurrentHealth() == 0) {
            game.setScreen(new BoatDeathScreen(game));
        }

        checkAllBoatsForFinished();

        finishLineObstacle.render(game.batch, pb.getInGamePos());

        this.showHUD();
    }

    /**
     * Check if any boats have finished, and if the player boat estimate the finish times of the cpu boats.
     */
    private void checkAllBoatsForFinished() {

        finishLineObstacle.getPos().y = finishLine; //this will make the finish line appear on the screen

        // Check all but the player being finished
        for (Lane lane : lanes) {
            if (!lane.isPlayerLane) {
                lane.checkBoatFinished(finishLine, this.raceStartTime);
            }
        }

        // Check if the player has finished
        if (pb.checkFinished(finishLine, this.raceStartTime)) {
            //calculate the times it would have taken or did take the cpus to finish
            //send every boats finishing time to the next screen along w the current round

            for (Lane lane : lanes) {
                if (!lane.isPlayerLane) {
                    if (!lane.checkBoatFinished(finishLine, this.raceStartTime)) {
                        long timeEstimate = (long) ((pb.getFinishTimeLong()) * (finishLine / lane.getBoatGamePos().y));
                        lane.setBoatFinishTime(timeEstimate);
                    }
                }
            }

            if (round != 3) {
                //go to mid round screen
                game.setScreen(new midRoundScreen(game, round, this.lanes, this.pb, this.difficulty));
            } else {
                //go to final results screen
                game.setScreen(new Finale(game, this.lanes, pb));
            }

        }
    }

    /**
     * Render the UI.
     */
    private void showHUD() {
        // Distance bar background
        this.game.shapeRenderer.begin(ShapeType.Filled);
        this.game.shapeRenderer.setColor(Color.DARK_GRAY);
        this.game.shapeRenderer.rect(
                0.02f * Gdx.graphics.getWidth(),
                0.96f * Gdx.graphics.getHeight(),
                0.2f * Gdx.graphics.getWidth(),
                0.02f * Gdx.graphics.getHeight()
        );
        this.game.shapeRenderer.end();
        // Distance bar progress
        this.game.shapeRenderer.begin(ShapeType.Filled);
        this.game.shapeRenderer.setColor(Color.LIGHT_GRAY);
        this.game.shapeRenderer.rect(
                0.02f * Gdx.graphics.getWidth(),
                0.96f * Gdx.graphics.getHeight(),
                0.2f * Gdx.graphics.getWidth() * (pb.getDistanceTravelled() / this.finishLine),
                0.02f * Gdx.graphics.getHeight()
        );
        this.game.shapeRenderer.end();
        // Distance bar outline
        this.game.shapeRenderer.begin(ShapeType.Line);
        this.game.shapeRenderer.setColor(Color.BLACK);
        this.game.shapeRenderer.rect(
                0.02f * Gdx.graphics.getWidth(),
                0.96f * Gdx.graphics.getHeight(),
                0.2f * Gdx.graphics.getWidth(),
                0.02f * Gdx.graphics.getHeight()
        );
        this.game.shapeRenderer.end();
        // Distance bar text
        this.game.batch.begin();
        this.font.draw(
                this.game.batch,
                "Distance",
                0.022f * Gdx.graphics.getWidth(),
                0.975f * Gdx.graphics.getHeight());
        this.game.batch.end();

        // Health bar background
        this.game.shapeRenderer.begin(ShapeType.Filled);
        this.game.shapeRenderer.setColor(Color.valueOf("#800f0f"));
        this.game.shapeRenderer.rect(
                0.02f * Gdx.graphics.getWidth(),
                0.92f * Gdx.graphics.getHeight(),
                0.2f * Gdx.graphics.getWidth(),
                0.02f * Gdx.graphics.getHeight()
        );
        this.game.shapeRenderer.end();
        // Health bar amount
        this.game.shapeRenderer.begin(ShapeType.Filled);
        this.game.shapeRenderer.setColor(Color.RED);
        this.game.shapeRenderer.rect(
                0.02f * Gdx.graphics.getWidth(),
                0.92f * Gdx.graphics.getHeight(),
                0.2f * Gdx.graphics.getWidth() * (pb.getCurrentHealth() / pb.getType().getMaxHealth()),
                0.02f * Gdx.graphics.getHeight()
        );
        this.game.shapeRenderer.end();
        // Health bar outline
        this.game.shapeRenderer.begin(ShapeType.Line);
        this.game.shapeRenderer.setColor(Color.BLACK);
        this.game.shapeRenderer.rect(
                0.02f * Gdx.graphics.getWidth(),
                0.92f * Gdx.graphics.getHeight(),
                0.2f * Gdx.graphics.getWidth(),
                0.02f * Gdx.graphics.getHeight()
        );
        this.game.shapeRenderer.end();
        // Health bar text
        this.game.batch.begin();
        this.font.draw(
                this.game.batch,
                "Health",
                0.022f * Gdx.graphics.getWidth(),
                0.935f * Gdx.graphics.getHeight());
        this.game.batch.end();

        // Stamina bar background
        this.game.shapeRenderer.begin(ShapeType.Filled);
        this.game.shapeRenderer.setColor(Color.NAVY);
        this.game.shapeRenderer.rect(
                0.02f * Gdx.graphics.getWidth(),
                0.88f * Gdx.graphics.getHeight(),
                0.2f * Gdx.graphics.getWidth(),
                0.02f * Gdx.graphics.getHeight()
        );
        this.game.shapeRenderer.end();
        // Stamina bar amount
        this.game.shapeRenderer.begin(ShapeType.Filled);
        this.game.shapeRenderer.setColor(Color.BLUE);
        this.game.shapeRenderer.rect(
                0.02f * Gdx.graphics.getWidth(),
                0.88f * Gdx.graphics.getHeight(),
                0.2f * Gdx.graphics.getWidth() * (pb.getCurrentStamina() / pb.getMaxStamina()),
                0.02f * Gdx.graphics.getHeight()
        );
        this.game.shapeRenderer.end();
        // Stamina bar outline
        this.game.shapeRenderer.begin(ShapeType.Line);
        this.game.shapeRenderer.setColor(Color.BLACK);
        this.game.shapeRenderer.rect(
                0.02f * Gdx.graphics.getWidth(),
                0.88f * Gdx.graphics.getHeight(),
                0.2f * Gdx.graphics.getWidth(),
                0.02f * Gdx.graphics.getHeight()
        );
        this.game.shapeRenderer.end();
        // Stamina bar text
        this.game.batch.begin();
        this.font.draw(
                this.game.batch,
                "Stamina",
                0.022f * Gdx.graphics.getWidth(),
                0.895f * Gdx.graphics.getHeight());
        this.game.batch.end();

        // Speed bar background
        this.game.shapeRenderer.begin(ShapeType.Filled);
        this.game.shapeRenderer.setColor(Color.valueOf("#838510"));
        this.game.shapeRenderer.rect(
                0.02f * Gdx.graphics.getWidth(),
                0.84f * Gdx.graphics.getHeight(),
                0.2f * Gdx.graphics.getWidth(),
                0.02f * Gdx.graphics.getHeight()
        );
        this.game.shapeRenderer.end();
        // Speed bar amount
        this.game.shapeRenderer.begin(ShapeType.Filled);
        this.game.shapeRenderer.setColor(Color.YELLOW);
        this.game.shapeRenderer.rect(
                0.02f * Gdx.graphics.getWidth(),
                0.84f * Gdx.graphics.getHeight(),
                0.2f * Gdx.graphics.getWidth() * Math.min((pb.getCurrentSpeed() / pb.getType().getSpeed()), 1),
                0.02f * Gdx.graphics.getHeight()
        );
        this.game.shapeRenderer.end();
        // Speed bar outline
        this.game.shapeRenderer.begin(ShapeType.Line);
        this.game.shapeRenderer.setColor(Color.BLACK);
        this.game.shapeRenderer.rect(
                0.02f * Gdx.graphics.getWidth(),
                0.84f * Gdx.graphics.getHeight(),
                0.2f * Gdx.graphics.getWidth(),
                0.02f * Gdx.graphics.getHeight()
        );
        this.game.shapeRenderer.end();
        // Speed bar text
        this.game.batch.begin();
        this.font.draw(
                this.game.batch,
                "Speed",
                0.022f * Gdx.graphics.getWidth(),
                0.857f * Gdx.graphics.getHeight());
        this.game.batch.end();
    }

    /**
     * Dispose of all the game elements.
     *
     * @see Lane
     * @see com.dragonboatrace.game.entities.Boat
     * @see Background
     */
    @Override
    public void dispose() {
        for (Lane lane : lanes) {
            lane.dispose();
        }
        for (LaneMarker l : this.laneMarkers) {
            l.dispose();
        }
        for (Background b : this.backgrounds) {
            b.dispose();
        }
    }
}
