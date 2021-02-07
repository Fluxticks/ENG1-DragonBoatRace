package com.dragonboatrace.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.JsonValue;
import com.dragonboatrace.game.entities.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.ListIterator;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Represents a lane to be used for separating boats and obstacles into specific lanes.
 *
 * @author Benji Garment
 */
public class Lane {

    /**
     * The list of the current obstacles in the lane.
     */
    private ArrayList<Obstacle> obstacles;
    /**
     * The currently available power up in the lane.
     */
    private PowerUp currentPower;
    /**
     * The chance per-frame for a power up to be spawned.
     */
    private final float powerChance = 0.05f;
    /**
     * The current round of the race.
     */
    private int round;
    /**
     * The boat in the lane.
     */
    private final Boat boat;
    /**
     * The maximum number of obstacles in the lane.
     */
    private int maxObstacles;
    /**
     * An instance of the player boat.
     */
    private PlayerBoat pb;
    /**
     * A boolean of if the lane belongs to the player.
     */
    public boolean isPlayerLane;

    /**
     * Creates a new lane given a boat and an instance of the player boat.
     *
     * @param boatInLane The boat that is in the lane.
     * @param pb         An instance of the player boats lane.
     */
    public Lane(Boat boatInLane, PlayerBoat pb) {
        this.boat = boatInLane;
        this.obstacles = new ArrayList<>();
        this.currentPower = null;
        this.pb = pb;
        this.isPlayerLane = boatInLane instanceof PlayerBoat;
    }

    /**
     * Creates a new lane given a json string loaded from a save file.
     *
     * @param jsonString A json string representing a lane.
     */
    public Lane(JsonValue jsonString) {
        if (jsonString.getInt("isPlayer") == 1) {
            this.boat = new PlayerBoat(jsonString.get("boat"));
        } else {
            this.boat = new CPUBoat(jsonString.get("boat"));
        }

        this.obstacles = new ArrayList<>();
        for (JsonValue o : jsonString.get("obstacles")) {
            this.obstacles.add(new Obstacle(o));
        }

        this.isPlayerLane = jsonString.getInt("isPlayer") == 1;
        if (jsonString.get("powerup").get(0) != null) {
            this.currentPower = new PowerUp(jsonString.get("powerup"));
        } else {
            this.currentPower = null;
        }
        this.pb = null;
    }

    /**
     * Compares an object to a lane and determines if the object is an identical lane.
     *
     * @param obj The object being checked against.
     * @return A boolean of weather the object is an identical lane.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj.getClass() == this.getClass()) {
            Lane objLane = (Lane) obj;
            if (objLane.obstacles.size() != this.obstacles.size()) {
                return false;
            }
            boolean obstacleBool = true;
            for (int i = 0; i < obstacles.size(); i++) {
                obstacleBool = obstacleBool && objLane.obstacles.get(i).equals(this.obstacles.get(i));
            }
            boolean powerBool = false;
            if (this.currentPower != null) {
                powerBool = this.currentPower.equals(objLane.getCurrentPower());
            } else if (this.currentPower == null && objLane.getCurrentPower() == null) {
                powerBool = true;
            }
            boolean boatBool = this.boat.equals(objLane.getBoat());
            boolean pbBool = this.pb.equals(objLane.pb);
            return obstacleBool && powerBool && boatBool && pbBool;
        } else {
            return false;
        }
    }

    /**
     * Get the currently available power up.
     *
     * @return A power up or null if no power up available.
     */
    public PowerUp getCurrentPower() {
        return currentPower;
    }

    /**
     * Get a json string of the boat, power up and obstacles in the lane.
     *
     * @return A string representing the lane and all the attributes in the lane.
     */
    public String save() {
        String[] obstacleStrings = new String[this.obstacles.size()];
        for (int i = 0; i < obstacleStrings.length; i++) {
            obstacleStrings[i] = this.obstacles.get(i).save();
        }

        return String.format("{boat:%s, obstacles:%s, powerup:%s, isPlayer:%d}",
                this.boat.save(),
                Arrays.toString(obstacleStrings),
                (this.currentPower != null) ? this.currentPower.save() : null,
                this.isPlayerLane ? 1 : 0);


    }

    /**
     * Update the contents of the lane.
     *
     * @param deltaTime The time since the previous frame.
     * @see Boat
     * @see Obstacle
     * @see PowerUp
     */
    public void update(float deltaTime) {
        ListIterator<Obstacle> iter = obstacles.listIterator();
        while (iter.hasNext()) {
            Obstacle obstacle = iter.next();
            if (this.checkEntityNotOnScreen(obstacle)) {
                iter.remove();    // If the obstacles is off the screen (apart from the top) delete it
            } else {
                this.boat.checkForCollision(obstacle);
                obstacle.move(deltaTime);    // Run the obstacles mover
                obstacle.update(deltaTime);    // Update the position of the obstacle
            }
        }

        if (this.obstacles.size() < maxObstacles) {
            Obstacle o = spawnObstacle();
            o.loadTexture();
            this.obstacles.add(o);
        }

        if (this.currentPower != null) {
            updatePowerUp(deltaTime);
        } else {
            this.currentPower = spawnPowerUp();
        }

        // Don't like this but not sure how best to do this without swapping to the structure in the old game.
        if (!this.isPlayerLane) {
            ((CPUBoat) this.boat).decideMovement(obstacles);
        }

        this.boat.move(deltaTime);
        this.boat.update(deltaTime);
    }

