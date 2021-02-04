package com.dragonboatrace.game.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;

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

    public Obstacle(JsonValue jsonString) {
        super(new Vector2(jsonString.get("pos").getFloat("x"), jsonString.get("pos").getFloat("y")),
                new Json().fromJson(ObstacleType.class,jsonString.getString("type")).getSize(),
                new Json().fromJson(ObstacleType.class,jsonString.getString("type")).getWeight());
        this.vel = new Vector2(jsonString.get("vel").getFloat("x"), jsonString.get("vel").getFloat("y"));
        this.constantVel = new Vector2(jsonString.get("constantVel").getFloat("x"), jsonString.get("constantVel").getFloat("y"));
        this.obstacleType = new Json().fromJson(ObstacleType.class,jsonString.getString("type"));

    }

    public String save() {
        return String.format("{type:%s, pos:{x:%f, y:%f}, vel:{x:%f, y:%f}, constantVel:{x:%f, y:%f}}",
                this.obstacleType,
                this.inGamePos.x,
                this.inGamePos.y,
                this.vel.x,
                this.vel.y,
                this.constantVel.x,
                this.constantVel.y
                );
    }

    public void collide(Obstacle o) {

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
