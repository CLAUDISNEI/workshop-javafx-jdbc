package gui;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import application.Main;
import gui.listeners.AlterarDadosListeners;
import gui.util.Alerts;
import gui.util.Utilidades;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.entities.Departamento;
import model.services.DepartamentoServicos;

public class ListaDepartamentoControler implements Initializable, AlterarDadosListeners {

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
	
	/*o método recebe um evento para determinar a janela onde está o controle
	 * e sobrepor a janela que será criada, inclusive mantedo-a no modelo 
	 * modal
	 */
	@FXML
	public void onBtNovoAction(ActionEvent evento) {
		/*atraves do metodo palcoatual da classe utilidades
		 *é possível setar esta informação na variavel parenteStage
		 *e passala para o metodo criarjanela
		 */
		Stage parentStage = Utilidades.palcoAtual(evento);
		/*criado instancia de um Departamento que será utilizada para
		 * cadastrar o novo departamento 
		 */		
		Departamento departamento = new Departamento();
		criarJanelaDialogoForm(departamento, "/gui/DepartamentoForm.fxml",  parentStage);
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

	/*Será informado para o metodo o Stage que esta criando a janela de dialogo 
	 * e o nome da nova janela.
	 * 
	 */
	private void criarJanelaDialogoForm(Departamento departamento, String nomeTela, Stage parentStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(nomeTela));
			Pane painel = loader.load();
			
			/*Criando uma instancia do Departamentocontroler para 
			 * pegar o controlador da tela que foi carregada no loader
			 */
			DepartamentoFormControler controle = loader.getController();
			controle.setarDepartamento(departamento);
			controle.setarDepartamentoServicos(new DepartamentoServicos());
			//realiza a inscrição do controler na lista de ouvintes para atualizar os dados na tabela
			controle.subscriverDataChangeListeners(this);
			controle.atualizarDadosFormulario();
			
			//configuraão da nova janela
			Stage dialogoStage = new Stage();
			dialogoStage.setTitle("Entre os dados do departamento");
			dialogoStage.setScene(new Scene(painel));
			dialogoStage.setResizable(false);
			//informa quem é a janela pai
			dialogoStage.initOwner(parentStage);
			//habilita a propriedade modal na janela 
			dialogoStage.initModality(Modality.WINDOW_MODAL);
			dialogoStage.showAndWait();
			
		}catch(IOException e) {
			Alerts.showAlerts("IoException", "Erro ao Carrega a Janela", e.getMessage(), AlertType.ERROR);
		}
	}

	@Override
	public void dadosAlterados() {
		atualizarTableView();		
	}
}
