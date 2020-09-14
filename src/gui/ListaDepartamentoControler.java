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
	 * Criando um depend�ncia para a classe DepartamentoServicos e
	 * injetando na classe
	 */
	private DepartamentoServicos servicos;
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
	private ObservableList<Departamento> obsLista;
	
	/*o m�todo recebe um evento para determinar a janela onde est� o controle
	 * e sobrepor a janela que ser� criada, inclusive mantedo-a no modelo 
	 * modal
	 */
	@FXML
	public void onBtNovoAction(ActionEvent evento) {
		/*atraves do metodo palcoatual da classe utilidades
		 *� poss�vel setar esta informa��o na variavel parenteStage
		 *e passala para o metodo criarjanela
		 */
		Stage parentStage = Utilidades.palcoAtual(evento);
		/*criado instancia de um Departamento que ser� utilizada para
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
		//c�digo necess�rio para carregar os valores nas colunas da tabela
		colunaID.setCellValueFactory(new PropertyValueFactory<>("Id"));
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
	
	/*
	 * m�todo responsavel por carregar a lista de departamento na table view
	 */
	public void atualizarTableView() {
		//associando a observable list na table view
		
		/* caso o programador esque�a de carregar o servi�o ser� lan�ado uma
		 * excess�o pois assim n�o teremos nenhum departamento para mostrar
		 */
		if(servicos == null) {
			throw new IllegalStateException("Servi�o n�o declarado!");
		}
		
		/*
		 * pegou a lista com a classe de servicos de departamentos e passou como
		 * par�metro para a entidade servicos declarada nesta classe, sabendo que
		 * automaticamente ser� chamado o m�todo setarDepartamentoServico.
		 */
		List<Departamento> listaDepartamentos = servicos.encontrarTodosDepartamentos();
		//carrega a lista no observablelist
		obsLista = FXCollections.observableArrayList(listaDepartamentos);
		//carrega a observablelist na tabview
		tableViewDepartamentos.setItems(obsLista);
	}

	/*Ser� informado para o metodo o Stage que esta criando a janela de dialogo 
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
			//realiza a inscri��o do controler na lista de ouvintes para atualizar os dados na tabela
			controle.subscriverDataChangeListeners(this);
			controle.atualizarDadosFormulario();
			
			//configura�o da nova janela
			Stage dialogoStage = new Stage();
			dialogoStage.setTitle("Entre os dados do departamento");
			dialogoStage.setScene(new Scene(painel));
			dialogoStage.setResizable(false);
			//informa quem � a janela pai
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
