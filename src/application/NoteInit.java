package application;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javafx.util.Duration;

public interface NoteInit {
	/*��������� ������ � ����������*/
	public void download();
	
	/*��������� ����� �������*/
	public void addNote();
	
	/*��������� ������ �� ����������*/
	public void load(File soundFile, File directory, double volumeValue, 
			Duration timePos, List<Note> noteLists);
	
	public File getDirectory();
	
	public Duration getDuration();
	
	public File getSoundFile();
	
	public double getVolumeValue();
	
	public List<Note> getNotes();
	
}
