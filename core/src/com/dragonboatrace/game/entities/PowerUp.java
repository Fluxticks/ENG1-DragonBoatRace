package com.dragonboatrace.game.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.Timer;

public class PowerUp extends Entity {

    protected PowerUpType type;
    protected Vector2 constantVel;
    protected Texture image;

    public PowerUp(PowerUpType type, Vector2 pos, Vector2 velocity) {
        super(pos, type.getSize(), type.getWeight());
        this.type = type;
        this.constantVel = velocity.cpy();
        this.vel = velocity;
    }

    public PowerUp(JsonValue jsonString) {
        super(new Vector2(jsonString.get("pos").getFloat("x"), jsonString.get("pos").getFloat("y")),
                new Json().fromJson(PowerUpType.class, jsonString.getString("type")).getSize(),
                new Json().fromJson(PowerUpType.class, jsonString.getString("type")).getWeight());
        this.vel = new Vector2(jsonString.get("vel").getFloat("x"), jsonString.get("vel").getFloat("y"));
        this.constantVel = new Vector2(jsonString.get("constantVel").getFloat("x"), jsonString.get("constantVel").getFloat("y"));
        this.type = new Json().fromJson(PowerUpType.class, jsonString.getString("type"));
    }

    @Override
    public void loadTexture() {
        this.image = new Texture(this.type.imageSrc);
    }

    public String save() {
        return String.format("{type:%s, pos:{x:%f, y:%f}, vel:{x:%f, y:%f}, constantVel:{x:%f, y:%f}}",
                this.type,
                this.inGamePos.x,
                this.inGamePos.y,
                this.vel.x,
                this.vel.y,
                this.constantVel.x,
                this.constantVel.y
        );
    }

    public void applyEffect(Boat boatAffected) throws NullPointerException{
        switch (this.type) {
            case SPEED:
                this.applySpeed(boatAffected);
                break;
            case HEALTH:
                this.applyHealth(boatAffected);
                break;
            case AGILITY:
                this.applyAgility(boatAffected);
                break;
            case STAMINA:
                this.applyStamina(boatAffected);
                break;
            case TIMER:
                this.applyTimer(boatAffected);
                break;
            case NOCOLLIDE:
                this.applyCollide(boatAffected);
                break;
            default:
                throw new NullPointerException("No power up type defined");
        }
    }

    /*
    * Refactored methods to so they can be tested
    */

    public float applySpeed(Boat boatAffected){
        float velBefore = boatAffected.vel.y;
        boatAffected.increaseYVelocity(type.effect);
        return boatAffected.vel.y - velBefore;
    }

    public float applyHealth(Boat boatAffected){
        float healthBefore = boatAffected.currentHealth;
        boatAffected.setCurrentHealth(Math.min(boatAffected.getCurrentHealth() + type.effect, boatAffected.boatType.maxHealth));
        return boatAffected.currentHealth - healthBefore;
    }

    public float applyAgility(Boat boatAffected){
        float handlingBefore = boatAffected.getCurrentHandling();
        boatAffected.increaseHandling(type.effect);
        return boatAffected.getCurrentHandling() - handlingBefore;
    }

    public float applyStamina(Boat boatAffected){
        float staminaBefore = boatAffected.currentStamina;
        boatAffected.setCurrentStamina(Math.min(boatAffected.getCurrentStamina() + type.effect, boatAffected.maxStamina));
        return boatAffected.currentStamina - staminaBefore;
    }

    public long applyTimer(Boat boatAffected){
        long timeBefore = boatAffected.totalTime;
        boatAffected.setTotalTime(boatAffected.getTotalTimeLong() - (long) type.effect);
        return boatAffected.totalTime - timeBefore;
    }

    //TODO: Run this when the player loads the game
    private boolean applyCollide(final Boat timedBoat) {
        timedBoat.setNoCollide(true);
        Timer.Task countDown = new Timer.Task() {
            @Override
            public void run() {
                timedBoat.setNoCollide(false);
            }
        };
        Timer timer = new Timer();
        timer.scheduleTask(countDown, this.type.effect);
        return timedBoat.getNoCollide();
    }

    /*
    * End
     */


    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj.getClass() == this.getClass()) {
            PowerUp objObs = (PowerUp) obj;
            boolean constantVelBool = objObs.getConstantVel().equals(this.getConstantVel());
            return constantVelBool && super.equals(obj);
        } else {
            return false;
        }
    }

    public void move(float deltaTime) {
        this.type.getMover().updateVel(deltaTime, this.constantVel);
        this.vel = constantVel.cpy();
    }

    public void render(SpriteBatch batch, Vector2 relPos) {
        batch.begin();
        batch.draw(this.image,
                (this.pos.x), (this.pos.y - relPos.y),
                this.type.getSize().x, this.type.getSize().y);
        batch.end();
    }

    public void bounceEdge(float deltaTime) {
        //this.vel.x = this.constantVel.x * -1;
        //this.pos.x += (this.vel.x * - 1 + 5) * deltaTime;
        //this.inGamePos.x += (this.vel.x * - 1 + 5) * deltaTime;
        this.vel.x = 0;
    }

    public PowerUpType getType() {
        return this.type;
    }

    public Vector2 getConstantVel() {
        return this.constantVel;
    }

    public void setVel(Vector2 newVel) {
        this.vel = newVel;
    }

    @Override
    public void dispose() {
        this.image.dispose();
    }
}
