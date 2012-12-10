package ca.qc.cvm.cvmandengine.entity;

import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.region.BaseTextureRegion;
import org.andengine.opengl.texture.region.TextureRegion;

public abstract class CVMSprite {
	private float initialX;
	private float initialY;
	private int width;
	private int height;
	private int textureId;
	
	private BaseTextureRegion textureRegion;
	private Sprite sprite;
	
	public CVMSprite(float initialX, float initialY, int width, int height, int textureId) {
		this.initialX = initialX;
		this.initialY = initialY;
		this.width = width;
		this.height = height;
		this.textureId = textureId;
	}
	
	public BaseTextureRegion getTextureRegion() {
		return textureRegion;
	}
	
	public void setTextureRegion(BaseTextureRegion textureRegion) {
		this.textureRegion = textureRegion;
	}
	
	public Sprite getSprite() {
		return sprite;
	}
	
	public void setSprite(Sprite sprite) {
		this.sprite = sprite;
		
		if (sprite != null) {
			sprite.setUserData(this);
		}
	}
	
	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public float getInitialX() {
		return initialX;
	}

	public float getInitialY() {
		return initialY;
	}

	public int getTextureId() {
		return textureId;
	}
}
