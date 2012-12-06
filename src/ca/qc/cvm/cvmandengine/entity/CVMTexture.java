package ca.qc.cvm.cvmandengine.entity;

import org.andengine.opengl.texture.region.TextureRegion;

public class CVMTexture {
	private String texturePath;
	private int width;
	private int height;
	private int textureId;
	
	private TextureRegion textureRegion;
	
	public CVMTexture(int textureId, String texturePath, int width, int height) {
		this.texturePath = texturePath;
		this.width = width;
		this.height = height;
		this.textureId = textureId;
	}

	public int getTextureId() {
		return textureId;
	}
	
	public String getTexturePath() {
		return texturePath;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public TextureRegion getTextureRegion() {
		return textureRegion;
	}

	public void setTextureRegion(TextureRegion textureRegion) {
		this.textureRegion = textureRegion;
	}
	
	
}
