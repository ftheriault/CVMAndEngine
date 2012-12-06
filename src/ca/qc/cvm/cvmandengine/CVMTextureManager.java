package ca.qc.cvm.cvmandengine;

import java.util.ArrayList;
import java.util.List;

import org.andengine.opengl.texture.TextureManager;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.TextureRegion;

import ca.qc.cvm.cvmandengine.entity.CVMTexture;
import ca.qc.cvm.cvmandengine.ui.CVMGameActivity;

public class CVMTextureManager {
	private List<CVMTexture> textureList;
	
	public CVMTextureManager() {
		textureList = new ArrayList<CVMTexture>();
	}

	public void load(TextureManager textManager, CVMGameActivity context) {		
		for (CVMTexture texture : textureList) {
			BitmapTextureAtlas textureAtlas = new BitmapTextureAtlas(textManager, texture.getWidth(), texture.getHeight());
	    	TextureRegion spriteRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(textureAtlas, context, texture.getTexturePath(), 0, 0);
	    	
	    	texture.setTextureRegion(spriteRegion);
	    	
	    	textureAtlas.load();
		}
	}
	
	public void addTexture(CVMTexture texture) {
		textureList.add(texture);
	}
	
	public TextureRegion getTextureById(int id) {
		TextureRegion region = null;
		
		for (CVMTexture texture : textureList) {
			if (texture.getTextureId() == id) {
				region = texture.getTextureRegion();
			}
		}
		
		return region;
	}
}
