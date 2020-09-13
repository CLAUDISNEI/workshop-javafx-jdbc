package gui;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import application.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.entities.Departamento;
import model.services.DepartamentoServicos;

public class ListaDepartamentoControler implements Initializable {

	/*
	 * Criando um dependência para a classe DepartamentoServicos e
	 * injetando na classe
	 */
	private DepartamentoServicos servicos;
	
	
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
	
	
	private ObservableList<Departamento> obsLista;
	
	@FXML
	public void onBtNovoAction() {
		System.out.println("Ação");
	}
	
	/*
	 * metodo criado para a injecao de dependencia da classe DepartamentoServicos
	 */
	public void setarDepartamentoServico(DepartamentoServicos servicos) {
		this.servicos = servicos;
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
		colunaID.setCellValueFactory(new PropertyValueFactory<>("Id"));
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
	
	/*
	 * método responsavel por carregar a lista de departamento na table view
	 */
	public void atualizarTableView() {
		//associando a observable list na table view
		
		/* caso o programador esqueça de carregar o serviço será lançado uma
		 * excessão pois assim não teremos nenhum departamento para mostrar
		 */
		if(servicos == null) {
			throw new IllegalStateException("Serviço não declarado!");
		}
		
		/*
		 * pegou a lista com a classe de servicos de departamentos e passou como
		 * parâmetro para a entidade servicos declarada nesta classe, sabendo que
		 * automaticamente será chamado o método setarDepartamentoServico.
		 */
		List<Departamento> listaDepartamentos = servicos.encontrarTodosDepartamentos();
		//carrega a lista no observablelist
		obsLista = FXCollections.observableArrayList(listaDepartamentos);
		//carrega a observablelist na tabview
		tableViewDepartamentos.setItems(obsLista);
	}

}
