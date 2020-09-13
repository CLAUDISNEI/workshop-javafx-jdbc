package gui;

import java.net.URL;
import java.util.ResourceBundle;

import gui.util.ValidacaoTxtField;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class DepartamentoFormControler implements Initializable {

	
	@FXML
	private TextField txtId;
	
	@FXML
	private TextField txtNome;
	
	@FXML
	private Label lblError;

	@FXML
	private Button btSalvar;
	
	@FXML
	private Button btCancelar;
	
	@FXML
	private void onBtSalvarAction() {
		System.out.println("Botão salvar");
	}
	
	@FXML
	private void onBtCancelarAction() {
		System.out.println("Botão cancelar");
	}
	

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		// TODO Auto-generated method stub
		
	}
	
	private void inicializandoControles() {
		ValidacaoTxtField.setarCampoInteger(txtId);
		ValidacaoTxtField.setarTamanhoMaximoTextField(txtNome, 30);
	}

}
