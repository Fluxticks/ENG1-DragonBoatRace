package com.dragonboatrace.game.entities;

import com.badlogic.gdx.math.Vector2;

/**
 * Holds the information for an entities hit box.
 *
 * @author Benji Garment
 */
public class EntityHitbox {

    /**
     * The position of the hit box.
     */
    private Vector2 position;
    /**
     * The size of the hit box.
     */
    private final Vector2 size;

    /**
     * Creates a hit box at a position with a specific size.
     *
     * @param pos  The initial position of the hit box.
     * @param size The size of the hit box.
     */
    public EntityHitbox(Vector2 pos, Vector2 size) {
        this.position = pos;
        this.size = size;
    }

    /**
     * Set the hit box to a position.
     *
     * @param newPos The position to be set to.
     */
    public void setToPosition(Vector2 newPos) {
        this.position = newPos.cpy();
    }

    /**
     * Move the hit box relative to where it is currently.
     *
     * @param addPos The amount to move by as a vector.
     */
    public void movePosition(Vector2 addPos) {
        this.position.add(addPos);
    }

    /**
     * Check if two hit boxes are colliding.
     *
     * @param otherEntity The other hit box to be checked.
     * @return A boolean of if the hit boxes are colliding.
     */
    public boolean checkCollision(EntityHitbox otherEntity) {
        return this.position.x + this.size.x > otherEntity.getPosition().x && this.position.x < otherEntity.getPosition().x + otherEntity.getSize().x && this.position.y < otherEntity.getPosition().y + otherEntity.getSize().y && this.position.y + this.size.y > otherEntity.getPosition().y;
    }

    /**
     * Get the current position of the hit box.
     *
     * @return A vector2d of the position of the hit box.
     */
    public Vector2 getPosition() {
        return position;
    }

    /**
     * Get the size of the hit box.
     *
     * @return A vector2d of the size of the hit box.
     */
    public Vector2 getSize() {
        return size;
    }
}
