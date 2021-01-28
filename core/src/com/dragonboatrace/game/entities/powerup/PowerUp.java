package com.dragonboatrace.game.entities.powerup;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.dragonboatrace.game.entities.Boat;
import com.dragonboatrace.game.entities.Entity;

public abstract class PowerUp extends Entity {

    protected PowerUpType type;
    protected Vector2 constantVel;

    public PowerUp(PowerUpType type, Vector2 pos, Vector2 velocity) {
        super(pos, type.getSize(), type.getWeight());
        this.type = type;
        this.constantVel = velocity.cpy();
        this.vel = velocity;
    }

    public abstract void applyEffect(Boat boatAffected);

    public void move(float deltaTime){
        this.type.getMover().updateVel(deltaTime, this.constantVel);
        this.vel = constantVel.cpy();
    }

    public void render(SpriteBatch batch, Vector2 relPos){
        batch.begin();
        batch.draw(this.type.getImage(),
                (this.pos.x), (this.pos.y - relPos.y),
                this.type.getSize().x, this.type.getSize().y);
        batch.end();
    }
}
