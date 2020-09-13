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
	 * Criando referências para os componentes que serão utilizados na tela 
	 * ListaDepartamento.fxml
	 * 
	 */
	//cria a referencia da table view já com o tipo que ela irá carregar
	@FXML
	private TableView<Departamento> tableViewDepartamentos;
	
	
	/*a coluna recebe dois parâmentros o primeiro é o tipo da entidade e o segundo
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
		System.out.println("Ação");
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
		//código necessário para carregar os valores nas colunas da tabela
		colunaID.setCellValueFactory(new PropertyValueFactory<>("ID"));
		colunaNome.setCellValueFactory(new PropertyValueFactory<>("Nome"));
		
		/*código para pegar a referência de um estado da tela principal
		 * porém é necessário fazer um dowcast, pois Window é uma super
		 * classe de Stage.
		 */
		Stage estado = (Stage) Main.obterCenaPrincipal().getWindow();
		
		/*Após criar uma variavel de estado da tela principal, passamos por 
		 * parâmetro a propriedade de altura da variavel de estado para table
		 * view, para que ela tenha esta mesma altura.
		 */
		tableViewDepartamentos.prefHeightProperty().bind(estado.heightProperty());
	}

}
