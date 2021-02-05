package com.dragonboatrace.game.entities;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.JsonValue;
import com.dragonboatrace.game.Tuple;

import java.util.ArrayList;

/**
 * A boat controlled by the computer.
 *
 * @author Jacob Turner
 */
public class CPUBoat extends Boat {

    /**
     * The current direction the boat is moving in horizontally.
     */
    private int dir;
    /**
     * The multiplier that specifies how big the area being checked for obstacles is.
     */
    private final float areaMulti = 0.5f;
    /**
     * The area in which obstacles are checked for.
     */
    private final EntityHitbox areaChecker;

    /**
     * Creates a boat using a specific boat type and position to be controlled by the computer.
     *
     * @param boatType   The type of boat to be used as a template for statistics.
     * @param pos        The starting position of the boat.
     * @param laneBounds The x coordinates of the lane the boat is in.
     */
    public CPUBoat(BoatType boatType, Vector2 pos, Tuple<Float, Float> laneBounds) {
        super(boatType, pos, laneBounds);
        this.startPos = pos;
        this.dir = 0;
        this.areaChecker = new EntityHitbox(new Vector2(this.inGamePos.x - this.size.x * (this.areaMulti / 2f), this.inGamePos.y), new Vector2(this.size.x + this.size.x * this.areaMulti, this.size.y));
    }

    /**
     * Creates a boat controlled by the computer from a save file.
     *
     * @param jsonString The json string representing the computer boat from a save file.
     */
    public CPUBoat(JsonValue jsonString) {
        super(jsonString);
        startPos = new Vector2(jsonString.get("pos").getFloat("x"), jsonString.get("pos").getFloat("y"));
        this.dir = 0;
        this.areaChecker = new EntityHitbox(new Vector2(this.inGamePos.x - this.size.x * (this.areaMulti / 2f), this.inGamePos.y), new Vector2(this.size.x + this.size.x * this.areaMulti, this.size.y));

    }

    /**
     * Performs the movement of the boat given the time since the previous frame.
     *
     * @param deltaTime The time since the previous frame.
     */
    @Override
    public void move(float deltaTime) {

        //basically they just move in a straight line until they break 
        if (this.vel.y < this.currentMaxSpeed) {
            this.vel.add(0, ((this.boatType.getAcceleration() / 100) / (deltaTime * 60)));
        } else if (this.vel.y > this.currentMaxSpeed) {
            this.vel.add(0, -((this.boatType.getAcceleration() / 100) / (deltaTime * 60)));
        }

        this.vel.add((dir * this.boatType.getHandling() / (deltaTime * 60)), 0);

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

    /**
     * Decides which direction to move (left/right) given the position of the obstacles around the boat.
     *
     * @param obstacles A list of the obstacles in the boat's lane.
     */
    public void decideMovement(ArrayList<Obstacle> obstacles) {

        boolean obstacleInZone = false;
        float thisCenter = this.size.x / 2f + this.inGamePos.x;

        // Find an obstacle in the area being checked.
        for (Obstacle obstacle : obstacles) {
            if (this.areaChecker.checkCollision(obstacle.getHitbox())) {
                float obstacleCenter = obstacle.size.x / 2f + obstacle.inGamePos.x;
                obstacleInZone = true;
                this.dir = decideDirection(thisCenter, obstacleCenter);
                break;
            }
        }

        // If there was no obstacle found try and stay in the middle of the lane
        if (!obstacleInZone) {
            float laneCenter = (laneBounds.b + laneBounds.a) / 2f;

            // Find if the boat is more to the left or right of the lane and set
            // the direction to move towards the center.
            // 1 moves right, -1 moves left, 0 goes forward.
            if (thisCenter + this.size.x < laneCenter) {
                this.dir = 1;
            } else if (thisCenter - this.size.x > laneCenter) {
                this.dir = -1;
            } else {
                this.dir = 0;
            }
        }
    }

    /**
     * Decides the direction to move based on the position of an obstacle.
     *
     * @param thisCenter     The x coordinate of the center of the boat.
     * @param obstacleCenter The x coordinate of the center of the obstacle.
     * @return -1 or 1 for a given direction. -1 for moving left, 1 for moving right.
     */
    private int decideDirection(float thisCenter, float obstacleCenter) {

        // Try to stay in the lane.
        if (this.inGamePos.x - 10 < this.laneBounds.a) {
            return 1;
        } else if (this.inGamePos.x + this.size.x + 10 > this.laneBounds.b) {
            return -1;
        }

        // If the obstacle is to the right of our center move to the left.
        if (thisCenter < obstacleCenter) {
            return -1;
        } else {
            return 1;
        }
    }

    /**
     * Determines if the given object is an identical {@link CPUBoat}
     *
     * @param obj The object to be compared to.
     * @return True or False if the given object has the same values.
     */
    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    /**
     * Updates the position, velocity and distance travelled for the boat given the time passed since the previous frame.
     *
     * @param deltaTime The time passed since the previous frame.
     */
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
        this.areaChecker.movePosition(new Vector2(deltaX, deltaY));
        this.hitbox.setToPosition(this.inGamePos);
    }

}
