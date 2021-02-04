package com.dragonboatrace.game.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.dragonboatrace.game.Tuple;

import java.util.ArrayList;

/**
 * Represents a generic Boat.
 *
 * @author Jacob Turner
 */
public abstract class Boat extends Entity {

    /**
     * The generic boat type used to keep track of the chosen boat type.
     */
    protected BoatType boatType;
    /**
     * Stores the current health of the boat.
     */
    protected float currentHealth;
    /**
     * Stores the current maximum y velocity the boat can go.
     */
    protected float currentMaxSpeed;
    /**
     * The list of obstacles recently collided with. Means there is only one collision per obstacle.
     */
    protected ArrayList<Obstacle> collided;
    /**
     * The current stamina of the boat.
     */
    protected float currentStamina;
    /**
     * The maximum stamina the boat can store at anytime.
     */
    protected float maxStamina;
    /**
     * The amount of time to be added as penalties at the end of a given round.
     */
    protected float timePenalties;
    /**
     * The amount of time to wait before adding more time to the penalty.
     */
    protected float penaltyResetDelay;
    /**
     * The base handling of the boat, used to reset the handling at the beginning of each round.
     */
    protected float defaultHandling;
    /**
     * The time at which the boat finished the race.
     */
    protected long finishTime;
    /**
     * Weather the boat has finished the round or not.
     */
    protected boolean finished = false;
    /**
     * The distance travelled in the current round.
     */
    protected float distanceTravelled;
    /**
     * The cumulative time from all the rounds.
     */
    protected long totalTime;
    /**
     * The start and end bound of the lane the boat is in.
     */
    protected Tuple<Float, Float> laneBounds;
    /**
     * Weather the boat is able to collide with obstacles.
     * <p>False is when it is able to collide</p>
     * <p>True is when it is not able to collide</p>
     */
    private boolean noCollide = false;
    /**
     * The texture of boat.
     */
    protected Texture image;
    /**
     * Stores the position of where to reset the boat to at the beginning of each round.
     */
    protected Vector2 startPos;

    /**
     * Creates a generic boat from a BoatType, Position and with Lane bounds.
     * @param boatType The type of boat to take statistics for. Of type {@link BoatType}
     * @param pos The initial position of the boat. Of type {@link Vector2}
     * @param laneBounds The x bounds of the lane the boat is in. Of type {@link Tuple}
     */
    public Boat(BoatType boatType, Vector2 pos, Tuple<Float, Float> laneBounds) {
        super(pos.cpy(), boatType.getSize().cpy(), boatType.getWeight());
        this.startPos = this.pos.cpy();
        this.boatType = boatType;
        this.currentHealth = this.boatType.getMaxHealth();
        this.currentMaxSpeed = this.boatType.getSpeed();
        this.collided = new ArrayList<>();
        this.currentStamina = 1000;
        this.maxStamina = 1000;
        this.defaultHandling = boatType.handling;
        this.distanceTravelled = 0;
        this.totalTime = 0;
        this.laneBounds = laneBounds;
        if (!this.boatType.imageSrc.equals("Testing"))
            this.image = new Texture(this.boatType.imageSrc);
    }

    /**
     * Creates a generic boat from a json string, imported from a save file.
     * @param jsonString The json string that represents a boat as a string. Of type {@link JsonValue}
     */
    public Boat(JsonValue jsonString) {
        super(new Vector2(jsonString.get("pos").getFloat("x"), jsonString.get("pos").getFloat("y")),
                new Vector2(jsonString.get("vel").getFloat("x"), jsonString.get("vel").getFloat("y")),
                new Json().fromJson(BoatType.class, jsonString.getString("type")).getSize().cpy(),
                new Json().fromJson(BoatType.class, jsonString.getString("type")).getWeight());
        this.boatType = new Json().fromJson(BoatType.class, jsonString.getString("type"));
        this.startPos = new Vector2();
        this.startPos.x = jsonString.get("startPos").getFloat("x");
        this.startPos.y = jsonString.get("startPos").getFloat("y");
        this.currentHealth = jsonString.getFloat("health");
        this.currentStamina = jsonString.getFloat("stamina");
        this.distanceTravelled = jsonString.getFloat("distance");
        this.totalTime = jsonString.getInt("totalTime");
        this.laneBounds = new Tuple<>(jsonString.get("laneBounds").getFloat("x"), jsonString.get("laneBounds").getFloat("y"));
        this.currentMaxSpeed = this.boatType.getSpeed();
        this.maxStamina = 1000;
        this.collided = new ArrayList<>();
        this.inGamePos.x = jsonString.get("inGamePos").getFloat("x");
        this.inGamePos.y = jsonString.get("inGamePos").getFloat("y");
        this.hitbox.setToPosition(this.inGamePos);
        if (!this.boatType.imageSrc.equals("Testing"))
            this.image = new Texture(this.boatType.imageSrc);
    }

