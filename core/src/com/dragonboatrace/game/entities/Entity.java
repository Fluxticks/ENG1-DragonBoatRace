package com.dragonboatrace.game.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

/**
 * Represents a generic entity.
 *
 * @author Jacob Turner
 */
public abstract class Entity {

    /**
     * The actual position of the entity.
     */
    protected Vector2 pos;
    /**
     * The position the entity is being rendered at.
     */
    protected Vector2 inGamePos;
    /**
     * The current velocity of the entity.
     */
    protected Vector2 vel;
    /**
     * The current acceleration of the entity.
     */
    protected Vector2 acc;
    /**
     * The dimensions of the entity.
     */
    protected Vector2 size;
    /**
     * The weight of the entity, use for collisions.
     */
    protected float weight;
    /**
     * The rate at which the velocity of the entity is dampened.
     */
    protected final float dampening = 0.2f;
    /**
     * The hit box of the entity.
     */
    protected EntityHitbox hitbox;

    /**
     * Creates a new entity at a position with a size and weight.
     *
     * @param pos    The initial position of the entity.
     * @param size   The size of the entity.
     * @param weight The weight of the entity.
     */
    public Entity(Vector2 pos, Vector2 size, float weight) {
        this.pos = pos;
        this.inGamePos = new Vector2(pos);
        this.vel = new Vector2();
        this.acc = new Vector2();
        this.size = size;
        this.weight = weight;
        this.hitbox = new EntityHitbox(this.inGamePos, this.size);
    }

    /**
     * Creates a new entity where the initial velocity is not 0, but instead the given value.
     *
     * @param pos    The initial position of the entity.
     * @param vel    The velocity to start the entity at.
     * @param size   The size of the entity.
     * @param weight The weight of the entity.
     */
    public Entity(Vector2 pos, Vector2 vel, Vector2 size, float weight) {
        this.pos = pos;
        this.inGamePos = new Vector2(pos);
        this.vel = new Vector2(vel);
        this.size = size;
        this.weight = weight;
        this.hitbox = new EntityHitbox(this.inGamePos, this.size);
    }

    /**
     * Checks if two entities have the same values.
     *
     * @param obj The object being checked against.
     * @return A boolean if the object and entity have the same values.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj.getClass() == this.getClass()) {
            Entity objEntity = (Entity) obj;
            boolean inGame = objEntity.getInGamePos().equals(this.getInGamePos());
            boolean actualPos = objEntity.getPos().equals(this.getPos());
            boolean actualVel = objEntity.getVel().equals(this.getVel());
            return inGame && actualPos && actualVel;
        } else {
            return false;
        }
    }

    /**
     * Update the velocity, position and in-game position of the entity based on the time since the previous frame.
     *
     * @param deltaTime The time since the previous frame.
     */
    public void update(float deltaTime) {
        // Dampen velocity
        float deltaX = this.vel.x * this.dampening / (deltaTime * 60);
        float deltaY = this.vel.y * this.dampening / (deltaTime * 60);

        if (deltaX != 0) {
            this.pos.add(deltaX, 0);
            this.inGamePos.add(deltaX, 0);
            this.vel.add(-deltaX, 0);

        }
        if (deltaY != 0) {
            this.pos.add(0, deltaY);
            this.inGamePos.add(0, deltaY);
            this.vel.add(0, -deltaY);

        }
        this.hitbox.setToPosition(this.inGamePos);
    }

    /**
     * Check if an obstacle and an entity are colliding.
     *
     * @param e The obstacle being checked for collision.
     * @return A boolean of if the entity and obstacle are colliding.
     */
    public boolean checkCollision(Obstacle e) {
        return this.hitbox.checkCollision(e.getHitbox());
    }

    /**
     * Adds the entity to the given spritebatch to be rendered.
     *
     * @param batch The spritebatch to be added to.
     */
    public void render(SpriteBatch batch) {
        this.render(batch, new Vector2());
    }

    /**
     * Gives the position of an entity compared to a given vector. Only affects the y-position.
     *
     * @param relPos The other vector being compared to.
     * @return The relative position to relPos.
     */
    public Vector2 getRelPos(Vector2 relPos) {
        return new Vector2((this.pos.x), (this.pos.y - relPos.y));
    }

    /**
     * Draws a box around where the hit box is. Used for debugging.
     *
     * @param relPos        The position to be rendered relative to.
     * @param shapeRenderer The ShapeRenderer to be used to draw the box.
     */
    public void renderHitBox(Vector2 relPos, ShapeRenderer shapeRenderer) {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.RED);
        shapeRenderer.rect(this.hitbox.getPosition().x, this.hitbox.getPosition().y - relPos.y, this.size.x, this.size.y);
        shapeRenderer.end();
    }

    /**
     * Get the entities hit box.
     *
     * @return The hit box of the entity.
     */
    public EntityHitbox getHitbox() {
        return this.hitbox;
    }

    /**
     * Get the render position of the entity.
     *
     * @return A vector2d of the entities render position.
     */
    public Vector2 getInGamePos() {
        return this.inGamePos;
    }

    /**
     * Gets the current velocity of the entity.
     *
     * @return A vector2d of the current velocity.
     */
    public Vector2 getVel() {
        return this.vel;
    }

    /**
     * Get the actual position of the entity.
     *
     * @return A vector2d of the entities actual position.
     */
    public Vector2 getPos() {
        return this.pos;
    }

    /**
     * Get the size of the entity.
     *
     * @return A vector2d representing the size of the entity.
     */
    public Vector2 getSize() {
        return size;
    }

    /**
     * Get the weight of the entity.
     *
     * @return A float of the entities weight.
     */
    public float getWeight() {
        return weight;
    }

    public abstract void render(SpriteBatch batch, Vector2 relPos);

    public abstract void move(float deltaTime);

    public abstract void dispose();

    public abstract void loadTexture();
}