    /**
     * Add the contents to the given SpriteBatch and render it.
     *
     * @param batch The spritebatch to be added to.
     * @see Boat
     * @see Obstacle
     * @see PowerUp
     */
    public void render(SpriteBatch batch) {
        for (Obstacle obstacle : this.obstacles) {
            obstacle.render(batch, this.pb.getInGamePos());
        }
        if (this.currentPower != null) this.currentPower.render(batch, this.pb.getInGamePos());
        this.boat.render(batch, this.pb.getInGamePos());
    }

    /**
     * Check if a given entity is still on the screen.
     *
     * @param entity The entity being checked.
     * @return A boolean of if the given entity is on the screen.
     */
    private boolean checkEntityNotOnScreen(Entity entity) {
        Vector2 renderPos = entity.getRelPos(this.pb.getInGamePos());
        return renderPos.x > Gdx.graphics.getWidth() + 30 || renderPos.x + entity.getSize().x < -30 || renderPos.y + entity.getSize().y < -100;
    }

    /**
     * Check if a power up is in the lane bounds.
     *
     * @return A boolean of if the power up is in the lane.
     */
    private boolean checkPowerUpNotInEdges() {
        return this.currentPower.getInGamePos().x < this.boat.getLaneBounds().a + 5 || this.currentPower.getInGamePos().x + this.currentPower.getSize().x > this.boat.getLaneBounds().b - 5;
    }

    /**
     * Perform all the updates required for the current power up.
     *
     * @param deltaTime The time since the previous frame.
     */
    private void updatePowerUp(float deltaTime) {
        if (this.boat.getHitbox().checkCollision(this.currentPower.getHitbox())) {
            this.currentPower.applyEffect(this.boat);
            this.currentPower = null;
        } else if (this.checkEntityNotOnScreen(this.currentPower)) {
            this.currentPower = null;
        } else if (this.checkPowerUpNotInEdges()) {
            this.currentPower.bounceEdge(deltaTime);
        } else {
            this.currentPower.move(deltaTime);
            this.currentPower.update(deltaTime);
        }
    }

