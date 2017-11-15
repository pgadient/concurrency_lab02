package support;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class SoundClips {
	private Clip crashSound;
	
	public SoundClips() {
		this.crashSound = getClip("/resources/crash.wav");
	}

	public void crash() {
		this.makeNoise(this.crashSound);
	}
	
	private void makeNoise(Clip clip) {
		if (clip != null) {
            clip.setFramePosition(0); //rewind
			clip.start();
		}
	}
	
	public Clip getClip(String path) {
		try {
			InputStream inputStream = new BufferedInputStream(getClass().getResourceAsStream(path));
			AudioFileFormat format = AudioSystem.getAudioFileFormat(inputStream);
			DataLine.Info info = new DataLine.Info(Clip.class, format.getFormat());
            Clip clip = (Clip) AudioSystem.getLine(info);
			AudioInputStream audioStream = AudioSystem.getAudioInputStream(inputStream);            
			clip.open(audioStream);
            audioStream.close();
			return clip;
		} catch (LineUnavailableException lue) {
			lue.printStackTrace();
			return null;
		} catch (UnsupportedAudioFileException uafe) {
			uafe.printStackTrace();
			return null;
		} catch (IllegalArgumentException iae) {
			iae.printStackTrace();
			return null;
		} catch (IOException ioe) {
			ioe.printStackTrace();
			return null;
		}
	}
}
