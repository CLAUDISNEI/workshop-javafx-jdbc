package application;
	
import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.stage.Stage;


public class Main extends Application {
	
	//atributo declarado para poder fornecer a cena para outra tela
	private static Scene cenaPrincipal;
	
	@Override
	public void start(Stage primaryStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/MainView.fxml"));
			ScrollPane scrollPane = loader.load();
			
			scrollPane.setFitToHeight(true);
			scrollPane.setFitToWidth(true);
			
			cenaPrincipal = new Scene(scrollPane);
			primaryStage.setScene(cenaPrincipal);
			primaryStage.setTitle("Exemplo de aplicação JavaFX");
			primaryStage.show();
			} catch (IOException e) {
			e.printStackTrace();
			}
	}
	
	//metodo que fornece a cena para outra
	public static Scene obterCenaPrincipal() {
		return cenaPrincipal;
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
