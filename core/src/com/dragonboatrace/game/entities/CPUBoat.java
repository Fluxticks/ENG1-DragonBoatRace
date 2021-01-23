package com.dragonboatrace.game.entities;

import com.badlogic.gdx.math.Vector2;
import com.dragonboatrace.game.Tuple;

import java.util.ArrayList;

public class CPUBoat extends Boat {

    int difficulty;
    Vector2 startPos;
    private int dir;

    public CPUBoat(BoatType boatType, Vector2 pos, int difficulty, Tuple<Float, Float> laneBounds) {
        super(boatType, pos, laneBounds);
        this.difficulty = difficulty;
        this.startPos = pos;
        this.dir = 0;
    }

    @Override
    public void move(float deltaTime) {

        //basically they just move in a straight line until they break 
        if (this.vel.y < this.currentMaxSpeed) {
            this.vel.add(0, ((this.boatType.getAcceleration() / 100) / (deltaTime * 60)));
        } else if (this.vel.y > this.currentMaxSpeed) {
            this.vel.add(0, -((this.boatType.getAcceleration() / 100) / (deltaTime * 60)));
        }

        this.vel.add((dir * this.boatType.getHandling() / (deltaTime * 60)),0);

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
    public void decideMovement(ArrayList<Obstacle> obstacles) {

        // The amount on each side to check for obstacles
        int paddingX = 5;
        int paddingY = 75;
        boolean obstacleInZone = false;
        float thisCenter = this.size.x/2f + this.inGamePos.x;

        for(Obstacle obstacle : obstacles){
            if(this.inGamePos.x + this.size.x + paddingX > obstacle.inGamePos.x && this.inGamePos.x - paddingX < obstacle.inGamePos.x + obstacle.size.x && this.inGamePos.y < obstacle.inGamePos.y + obstacle.size.y && this.inGamePos.y + this.size.y + paddingY > obstacle.inGamePos.y){
                float obstacleCenter = obstacle.size.x/2f + obstacle.inGamePos.x;
                obstacleInZone = true;
                dir = decideDirection(thisCenter, obstacleCenter);
                break;
            }
        }

        if(!obstacleInZone){
            float laneCenter = (laneBounds.b + laneBounds.a)/2f;
            if(thisCenter + 100 < laneCenter){
                this.dir = 1;
            }else if(thisCenter - 100 > laneCenter){
                this.dir = -1;
            }else{
                this.dir = 0;
            }
        }
    }

    private int decideDirection(float thisCenter, float obstacleCenter){

        // Try to stay in the lane.
        if(this.inGamePos.x - 10 < this.laneBounds.a){
            return 1;
        }else if(this.inGamePos.x + this.size.x + 10 > this.laneBounds.b){
            return -1;
        }

        // If the obstacle is to the right of our center move to the left.
        if(thisCenter < obstacleCenter){
            return -1;
        }else{
            return 1;
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

    //this is used for debugging, it tells you where they are on screen and where they are relative to the start line
    public String getCurrentPos() {
        return this.pos.toString() + " , " + this.inGamePos.toString();
    }

}
