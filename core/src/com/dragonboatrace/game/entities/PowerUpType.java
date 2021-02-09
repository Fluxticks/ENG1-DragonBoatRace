package com.dragonboatrace.game.entities;

import com.badlogic.gdx.math.Vector2;
import com.dragonboatrace.game.MovementCharacteristics;

import java.util.concurrent.ThreadLocalRandom;

// THIS WHOLE CLASS IS NEW
/**
 * Represents a generic power up template.
 *
 * @author Benji Garment
 */
public enum PowerUpType {

    // Health to add, weight, speed of obstacle, size of obstacle, MovementCharacteristic, Path to image
    HEALTH(5, 0, 0, new Vector2(40, 40), MovementCharacteristics.STATIC, "PowerUps/health.png"),
    // Speed to add, weight, speed of obstacle, size of obstacle, MovementCharacteristic, Path to image
    SPEED(20, 0, 4, new Vector2(30, 30), MovementCharacteristics.CONSTANT, "PowerUps/speed.png"),
    // Stamina to add, weight, speed of obstacle, size of obstacle, MovementCharacteristic, Path to image
    STAMINA(100, 0, 4, new Vector2(35, 35), MovementCharacteristics.CONSTANT, "PowerUps/stamina.png"),
    // Milliseconds to take away, weight, speed of obstacle, size of obstacle, MovementCharacteristic, Path to image
    TIMER(500, 0, 10, new Vector2(20, 20), MovementCharacteristics.CONSTANT, "PowerUps/timer.png"),
    // Seconds spent invulnerable, weight, speed of obstacle, size of obstacle, MovementCharacteristic, Path to image
    NOCOLLIDE(5, 0, 10, new Vector2(20, 20), MovementCharacteristics.CONSTANT, "PowerUps/nocollide.png"),
    // Agility to add, weight, speed of obstacle, size of obstacle, MovementCharacteristic, Path to image
    AGILITY(3, 0, 3, new Vector2(25, 25), MovementCharacteristics.CONSTANT, "PowerUps/agility.png"),
    // Test Effect, weight, speed of obstacle, size of obstacle, MovementCharacteristic, Path to image
    TESTING(3, 0, 3, new Vector2(25, 25), MovementCharacteristics.CONSTANT, "Testing");

    /**
     * The size of the power up.
     */
    Vector2 size;
    /**
     * How the power up will move.
     */
    MovementCharacteristics mover;
    /**
     * The weight of the power up.
     */
    float weight;
    /**
     * The y-velocity of the power up.
     */
    float speed;
    /**
     * The amount of the desired effect.
     */
    float effect;
    /**
     * The path to the image.
     */
    String imageSrc;

    /**
     * Creates a power up template with a given effect, speed, size, and movement characteristics.
     *
     * @param effect    The amount of the desired effect.
     * @param weight    The weight of the power up.
     * @param speed     The y-velocity of the power up.
     * @param size      The size of the power up.
     * @param mover     How the power up will move.
     * @param imagePath A string of the path to the image of the power up.
     */
    PowerUpType(float effect, float weight, float speed, Vector2 size, MovementCharacteristics mover, String imagePath) {
        this.effect = effect;
        this.weight = weight;
        this.speed = speed;
        this.size = size;
        this.mover = mover;
        this.imageSrc = imagePath;
    }

    /**
     * Chooses a random type of Power Up Type.
     *
     * @return A random {@link PowerUpType}
     */
    public static PowerUpType chooseRandomType() {
        // -1 is so that the TESTING power-up is never chosen in a game.
        int length = PowerUpType.values().length - 1;
        int random = ThreadLocalRandom.current().nextInt(length);
        return PowerUpType.values()[random];
    }

    /**
     * Get the image path source.
     *
     * @return A string of the path to the image.
     */
    public String getImageSrc() {
        return imageSrc;
    }

    /**
     * Get the size of the power up type.
     *
     * @return A vector2d of the size of the power up type.
     */
    public Vector2 getSize() {
        return size;
    }

    /**
     * Get the Characteristic of how the power up type moves.
     *
     * @return A MovementCharacteristic of how the power up type moves.
     */
    public MovementCharacteristics getMover() {
        return mover;
    }

    /**
     * Get the weight of the power up type.
     *
     * @return A float of the power up type.
     */
    public float getWeight() {
        return weight;
    }

    /**
     * Get the y-velocity of the power up type.
     *
     * @return A float of the y-velocity of the power up type.
     */
    public float getSpeed() {
        return speed;
    }

    /**
     * Get the amount of the effect.
     *
     * @return A float of the scale of the effect.
     */
    public float getEffect() {
        return effect;
    }
}
