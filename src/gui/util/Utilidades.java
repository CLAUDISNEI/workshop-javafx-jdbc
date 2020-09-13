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

	// metodo para retornar a conversão de string para inteiro
	public static Integer converterParaInteiro(String valor) {
		/*caso ocorra um erro no formato informado, será lançado a exção do tipo
		 * NumberFormatException e valor será retornado null;
		 */
		try {
			return Integer.parseInt(valor);
		} catch (NumberFormatException e) {
			return null;
		}
	}

}
