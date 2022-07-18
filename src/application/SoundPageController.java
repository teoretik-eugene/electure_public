package application;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ResourceBundle;
import java.util.concurrent.Callable;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Callback;
import javafx.util.Duration;

public class SoundPageController implements Initializable, Serializable{
	
	/*
	 * ���� ��������
	 */
	private static final long serialVersionUID = 1L;
	// ������ ������/������� �� ����� �����
	@FXML
	private Button playButton;
	// ������ ������������� �����, ��������� � ������
	@FXML
	private Button stopButton;
	// ������ ���������
	@FXML
	private Button volumeButton;
	
	@FXML
	private Slider volumeSlider;
	
	@FXML
	private ListView<Note> noteList;
	// �������� ������� 
	@FXML
	private Slider audioBar;
	
	@FXML
	private Label timeLabel;
	
	@FXML
	private ImageView playImage;
	
	@FXML
	private ImageView stopImage;
	
	@FXML
	private ImageView volumeImage;
	
	@FXML
	private Button addButton;
	
	@FXML
	private Button deleteButton;
	
	@FXML
	private TextField noteArea;
	
	@FXML
	private MenuItem saveButton;
	
	/*	������� ����	*/
	private Media media;
	private MediaPlayer player;	
	private File soundFile;
	private File dir;
	private NoteData savedFile;
	private File lectFile;
	private ObjectInputStream inputStream;
	private ObjectOutputStream outputStream;
	
	private boolean isPlaying;
	private double duration;
	private boolean endOfAudio;
	private boolean isMuted;
	private double lastVolume;
	private Note lastPeak;
	
	/*
	 * ����� ������� ���� name.lect
	 */
	
	/*������������� ������*/
	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		System.out.println("init");
		/*���� ����� ���� ����������, �� ��������� ������*/
		volumeSlider.setValue(1);
		audioBar.setValue(0);
		isPlaying = false;				// ���� �� ��������������� �������������
		endOfAudio = false;				// ����� ����� ��� �� ������
		isMuted = false;		// ���� �� ��������
		lastVolume = 1;
		
		/*�������� Cell Factory
		 * ����������� ��� ��� ��������*/
		noteList.setCellFactory(new Callback<ListView<Note>, ListCell<Note>>() {

			@Override
			public ListCell<Note> call(ListView<Note> arg0) {
				
				return new ListCell<>() {
					@Override
					protected void updateItem(Note note, boolean empty) {
						super.updateItem(note, empty);
						if(empty || note == null)
							setText(null);
						else {
							setText(note.getNote());
						}
					};
				};
			}
		});
		
