package ca.qc.cvm.cvmandengine.entity;

import org.andengine.opengl.texture.region.BaseTextureRegion;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.opengl.texture.region.TiledTextureRegion;

public class CVMTexture {
	private String texturePath;
	private int width;
	private int height;
	private int textureId;
	private int columnCount;
	private int rowCount;
	
	private BaseTextureRegion textureRegion;
	
	public CVMTexture(int textureId, String texturePath, int width, int height) {
		this.texturePath = texturePath;
		this.width = width;
		this.height = height;
		this.textureId = textureId;
	}
	
	public CVMTexture(int textureId, String texturePath, int width, int height, int columnCount, int rowCount) {
		this.texturePath = texturePath;
		this.width = width;
		this.height = height;
		this.textureId = textureId;
		this.columnCount = columnCount;
		this.rowCount = rowCount;
	}
	
	public boolean isTiled() {
		return columnCount > 0 || rowCount > 0;
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

	public BaseTextureRegion getTextureRegion() {
		return textureRegion;
	}

	public void setTextureRegion(BaseTextureRegion textureRegion) {
		this.textureRegion = textureRegion;
	}
	
	public int getColumnCount() {
		return columnCount;
	}

	public int getRowCount() {
		return rowCount;
	}
	
	
}
