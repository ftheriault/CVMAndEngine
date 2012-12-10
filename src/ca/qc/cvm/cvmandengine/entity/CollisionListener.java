package ca.qc.cvm.cvmandengine.entity;

import ca.qc.cvm.cvmandengine.scene.CVMAbstractScene;
import ca.qc.cvm.cvmandengine.ui.CVMGameActivity;

public interface CollisionListener {
	public abstract void collidedWith(CVMGameActivity activity, CVMAbstractScene scene, CVMSprite sprite);
}
