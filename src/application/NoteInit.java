package application;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javafx.util.Duration;

public interface NoteInit {
	/*Загрузить данные в приложение*/
	public void download();
	
	/*Загрузить новую заметку*/
	public void addNote();
	
	/*Загрузить данные из приложения*/
	public void load(File soundFile, File directory, double volumeValue, 
			Duration timePos, List<Note> noteLists);
	
	public File getDirectory();
	
	public Duration getDuration();
	
	public File getSoundFile();
	
	public double getVolumeValue();
	
	public List<Note> getNotes();
	
}