    /**
     * Checks if an object is equal to this boat, attribute by attribute
     * @param obj The object to be compared to.
     * @return True if all the attributes of the object are the same as this boats. False otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj.getClass() == this.getClass()) {
            Boat objBoat = (Boat) obj;
            boolean type = objBoat.getType() == this.getType();
            boolean health = objBoat.getCurrentHealth() == this.getCurrentHealth();
            boolean stamina = objBoat.getCurrentStamina() == this.getCurrentStamina();
            boolean travelled = Float.compare(objBoat.getDistanceTravelled(), this.getDistanceTravelled()) != 1;
            boolean time = objBoat.getTotalTimeLong() == this.getTotalTimeLong();
            boolean bounds = objBoat.getLaneBounds().equals(this.getLaneBounds());
            return type && health && stamina && travelled && time && bounds && super.equals(obj);
        } else {
            return false;
        }
    }

    /**
     * Creates a JSON string of this boat.
     * @return A string of the attributes formatted for a JSON.
     */
    public String save() {
        return String.format("{type:%s, health:%f, stamina:%f, distance:%f, totalTime:%d, laneBounds:{x:%f, y:%f}, inGamePos:{x:%f, y:%f}, pos:{x:%f, y:%f}, vel:{x:%f, y:%f}, startPos:{x:%f, y:%f}}",
                this.boatType,
                this.currentHealth,
                this.currentStamina,
                this.distanceTravelled,
                this.totalTime,
                this.laneBounds.a,
                this.laneBounds.b,
                this.inGamePos.x,
                this.inGamePos.y,
                this.pos.x,
                this.pos.y,
                this.vel.x,
                this.vel.y,
                this.startPos.x,
                this.startPos.y
        );
    }

    /**
     * Perform the collision logic after the check of if an obstacle is actually colliding.
     * @param colliding If the obstacle o is colliding with the boat.
     * @param o The obstacle being checked.
     */
    public void doCollision(boolean colliding, Obstacle o) {
        if (colliding) {
            if (!this.collided.contains(o)) {
                this.collided.add(o);
                this.currentHealth -= o.weight;
                this.currentMaxSpeed = this.boatType.getSpeed() / o.weight;
                this.currentStamina = Math.max(this.currentStamina - 100 * o.weight, 0);
            }
        } else if (this.collided.contains(o)) {
            this.collided.remove(o);
            this.currentMaxSpeed = this.boatType.getSpeed();
        }
    }

    /**
     * Check if there is a collision between an obstacle o and the boat.
     * @param o The obstacle being checked.
     */
    public void checkForCollision(Obstacle o) {
        if (!this.noCollide) {
            doCollision(super.checkCollision(o), o);
        }
    }

    /**
     * Renders the boat to a spritebatch and relative to the y-coordinate of relPos
     * @param batch The spritebatch to be added to.
     * @param relPos The position to be drawn relative to.
     */
    public void render(SpriteBatch batch, Vector2 relPos) {
        batch.begin();
        batch.draw(this.image,
                (this.pos.x), (this.pos.y - relPos.y),
                this.size.x, this.size.y);
        batch.end();
    }

    /**
     * Update the position of the boat based on the time passed since the previous frame.
     * @param deltaTime The time passed since the previous frame.
     */
    @Override
    public void update(float deltaTime) {
        this.currentMaxSpeed = this.boatType.getSpeed();
        if (this.collider != null) {
            this.currentMaxSpeed /= this.collider.weight;
        }

        super.update(deltaTime);
    }

    /**
     * Check if the boat has crossed the finish line.
     * @param finishLine The position of the finish line.
     * @param startTime The time the round started.
     * @return If the boat has finished or not.
     */
    public boolean checkFinished(int finishLine, long startTime) {
        //finish line is the pixels from the start that the boats have to travel
        //start time is the system time when the race started

        if (this.finished) {
            return true;
        } else if (this.isFinished(finishLine)) {
            this.setFinishTime(System.currentTimeMillis() - startTime + (long) this.timePenalties);
            this.finished = true;
        }
        return this.finished;

    }

    /**
     * Turn the time taken to finish into a string.
     * @return A string of how long the boat took to finish.
     */
    public String getFinishTimeString() {
        if (this.finishTime == 0) {
            //calculate an estimate dnf is just temporary?
            return "DNF";
        } else {
            //returns the finish time in minutes:seconds
            if (((this.finishTime / 1000) / 60) == 0) {
                return (int) ((this.finishTime / 1000) % 60) + " Seconds";
            }
            return (int) ((this.finishTime / 1000) / 60) + " Minutes and " + (int) ((this.finishTime / 1000) % 60) + " Seconds";
        }
    }