    /**
     * Render the edges of the lane.
     *
     * @param shapeRenderer The renderer used to display the lines.
     */
    private void renderBounds(ShapeRenderer shapeRenderer) {
        Tuple<Float, Float> bounds = this.boat.getLaneBounds();
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.RED);
        shapeRenderer.rect(bounds.a, 0, bounds.b - bounds.a, Gdx.graphics.getHeight());
        shapeRenderer.end();

    }

    /**
     * Update the current round.
     *
     * @param newRound           The round to be set to.
     * @param obstacleMultiplier The multiplier for the number of obstacles.
     * @return The new number of max obstacles.
     */
    public int updateRound(int newRound, float obstacleMultiplier) {
        this.round = newRound;

        int offset = 3;

        this.maxObstacles = (int) ((offset + newRound) * obstacleMultiplier);
        return this.maxObstacles;
    }

    /**
     * Create a new random obstacle at a random position in the lane.
     * @return An obstacle of random type and at random position.
     */
    private Obstacle spawnObstacle() {
        Vector2 spawnPos;    // position the obstacle will spawn at
        Vector2 dir;        // direction the obstacle will start travelling in
        ObstacleType obs;    // type of the obstacle that will be spawned
        int obstacleChoice;    // index of the type of obstacle that will be spawned
        int side;            // the side of the screen the obstacle will spawn at
        ThreadLocalRandom rand = ThreadLocalRandom.current();

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

        obs = ObstacleType.values()[obstacleChoice];    // Select the obstacle type from the list
        if (obs.getMover() == MovementCharacteristics.STATIC) {    // Static moving obstacles can only be spawned from the top
            side = 0;
        } else {
            side = rand.nextInt(3);        // Any other obstacles can spawn on any other side
        }

        // Creating a spawning position along the chosen edge
        spawnPos = this.createObstacleOnSide(side);

        dir = spawnPos.cpy().sub(new Vector2(    // Create a vector pointing from the spawn pos to a random point on the screen
                rand.nextFloat() * Gdx.graphics.getWidth(),
                rand.nextFloat() * Gdx.graphics.getHeight())
        );
        dir.limit(obs.getSpeed());    // Limit the vector to the max speed of the obstacle

        return new Obstacle(obs, spawnPos, dir);    // Return the new obstacle
    }

    /**
     * Randomly create a new power up. Return null if it doesn't spawn one.
     * @return Null if no obstacle spawned, otherwise an obstacle of random type and in a random place in the lane.
     */
    private PowerUp spawnPowerUp() {

        float random = ThreadLocalRandom.current().nextFloat();

        if (random <= this.powerChance) {
            int side;
            Vector2 spawnPos;
            Vector2 dir;
            PowerUpType randomType = PowerUpType.chooseRandomType();
            if (randomType.getMover() == MovementCharacteristics.STATIC) {
                side = 0;
            } else {
                side = ThreadLocalRandom.current().nextInt(3);
            }
            spawnPos = this.createObstacleOnSide(side);
            dir = spawnPos.cpy().sub(new Vector2(    // Create a vector pointing from the spawn pos to a random point on the screen
                    ThreadLocalRandom.current().nextFloat() * Gdx.graphics.getWidth(),
                    ThreadLocalRandom.current().nextFloat() * Gdx.graphics.getHeight())
            );
            dir.limit(randomType.getSpeed());    // Limit the vector to the max speed of the obstacle
            PowerUp power = new PowerUp(randomType, spawnPos, dir);
            power.loadTexture();
            return power;
        } else {
            return null;
        }
    }

    /**
     * Choose a random location on a side to spawn an obstacle.
     * @param side The side to choose from.
     * @return A vector2d of the position to spawn the obstacle on a side.
     */
    private Vector2 createObstacleOnSide(int side) {
        Vector2 spawnPos;
        if (side == 0) {
            spawnPos = new Vector2(    // If spawning along the top edge, pick a random x coord and a random y within the bounds of the screen, will be translated off screen
                    0,
                    (ThreadLocalRandom.current().nextFloat()) * Gdx.graphics.getHeight()
            );
        } else {
            spawnPos = new Vector2(    // For the edge spawning, spawn slightly off screen but not enough to be deleted, and in the top 2/3rds of the side
                    0,
                    ThreadLocalRandom.current().nextFloat() * Gdx.graphics.getHeight() * 2 / 3
            );
        }
        spawnPos.x = (float) ThreadLocalRandom.current().nextDouble(this.boat.getLaneBounds().a, this.boat.getLaneBounds().b);
        spawnPos.y = 2 * Gdx.graphics.getHeight() - spawnPos.y + this.pb.getInGamePos().y;        // Translate 2 screens up and relative to the player

        return spawnPos;
    }

    /**
     * Move the boat to the start and reset the obstacles and power up.
     */
    public void moveBoatToStart() {
        this.boat.moveToStart();
        this.currentPower = null;
        this.obstacles = new ArrayList<>();
    }

    /**
     * Check if the boat has crossed the finish line.
     * @param finishLine The position of the finish line.
     * @param startTime The time at which the boat started the round.
     * @return A boolean of if the boat has crossed the line.
     */
    public boolean checkBoatFinished(int finishLine, long startTime) {
        return this.boat.checkFinished(finishLine, startTime);
    }

    /**
     * Set the boats finish time.
     * @param finishTime The time to set the finish time to.
     */
    public void setBoatFinishTime(long finishTime) {
        this.boat.setFinishTime(finishTime);
    }

    /**
     * Get the boat that is in the lane.
     * @return A boat of type {@link PlayerBoat} or {@link CPUBoat}
     */
    public Boat getBoat() {
        return this.boat;
    }

    /**
     * Set the instance of the player boat.
     * @param pb The boat to set as the player boat.
     */
    public void setPb(PlayerBoat pb) {
        this.pb = pb;
    }

    /**
     * Get the boat in the lane's render position.
     * @return A vector2d of the boat's in-game position.
     */
    public Vector2 getBoatGamePos() {
        return this.boat.getInGamePos();
    }

    /**
     * Get the time at which the boat finished as a long.
     * @return A long of the time at which the boat finished.
     */
    public long getBoatFinishTimeLong() {
        return this.boat.getFinishTimeLong();
    }

    /**
     * Get the total time of all the rounds added together the boat has done.
     * @return A long of all the rounds added up the boat has done.
     */
    public long getBoatTotalTimeLong() {
        return this.boat.getTotalTimeLong();
    }

    /**
     * Dispose of the boat texture, current power up texture and the obstacle textures.
     * @see Boat
     * @see PowerUp
     * @see Obstacle
     */
    public void dispose() {
        for (Obstacle obstacle : obstacles) {
            obstacle.dispose();
        }
        this.boat.dispose();
    }

    /**
     * Load the boats texture.
     */
    public void loadTexture() {
        this.boat.loadTexture();
        for(Obstacle obstacle : obstacles){
            obstacle.loadTexture();
        }
        if(currentPower!=null)currentPower.loadTexture();
    }
}
