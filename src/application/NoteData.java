package application;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javafx.util.Duration;

public class NoteData implements Serializable, NoteInit{
	
	private static final long serialVersionUID = 3L;
	/*������ ��� ����������*/
	private File soundFile;			// ������ �� ���� ���� mp3, m4a, wav
	private File directory;			// ������ �� ����������
	private List<Note> noteList;	// ������ � ���������
	private double volumeValue;		// ��������� ������������� ���������
	private Duration timePos;		// ������� �������������
	/*
	public NoteData(File file) {
		soundFile = file;		// ��������� ������ �� ����� ����
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
		// ���� �������� ���������
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

	/*������� ����*/
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
