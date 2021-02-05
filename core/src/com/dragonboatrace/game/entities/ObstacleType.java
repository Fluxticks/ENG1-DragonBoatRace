package com.dragonboatrace.game.entities;

import com.badlogic.gdx.math.Vector2;
import com.dragonboatrace.game.MovementCharacteristics;

/**
 * Represents an Obstacle Template.
 *
 * @author Jacob Turner
 */
public enum ObstacleType {
    // ID, Weight,  Size,  speed,  imageSrc,  mover
    BUOY(3, new Vector2(50, 50), 0, "Obstacles/buoy.png", MovementCharacteristics.STATIC),
    ROCK(4, new Vector2(50, 50), 0, "Obstacles/rocks.png", MovementCharacteristics.STATIC),
    BRANCH(1.5f, new Vector2(50, 50), 1, "Obstacles/log.png", MovementCharacteristics.CONSTANT),
    DUCK(2, new Vector2(50, 50), 2, "Obstacles/duck.png", MovementCharacteristics.WANDER),
    RUBBISH(1.5f, new Vector2(50, 50), 3, "Obstacles/garbage.png", MovementCharacteristics.STATIC),
    LONGBOI(2, new Vector2(50, 50), 4, "Obstacles/longboi.png", MovementCharacteristics.STATIC),
    BOAT(5, new Vector2(50, 50), 5, "Obstacles/shipwreck.png", MovementCharacteristics.STATIC),
    FINISHLINE(0, new Vector2(50, 50), 1, "Obstacles/finishline.png", MovementCharacteristics.STATIC),
    TESTING(0, new Vector2(50, 50), 0, "Testing", MovementCharacteristics.STATIC);

    /**
     * The weight of the obstacle type.
     */
    float weight;
    /**
     * The size of the obstacle.
     */
    Vector2 size;
    /**
     * The y-velocity of the obstacle.
     */
    float speed;
    /**
     * The path of the image used for the obstacle texture.
     */
    String imageSrc;
    /**
     * How the obstacle moves.
     */
    MovementCharacteristics mover;

    /**
     * Creates a template obstacle with given attributes.
     *
     * @param weight   The weight of the obstacle type.
     * @param size     The size of the obstacle type.
     * @param speed    The y-velocity of the obstacle type.
     * @param imageSrc The path of the image used as the texture.
     * @param mover    The movement characteristic that defines how the obstacle moves.
     */
    ObstacleType(float weight, Vector2 size, float speed, String imageSrc, MovementCharacteristics mover) {
        this.weight = weight;
        this.size = size;
        this.speed = speed;
        this.imageSrc = imageSrc;
        this.mover = mover;
    }

    /**
     * Get the weight of the obstacle type.
     *
     * @return A float of the weight of the obstacle type.
     */
    public float getWeight() {
        return this.weight;
    }

    /**
     * Get the size of the obstacle type.
     *
     * @return A vector2d of the size of the obstacle type.
     */
    public Vector2 getSize() {
        return this.size;
    }

    /**
     * Get the y-velocity of the obstacle type.
     *
     * @return A float of the obstacle type's y-velocity.
     */
    public float getSpeed() {
        return this.speed;
    }

    /**
     * Get the path to the image of the texture.
     *
     * @return A string of the image's path.
     */
    public String getImageSrc() {
        return this.imageSrc;
    }

    /**
     * Get the type of movement the obstacle type has.
     *
     * @return The MovementCharacteristic of the obstacle type.
     */
    public MovementCharacteristics getMover() {
        return this.mover;
    }
}