package com.dragonboatrace.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.dragonboatrace.game.entities.Entity;

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
        FileHandle backgroundsDirectory = Gdx.files.local("Backgrounds");
        FileHandle[] backgroundFiles = backgroundsDirectory.list();
        this.allTextures = new Texture[backgroundFiles.length];
        for (int i = 0; i < backgroundFiles.length; i++) {
            this.allTextures[i] = new Texture(backgroundFiles[i]);
        }
    }

    public void dispose() {
        for (Texture t : this.allTextures) {
            t.dispose();
        }
    }

    public void move(float deltaTime) {
    }

}