package com.dragonboatrace.game.entities;

import com.badlogic.gdx.math.Vector2;

public class EntityHitbox {

    private Vector2 position;
    private Vector2 size;

    public EntityHitbox(Vector2 pos, Vector2 size){
        this.position = pos;
        this.size = size;
    }

    public void setToPosition(Vector2 newPos){
        this.position = newPos.cpy();
    }

    public void movePosition(Vector2 addPos){
        this.position.add(addPos);
    }

    public boolean checkCollision(EntityHitbox otherEntity){
        return this.position.x + this.size.x > otherEntity.getPosition().x && this.position.x < otherEntity.getPosition().x + otherEntity.getSize().x && this.position.y < otherEntity.getPosition().y + otherEntity.getSize().y && this.position.y + this.size.y > otherEntity.getPosition().y;
    }

    public Vector2 getPosition() {
        return position;
    }

    public Vector2 getSize() {
        return size;
    }
}
