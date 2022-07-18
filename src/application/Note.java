package application;

import java.io.Serializable;

import javafx.util.Duration;

/*
 * Добавить Comparable, 
 */
public class Note implements Serializable {
	
	private static final long serialVersionUID = 2L;
	public String note;
	public Duration timePos;
	public StringBuilder text;
	
	/*		Пока в разработке
	public Note(String note, Duration timePos, StringBuilder text) {
		this.note = note;
		this.timePos = timePos;
		this.text = text;
	}
	*/
	public void setNote(String note) {
		this.note = note;
	}
	public void setTimePos(Duration timePos) {
		this.timePos = timePos;
	}
	public void setText(StringBuilder text) {
		this.text = text;
	}
	
	/*Getters*/
	public String getNote() {
		return note;
	}
	public Duration getTimePos() {
		return timePos;
	}
	public StringBuilder getText() {
		return text;
	}
	
}
