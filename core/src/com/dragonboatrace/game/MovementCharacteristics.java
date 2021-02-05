package com.dragonboatrace.game;

import com.badlogic.gdx.math.Vector2;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Defines how an obstacle can move.
 *
 * @author Jacob Turner
 */
public enum MovementCharacteristics {
    // Angle delta, Wander Delay Range
    STATIC(new Tuple<>(0, 0), new Tuple<>((float) 0, (float) 0)),
    CONSTANT(new Tuple<>(0, 0), new Tuple<>((float) 0, (float) 0)),
    WANDER(new Tuple<>(-45, 45), new Tuple<>((float) 0.5, (float) 2));

    /**
     * The angle range in which the obstacle can turn its velocity.
     */
    Tuple<Integer, Integer> angleDelta;
    /**
     * The range of amount of time a obstacle can wait before changing its direction.
     */
    Tuple<Float, Float> wanderDelayRange;
    /**
     * The time since last change.
     */
    float wanderDelay;

    MovementCharacteristics(Tuple<Integer, Integer> angleDelta, Tuple<Float, Float> wanderDelayRange) {
        this.angleDelta = angleDelta;
        this.wanderDelayRange = wanderDelayRange;
        this.wanderDelay = 0;
    }

    /**
     * Perform the change in velocity given the angle and delay.
     *
     * @param deltaTime The time since the previous frame.
     * @param vel       The velocity to modify.
     */
    public void updateVel(float deltaTime, Vector2 vel) {
        this.wanderDelay -= deltaTime;
        if (this.wanderDelay <= 0) {
            this.wanderDelay = ThreadLocalRandom.current().nextFloat() * (wanderDelayRange.b - wanderDelayRange.a) + wanderDelayRange.a;
            vel.rotate(ThreadLocalRandom.current().nextInt(angleDelta.a, angleDelta.b + 1));
        }
    }
}