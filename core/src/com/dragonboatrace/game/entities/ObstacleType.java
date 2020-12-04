package com.dragonboatrace.game.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.dragonboatrace.game.MovementCharacteristics;

public enum ObstacleType {
    //          ID,                Weight,       Size,                                                                      speed,        imageSrc,                           mover
    BUOY(      "buoy",         3,      new Vector2(50, 50),                                                 0,      "Obstacles/buoy.png",       MovementCharacteristics.STATIC),
    ROCK(      "rock",         4,      new Vector2(50, 50),                                                 0,      "Obstacles/rocks.png",      MovementCharacteristics.STATIC),
    BRANCH(    "branch",       1.5f,   new Vector2(50, 50),                                                 1,      "Obstacles/log.png",        MovementCharacteristics.CONSTANT),
    DUCK(      "duck",         2,      new Vector2(50, 50),                                                 2,      "Obstacles/duck.png",       MovementCharacteristics.WANDER),
    RUBBISH(   "rubbish" ,     1.5f,   new Vector2(50, 50),                                                 3,      "Obstacles/garbage.png",    MovementCharacteristics.STATIC),
    LONGBOI(   "longboi",      2,      new Vector2(50, 50),                                                 4,      "Obstacles/longboi.png",    MovementCharacteristics.STATIC),
    BOAT(      "boat",         5,      new Vector2(50, 50),                                                 5,      "Obstacles/shipwreck.png",  MovementCharacteristics.STATIC),
    FINISHLINE("finish",       0,      new Vector2(Gdx.graphics.getWidth(), Gdx.graphics.getHeight() * .185f), 0,      "Obstacles/finishline.png", MovementCharacteristics.STATIC);
    
    String ID;
    float weight;
    Vector2 size;
    float speed;
    Texture image;
    MovementCharacteristics mover;

    ObstacleType(String ID, float weight, Vector2 size, float speed, String imageSrc, MovementCharacteristics mover){
        this.ID = ID;
        this.weight = weight;
        this.size = size;
        this.speed = speed;
        this.image = new Texture(imageSrc);
        this.mover = mover;
    }

    public String getID(){
        return this.ID;
    }

    public float getWeight(){
        return this.weight;
    }

    public Vector2 getSize(){
        return this.size;
    }

    public float getSpeed() {
        return this.speed;
    }

    public Texture getImage(){
        return this.image;
    }

    public MovementCharacteristics getMover() {
        return this.mover;
    }
}