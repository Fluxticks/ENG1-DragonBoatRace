package com.dragonboatrace.game.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.dragonboatrace.game.Tuple;

import java.util.ArrayList;

public abstract class Boat extends Entity {

    protected BoatType boatType;
    protected float currentHealth, currentMaxSpeed;
    protected ArrayList<Obstacle> collided;
    protected float stamina, maxStamina, timePenalties, penaltyResetDelay, defaultHandling;
    protected long finishTime;
    protected boolean finished = false;
    protected float distanceTravelled; //distance travelled in one round
    protected long totalTime;
    protected Tuple<Float, Float> laneBounds;
    private boolean noCollide = false;
    protected Texture image;

    protected Vector2 startPos;

    public Boat(BoatType boatType, Vector2 pos, Tuple<Float, Float> laneBounds) {
        super(pos.cpy(), boatType.getSize().cpy(), boatType.getWeight());
        this.boatType = boatType;
        this.currentHealth = this.boatType.getMaxHealth();
        this.currentMaxSpeed = this.boatType.getSpeed();
        this.collided = new ArrayList<>();
        this.stamina = 1000;
        this.maxStamina = 1000;
        this.defaultHandling = boatType.handling;
        this.distanceTravelled = 0;
        this.totalTime = 0;
        this.laneBounds = laneBounds;
        if (!this.boatType.imageSrc.equals("Testing"))
            this.image = new Texture(this.boatType.imageSrc);

    }

    public void checkForCollision(Obstacle o){
        if(!this.noCollide)doCollision(super.checkCollision(o), o);
    }

    public void doCollision(boolean colliding, Obstacle o){

        if (colliding) {
            if (!this.collided.contains(o)) {
                this.collided.add(o);
                this.currentHealth -= o.weight;
                this.currentMaxSpeed = this.boatType.getSpeed() / o.weight;
                this.stamina = Math.max(this.stamina - 100 * o.weight, 0);
            }
        } else if (this.collided.contains(o)) {
            this.collided.remove(o);
            this.currentMaxSpeed = this.boatType.getSpeed();
        }
    }

    public void render(SpriteBatch batch, Vector2 relPos) {
        batch.begin();
        batch.draw(this.image,
                (this.pos.x), (this.pos.y - relPos.y),
                this.size.x, this.size.y);
        batch.end();
    }

    @Override
    public void update(float deltaTime) {
        this.currentMaxSpeed = this.boatType.getSpeed();
        if (this.collider != null) {
            this.currentMaxSpeed /= this.collider.weight;
        }

        super.update(deltaTime);
    }

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

    public long getTotalTimeLong(){
        return this.totalTime;
    }

    public void saveStartPos() {
        this.startPos = this.pos.cpy();
    }

    public void moveToStart() {
        this.inGamePos = startPos.cpy();
        this.pos = startPos.cpy();
        this.hitbox.setToPosition(this.inGamePos);
        this.vel = new Vector2();
        this.stamina = maxStamina;
        this.distanceTravelled = 0;
        this.totalTime += finishTime;
        this.finishTime = 0;
        this.boatType.handling = defaultHandling;
        this.finished = false;
    }

    public void dispose() {
        this.image.dispose();
    }

    public boolean isFinished(int finishLine) {
        return this.distanceTravelled >= finishLine;
    }

    public void setFinishTime(long finishTime) {
        this.finishTime = finishTime;
    }

    public float getPenalty() {
        return this.timePenalties;
    }

    public long getFinishTimeLong() {
        return this.finishTime;
    }

    public float getMaxSpeed() {
        return this.currentMaxSpeed;
    }

    public float getHealth() {
        return this.currentHealth;
    }

    public float getStamina() {
        return this.stamina;
    }

    public float getDistanceTravelled() {
        return this.distanceTravelled;
    }

    public BoatType getType() {
        return this.boatType;
    }

    public float getMaxStamina() {
        return this.maxStamina;
    }

    public float getCurrentSpeed() {
        return this.vel.y;
    }

    public Tuple<Float,Float> getLaneBounds(){
        return this.laneBounds;
    }

    public void setCurrentHealth(float healthToSet){
        this.currentHealth = healthToSet;
    }

    public void increaseYVelocity(float speedToAdd){
        this.vel.y += speedToAdd;
    }

    public void increaseXVelocity(float speedToAdd){
        this.boatType.handling += speedToAdd;
    }

    public void setCurrentStamina(float staminaToSet)
    {
        this.stamina = staminaToSet;
    }

    public void setTotalTime(long timeToSet){
        this.totalTime = timeToSet;
    }

    public void setNoCollide(boolean toSet){
        this.noCollide = toSet;
    }

    public void setDistanceTravelled(float newDistance){
        this.distanceTravelled = newDistance;
    }
}
