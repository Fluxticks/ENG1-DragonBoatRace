package com.dragonboatrace.game.entities.powerup;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.dragonboatrace.game.MovementCharacteristics;

public enum PowerUpType {

    HEALTH(0, 2, new Vector2(40,40), MovementCharacteristics.WANDER, ""),
    SPEED(0, 4, new Vector2(30,30), MovementCharacteristics.WANDER, ""),
    AGILITY(0, 3, new Vector2(25,25), MovementCharacteristics.WANDER, "");

    Vector2 size;
    MovementCharacteristics mover;
    float weight;
    float speed;
    Texture image;

    PowerUpType(float weight, float speed, Vector2 size, MovementCharacteristics mover, String imagePath){
        this.weight = weight;
        this.speed = speed;
        this.size = size;
        this.mover = mover;
        this.image = new Texture(Gdx.files.local(imagePath));
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
}
