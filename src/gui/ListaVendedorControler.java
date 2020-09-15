package gui;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import application.Main;
import db.DbIntegrityException;
import gui.listeners.AlterarDadosListeners;
import gui.util.Alerts;
import gui.util.Utilidades;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.entities.Vendedor;
import model.services.VendedorServicos;

public class ListaVendedorControler implements Initializable, AlterarDadosListeners {

	/*
	 * Criando um dependência para a classe VendedorServicos e injetando na
	 * classe
	 */
	private VendedorServicos servicos;
	/*
	 * Criando referências para os componentes que serão utilizados na tela
	 * ListaVendedor.fxml
	 * 
	 */
	// cria a referencia da table view já com o tipo que ela irá carregar
	@FXML
	private TableView<Vendedor> tableViewVendedores;
	/*
	 * a coluna recebe dois parâmentros o primeiro é o tipo da entidade e o segundo
	 * o tipo da coluna
	 */
	@FXML
	private TableColumn<Vendedor, Integer> colunaID;
	@FXML
	private TableColumn<Vendedor, String> colunaNome;
	@FXML
	private TableColumn<Vendedor, String> colunaEmail;
	@FXML
	private TableColumn<Vendedor, Date> colunaNascimento;
	@FXML
	private TableColumn<Vendedor, Double> colunaSalarioBase;
	@FXML
	private TableColumn<Vendedor, Vendedor> colunaEditar;
	@FXML
	private TableColumn<Vendedor, Vendedor> colunaRemover;

	@FXML
	private Button btNovo;
	private ObservableList<Vendedor> obsLista;

	/*
	 * o método recebe um evento para determinar a janela onde está o controle e
	 * sobrepor a janela que será criada, inclusive mantedo-a no modelo modal
	 */
	@FXML
	public void onBtNovoAction(ActionEvent evento) {
		/*
		 * atraves do metodo palcoatual da classe utilidades é possível setar esta
		 * informação na variavel parenteStage e passala para o metodo criarjanela
		 */
		Stage parentStage = Utilidades.palcoAtual(evento);
		/*
		 * criado instancia de um Vendedor que será utilizada para cadastrar o novo
		 * vendedor
		 */
		Vendedor vendedor = new Vendedor();
		criarJanelaDialogoForm(vendedor, "/gui/VendedorForm.fxml", parentStage);
	}

	/*
	 * metodo criado para a injecao de dependencia da classe VendedorServicos
	 */
	public void setarVendedorServico(VendedorServicos servicos) {
		this.servicos = servicos;
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		carregaNodes();
	}

	/*
	 * metodo criado para carregar os componentes na tela principal, com
	 * propriedades ajustadas
	 */
	private void carregaNodes() {
		// código necessário para carregar os valores nas colunas da tabela
		colunaID.setCellValueFactory(new PropertyValueFactory<>("Id"));
		colunaNome.setCellValueFactory(new PropertyValueFactory<>("name"));
		colunaEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
		colunaNascimento.setCellValueFactory(new PropertyValueFactory<>("birthDate"));
		Utilidades.formatarTipoDataEmColunas(colunaNascimento, "dd/MM/yyyy");
		colunaSalarioBase.setCellValueFactory(new PropertyValueFactory<>("baseSalary"));
		Utilidades.formatarTipoDoubleColunas(colunaSalarioBase, 2);

		/*
		 * código para pegar a referência de um estado da tela principal porém é
		 * necessário fazer um dowcast, pois Window é uma super classe de Stage.
		 */
		Stage estado = (Stage) Main.obterCenaPrincipal().getWindow();

		/*
		 * Após criar uma variavel de estado da tela principal, passamos por parâmetro a
		 * propriedade de altura da variavel de estado para table view, para que ela
		 * tenha esta mesma altura.
		 */
		tableViewVendedores.prefHeightProperty().bind(estado.heightProperty());
	}

