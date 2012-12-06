package ca.qc.cvm.cvmandengine.entity;

import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.region.TextureRegion;

public abstract class CVMSprite {
	private float posX;
	private float posY;
	private int width;
	private int height;
	private String texturePath;
	
	private TextureRegion textureRegion;
	private Sprite sprite;
	
	public CVMSprite(float posX, float posY, int width, int height, String texturePath) {
		this.posX = posX;
		this.posY = posY;
		this.width = width;
		this.height = height;
		this.texturePath = texturePath;
	}
	
	public TextureRegion getTextureRegion() {
		return textureRegion;
	}
	
	public void setTextureRegion(TextureRegion textureRegion) {
		this.textureRegion = textureRegion;
	}
	
	public Sprite getSprite() {
		return sprite;
	}
	
	public void setSprite(Sprite sprite) {
		this.sprite = sprite;
	}
	
	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public float getPosX() {
		return posX;
	}

	public float getPosY() {
		return posY;
	}

	public String getTexturePath() {
		return texturePath;
	}
}
