package gui.util;

import javafx.scene.control.TextField;

public class ValidacaoTxtField {
	public static void setarCampoInteger(TextField txt) {
		txt.textProperty().addListener((obs, valorAntigo, valorNovo) -> {
			if(valorNovo != null && !valorNovo.matches("\\d*")) {
				txt.setText(valorAntigo);
			}
		});
	}
	
	public static void setarTamanhoMaximoTextField(TextField txt, int max) {
		txt.textProperty().addListener((obs, valorVelho, valorNovo)-> {
			if(valorNovo != null && valorNovo.length() > max ) {
				txt.setText(valorVelho);
			}
		});
	}
	
	public static void setarCampoDouble(TextField txt) {
		txt.textProperty().addListener((obs, valorVelho, valorNovo) -> {
			if(valorNovo != null && !valorNovo.matches("\\d*([\\.]\\d*)?")) {
				txt.setText(valorVelho);
			}
		});
	}
}
