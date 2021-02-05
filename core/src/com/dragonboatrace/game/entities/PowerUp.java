package com.dragonboatrace.game.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.Timer;

/**
 * Represents a Power Up obstacle
 *
 * @author Benji Garment
 */
public class PowerUp extends Entity {

    /**
     * The type of Power Up.
     */
    protected PowerUpType type;
    /**
     * The constant velocity, used to reset the current velocity.
     */
    protected Vector2 constantVel;
    /**
     * The texture of the power up.
     */
    protected Texture image;

    /**
     * Creates a new power up given a power up type as a template, an initial position and an initial velocity.
     *
     * @param type     The power up type used as a template.
     * @param pos      The initial position of the power up.
     * @param velocity The initial velocity of the power up.
     */
    public PowerUp(PowerUpType type, Vector2 pos, Vector2 velocity) {
        super(pos, type.getSize(), type.getWeight());
        this.type = type;
        this.constantVel = velocity.cpy();
        this.vel = velocity;
    }

    /**
     * Creates a new power up from a json string loaded from a save file.
     *
     * @param jsonString The json string representing the power up.
     */
    public PowerUp(JsonValue jsonString) {
        super(new Vector2(jsonString.get("pos").getFloat("x"), jsonString.get("pos").getFloat("y")),
                new Json().fromJson(PowerUpType.class, jsonString.getString("type")).getSize(),
                new Json().fromJson(PowerUpType.class, jsonString.getString("type")).getWeight());
        this.vel = new Vector2(jsonString.get("vel").getFloat("x"), jsonString.get("vel").getFloat("y"));
        this.constantVel = new Vector2(jsonString.get("constantVel").getFloat("x"), jsonString.get("constantVel").getFloat("y"));
        this.type = new Json().fromJson(PowerUpType.class, jsonString.getString("type"));
    }

    /**
     * Load the texture from the image source. Is required before rendering.
     */
    @Override
    public void loadTexture() {
        this.image = new Texture(this.type.imageSrc);
    }

    /**
     * Creates a json string of all the current attributes of the power up.
     *
     * @return A json string representing a power up.
     */
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

    /**
     * Apply the effect of the power up to the boat given.
     *
     * @param boatAffected The boat to be affected by the effect.
     * @throws NullPointerException If the power up type is not a valid type, a NullPointerException will be thrown.
     */
    public void applyEffect(Boat boatAffected) throws NullPointerException {
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

    /**
     * Apply the speed effect.
     *
     * @param boatAffected The boat being affected by the power up.
     * @return A float of the delta between the speed before and after the power up.
     */
    public float applySpeed(Boat boatAffected) {
        float velBefore = boatAffected.vel.y;
        boatAffected.increaseYVelocity(type.effect);
        return boatAffected.vel.y - velBefore;
    }

    /**
     * Apply the health effect.
     *
     * @param boatAffected The boat being affected by the power up.
     * @return A float of the delta between the health before and after the power up.
     */
    public float applyHealth(Boat boatAffected) {
        float healthBefore = boatAffected.currentHealth;
        boatAffected.setCurrentHealth(Math.min(boatAffected.getCurrentHealth() + type.effect, boatAffected.boatType.maxHealth));
        return boatAffected.currentHealth - healthBefore;
    }

    /**
     * Apply the agility effect.
     *
     * @param boatAffected The boat being affected by the power up.
     * @return A float of the delta between the agility before and after the power up.
     */
    public float applyAgility(Boat boatAffected) {
        float handlingBefore = boatAffected.getCurrentHandling();
        boatAffected.increaseHandling(type.effect);
        return boatAffected.getCurrentHandling() - handlingBefore;
    }

    /**
     * Apply the stamina effect.
     *
     * @param boatAffected The boat being affected by the power up.
     * @return A float of the delta between the agility before and after the power up.
     */
    public float applyStamina(Boat boatAffected) {
        float staminaBefore = boatAffected.currentStamina;
        boatAffected.setCurrentStamina(Math.min(boatAffected.getCurrentStamina() + type.effect, boatAffected.maxStamina));
        return boatAffected.currentStamina - staminaBefore;
    }

    /**
     * Apply the timer effect.
     *
     * @param boatAffected The boat being affected by the power up.
     * @return A long of the delta between the total time before and after the power up.
     */
    public long applyTimer(Boat boatAffected) {
        long timeBefore = boatAffected.totalTime;
        boatAffected.setTotalTime(boatAffected.getTotalTimeLong() - (long) type.effect);
        return boatAffected.totalTime - timeBefore;
    }

    /**
     * Apply the no collide effect.
     *
     * @param timedBoat The boat being timed to removed the effect after the desired time.
     * @return A boolean of the status of the no collide of the boat affected.
     */
    private boolean applyCollide(final Boat timedBoat) {
        //TODO: Run this when the player loads the game
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

    /**
     * Compares an object to an power up and determines if the object is an identical power up.
     *
     * @param obj The object being checked against.
     * @return A boolean of weather the object is an identical power up.
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

    /**
     * Move the power up using the given movement characteristic
     *
     * @param deltaTime The time since the previous frame.
     */
    public void move(float deltaTime) {
        this.type.getMover().updateVel(deltaTime, this.constantVel);
        this.vel = constantVel.cpy();
    }

    /**
     * Add the power up texture to the given SpriteBatch and render it.
     *
     * @param batch  The SpriteBatch to be added to.
     * @param relPos The position to be rendered relative to.
     */
    public void render(SpriteBatch batch, Vector2 relPos) {
        batch.begin();
        batch.draw(this.image,
                (this.pos.x), (this.pos.y - relPos.y),
                this.type.getSize().x, this.type.getSize().y);
        batch.end();
    }

    /**
     * Used to invert the x-velocity of the power up when it is at an edge.
     *
     * @param deltaTime The time since the previous frame.
     */
    public void bounceEdge(float deltaTime) {
        //this.vel.x = this.constantVel.x * -1;
        //this.pos.x += (this.vel.x * - 1 + 5) * deltaTime;
        //this.inGamePos.x += (this.vel.x * - 1 + 5) * deltaTime;
        this.vel.x = 0;
    }

    /**
     * Get the type of the power up.
     *
     * @return The power up type.
     */
    public PowerUpType getType() {
        return this.type;
    }

    /**
     * Get the constant velocity attribute of the power up.
     *
     * @return A vector2d of the constant velocity.
     */
    public Vector2 getConstantVel() {
        return this.constantVel;
    }

    /**
     * Sets the velocity of the power up.
     *
     * @param newVel The new velocity to set the power up to.
     */
    public void setVel(Vector2 newVel) {
        this.vel = newVel.cpy();
    }

    /**
     * Dispose the power up texture.
     */
    @Override
    public void dispose() {
        this.image.dispose();
    }
}
