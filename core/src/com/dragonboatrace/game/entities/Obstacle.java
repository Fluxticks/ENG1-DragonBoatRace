package com.dragonboatrace.game.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;

/**
 * Represents an obstacle.
 *
 * @author Jacob Turner
 */
public class Obstacle extends Entity {
    /**
     * The kind of obstacle, used as a template.
     */
    protected ObstacleType obstacleType;
    /**
     * The constant velocity, used to reset the current velocity.
     */
    protected Vector2 constantVel;
    /**
     * The texture of the obstacle, used for rendering.
     */
    protected Texture image;

    /**
     * Creates a new obstacle of a specific type and at a position.
     *
     * @param obstacleType The type of obstacle to use as a template.
     * @param pos          The initial position of the obstacle.
     * @param vel          The initial velocity of the obstacle.
     */
    public Obstacle(ObstacleType obstacleType, Vector2 pos, Vector2 vel) {
        super(pos, obstacleType.getSize(), obstacleType.getWeight());
        this.vel = vel;
        this.constantVel = vel.cpy();
        this.obstacleType = obstacleType;
    }

    /**
     * Creates a new obstacle from json string from a save file.
     *
     * @param jsonString The string representing an obstacle.
     */
    public Obstacle(JsonValue jsonString) {
        super(new Vector2(jsonString.get("pos").getFloat("x"), jsonString.get("pos").getFloat("y")),
                new Json().fromJson(ObstacleType.class, jsonString.getString("type")).getSize(),
                new Json().fromJson(ObstacleType.class, jsonString.getString("type")).getWeight());
        this.vel = new Vector2(jsonString.get("vel").getFloat("x"), jsonString.get("vel").getFloat("y"));
        this.constantVel = new Vector2(jsonString.get("constantVel").getFloat("x"), jsonString.get("constantVel").getFloat("y"));
        this.obstacleType = new Json().fromJson(ObstacleType.class, jsonString.getString("type"));
    }

    /**
     * Load the texture for the obstacle. Is required before rendering.
     */
    @Override
    public void loadTexture() {
        this.image = new Texture(this.obstacleType.imageSrc);
    }

    /**
     * Create a json string that represents the current obstacle.
     *
     * @return A json string representing the obstacle
     */
    public String save() {
        return String.format("{type:%s, pos:{x:%f, y:%f}, vel:{x:%f, y:%f}, constantVel:{x:%f, y:%f}}",
                this.obstacleType,
                this.inGamePos.x,
                this.inGamePos.y,
                this.vel.x,
                this.vel.y,
                this.constantVel.x,
                this.constantVel.y
        );
    }

    /**
     * Compares an object to an obstacle and determines if the object is an identical obstacle.
     *
     * @param obj The object being checked against.
     * @return A boolean of weather the object is an identical obstacle.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj.getClass() == this.getClass()) {
            Obstacle objObs = (Obstacle) obj;
            boolean constantVelBool = objObs.getConstantVel().equals(this.getConstantVel());
            return constantVelBool && super.equals(obj);
        } else {
            return false;
        }
    }

    /**
     * Get the obstacles constant velocity.
     *
     * @return A vector2d of the obstacles constant velocity.
     */
    public Vector2 getConstantVel() {
        return this.constantVel;
    }

    /**
     * Updates the velocity of the obstacle given the time since the previous frame.
     *
     * @param deltaTime The time since the previous frame.
     */
    public void move(float deltaTime) {
        this.obstacleType.getMover().updateVel(deltaTime, this.constantVel);
        this.vel = constantVel.cpy();
    }

    /**
     * Add the obstacle's texture to the spritebatch given and draw it.
     *
     * @param batch  The SpriteBatch to be added to.
     * @param relPos The position to be rendered relative to.
     */
    public void render(SpriteBatch batch, Vector2 relPos) {
        batch.begin();
        batch.draw(this.image,
                (this.pos.x), (this.pos.y - relPos.y),
                this.obstacleType.getSize().x, this.obstacleType.getSize().y);
        batch.end();
    }

    /**
     * Dispose the obstacle's texture.
     */
    public void dispose() {
        this.image.dispose();
    }

    /**
     * Get the type of obstacle a given obstacle is.
     *
     * @return An ObstacleType that represents a given obstacle.
     */
    public ObstacleType getType() {
        return this.obstacleType;
    }
}
