package com.dragonboatrace.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.dragonboatrace.game.entities.*;

import java.util.ArrayList;
import java.util.ListIterator;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class Lane {

    private ArrayList<Obstacle> obstacles;

    private int round;

    private Boat boat;

    private int maxObstacles;

    private PlayerBoat pb;

    public boolean isPlayerLane;

    public Lane(Boat boatInLane, PlayerBoat pb){
        this.boat = boatInLane;
        this.obstacles = new ArrayList<>();
        this.pb = pb;
        this.isPlayerLane = (this.pb == this.boat);
    }

    public void update(float deltaTime){
        ListIterator<Obstacle> iter = obstacles.listIterator();
        while (iter.hasNext()) {
            Obstacle obstacle = iter.next();
            Vector2 renderPos = obstacle.getRelPos(this.pb.getInGamePos());
            if (renderPos.x > Gdx.graphics.getWidth() + 30 ||
                    renderPos.x + obstacle.getSize().x < -30 ||
                    // renderPos.y > Gdx.graphics.getHeight() +10 ||
                    renderPos.y + obstacle.getSize().y < -100) {
                iter.remove();    // If the obstacles is off the screen (apart from the top) delete it
            }
            else {
                this.pb.checkCollision(obstacle);
                obstacle.move(deltaTime);    // Run the obstacles mover
                obstacle.update(deltaTime);    // Update the position of the obstacle
            }
        }

        if(this.obstacles.size() < maxObstacles){
            this.obstacles.add(spawnObstacle());
        }
        System.out.println("Obstacles size: " + obstacles.size());

        this.boat.move(deltaTime);
        this.boat.update(deltaTime);
    }

    public void render(SpriteBatch batch){
        for(Obstacle obstacle : this.obstacles){
            obstacle.render(batch, this.pb.getInGamePos());
        }
        this.boat.render(batch, this.pb.getInGamePos());
    }

    public void updateRound(int newRound){
        this.round= newRound;

        switch (this.round) {    // The max number of obstacles changes from round to round
            case 0:
                this.maxObstacles = 5;
                break;
            case 1:
                this.maxObstacles = 8;
                break;
            case 2:
                this.maxObstacles = 11;
                break;
            case 3:
                this.maxObstacles = 14;
                break;
            default:
                this.maxObstacles = 0;
                break;
        }
    }

    public void updateMaxObstacles(int newObstacleCount){
        this.maxObstacles = newObstacleCount;
    }

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
        if (side == 0) {
            spawnPos = new Vector2(    // If spawning along the top edge, pick a random x coord and a random y within the bounds of the screen, will be translated off screen
                    0,
                    (rand.nextFloat()) * Gdx.graphics.getHeight()
            );
        } else {
            spawnPos = new Vector2(    // For the edge spawning, spawn slightly off screen but not enough to be deleted, and in the top 2/3rds of the side
                    0,
                    rand.nextFloat() * Gdx.graphics.getHeight() * 2 / 3
            );
        }
        spawnPos.x = (float)rand.nextDouble(this.boat.getLaneBounds().a, this.boat.getLaneBounds().b);
        spawnPos.y = 2 * Gdx.graphics.getHeight() - spawnPos.y + this.pb.getInGamePos().y;        // Translate 2 screens up and relative to the player
        //spawnPos.y = 2 * Gdx.graphics.getHeight() - spawnPos.y;        // Translate 2 screens up and relative to the player

        System.out.println("SpawnPos: (" + spawnPos.x + "," + spawnPos.y + ")");

        dir = spawnPos.cpy().sub(new Vector2(    // Create a vector pointing from the spawn pos to a random point on the screen
                rand.nextFloat() * Gdx.graphics.getWidth(),
                rand.nextFloat() * Gdx.graphics.getHeight())
        );
        dir.limit(obs.getSpeed());    // Limit the vector to the max speed of the obstacle

        return new Obstacle(obs, spawnPos, dir);    // Return the new obstacle
    }

    public void moveBoatToStart(){
        this.boat.moveToStart();
    }

    public boolean checkBoatFinished(int finishTime, long startTime){
        return this.boat.checkFinished(finishTime,startTime);
    }

    public void setBoatFinishTime(long finishTime){
        this.boat.setFinishTime(finishTime);
    }

    public Vector2 getBoatGamePos(){
        return this.boat.getInGamePos();
    }

    public long getBoatFinishTimeLong() {
        return this.boat.getFinishTimeLong();
    }

    public long getBoatTotalTimeLong() {
        return this.boat.getTotalTimeLong();
    }

    public void dispose(){
        for(Obstacle obstacle : obstacles){
            obstacle.dispose();
        }
        this.boat.dispose();
    }
}
