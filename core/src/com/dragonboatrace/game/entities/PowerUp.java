package com.dragonboatrace.game.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Timer;

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
            case HEALTH: boatAffected.setCurrentHealth(Math.min(boatAffected.getHealth() + type.effect, boatAffected.boatType.maxHealth)); break;
            case AGILITY: boatAffected.increaseXVelocity(type.effect); break;
            case STAMINA: boatAffected.setCurrentStamina(Math.min(boatAffected.getStamina() + type.effect, boatAffected.maxStamina)); break;
            case TIMER: boatAffected.setTotalTime(boatAffected.getTotalTimeLong() - (long)type.effect);
            case NOCOLLIDE: this.noCollideEffect(boatAffected); break;
            default: throw new NullPointerException("No power up type defined");
        }
    }

    private void noCollideEffect(final Boat timedBoat){
        timedBoat.setNoCollide(true);
        Timer.Task countDown = new Timer.Task(){
            @Override
            public void run(){
                timedBoat.setNoCollide(false);
            }
        };
        Timer timer = new Timer();
        timer.scheduleTask(countDown, this.type.effect);
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

    public void bounceEdge(float deltaTime){
        //this.vel.x = this.constantVel.x * -1;
        //this.pos.x += (this.vel.x * - 1 + 5) * deltaTime;
        //this.inGamePos.x += (this.vel.x * - 1 + 5) * deltaTime;
        this.vel.x = 0;
    }

    @Override
    public void dispose() {
        this.type.getImage().dispose();
    }
}