		/*���������� ��� �������*/
		noteList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Note>() {

			@Override
			public void changed(ObservableValue<? extends Note> arg0, Note arg1, Note arg2) {
				lastPeak = noteList.getSelectionModel().getSelectedItem();			
			}
		});
		
		playButton.setOnAction(this::playAudio);
		stopButton.setOnAction(this::stopAudio);
		volumeButton.setOnAction(this::muteVolume);
		addButton.setOnAction(this::addNote);
		deleteButton.setOnAction(this::deleteNote);
		saveButton.setOnAction(arg0 -> {
			try {
				saveAction(arg0);
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println(e);
			}
		});
		
		noteList.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent mouseEvent) {
				if(mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
					if(mouseEvent.getClickCount() == 2) {
						if(noteList.getItems().size() == 0)
							return;
						System.out.println("Click 2 times");
						double current = lastPeak.getTimePos().toSeconds();		// �������� �������
						player.seek(Duration.seconds(current));
					}
				}
				
			}
			
		});
	}
	
	/* ����� ��� �������� ��������������� �����*/
	public void setSoundFile(File soundFile, File dir) throws IOException {
		
		System.out.println("==meth==");		// �������� �����
		this.soundFile = soundFile;		// ��������� ���������� ����
		//System.out.println("this: " + this.soundFile);
		this.dir = dir;
		media = new Media(soundFile.toURI().toString());
		player = new MediaPlayer(media);			// ��������� MediaPlayer
		/* �������� ����� ���������� */
		System.out.println("Check save:\n" + savedFile);
		if(savedFile == null) {				// ���� ��� ������ �� ����������� ����
			// �������� �����, ������� ����� ���������������
			System.out.println("Creating new File");
			savedFile = new NoteData();	// ���� ���������� ��������� �� �����
			
			// ������� ���� ��� .lect �����
			lectFile = new File(dir.toString() + "\\" + 
					soundFile.getName().substring(0, soundFile.getName().length() - 4) + ".lect");
			lectFile.createNewFile();		// ������� ����
			
			// �������� ��������� ������, � ��� ��������� �������� ��������� �����
			/*
			 * ���� ��������, ��� �������� ObjectOutputStream � ���� ������������ 
			 * ���������� ������
			 */
			outputStream = new ObjectOutputStream(new FileOutputStream(lectFile));
			savedFile.load(soundFile, dir, lastVolume, player.getCurrentTime(), noteList.getItems());
			outputStream.writeObject(savedFile);
		}
		/*���� ����������� ���� ������������*/
		else {
			System.out.println("Already exist");
			// ��������� �������
			noteList.getItems().addAll(savedFile.getNotes());
			// ������ ������������� ���������
			volumeSlider.setValue(savedFile.getVolumeValue());
			// ������ ������� �� �������
			//audioBar.setValue(savedFile.getDuration().toSeconds());
			
			//outputStream = new ObjectOutputStream(new FileOutputStream(lectFile));
			//outputStream.writeObject(savedFile);
		}
		
		/*���������� ����� �� ������*/
		player.volumeProperty().bindBidirectional(volumeSlider.valueProperty());
		
		// ������������� ����� ����-����
		bindCurrentTimeLabel();
		
		player.setOnReady(new Runnable() {
			
			@Override
			public void run() {
				duration = media.getDuration().toSeconds();
				audioBar.setMax(duration);
				
				/*�������� �������� �������*/
				player.currentTimeProperty().addListener(ev ->{
					if(!audioBar.isValueChanging()) {
						double current = player.getCurrentTime().toSeconds();
						System.out.println(current);
						audioBar.setValue(current);
						// ���� ������ ����� ��������������
					}
				});
				
				/*����������� �������� �������*/
				audioBar.valueProperty().addListener(new ChangeListener<Number>() {
					@Override
					public void changed(ObservableValue<? extends Number> arg0, Number arg1, Number arg2) {
						if(audioBar.isValueChanging()) {
							player.seek(Duration.seconds(audioBar.getValue()));
							System.out.println("Change: " + audioBar.getValue());
							System.out.println(audioBar.isValueChanging());
						}
					}
				});		
			}
		});
		
		/*����� �����*/
		player.setOnEndOfMedia(new Runnable() {
			
			@Override
			public void run() {
				System.out.println("End of media");
				player.stop();
				isPlaying = false;
				endOfAudio = true;
				player.seek(Duration.seconds(0.0));
				audioBar.setValue(0.0);
			}
		});
	}
	
	/*��������� Label � ����� ������������*/
	private void bindCurrentTimeLabel() {
		timeLabel.textProperty().bind(Bindings.createStringBinding(new Callable<String>() {
			@Override
			public String call() throws Exception {
				return formatTime((int)player.getCurrentTime().toSeconds());
			}
		}, player.currentTimeProperty()));
	}
	
	/*NoteData*/
	public void setNoteData(NoteData inputData, File openedFile) {
		savedFile = inputData;		// inputData - ��� ����� ����������
		lectFile = openedFile;		// openedFile - ��� ���� ���� .lect
	}
	
	/*����� ������������/����� �����*/
	public void playAudio(ActionEvent event) {
		if(isPlaying) {
			isPlaying = false;
			player.pause();
		}
		else {
			isPlaying = true;
			player.play();
		}
	}
	
	/*����� ���������*/
	public void stopAudio(ActionEvent event) {
		player.stop();
		isPlaying = false;
	}
	
	/*���������/�������� ����*/
	public void muteVolume(ActionEvent event) {
		if(isMuted) {
			volumeSlider.setValue(lastVolume);
			isMuted = false;
		}
		else {
			lastVolume = volumeSlider.getValue();
			volumeSlider.setValue(0);
			isMuted = true;
		}
	}
	
	/*��������� �� �������*/
	public void spaceStop(KeyEvent event) {
		if(event.getCode() == KeyCode.SPACE) {
			if(isPlaying) {
				isPlaying = false;
				player.pause();
			}
			else {
				isPlaying = true;
				player.play();
			}
		}
	}
	
	/*����� �������������� ������� �� ������ � ���������� �����*/
	private String formatTime(int seconds) {
		
		int sec = seconds % 60;
		int minutes = seconds / 60;
		int hours = minutes / 60;
		minutes = minutes % 60;
		
		String result = "";
		
		if(hours > 0) {
			if(hours >= 10)
				result += String.valueOf(hours) + ":";
			else
				result += "0" + String.valueOf(hours) + ":";
		}
		if(minutes >= 0) {
			if(minutes >= 10)
				result += String.valueOf(minutes) + ":";
			else
				result += "0" + String.valueOf(minutes) + ":";
		}
		
		if(sec >= 0) {
			if(sec >= 10)
				result += String.valueOf(sec);
			else
				result += "0" + String.valueOf(sec);
		}
		
		return result;
	}
	
	/*������, ����������� � ��������*/
	public void addNote(ActionEvent event) {
		if(!noteArea.getText().trim().isEmpty()) {
			Note note = new Note();
			note.note = noteArea.getText();
			note.timePos = player.getCurrentTime();
			noteList.getItems().add(note);
			System.out.println(note.note);
			noteArea.clear();
		}
		else
			System.out.println("Enter again please");
	}
	
	/*������� �������*/
	public void deleteNote(ActionEvent event) {
		noteList.getItems().remove(lastPeak);
	}
	
	public void saveAction(ActionEvent event) throws IOException {
		savedFile.load(soundFile, dir, volumeSlider.getValue(),
				player.getCurrentTime(), noteList.getItems());
		
		// ��� �� Stack-Overflow, ����� �������� ���� � �������� ��-������
		//Files.newInputStream(Paths.get(lectFile.toURI()), StandardOpenOption.TRUNCATE_EXISTING);
		
		outputStream = new ObjectOutputStream(new FileOutputStream(lectFile));
		outputStream.writeObject(savedFile);	
		
	}
	
}
