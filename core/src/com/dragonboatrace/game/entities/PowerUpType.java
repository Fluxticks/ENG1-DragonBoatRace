package com.dragonboatrace.game.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.dragonboatrace.game.MovementCharacteristics;

import java.util.concurrent.ThreadLocalRandom;

public enum PowerUpType {

    HEALTH(5,0, 2, new Vector2(40,40), MovementCharacteristics.CONSTANT, "PowerUps/health.png"),
    SPEED(20,0, 4, new Vector2(30,30), MovementCharacteristics.WANDER, "PowerUps/speed.png"),
    AGILITY(3,0, 3, new Vector2(25,25), MovementCharacteristics.WANDER, "PowerUps/agility.png");

    Vector2 size;
    MovementCharacteristics mover;
    float weight;
    float speed;
    float effect;
    Texture image;

    PowerUpType(float effect, float weight, float speed, Vector2 size, MovementCharacteristics mover, String imagePath){
        this.effect = effect;
        this.weight = weight;
        this.speed = speed;
        this.size = size;
        this.mover = mover;
        this.image = new Texture(Gdx.files.local(imagePath));
    }


    public static PowerUpType chooseRandomType(){
        int length = PowerUpType.values().length;
        int random = ThreadLocalRandom.current().nextInt(length);
        return PowerUpType.values()[random];
    }


    public Texture getImage() {
        return image;
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
