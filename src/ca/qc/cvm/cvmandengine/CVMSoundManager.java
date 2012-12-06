package ca.qc.cvm.cvmandengine;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.andengine.audio.sound.Sound;
import org.andengine.audio.sound.SoundFactory;
import org.andengine.audio.sound.SoundManager;

import ca.qc.cvm.cvmandengine.entity.CVMSound;
import ca.qc.cvm.cvmandengine.ui.CVMGameActivity;

public abstract class CVMSoundManager {
	public List<CVMSound> soundList;
	
	public CVMSoundManager() {
		soundList = new ArrayList<CVMSound>();
	}
	
	public void load(SoundManager soundManager, CVMGameActivity cvmGameActivity) throws IOException {
		for (CVMSound sound : soundList) {
			Sound tmp = SoundFactory.createSoundFromAsset(soundManager, cvmGameActivity, sound.getSoundPath());
			sound.setSound(tmp);
		}
	}
	
	public void addSound(CVMSound sound) {
		soundList.add(sound);
	}
	
	public void playSound(int soundId) {
		CVMSound soundToPlay = null;
		
		for (CVMSound sound : soundList) {
			if (sound.getId() == soundId) {
				soundToPlay = sound;
				break;
			}
		}
		
		if (soundToPlay != null) {
			soundToPlay.getSound().play();
		}
	}
}
