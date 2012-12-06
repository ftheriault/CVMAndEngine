package ca.qc.cvm.cvmandengine.entity;

import ca.qc.cvm.cvmandengine.scene.CVMAbstractScene;

public interface ManagedUpdateListener {
	public abstract void managedUpdate(float secondsElapsed, CVMAbstractScene scene);
}
