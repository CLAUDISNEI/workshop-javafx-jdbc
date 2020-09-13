package gui.util;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.stage.Stage;

public class Utilidades {
	/*
	 * capturando o palco de um evento de um controle qualquer transformando para
	 * Node e fazendo um dowcast para Stage, uma vez que o Node captrou uma cena do
	 * tipo window que e super classe de Stage *
	 */
	public static Stage palcoAtual(ActionEvent evento) {
		return (Stage) ((Node) evento.getSource()).getScene().getWindow();
	}

	// metodo para retornar a convers�o de string para inteiro
	public static Integer converterParaInteiro(String valor) {
		/*caso ocorra um erro no formato informado, ser� lan�ado a ex��o do tipo
		 * NumberFormatException e valor ser� retornado null;
		 */
		try {
			return Integer.parseInt(valor);
		} catch (NumberFormatException e) {
			return null;
		}
	}

}
