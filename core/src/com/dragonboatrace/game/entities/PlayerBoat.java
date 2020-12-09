package com.dragonboatrace.game.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.dragonboatrace.game.Tuple;

public class PlayerBoat extends Boat {
    Vector2 startPos;

    public PlayerBoat(BoatType boatType, Vector2 pos, Tuple<Float, Float> laneBounds) {
        super(boatType, pos, laneBounds);
        startPos = pos;
    }

    @Override
    public void move(float deltaTime) {
        if (this.stamina > 0 && this.currentHealth > 0) {
            if (Gdx.input.isKeyPressed(Keys.LEFT)) {
                this.vel.add(-(1 * this.boatType.getHandling() / (deltaTime * 60)), 0);
                this.stamina -= 2 / (60 * deltaTime);
            } else if (Gdx.input.isKeyPressed(Keys.RIGHT)) {
                this.vel.add((1 * this.boatType.getHandling() / (deltaTime * 60)), 0);
                this.stamina -= 2 / (60 * deltaTime);
            }
            if (Gdx.input.isKeyPressed(Keys.UP) && (this.vel.y < this.currentMaxSpeed) || (this.vel.y < 0)) {
                this.vel.add(0, ((this.boatType.getAcceleration() / 100) / (deltaTime * 60)));
                this.stamina -= 2 / (60 * deltaTime);
            } else if (Gdx.input.isKeyPressed(Keys.DOWN) && (this.vel.y > 0) || this.vel.y > this.currentMaxSpeed) {
                this.vel.add(0, -((this.boatType.getAcceleration() / 100) / (deltaTime * 60)));
                this.stamina -= 2 / (60 * deltaTime);
            }
        }
        if (this.stamina < this.maxStamina) {
            this.stamina += 1 / (60 * deltaTime);
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

    public void render(SpriteBatch batch, Vector2 relPos){
        super.render(batch, new Vector2());
    }

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
    }
}