package application;
	
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;


public class Main extends Application {
	
	/*Кнопка открытия нового файла*/
	@FXML
	private Button openNewButton;
	
	@FXML
	private Button openButton;
	
	/*Обычные поля*/
	private DirectoryChooser direct;
	private FileChooser fileChoose;
	private File soundFile;
	private DirectoryChooser directoryChooser;
	private ObjectInputStream inputStream;
	
	@Override
	public void init() throws Exception {
		System.out.println("Start the programm");
		super.init();
	}
	
	@Override
	public void stop() throws Exception {
		System.out.println("Finish the programm");
		super.stop();
	}
	
	@Override
	public void start(Stage primaryStage) {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("MainPage.fxml"));
			//BorderPane root = new BorderPane();
			Scene scene = new Scene(root,800,600);
			primaryStage.setScene(scene);
			primaryStage.getIcons().add(new Image("C:\\Users\\genyl\\eclipse-workspace\\ELect\\src\\Lectures\\resources\\Images\\icon.png"));
			primaryStage.setTitle("E-Lecture");
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
	/*Методы для событий*/
	public void openNew(ActionEvent event) throws IOException {
		
		direct = new DirectoryChooser();
		fileChoose = new FileChooser();
		
		// Добавляем фильтр, чтобы открывать mp3 files
		fileChoose.getExtensionFilters().addAll(
				new FileChooser.ExtensionFilter("Audio Files", "*mp3", "*m4a", "*wav")
				);
		
		/* Достали stage и установили новый файл
		 * поработать над директорией
		 */
		Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		soundFile = fileChoose.showOpenDialog((Stage)((Node)event.getSource()).getScene().getWindow());
		
		// Если выбранный файл не пустой, то открываем окно и передаем файл
		if(soundFile != null) {
			
			/*Выбрать директорию*/
			DirectoryChooser directoryChooser = new DirectoryChooser();
			File dir = directoryChooser.showDialog(stage);
			if(dir != null) {
				System.out.println("Directory: " + dir.toString());
				System.out.println("File: " + soundFile);
				FXMLLoader loader = new FXMLLoader(getClass().getResource("SoundPage.fxml"));
				Parent root = loader.load();
				SoundPageController pageController = loader.getController();
				pageController.setSoundFile(soundFile, dir);
				stage.setMinWidth(1000);
				stage.setMinHeight(600);
				stage.setTitle("Новый файл");
				Scene scene = new Scene(root);
				stage.setScene(scene);
				scene.setOnKeyPressed(pageController::spaceStop);
				stage.setTitle("E-Lecture - " + soundFile.getName());
				//stage.setMaximized(true);
				stage.show();
			}
		}		
	}
	
	/*Открыть существующий файл*/
	public void openExist(ActionEvent event) throws IOException, ClassNotFoundException {
		// Создали FileChooser
		fileChoose = new FileChooser();
		// Дополнили фильтрами
		fileChoose.getExtensionFilters().addAll(
				new FileChooser.ExtensionFilter("Lecture Files", "*lect")
				);
		
		Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		// Открыли диалоговое окно для выбора файла
		// Файл типа .lect
		soundFile = fileChoose.showOpenDialog(stage);
		
		// В данном случае soundFile - это файл типа .lect
		if(soundFile != null) {		// Если ссылка на файл не пустая
			System.out.println(soundFile);		// Вывести адрес файла
			FXMLLoader loader = new FXMLLoader(getClass().getResource("SoundPage.fxml"));
			Parent root = loader.load();
			SoundPageController pageController = loader.getController();
			System.out.println("Disser");
			// Дессиреализация					// Считываем из файла .lect
			FileInputStream fileInputStream = new FileInputStream(soundFile);
			inputStream = new ObjectInputStream(fileInputStream);
			// Получаем сохраненные данные 
			NoteData inputData = (NoteData) inputStream.readObject();
			System.out.println("Input data: " + inputData.toString());
			// Закрываем
			fileInputStream.close();
			inputStream.close();
			// Передаем объект с сохранениями и ссылку на файл типа .lect
			pageController.setNoteData(inputData, soundFile);
			pageController.setSoundFile(inputData.getSoundFile(), inputData.getDirectory());
			
			stage.setMinWidth(1000);
			stage.setMinHeight(600);
			Scene scene = new Scene(root);
			stage.setScene(scene);
			scene.setOnKeyPressed(pageController::spaceStop);
			stage.setTitle("E-Lecture - " + soundFile.getName());
			stage.show();
		}
	}
}
