package com.dragonboatrace.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.dragonboatrace.game.*;
import com.dragonboatrace.game.entities.CPUBoat;
import com.dragonboatrace.game.entities.Obstacle;
import com.dragonboatrace.game.entities.ObstacleType;
import com.dragonboatrace.game.entities.PlayerBoat;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public class GameScreen extends ScreenAdapter {

    DragonBoatRace game;
    CPUBoat[] CPUs;
    PlayerBoat pb;
    Obstacle finishLineObstacle;    //the finish line that appears on screen
    int finishLine;                    //the y position in the game that the players have to pass to finish
    BitmapFont font;
    int round;
    ArrayList<Obstacle> obstacleList;
    ObstacleType[] obstacles;
    Obstacle collider;
    LaneMarker[] laneMarkers;
    Background[] backgrounds;
    int maxObstacles, laneCount;
    long raceStartTime;

    public GameScreen(DragonBoatRace game, int round, CPUBoat[] CPUs, PlayerBoat playerBoat) {
        this.game = game;
        this.create(round);
        this.CPUs = CPUs;
        this.pb = playerBoat;
        this.game.toDispose.add(this);
        this.finishLineObstacle = new Obstacle(ObstacleType.FINISHLINE, new Vector2(0, 0), new Vector2(0, 0));
        this.round = round;
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean keyDown(int keyCode) {
                if (keyCode == Input.Keys.ESCAPE) {
                    game.dispose();
                    System.exit(0);
                }
                return true;
            }
        });
    }

    public void create(int round) {

        raceStartTime = System.currentTimeMillis();

        obstacles = new ObstacleType[]{ObstacleType.BUOY, ObstacleType.ROCK, ObstacleType.BRANCH, ObstacleType.DUCK, ObstacleType.RUBBISH, ObstacleType.LONGBOI, ObstacleType.BOAT};    // The
        laneCount = 7;
        laneMarkers = new LaneMarker[laneCount + 1];
        for (int i = 0; i < laneCount + 1; i++) {
            laneMarkers[i] = new LaneMarker(new Vector2(i * Gdx.graphics.getWidth() / ((float)laneCount), 0));
        }

        int backgroundCount = 5;
        backgrounds = new Background[backgroundCount];
        for (int i = 0; i < backgroundCount; i++) {
            backgrounds[i] = new Background(new Vector2(Gdx.graphics.getWidth() / 2f, i * 1080));
        }

        obstacleList = new ArrayList<>();    // Creating the empty arrayList of obstacles

        switch (round) {    // The max number of obstacles changes from round to round
            case 0:
                maxObstacles = 10;
                break;
            case 1:
                maxObstacles = 15;
                break;
            case 2:
                maxObstacles = 20;
                break;
            case 3:
                maxObstacles = 30;
                break;
            default:
                maxObstacles = 0;
                break;
        }

        switch (round) {
            case 0:
                finishLine = 20000;
                break;
            case 1:
                finishLine = 24000;
                break;
            case 2:
                finishLine = 28000;
                break;
            case 3:
                finishLine = 30000;
                break;
            default:
                finishLine = 1000;
                break;
        }

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/FreeMono.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 24;
        parameter.color = Color.WHITE;
        parameter.borderColor = Color.BLACK;
        parameter.borderWidth = 1;
        this.font = generator.generateFont(parameter);
    }

    public void render(float delta) {
        float deltaTime = Gdx.graphics.getDeltaTime();    // Getting time since last frame
        Gdx.gl.glClearColor(0, 0, 1, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        for (Background b : backgrounds) {
            b.render(game.batch, pb.getInGamePos());
        }

        for (LaneMarker l : laneMarkers) {
            l.render(game.batch, pb.getInGamePos());
        }
		
/*
		game.batch.begin();	// Start drawing HUD (For debugging)
		String debugString = String.format("stamina: %f\nhealth: %f\npos.x: %f\npos.y: %f\nvel.x: %f\nvel.y: %f\nmaxSpeed: %f\nhealth: %f\nobstacles: %d\ncolliding: %s\nPenalties: %f\nResetDelay: %f", 
			pb.getStamina(),
			pb.getHealth(),
			pb.getPos().x, 
			pb.getPos().y, 
			pb.getVel().x,
			pb.getVel().y,
			pb.getMaxSpeed(),
			pb.getHealth(),
			obstacleList.size(),
			collider != null ? collider.getType().getID() : "null",
			pb.timePenalties,
			pb.penaltyResetDelay);
		// game.font.draw(game.batch, debugString, 10, Gdx.graphics.getHeight() - 10);
		game.batch.end();
*/

        checkAllBoatsForFinished();

        Iterator<Obstacle> obstacleIterator = obstacleList.iterator();    // Create iterator for iterating over the obstacles

        collider = null;

        while (obstacleIterator.hasNext()) {    // While there is another obstacle, continute iterating
            Obstacle o = obstacleIterator.next();    // Get the next obstacle
            Vector2 renderPos = o.getRelPos(pb.getInGamePos());    // Get the position of the obstacle, relative to the players boat
            if (renderPos.x > Gdx.graphics.getWidth() + 30 ||
                    renderPos.x + o.getSize().x < -30 ||
                    // renderPos.y > Gdx.graphics.getHeight() +10 ||
                    renderPos.y + o.getSize().y < -100) {
                obstacleIterator.remove();    // If the obstacles is off the screen (apart from the top) delete it
            } else {
                o.render(game.batch, pb.getInGamePos());    // If the obstacle is not off the screen, render it
                o.move(deltaTime);    // Run the obstacles mover
                o.update(deltaTime);    // Update the position of the obstacle
                if (pb.checkCollision(o)) {    // See if the players boat is colliding with the obstacle
                    collider = o;
                }

                for (CPUBoat c : CPUs) {
                    c.checkCollision(o);
                }
            }
        }
        if (obstacleList.size() < maxObstacles) {
            obstacleList.add(spawnObstacle());
        }

        finishLineObstacle.render(game.batch, pb.getInGamePos());

        pb.render(game.batch);    // Render the boat
        pb.move(deltaTime);    // Move the boat based on player inputs
        pb.update(deltaTime);    // Update the position of the boat

        if (pb.getHealth() == 0) {
            game.setScreen(new BoatDeathScreen(game));
        }

        for (CPUBoat c : CPUs) {
            c.render(game.batch, pb.getInGamePos());
            c.move(deltaTime);
            c.update(deltaTime);
        }


        this.showHUD();
    }

    private Obstacle spawnObstacle() {
        Vector2 spawnPos;    // position the obstacle will spawn at
        Vector2 dir;        // direction the obstacle will start travelling in
        ObstacleType obs;    // type of the obstacle that will be spawned
        int obstacleChoice;    // index of the type of obstacle that will be spawned
        int side;            // the side of the screen the obstacle will spawn at
        Random rand = new Random();

        switch (round) {    // The types of obstacles that can spawn is dependant on the round
            case 0:
                obstacleChoice = rand.nextInt(2);    // The following can be spawned in round 0: {BUOY, ROCK}
                break;
            case 1:
                obstacleChoice = rand.nextInt(4);    // The following can be spawned in round 1: {BUOY, ROCK, BRANCH, DUCK}
                break;
            case 2:
                obstacleChoice = rand.nextInt(6);    // The following can be spawned in round 2: {BUOY, ROCK, BRANCH, DUCK, RUBBISH, LONGBOI}
                break;
            case 3:
                obstacleChoice = rand.nextInt(7);    // The following can be spawned in round 3: {BUOY, ROCK, BRANCH, DUCK, RUBBISH, LONGBOI, BOAT}
                break;
            default:
                obstacleChoice = -1;    // Should never be the case, -1 will cause an error, as there is something wrong.
                break;
        }

        obs = obstacles[obstacleChoice];    // Select the obstacle type from the list
        if (obs.getMover().getID().equals("static")) {    // Static moving obstacles can only be spawned from the top
            side = 0;
        } else {
            side = rand.nextInt(3);        // Any other obstacles can spawn on any other side
        }

        switch (side) {        // Creating a spawning position along the chosen edge
            case 0:
                spawnPos = new Vector2(    // If spawning along the top edge, pick a random x coord and a random y within the bounds of the screen, will be translated off screen
                        rand.nextFloat() * Gdx.graphics.getWidth(),
                        (rand.nextFloat()) * Gdx.graphics.getHeight()
                );
                break;
            case 1:
                spawnPos = new Vector2(    // For the edge spawning, spawn slightly off screen but not enough to be deleted, and in the top 2/3rds of the side
                        -10,
                        rand.nextFloat() * Gdx.graphics.getHeight() * 2 / 3
                );
                break;
            case 2:
                spawnPos = new Vector2(    // Same as above
                        Gdx.graphics.getWidth() + 10,
                        rand.nextFloat() * Gdx.graphics.getHeight() * 2 / 3
                );
                break;
            default:    // By default spawn at 0,0
                spawnPos = new Vector2();
                break;
        }
        spawnPos.y = 2 * Gdx.graphics.getHeight() - spawnPos.y + pb.getInGamePos().y;        // Translate 2 screens up and relative to the player

        dir = spawnPos.cpy().sub(new Vector2(    // Create a vector pointing from the spawn pos to a random point on the screen
                rand.nextFloat() * Gdx.graphics.getWidth(),
                rand.nextFloat() * Gdx.graphics.getHeight())
        );
        dir.limit(obs.getSpeed());    // Limit the vector to the max speed of the obstacle

        return new Obstacle(obs, spawnPos, dir);    // Return the new obstacle
    }

    private void checkAllBoatsForFinished() {

        finishLineObstacle.getPos().y = finishLine; //this will make the finish line appear on the screen

        for (CPUBoat cpu : CPUs) {
            cpu.checkFinished(finishLine, this.raceStartTime); //this checks every cpu to see if it's finished, if it has it'll update their finishing time
        }

        if (pb.checkFinished(finishLine, this.raceStartTime)) {
            //calculate the times it would have taken or did take the cpus to finish
            //send every boats finishing time to the next screen along w the current round

            for (CPUBoat cpu : CPUs) {
                //if a cpu hasn't finished the race, it will estimate when it would have finished based on the distance it managed to travel in the time it took the player to win
                if (!cpu.checkFinished(finishLine, this.raceStartTime)) {
                    long timeEstimate = (long) ((pb.getFinishTimeLong()) * (finishLine / cpu.getInGamePos().y));
                    cpu.setFinishTime(timeEstimate);
                }
            }

            if (round != 3) {
                //go to mid round screen
                game.setScreen(new midRoundScreen(game, round, CPUs, pb));
            } else {
                //go to final results screen
                game.setScreen(new Finale(game, CPUs, pb));
            }

        }
    }

    private void showHUD() {
        // Finish line progress
        this.game.shapeRenderer.begin(ShapeType.Filled);
        this.game.shapeRenderer.setColor(Color.DARK_GRAY);
        this.game.shapeRenderer.rect(
                0.02f * Gdx.graphics.getWidth(),
                0.96f * Gdx.graphics.getHeight(),
                0.2f * Gdx.graphics.getWidth(),
                0.02f * Gdx.graphics.getHeight()
        );
        this.game.shapeRenderer.end();

        this.game.shapeRenderer.begin(ShapeType.Filled);
        this.game.shapeRenderer.setColor(Color.LIGHT_GRAY);
        this.game.shapeRenderer.rect(
                0.02f * Gdx.graphics.getWidth(),
                0.96f * Gdx.graphics.getHeight(),
                0.2f * Gdx.graphics.getWidth() * (pb.getDistanceTravelled() / this.finishLine),
                0.02f * Gdx.graphics.getHeight()
        );
        this.game.shapeRenderer.end();

        this.game.shapeRenderer.begin(ShapeType.Line);
        this.game.shapeRenderer.setColor(Color.BLACK);
        this.game.shapeRenderer.rect(
                0.02f * Gdx.graphics.getWidth(),
                0.96f * Gdx.graphics.getHeight(),
                0.2f * Gdx.graphics.getWidth(),
                0.02f * Gdx.graphics.getHeight()
        );
        this.game.shapeRenderer.end();

        this.game.batch.begin();
        this.font.draw(
                this.game.batch,
                "Distance",
                0.022f * Gdx.graphics.getWidth(),
                0.975f * Gdx.graphics.getHeight());
        this.game.batch.end();


        // Health Bar
        this.game.shapeRenderer.begin(ShapeType.Filled);
        this.game.shapeRenderer.setColor(Color.valueOf("#800f0f"));
        this.game.shapeRenderer.rect(
                0.02f * Gdx.graphics.getWidth(),
                0.92f * Gdx.graphics.getHeight(),
                0.2f * Gdx.graphics.getWidth(),
                0.02f * Gdx.graphics.getHeight()
        );
        this.game.shapeRenderer.end();

        this.game.shapeRenderer.begin(ShapeType.Filled);
        this.game.shapeRenderer.setColor(Color.RED);
        this.game.shapeRenderer.rect(
                0.02f * Gdx.graphics.getWidth(),
                0.92f * Gdx.graphics.getHeight(),
                0.2f * Gdx.graphics.getWidth() * (pb.getHealth() / pb.getType().getMaxHealth()),
                0.02f * Gdx.graphics.getHeight()
        );
        this.game.shapeRenderer.end();

        this.game.shapeRenderer.begin(ShapeType.Line);
        this.game.shapeRenderer.setColor(Color.BLACK);
        this.game.shapeRenderer.rect(
                0.02f * Gdx.graphics.getWidth(),
                0.92f * Gdx.graphics.getHeight(),
                0.2f * Gdx.graphics.getWidth(),
                0.02f * Gdx.graphics.getHeight()
        );
        this.game.shapeRenderer.end();

        this.game.batch.begin();
        this.font.draw(
                this.game.batch,
                "Health",
                0.022f * Gdx.graphics.getWidth(),
                0.935f * Gdx.graphics.getHeight());
        this.game.batch.end();

        // Stamina Bar
        this.game.shapeRenderer.begin(ShapeType.Filled);
        this.game.shapeRenderer.setColor(Color.NAVY);
        this.game.shapeRenderer.rect(
                0.02f * Gdx.graphics.getWidth(),
                0.88f * Gdx.graphics.getHeight(),
                0.2f * Gdx.graphics.getWidth(),
                0.02f * Gdx.graphics.getHeight()
        );
        this.game.shapeRenderer.end();

        this.game.shapeRenderer.begin(ShapeType.Filled);
        this.game.shapeRenderer.setColor(Color.BLUE);
        this.game.shapeRenderer.rect(
                0.02f * Gdx.graphics.getWidth(),
                0.88f * Gdx.graphics.getHeight(),
                0.2f * Gdx.graphics.getWidth() * (pb.getStamina() / pb.getMaxStamina()),
                0.02f * Gdx.graphics.getHeight()
        );
        this.game.shapeRenderer.end();

        this.game.shapeRenderer.begin(ShapeType.Line);
        this.game.shapeRenderer.setColor(Color.BLACK);
        this.game.shapeRenderer.rect(
                0.02f * Gdx.graphics.getWidth(),
                0.88f * Gdx.graphics.getHeight(),
                0.2f * Gdx.graphics.getWidth(),
                0.02f * Gdx.graphics.getHeight()
        );
        this.game.shapeRenderer.end();

        this.game.batch.begin();
        this.font.draw(
                this.game.batch,
                "Stamina",
                0.022f * Gdx.graphics.getWidth(),
                0.895f * Gdx.graphics.getHeight());
        this.game.batch.end();

        // Speed Bar
        this.game.shapeRenderer.begin(ShapeType.Filled);
        this.game.shapeRenderer.setColor(Color.valueOf("#838510"));
        this.game.shapeRenderer.rect(
                0.02f * Gdx.graphics.getWidth(),
                0.84f * Gdx.graphics.getHeight(),
                0.2f * Gdx.graphics.getWidth(),
                0.02f * Gdx.graphics.getHeight()
        );
        this.game.shapeRenderer.end();

        this.game.shapeRenderer.begin(ShapeType.Filled);
        this.game.shapeRenderer.setColor(Color.YELLOW);
        this.game.shapeRenderer.rect(
                0.02f * Gdx.graphics.getWidth(),
                0.84f * Gdx.graphics.getHeight(),
                0.2f * Gdx.graphics.getWidth() * Math.min((pb.getCurrentSpeed() / pb.getType().getSpeed()), 1),
                0.02f * Gdx.graphics.getHeight()
        );
        this.game.shapeRenderer.end();

        this.game.shapeRenderer.begin(ShapeType.Line);
        this.game.shapeRenderer.setColor(Color.BLACK);
        this.game.shapeRenderer.rect(
                0.02f * Gdx.graphics.getWidth(),
                0.84f * Gdx.graphics.getHeight(),
                0.2f * Gdx.graphics.getWidth(),
                0.02f * Gdx.graphics.getHeight()
        );
        this.game.shapeRenderer.end();

        this.game.batch.begin();
        this.font.draw(
                this.game.batch,
                "Speed",
                0.022f * Gdx.graphics.getWidth(),
                0.857f * Gdx.graphics.getHeight());
        this.game.batch.end();
    }

    @Override
    public void dispose() {
        pb.dispose();
        for (CPUBoat c : this.CPUs) {
            c.dispose();
        }
        for (LaneMarker l : this.laneMarkers) {
            l.dispose();
        }
        for (Background b : this.backgrounds) {
            b.dispose();
        }
    }
}
