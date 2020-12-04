package com.dragonboatrace.game;

import com.badlogic.gdx.math.Vector2;

import java.util.concurrent.ThreadLocalRandom;

public enum MovementCharacteristics {
    STATIC("static", new Tuple<>(0, 0), new Tuple<>((float) 0, (float) 0)),
    CONSTANT("constant", new Tuple<>(0, 0), new Tuple<>((float) 0, (float) 0)),
    WANDER("wander", new Tuple<>(-45, 45), new Tuple<>((float) 0.5, (float) 2));

    String ID;
    Tuple<Integer, Integer> angleDelta;
    Tuple<Float, Float> wanderDelayRange;
    float wanderDelay;

    MovementCharacteristics(String ID, Tuple<Integer, Integer> angleDelta, Tuple<Float, Float> wanderDelayRange) {
        this.ID = ID;
        this.angleDelta = angleDelta;
        this.wanderDelayRange = wanderDelayRange;
        this.wanderDelay = 0;
    }

    public void updateVel(float deltaTime, Vector2 vel) {
        this.wanderDelay -= deltaTime;
        if (this.wanderDelay <= 0) {
            this.wanderDelay = ThreadLocalRandom.current().nextFloat() * (wanderDelayRange.b - wanderDelayRange.a) + wanderDelayRange.a;
            vel.rotate(ThreadLocalRandom.current().nextInt(angleDelta.a, angleDelta.b + 1));
        }
    }

    public String getID() {
        return this.ID;
    }
}