package com.dragonboatrace.game.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.JsonValue;
import com.dragonboatrace.game.Tuple;

/**
 * Represents the boat a player controls.
 *
 * @author Jacob Turner
 */
public class PlayerBoat extends Boat {

    /**
     * Creates a new player boat of a specific type at an initial position and with lane bounds.
     *
     * @param boatType   The boat type to be used as a template.
     * @param pos        The initial position of the boat.
     * @param laneBounds The x-coordinates of the lane bounds the boat is in.
     */
    public PlayerBoat(BoatType boatType, Vector2 pos, Tuple<Float, Float> laneBounds) {
        super(boatType, pos, laneBounds);
        startPos = pos.cpy();
    }

    /**
     * Creates a boat from a json string loaded from a save file.
     *
     * @param jsonString A json string representing a player boat.
     */
    public PlayerBoat(JsonValue jsonString) {
        super(jsonString);
    }

    /**
     * Get the input of the player to update the velocity, stamina and if any penalties need to be applied.
     *
     * @param deltaTime The time since the previous frame.
     */
    @Override
    public void move(float deltaTime) {
        if (this.currentStamina > 0 && this.currentHealth > 0) {
            if (Gdx.input.isKeyPressed(Keys.LEFT)) {
                this.vel.add(-(1 * this.boatType.getHandling() / (deltaTime * 60)), 0);
                this.currentStamina -= 2 / (60 * deltaTime);
            } else if (Gdx.input.isKeyPressed(Keys.RIGHT)) {
                this.vel.add((1 * this.boatType.getHandling() / (deltaTime * 60)), 0);
                this.currentStamina -= 2 / (60 * deltaTime);
            }
            if (Gdx.input.isKeyPressed(Keys.UP) && (this.vel.y < this.currentMaxSpeed) || (this.vel.y < 0)) {
                this.vel.add(0, ((this.boatType.getAcceleration() / 100) / (deltaTime * 60)));
                this.currentStamina -= 2 / (60 * deltaTime);
            } else if (Gdx.input.isKeyPressed(Keys.DOWN) && (this.vel.y > 0) || this.vel.y > this.currentMaxSpeed) {
                this.vel.add(0, -((this.boatType.getAcceleration() / 100) / (deltaTime * 60)));
                this.currentStamina -= 2 / (60 * deltaTime);
            }
        }
        if (this.currentStamina < this.maxStamina) {
            this.currentStamina += 1 / (60 * deltaTime);
        }
        if (this.penaltyResetDelay <= 0) {
            if (this.pos.x < this.laneBounds.a || this.laneBounds.b < this.pos.x) { // Boat has left the lane
                this.timePenalties += 5000;
                this.penaltyResetDelay = 5;
            }
        } else {
            this.penaltyResetDelay -= deltaTime;
        }
        if (this.currentHealth < 0) {
            this.currentHealth = 0;
        }
        if (this.vel.y < 0) {
            this.vel.y = 0;
        }
    }

    /**
     * Assert if an object is an identical PlayerBoat.
     *
     * @param obj The object to be compared to.
     * @return A boolean if the object is an identical boat.
     */
    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    /**
     * Adds the boats texture to a given SpriteBatch and renders it.
     *
     * @param batch  The spritebatch to be added to.
     * @param relPos The position to be drawn relative to.
     */
    public void render(SpriteBatch batch, Vector2 relPos) {
        super.render(batch, new Vector2());
    }

    /**
     * Update the actual position, the render position and the distance travelled of the boat.
     *
     * @param deltaTime The time passed since the previous frame.
     */
    @Override
    public void update(float deltaTime) {
        float deltaX = this.vel.x * this.dampening;
        float deltaY = this.vel.y * this.dampening;

        if (deltaX != 0) {
            this.pos.add(deltaX, 0);
            this.inGamePos.add(deltaX, 0);
            this.vel.add(-deltaX, 0);
        }
        if (deltaY != 0) {
            this.distanceTravelled += deltaY;
            this.inGamePos.add(0, deltaY);
        }
        this.hitbox.setToPosition(this.inGamePos);
    }
}