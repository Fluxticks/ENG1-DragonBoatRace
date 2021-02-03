package com.dragonboatrace.game.entities;

import com.badlogic.gdx.math.Vector2;
import com.dragonboatrace.game.Tuple;

public class CPUBoat extends Boat {

    int difficulty;
    Vector2 startPos;

    public CPUBoat(BoatType boatType, Vector2 pos, int difficulty, Tuple<Float, Float> laneBounds) {
        super(boatType, pos, laneBounds);
        this.difficulty = difficulty;
        this.startPos = pos;
    }

    @Override
    public void move(float deltaTime) {

        //basically they just move in a straight line until they break 
        if (this.vel.y < this.currentMaxSpeed) {
            this.vel.add(0, ((this.boatType.getAcceleration() / 100) / (deltaTime * 60)));
        } else if (this.vel.y > this.currentMaxSpeed) {
            this.vel.add(0, -((this.boatType.getAcceleration() / 100) / (deltaTime * 60)));
        }

        //this part stops them if they break
        if (this.currentHealth <= 0) {
            this.vel = new Vector2();
        }

        //this part adds on a penalty if they pass the barrier
        if (this.penaltyResetDelay <= 0) {
            if (this.pos.x < this.laneBounds.a || this.laneBounds.b < this.pos.x) { // Boat has left the lane
                this.timePenalties += 5000;
                this.penaltyResetDelay = 5;
            }
        } else {
            this.penaltyResetDelay -= deltaTime;
        }
    }

    @Override
    public void collide(Obstacle o) {

    }

    //this method isnt used yet bc it sucks, I was going to use it to decide where to move the cpus intelligently but that's too hard so might just make them move randomly 
    //please ignore this method
    public void decideMovement(float deltaTime, Obstacle[] obstacles) {

        Obstacle closestObstacle = obstacles[0];

        for (Obstacle i : obstacles) {
            Vector2 obstaclePos = i.getRelPos(pos);
            if (obstaclePos.y > 0) {
                if (obstaclePos.y < closestObstacle.getRelPos(pos).y) {
                    closestObstacle = i;
                }
            }
        }
    }

    @Override
    public void update(float deltaTime) {
        float deltaX = this.vel.x * this.dampening;
        float deltaY = this.vel.y * this.dampening;

        if (deltaX != 0) {
            this.pos.add(deltaX, 0);
            this.inGamePos.add(deltaX, 0);
            this.vel.add(-deltaX, 0);
        }
        if (deltaY != 0) {
            this.pos.add(0, deltaY);
            this.inGamePos.add(0, deltaY);
            this.distanceTravelled += deltaY;
        }
    }

    public int getDifficulty(){
        return this.difficulty;
    }

    //this is used for debugging, it tells you where they are on screen and where they are relative to the start line
    public String getCurrentPos() {
        return this.pos.toString() + " , " + this.inGamePos.toString();
    }

}
