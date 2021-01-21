package com.dragonboatrace.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.dragonboatrace.game.entities.Entity;
import com.dragonboatrace.game.entities.Obstacle;

public class Background extends Entity {

    protected Texture[] allTextures;
    protected Texture img;

    public Background(Vector2 pos) {
        super(pos, new Vector2(0, 0), 0);
        loadTextures();
        updateTexture();
    }

    public void render(SpriteBatch batch, Vector2 relPos) {

        //if the background tile has gone off the screen move it to the top and give it a new texture
        if (relPos.y - this.pos.y > this.img.getHeight()) {
            this.pos.y += Gdx.graphics.getHeight() + this.img.getHeight();
            updateTexture();
        }

        batch.begin();
        batch.draw(
                this.img,
                (0),
                (this.pos.y - relPos.y),
                Gdx.graphics.getWidth(),
                this.size.y
        );
        batch.end();
    }

    private void updateTexture() {
        this.img = this.allTextures[(int) (Math.random() * allTextures.length)];
        this.size = new Vector2(this.img.getWidth(), this.img.getHeight());
    }

    private void loadTextures() {
        String[] fileNames = Gdx.files.local("Backgrounds/catalog2.txt").readString().split("\n");
        String[] fileNames2 = Gdx.files.local("Backgrounds/catalog.txt").readString().split("\n");
        this.allTextures = new Texture[fileNames.length];
        try {
            for (int i = 0; i < fileNames.length; i++) {
                this.allTextures[i] =  new Texture(Gdx.files.local("Backgrounds/" + fileNames[i].replace("\r", "")));
                //this.allTextures[i] = new Texture(String.format("Backgrounds/%s", fileNames[i]));
            }
        } catch (Exception e) {
            for (int i = 0; i < fileNames.length; i++) {
                this.allTextures[i] =  new Texture(Gdx.files.local("Backgrounds/" + fileNames2[i].replace("\r", "")));
                //this.allTextures[i] = new Texture(String.format("Backgrounds/%s", fileNames2[i]));
            }
        }
    }

    public void dispose() {
        for (Texture t : this.allTextures) {
            t.dispose();
        }
    }

    public void collide(Obstacle o) {
    }

    public void move(float deltaTime) {
    }

}