package application;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javafx.util.Duration;

public class NoteData implements Serializable, NoteInit{
	
	private static final long serialVersionUID = 3L;
	/*Данные для сохранения*/
	private File soundFile;			// Ссылка на файл типа mp3, m4a, wav
	private File directory;			// Ссылка на директорию
	private List<Note> noteList;	// Список с заметками
	private double volumeValue;		// Последняя установленная громкость
	private Duration timePos;		// Позиция прослушивания
	/*
	public NoteData(File file) {
		soundFile = file;		// Указываем ссылку на аудио файл
	}*/

	@Override
	public void download() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addNote() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void load(File soundFile, File directory, double volumeValue, 
			Duration timePos, List<Note> noteLists) {		
		
		this.soundFile = soundFile;
		this.directory = directory;
		this.volumeValue = volumeValue;
		this.timePos = timePos;
		// Типа очистили коллекцию
		ArrayList<Note> ls = new ArrayList<>();
		noteList = ls;
		this.noteList.addAll(noteLists);
		
	}

	@Override
	public File getDirectory() {
		return directory;
	}

	@Override
	public Duration getDuration() {
		return timePos;
	}

	/*Вернуть файл*/
	@Override
	public File getSoundFile() {
		return soundFile;		
	}

	@Override
	public double getVolumeValue() {
		return volumeValue;
	}

	@Override
	public List<Note> getNotes() {
		return noteList;
	}
	
	public void setDirectory(File dir) {
		directory = dir;
	}
	
	@Override
	public String toString() {
		return "SoundFile: " + soundFile + "\n" + "TimePos: " + timePos + "\n" + 
				"Volume: " + volumeValue + "\n" + "Notes: " + noteList;
	}
}
