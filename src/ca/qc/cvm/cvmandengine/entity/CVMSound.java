package ca.qc.cvm.cvmandengine.entity;

import org.andengine.audio.sound.Sound;

public class CVMSound {
	private int id;
	private String soundPath;
	private Sound sound;
	
	public CVMSound(int id, String path) {
		this.id = id;
		this.soundPath = path;
	}

	public int getId() {
		return id;
	}

	public String getSoundPath() {
		return soundPath;
	}
	
	public Sound getSound() {
		return sound;
	}
	
	public void setSound(Sound sound) {
		this.sound = sound;
	}
}
