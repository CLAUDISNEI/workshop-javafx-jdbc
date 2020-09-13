package gui;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuItem;

public class MainViewController implements Initializable {

	
	@FXML
	private MenuItem itemMenuVendedor;
	@FXML
	private MenuItem itemMenuDepartamento;
	@FXML
	private MenuItem itemMenuAbout;
	@FXML
	public void onItemMenuVendedorAction() {
		System.out.println("Vendedor");
	}
	@FXML
	public void onItemMenuDepartamentoAction() {
		System.out.println("Departamento");
	}
	@FXML
	public void onItemMenuAbaoutAction() {
		System.out.println("About");
	}
	
	
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		
		
	}

}
