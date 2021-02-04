package com.dragonboatrace.game.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

public enum BoatType {
    //TYPE( ID,               speed,       acceleration,             maxHealth,           weight,        strength,           handling,          size,                          imageSrc)
    FAST("Fast Boat",     20,    15,             100,        1,      1,          1,          new Vector2(41, 131),  "Boats/FastBoat.PNG"),
    NORMAL("Normal Boat", 15,    10,             200,        2,      2,          2,          new Vector2(82, 113),  "Boats/NormalBoat.PNG"),
    HEAVY("Heavy Boat",   15,    5,              400,        3,      2,          1,          new Vector2(75, 120),  "Boats/HeavyBoat.PNG"),
    LIGHT("Light Boat",   15,    15,             100,        1,      1,          3,          new Vector2(62, 130),  "Boats/LightBoat.PNG"),
    AGILE("Agile Boat",   15,    10,             200,        2,      2,          3,          new Vector2(39, 133),  "Boats/AgileBoat.PNG"),
    STRONG("Strong Boat", 15,    10,             300,        3,      3,          1,          new Vector2(60, 129),  "Boats/StrongBoat.PNG"),
    TESTING("Testing Boat",15,10,300,1,2,2,new Vector2(50, 50),"Testing");;

    String ID, imageSrc;
    float speed, acceleration, maxHealth, weight, strength, handling;
    Vector2 size;

    BoatType(String ID, float speed, float acceleration, float maxHealth, float weight, float strength, float handling, Vector2 size, String imageSrc) {
        this.ID = ID;
        this.speed = speed;
        this.acceleration = acceleration;
        this.maxHealth = maxHealth;
        this.weight = weight;
        this.strength = strength;
        this.handling = handling;
        this.size = size;
        this.imageSrc = imageSrc;
    }

    public String getID() {return this.ID;}
    public float getSpeed() {return this.speed;}
    public float getAcceleration() {return this.acceleration;}
    public float getMaxHealth() {return this.maxHealth;}
    public float getWeight() {return this.weight;}
    public float getHandling() {return this.handling;}
    public Vector2 getSize() {return this.size;}
    public String getImageSrc() {return this.imageSrc;}
}