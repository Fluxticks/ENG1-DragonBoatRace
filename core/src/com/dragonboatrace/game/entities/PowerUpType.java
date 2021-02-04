package com.dragonboatrace.game.entities;

import com.badlogic.gdx.math.Vector2;
import com.dragonboatrace.game.MovementCharacteristics;

import java.util.concurrent.ThreadLocalRandom;

public enum PowerUpType {

    HEALTH(5, 0, 0, new Vector2(40, 40), MovementCharacteristics.STATIC, "PowerUps/health.png"),
    SPEED(20, 0, 4, new Vector2(30, 30), MovementCharacteristics.CONSTANT, "PowerUps/speed.png"),
    STAMINA(100, 0, 4, new Vector2(35, 35), MovementCharacteristics.CONSTANT, "PowerUps/stamina.png"),
    TIMER(500, 0, 10, new Vector2(20, 20), MovementCharacteristics.CONSTANT, "PowerUps/timer.png"),
    NOCOLLIDE(5, 0, 10, new Vector2(20, 20), MovementCharacteristics.CONSTANT, "PowerUps/nocollide.png"),
    AGILITY(3, 0, 3, new Vector2(25, 25), MovementCharacteristics.CONSTANT, "PowerUps/agility.png"),
    TESTING(3, 0, 3, new Vector2(25, 25), MovementCharacteristics.CONSTANT, "Testing");


    Vector2 size;
    MovementCharacteristics mover;
    float weight;
    float speed;
    float effect;
    String imageSrc;

    PowerUpType(float effect, float weight, float speed, Vector2 size, MovementCharacteristics mover, String imagePath) {
        this.effect = effect;
        this.weight = weight;
        this.speed = speed;
        this.size = size;
        this.mover = mover;
        this.imageSrc = imagePath;
    }


    public static PowerUpType chooseRandomType() {
        int length = PowerUpType.values().length - 1;
        int random = ThreadLocalRandom.current().nextInt(length);
        return PowerUpType.values()[random];
    }


    public String getImageSrc() {
        return imageSrc;
    }

    public Vector2 getSize() {
        return size;
    }

    public MovementCharacteristics getMover() {
        return mover;
    }

    public float getWeight() {
        return weight;
    }

    public float getSpeed() {
        return speed;
    }

    public float getEffect() {
        return effect;
    }
}
