package com.dragonboatrace.game.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public abstract class Entity {

    protected Vector2 pos, inGamePos, vel, inGameVel, acc, size;
    protected float weight, dampening;
    protected Obstacle collider;

    public Entity(Vector2 pos, Vector2 size, float weight) {
        this.pos = pos;
        this.inGamePos = new Vector2(pos);
        this.inGameVel = new Vector2();
        this.vel = new Vector2();
        this.acc = new Vector2();
        this.size = size;
        this.weight = weight;
        this.dampening = (float) 0.2;
        this.collider = null;
    }

    public void update(float deltaTime) {
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
    }

    public boolean checkCollision(Obstacle e) {
        this.collider = null;
        float x1 = this.inGamePos.x;
        float y1 = this.inGamePos.y;
        float x2 = this.inGamePos.x + this.size.x;
        float y2 = this.inGamePos.y + this.size.y;

        for (int dx = 0; dx <= 1; dx++) {
            for (int dy = 0; dy <= 1; dy++) {
                float checkX = e.inGamePos.x + (dx * e.size.x);
                float checkY = e.inGamePos.y + (dy * e.size.y);
                if ((x1 <= checkX && checkX <= x2) &&
                        (y1 <= checkY && checkY <= y2)) {
                    return true;
                }
            }
        }
        return false;
    }

    public void render(SpriteBatch batch) {
        this.render(batch, new Vector2());
    }

    public Vector2 getInGamePos() {
        return this.inGamePos;
    }

    public Vector2 getVel() {
        return this.vel;
    }

    public Vector2 getPos() {
        return this.pos;
    }

    public Obstacle getCollider() {
        return this.collider;
    }

    public abstract void render(SpriteBatch batch, Vector2 relPos);

    public abstract void move(float deltaTime);

    public abstract void dispose();

    public Vector2 getSize() {
        return size;
    }

    public float getWeight() {
        return weight;
    }
}