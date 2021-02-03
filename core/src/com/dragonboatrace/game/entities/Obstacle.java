package com.dragonboatrace.game.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class Obstacle extends Entity {
    protected ObstacleType obstacleType;
    protected Vector2 constantVel;
    protected Texture image;

    public Obstacle(ObstacleType obstacleType, Vector2 pos, Vector2 vel) {
        super(pos, obstacleType.getSize(), obstacleType.getWeight());
        this.vel = vel;
        this.constantVel = vel.cpy();
        this.obstacleType = obstacleType;
        if (!this.obstacleType.imageSrc.equals("Testing")){
            this.image = new Texture(this.obstacleType.imageSrc);
        }
    }

    public void move(float deltaTime) {
        this.obstacleType.getMover().updateVel(deltaTime, this.constantVel);
        this.vel = constantVel.cpy();
    }

    public void render(SpriteBatch batch, Vector2 relPos) {
        batch.begin();
        batch.draw(this.image,
                (this.pos.x), (this.pos.y - relPos.y),
                this.obstacleType.getSize().x, this.obstacleType.getSize().y);
        batch.end();
    }

    public void dispose() {
        this.image.dispose();
    }

    public ObstacleType getType() {
        return this.obstacleType;
    }
}
