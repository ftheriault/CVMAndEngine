package ca.qc.cvm.cvmandengine;

import java.util.ArrayList;
import java.util.List;

import org.andengine.opengl.texture.TextureManager;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.BaseTextureRegion;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.opengl.texture.region.TiledTextureRegion;

import ca.qc.cvm.cvmandengine.entity.CVMTexture;
import ca.qc.cvm.cvmandengine.ui.CVMGameActivity;

public class CVMTextureManager {
	private List<CVMTexture> textureList;
	
	public CVMTextureManager() {
		textureList = new ArrayList<CVMTexture>();
	}

	public void load(TextureManager textManager, CVMGameActivity context) {		
		for (CVMTexture texture : textureList) {
			if (texture.isTiled()) {
				BitmapTextureAtlas textureAtlas = new BitmapTextureAtlas(textManager, texture.getWidth(), texture.getHeight());
				TiledTextureRegion spriteRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(textureAtlas, context, texture.getTexturePath(), 0, 0, texture.getColumnCount(), texture.getRowCount());

		    	texture.setTextureRegion(spriteRegion);
		    	textureAtlas.load();
			}
			else {
				BitmapTextureAtlas textureAtlas = new BitmapTextureAtlas(textManager, texture.getWidth(), texture.getHeight());
		    	TextureRegion spriteRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(textureAtlas, context, texture.getTexturePath(), 0, 0);
		    	
		    	texture.setTextureRegion(spriteRegion);
		    	textureAtlas.load();
			}
		}
	}
	
	public void addTexture(CVMTexture texture) {
		textureList.add(texture);
	}
	
	public BaseTextureRegion getTextureById(int id) {
		BaseTextureRegion region = null;
		
		for (CVMTexture texture : textureList) {
			if (texture.getTextureId() == id) {
				region = texture.getTextureRegion();
			}
		}
		
		return region;
	}
	
	public boolean isTiled(int id) {
		boolean tiled = false;
		
		for (CVMTexture texture : textureList) {
			if (texture.getTextureId() == id) {
				tiled = texture.isTiled();
			}
		}
		
		return tiled;
	}
}
