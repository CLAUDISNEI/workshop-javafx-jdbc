package gui;

import java.net.URL;
import java.util.ResourceBundle;

import application.Main;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.entities.Departamento;

public class ListaDepartamentoControler implements Initializable {

	/*
	 * Criando refer�ncias para os componentes que ser�o utilizados na tela 
	 * ListaDepartamento.fxml
	 * 
	 */
	//cria a referencia da table view j� com o tipo que ela ir� carregar
	@FXML
	private TableView<Departamento> tableViewDepartamentos;
	
	
	/*a coluna recebe dois par�mentros o primeiro � o tipo da entidade e o segundo
	 * o tipo da coluna
	 */
	@FXML
	private TableColumn<Departamento, Integer> colunaID;
	
	@FXML
	private TableColumn<Departamento, String> colunaNome;
	
	@FXML
	private Button btNovo;
	
	@FXML
	public void onBtNovoAction() {
		System.out.println("A��o");
	}
	
	
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		
		carregaNodes();
	}


	/*
	 * metodo criado para carregar os componentes na tela principal, com propriedades
	 * ajustadas
	 */
	private void carregaNodes() {
		//c�digo necess�rio para carregar os valores nas colunas da tabela
		colunaID.setCellValueFactory(new PropertyValueFactory<>("ID"));
		colunaNome.setCellValueFactory(new PropertyValueFactory<>("Nome"));
		
		/*c�digo para pegar a refer�ncia de um estado da tela principal
		 * por�m � necess�rio fazer um dowcast, pois Window � uma super
		 * classe de Stage.
		 */
		Stage estado = (Stage) Main.obterCenaPrincipal().getWindow();
		
		/*Ap�s criar uma variavel de estado da tela principal, passamos por 
		 * par�metro a propriedade de altura da variavel de estado para table
		 * view, para que ela tenha esta mesma altura.
		 */
		tableViewDepartamentos.prefHeightProperty().bind(estado.heightProperty());
	}

}