    /**
     * The cumulative time of all the rounds as a string.
     * @return A string of all the times of the rounds added.
     */
    public String getTotalTimeString() {
        if (this.totalTime == 0) {
            return "DNF";
        } else {
            //returns the finish time in minutes:seconds
            if (((this.totalTime / 1000) / 60) == 0) {
                return (int) ((this.totalTime / 1000) % 60) + " Seconds";
            }
            return (int) ((this.totalTime / 1000) / 60) + " Minutes and " + (int) ((this.totalTime / 1000) % 60) + " Seconds";
        }
    }

    /**
     * Get the total time taken.
     * @return The total time taken as a long.
     */
    public long getTotalTimeLong() {
        return this.totalTime;
    }

    /**
     * The bounds of the lane the boat is in currently.
     * @return A tuple of the lane bounds of the lane.
     */
    public Tuple<Float, Float> getLaneBounds() {
        return this.laneBounds;
    }

    /**
     * Save the start position.
     */
    //TODO: Remove this.
    public void saveStartPos() {
        this.startPos = this.pos.cpy();
    }

    /**
     * Reset the boat to default values and to the start of the race.
     */
    public void moveToStart() {
        this.inGamePos = startPos.cpy();
        this.pos = startPos.cpy();
        this.hitbox.setToPosition(this.inGamePos);
        this.vel = new Vector2();
        this.currentStamina = maxStamina;
        this.distanceTravelled = 0;
        this.totalTime += finishTime;
        this.finishTime = 0;
        this.boatType.handling = defaultHandling;
        this.finished = false;
    }

    /**
     * Dispose the boat texture.
     */
    public void dispose() {
        this.image.dispose();
    }

    /**
     * Checks if the boat is past the finish line.
     * @param finishLine The position of the finish line.
     * @return True if the boat is past the line, false otherwise.
     */
    public boolean isFinished(int finishLine) {
        return this.distanceTravelled >= finishLine;
    }

    /**
     * Set the time at which the boat finished.
     * @param finishTime The time to set the finish time to.
     */
    public void setFinishTime(long finishTime) {
        this.finishTime = finishTime;
    }

    /**
     * Get the total time penalty.
     * @return The time penalty as a float.
     */
    public float getPenalty() {
        return this.timePenalties;
    }

    /**
     * Get the time at which the boat finished
     * @return A long of the time the boat finished.
     */
    public long getFinishTimeLong() {
        return this.finishTime;
    }

    /**
     * Get the current maximum y velocity the boat can go.
     * @return The maximum speed the boat can go in the y-direction.
     */
    public float getMaxSpeed() {
        return this.currentMaxSpeed;
    }

    /**
     * Get the current health of the boat.
     * @return A float of the current health.
     */
    public float getCurrentHealth() {
        return this.currentHealth;
    }

    /**
     * Get the current amount of stamina.
     * @return A float of the current stamina of the boat.
     */
    public float getCurrentStamina() {
        return this.currentStamina;
    }

    /**
     * Get the distance travelled in a round as a float.
     * @return The distance travelled as a float.
     */
    public float getDistanceTravelled() {
        return this.distanceTravelled;
    }

    /**
     * The type of boat the boat is.
     * @return A BoatType representing a boat.
     */
    public BoatType getType() {
        return this.boatType;
    }

    /**
     * Get the maximum amount of stamina the boat can have at any time.
     * @return A float of the maximum stamina.
     */
    public float getMaxStamina() {
        return this.maxStamina;
    }

    /**
     * Get the current y-velocity of the boat as a string.
     * @return Get the current y-velocity of the boat.
     */
    public float getCurrentSpeed() {
        return this.vel.y;
    }

    /**
     * Set the current health of the boat.
     * @param healthToSet The amount of health to set the boat to.
     */
    public void setCurrentHealth(float healthToSet) {
        this.currentHealth = healthToSet;
    }

    /**
     * Add to the Y velocity of the boat.
     * @param speedToAdd The velocity to add to the y-direction of the current velocity.
     */
    public void increaseYVelocity(float speedToAdd) {
        this.vel.y += speedToAdd;
    }

    /**
     * Add to the X velocity of the boat.
     * @param speedToAdd The velocity to add to the x-direction of the current velocity.
     */
    public void increaseXVelocity(float speedToAdd) {
        this.boatType.handling += speedToAdd;
    }

    /**
     * Set the current value for stamina.
     * @param staminaToSet The value to set the current stamina to.
     */
    public void setCurrentStamina(float staminaToSet) {
        this.currentStamina = staminaToSet;
    }

    /**
     * Set the total time passed.
     * @param timeToSet The time to set to.
     */
    public void setTotalTime(long timeToSet) {
        this.totalTime = timeToSet;
    }

    /**
     * Set weather the boat is currently no colliding.
     * @param toSet The boolean value to set to.
     */
    public void setNoCollide(boolean toSet) {
        this.noCollide = toSet;
    }

    /**
     * Set the total distance travelled.
     * @param newDistance The distance to change to.
     */
    public void setDistanceTravelled(float newDistance) {
        this.distanceTravelled = newDistance;
    }
}