	/*
	 * método responsavel por carregar a lista de vendedor na table view
	 */
	public void atualizarTableView() {
		// associando a observable list na table view

		/*
		 * caso o programador esqueça de carregar o serviço será lançado uma excessão
		 * pois assim não teremos nenhum vendedor para mostrar
		 */
		if (servicos == null) {
			throw new IllegalStateException("Serviço não declarado!");
		}

		/*
		 * pegou a lista com a classe de servicos de vendedors e passou como
		 * parâmetro para a entidade servicos declarada nesta classe, sabendo que
		 * automaticamente será chamado o método setarVendedorServico.
		 */
		List<Vendedor> listaVendedor = servicos.encontrarTodosVendedores();
		
		// carrega a lista no observablelist
		obsLista = FXCollections.observableArrayList(listaVendedor);
		// carrega a observablelist na tabview
		tableViewVendedores.setItems(obsLista);
		habilitarBotaoEditar();
		habilitarBotaoRemover();
	}

	/*
	 * Será informado para o metodo o Stage que esta criando a janela de dialogo e o
	 * nome da nova janela.
	 * 
	 */
	private void criarJanelaDialogoForm(Vendedor vendedor, String nomeTela, Stage parentStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(nomeTela));
			Pane painel = loader.load();

			/*
			 * Criando uma instancia do Vendedorcontroler para pegar o controlador da
			 * tela que foi carregada no loader
			 */
			VendedorFormControler controle = loader.getController();
			controle.setarVendedor(vendedor);
			controle.setarVendedorServicos(new VendedorServicos());
			// realiza a inscrição do controler na lista de ouvintes para atualizar os dados
			// na tabela
			controle.subscriverDataChangeListeners(this);
			controle.atualizarDadosFormulario();

			// configuraão da nova janela
			Stage dialogoStage = new Stage();
			dialogoStage.setTitle("Entre os dados do vendedor");
			dialogoStage.setScene(new Scene(painel));
			dialogoStage.setResizable(false);
			// informa quem é a janela pai
			dialogoStage.initOwner(parentStage);
			// habilita a propriedade modal na janela
			dialogoStage.initModality(Modality.WINDOW_MODAL);
			dialogoStage.showAndWait();

		} catch (IOException e) {
			Alerts.showAlerts("IoException", "Erro ao Carrega a Janela", e.getMessage(), AlertType.ERROR);
		}
	}

	@Override
	public void dadosAlterados() {
		atualizarTableView();
	}

	private void habilitarBotaoEditar() {
		colunaEditar.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		colunaEditar.setCellFactory(param -> new TableCell<Vendedor, Vendedor>() {
			private final Button botao = new Button("Editar");

			@Override
			protected void updateItem(Vendedor dep, boolean empty) {
				super.updateItem(dep, empty);
				if (dep == null) {
					setGraphic(null);
					return;
				}
				setGraphic(botao);
				botao.setOnAction(evento -> criarJanelaDialogoForm(dep, "/gui/VendedorForm.fxml",
						Utilidades.palcoAtual(evento)));
			}

		});

	}

	private void habilitarBotaoRemover() {
		colunaRemover.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		colunaRemover.setCellFactory(param -> new TableCell<Vendedor, Vendedor>() {
			private final Button botao = new Button("Remover");

			@Override
			protected void updateItem(Vendedor dep, boolean empty) {
				super.updateItem(dep, empty);

				if (dep == null) {
					setGraphic(null);
					return;
				}

				setGraphic(botao);
				botao.setOnAction(event -> removerEntidade(dep));

			}
		});
	}

	protected void removerEntidade(Vendedor dep) {
		
	Optional<ButtonType> resultado = Alerts.mostrarConfirmacao("Confirmação", "Confirma a exclusão?");
	
		if(resultado.get() == ButtonType.OK ) {
			if(servicos == null) {
				throw new IllegalStateException("O serviço é nulo");
			}
			try {
				servicos.remover(dep);
				atualizarTableView();
			}catch(DbIntegrityException e) {
				Alerts.showAlerts("Erro ao remover objeto", null, e.getMessage(), AlertType.ERROR);
			}
			
		}
	}

}
