package com.dragonboatrace.game.entities;

import com.badlogic.gdx.math.Vector2;

/**
 * Boat Type enum for storing basic values of different boat types.
 *
 * @author Jacob Turner
 */
public enum BoatType {
    //TYPE( ID,               speed,       acceleration,             maxHealth,           weight,        strength,           handling,          size,                          imageSrc)
    FAST(20, 15, 100, 1, 1, 1, new Vector2(41, 131), "Boats/FastBoat.PNG"),
    NORMAL(15, 10, 200, 2, 2, 2, new Vector2(82, 113), "Boats/NormalBoat.PNG"),
    HEAVY(15, 5, 400, 3, 2, 1, new Vector2(75, 120), "Boats/HeavyBoat.PNG"),
    LIGHT(15, 15, 100, 1, 1, 3, new Vector2(62, 130), "Boats/LightBoat.PNG"),
    AGILE(15, 10, 200, 2, 2, 3, new Vector2(39, 133), "Boats/AgileBoat.PNG"),
    STRONG(15, 10, 300, 3, 3, 1, new Vector2(60, 129), "Boats/StrongBoat.PNG"),
    TESTING(15, 10, 300, 1, 2, 2, new Vector2(50, 50), "Testing");

    String imageSrc;
    float speed, acceleration, maxHealth, weight, strength, handling;
    Vector2 size;

    /**
     * Creates a Boat template to define a generic {@link Boat}
     *
     * @param speed        The y-velocity of the boat type.
     * @param acceleration The acceleration of the boat type.
     * @param maxHealth    The maximum health of the boat type.
     * @param weight       The weight of the boat type.
     * @param strength     The strength of the boat type.
     * @param handling     The handling of the boat type.
     * @param size         The size of the boat type.
     * @param imageSrc     The path of the image source used as the boat types texture.
     */
    BoatType(float speed, float acceleration, float maxHealth, float weight, float strength, float handling, Vector2 size, String imageSrc) {
        this.speed = speed;
        this.acceleration = acceleration;
        this.maxHealth = maxHealth;
        this.weight = weight;
        this.strength = strength;
        this.handling = handling;
        this.size = size;
        this.imageSrc = imageSrc;
    }

    /**
     * Get the speed of the boat type.
     *
     * @return Returns type float of the speed.
     */
    public float getSpeed() {
        return this.speed;
    }

    /**
     * Get the boat type's acceleration.
     *
     * @return A float of the boats acceleration rate.
     */
    public float getAcceleration() {
        return this.acceleration;
    }

    /**
     * Get the maximum health of the boat type.
     *
     * @return A float of the maximum health.
     */
    public float getMaxHealth() {
        return this.maxHealth;
    }

    /**
     * Get the weight of the boat type, used for collision.
     *
     * @return The weight of the boat type.
     */
    public float getWeight() {
        return this.weight;
    }

    /**
     * Get the boat type's handling.
     *
     * @return A float of the boat type's handling.
     */
    public float getHandling() {
        return this.handling;
    }

    /**
     * Get the dimensions of the boat type.
     *
     * @return A 2d vector of the size of the boat type.
     */
    public Vector2 getSize() {
        return this.size;
    }

    /**
     * Get the path for the texture of the boat type.
     *
     * @return A string for the path to the boat type texture.
     */
    public String getImageSrc() {
        return this.imageSrc;
    }
}