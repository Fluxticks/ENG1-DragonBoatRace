package com.dragonboatrace.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.dragonboatrace.game.entities.*;

import java.util.ArrayList;
import java.util.ListIterator;
import java.util.concurrent.ThreadLocalRandom;

public class Lane {

    private ArrayList<Obstacle> obstacles;

    private PowerUp currentPower;

    private final float powerChance = 0.05f;

    private int round;

    private final Boat boat;

    private int maxObstacles;

    private PlayerBoat pb;

    public boolean isPlayerLane;

    public Lane(Boat boatInLane, PlayerBoat pb){
        this.boat = boatInLane;
        this.obstacles = new ArrayList<>();
        this.currentPower = null;
        this.pb = pb;
        this.isPlayerLane = (this.pb == this.boat);
    }

    public void update(float deltaTime){
        ListIterator<Obstacle> iter = obstacles.listIterator();
        while (iter.hasNext()) {
            Obstacle obstacle = iter.next();
            Vector2 renderPos = obstacle.getRelPos(this.pb.getInGamePos());
            if(this.checkEntityNotOnScreen(obstacle)){
                iter.remove();    // If the obstacles is off the screen (apart from the top) delete it
            }
            else {
                this.boat.checkForCollision(obstacle);
                obstacle.move(deltaTime);    // Run the obstacles mover
                obstacle.update(deltaTime);    // Update the position of the obstacle
            }
        }

        if(this.obstacles.size() < maxObstacles){
            this.obstacles.add(spawnObstacle());
        }

        if(this.currentPower != null){
            updatePowerUp(deltaTime);
        }else{
            this.currentPower = spawnPowerUp();
        }

        // Don't like this but not sure how best to do this without swapping to the structure in the old game.
        if(!this.isPlayerLane){
            ((CPUBoat)this.boat).decideMovement(obstacles);
        }

        this.boat.move(deltaTime);
        this.boat.update(deltaTime);
    }

    public void render(SpriteBatch batch){
        for(Obstacle obstacle : this.obstacles){
            obstacle.render(batch, this.pb.getInGamePos());
            //obstacle.renderHitBox(this.pb.getInGamePos());
        }
        if(this.currentPower!=null)this.currentPower.render(batch, this.pb.getInGamePos());
        this.boat.render(batch, this.pb.getInGamePos());
        //this.boat.renderHitBox(this.pb.getInGamePos());
        //renderBounds();
    }

    private boolean checkEntityNotOnScreen(Entity entity){
        Vector2 renderPos = entity.getRelPos(this.pb.getInGamePos());
        return renderPos.x > Gdx.graphics.getWidth() + 30 || renderPos.x + entity.getSize().x < -30 || renderPos.y + entity.getSize().y < -100;
        //return renderPos.x > this.boat.getLaneBounds().b + 30 || renderPos.x + entity.getSize().x < this.boat.getLaneBounds().a - 30 || renderPos.y + entity.getSize().y < -100;
    }

    private boolean checkPowerUpNotInEdges(){
        return this.currentPower.getInGamePos().x < this.boat.getLaneBounds().a + 5 || this.currentPower.getInGamePos().x + this.currentPower.getSize().x > this.boat.getLaneBounds().b - 5;
    }

    private void updatePowerUp(float deltaTime){
        if(this.boat.getHitbox().checkCollision(this.currentPower.getHitbox())) {
            this.currentPower.applyEffect(this.boat);
            this.currentPower = null;
        }else if(this.checkEntityNotOnScreen(this.currentPower)){
            this.currentPower = null;
        }else if(this.checkPowerUpNotInEdges()){
            this.currentPower.bounceEdge(deltaTime);
        }else{
            this.currentPower.move(deltaTime);
            this.currentPower.update(deltaTime);
        }
    }

    private void renderBounds(ShapeRenderer renderer){
        Tuple<Float,Float> bounds = this.boat.getLaneBounds();
        renderer.begin(ShapeRenderer.ShapeType.Line);
        renderer.setColor(Color.RED);
        renderer.rect(bounds.a, 0, bounds.b-bounds.a, Gdx.graphics.getHeight());
        renderer.end();
    }

    public int updateRound(int newRound, float obstacleMultiplier){
        this.round = newRound;

        int offset = 3;

        this.maxObstacles  = (int)((offset+newRound) * obstacleMultiplier);
        return this.maxObstacles;

        /*
        switch (this.round) {    // The max number of obstacles changes from round to round
            case 0:
                this.maxObstacles = (int)(3 * obstacleMultiplier);
                break;
            case 1:
                this.maxObstacles = (int)(4 * obstacleMultiplier);
                break;
            case 2:
                this.maxObstacles = (int)(5 * obstacleMultiplier);
                break;
            case 3:
                this.maxObstacles = (int)(6 * obstacleMultiplier);
                break;
            default:
                this.maxObstacles = 0;
                break;
        }*/
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
        spawnPos = this.createObstacleOnSide(side);

        dir = spawnPos.cpy().sub(new Vector2(    // Create a vector pointing from the spawn pos to a random point on the screen
                rand.nextFloat() * Gdx.graphics.getWidth(),
                rand.nextFloat() * Gdx.graphics.getHeight())
        );
        dir.limit(obs.getSpeed());    // Limit the vector to the max speed of the obstacle

        return new Obstacle(obs, spawnPos, dir);    // Return the new obstacle
    }

    private PowerUp spawnPowerUp(){

        float random = ThreadLocalRandom.current().nextFloat();

        if(random <= this.powerChance){
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
            return new PowerUp(randomType, spawnPos, dir);
        }else{
            return null;
        }
    }

    private Vector2 createObstacleOnSide(int side){
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
        spawnPos.x = (float)ThreadLocalRandom.current().nextDouble(this.boat.getLaneBounds().a, this.boat.getLaneBounds().b);
        spawnPos.y = 2 * Gdx.graphics.getHeight() - spawnPos.y + this.pb.getInGamePos().y;        // Translate 2 screens up and relative to the player

        return spawnPos;
    }

    public void moveBoatToStart(){
        this.boat.moveToStart();
        this.currentPower = null;
        this.obstacles = new ArrayList<>();
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
