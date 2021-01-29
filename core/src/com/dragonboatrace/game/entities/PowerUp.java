package com.dragonboatrace.game.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class PowerUp extends Entity {

    protected PowerUpType type;
    protected Vector2 constantVel;

    public PowerUp(PowerUpType type, Vector2 pos, Vector2 velocity) {
        super(pos, type.getSize(), type.getWeight());
        this.type = type;
        this.constantVel = velocity.cpy();
        this.vel = velocity;
    }

    public void applyEffect(Boat boatAffected){
        switch(type){
            case SPEED: boatAffected.increaseYVelocity(type.effect); break;
            case HEALTH:
                boatAffected.setCurrentHealth(Math.min(boatAffected.getHealth() + type.effect, boatAffected.boatType.maxHealth));
                break;
            case AGILITY: boatAffected.increaseXVelocity(type.effect); break;
            default: throw new NullPointerException("No power up type defined");
        }
    }

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

    public void bounceEdge(){
        this.vel.x = this.vel.x * -1;
    }

    @Override
    public void dispose() {
        this.type.getImage().dispose();
    }
}
