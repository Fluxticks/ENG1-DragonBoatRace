package com.dragonboatrace.game.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class Obstacle extends Entity {
    protected ObstacleType obstacleType;
    protected Vector2 constantVel;

    public Obstacle(ObstacleType obstacleType, Vector2 pos, Vector2 vel) {
        super(pos, obstacleType.getSize(), obstacleType.getWeight());
        this.vel = vel;
        this.constantVel = vel.cpy();
        this.obstacleType = obstacleType;
    }

    public void collide(Obstacle o) {

    }

    public void move(float deltaTime) {
        this.obstacleType.getMover().updateVel(deltaTime, this.constantVel);
        this.vel = constantVel.cpy();
    }

    public void render(SpriteBatch batch, Vector2 relPos) {
        batch.begin();
        batch.draw(this.obstacleType.getImage(),
                (this.pos.x), (this.pos.y - relPos.y),
                this.obstacleType.getSize().x, this.obstacleType.getSize().y);
        batch.end();
    }

    public void dispose() {
        this.obstacleType.getImage().dispose();
    }

    public Vector2 getRelPos(Vector2 relPos) {
        return new Vector2((this.pos.x), (this.pos.y - relPos.y));
    }

    public ObstacleType getType() {
        return this.obstacleType;
    }
}
