package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import application.Main;
import gui.util.Alerts;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import model.services.DepartamentoServicos;

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

	/*
	 * Adicionado um parâmetro na função para carregar os dados na tableview
	 */
	@FXML
	public void onItemMenuDepartamentoAction() {
		carregaTela("/gui/ListaDepartamento.fxml", (ListaDepartamentoControler controle) -> {
			controle.setarDepartamentoServico(new DepartamentoServicos());
			controle.atualizarTableView();
		});
	}

	@FXML
	public void onItemMenuAbaoutAction() {
		// carrega o método para chamar a tela de about
		carregaTela("/gui/About.fxml", x -> {
		});
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {

	}

	/*
	 * Agora a função é do tipo generica que possui um parâmetro que será passado
	 * como o tipo de expressão lambda ou seja alem de carregar a janela, será
	 * carregado também uma ação de atualizar a table view
	 */
	private synchronized <T> void carregaTela(String nomeTela, Consumer<T> inicializandoAcao) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(nomeTela));

			// janela que será aberta
			VBox novoVBox = loader.load();

			// pega a cena da tela princial main
			Scene cenaPrincipal = Main.obterCenaPrincipal();

			/*
			 * variavel utilizada para converter o scrooplane da janela main em um vbox onde
			 * será carregado o vbox da tela about
			 */
			VBox vboxTelaPrincipal = (VBox) ((ScrollPane) cenaPrincipal.getRoot()).getContent();

			/*
			 * variavel criada para guardar uma referência para o menu, preservando o menu
			 * da tela principal
			 */
			Node menuPrincipal = vboxTelaPrincipal.getChildren().get(0);

			// agora vamos limpar todos os filhos da tela principal
			vboxTelaPrincipal.getChildren().clear();

			/*
			 * agora vamos adicionar os items que foram preservados nas variaveis acima na
			 * tela principal, sendo que o novoVbox irá carregar a tela que forneceremos
			 * como parâmetro do método
			 */
			vboxTelaPrincipal.getChildren().add(menuPrincipal);
			vboxTelaPrincipal.getChildren().addAll(novoVBox.getChildren());

			/*
			 * 
			 * retornando o controlador do tipo que foi atribuido na função
			 */
			T controlador = loader.getController();

			/*
			 * função utilizada executar a ação, essa variavel foi declarada como parâmetro
			 * na metodo
			 */

			inicializandoAcao.accept(controlador);

		} catch (IOException e) {
			Alerts.showAlerts("IOException", "Erro ao carregar a pagina", e.getMessage(), AlertType.ERROR);
		}
	}

}
