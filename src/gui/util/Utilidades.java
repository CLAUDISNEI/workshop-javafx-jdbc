package gui.util;



import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.stage.Stage;

public class Utilidades {
	/*
	 * capturando o palco de um evento de um controle qualquer
	 * transformando para Node e fazendo um dowcast para Stage,
	 * uma vez que o Node captrou uma cena do tipo window que e super
	 * classe de Stage	 * 
	 */
	public static Stage palcoAtual(ActionEvent evento) {
		return (Stage) ((Node) evento.getSource()).getScene().getWindow();
	}
}
