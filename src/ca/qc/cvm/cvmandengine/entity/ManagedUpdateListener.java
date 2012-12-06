package ca.qc.cvm.cvmandengine.entity;

import org.andengine.input.touch.TouchEvent;

import ca.qc.cvm.cvmandengine.scene.CVMAbstractScene;
import ca.qc.cvm.cvmandengine.ui.CVMGameActivity;

public interface ManagedUpdateListener {
	public abstract void managedUpdate(float secondsElapsed);
}
