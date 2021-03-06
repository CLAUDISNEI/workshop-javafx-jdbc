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
import model.services.VendedorServicos;

public class MainViewController implements Initializable {

	@FXML
	private MenuItem itemMenuVendedor;
	@FXML
	private MenuItem itemMenuDepartamento;
	@FXML
	private MenuItem itemMenuAbout;

	@FXML
	public void onItemMenuVendedorAction() {
		carregaTela("/gui/ListaVendedor.fxml", (ListaVendedorControler controle) -> {
			controle.setarVendedorServico(new VendedorServicos());
			controle.atualizarTableView();
		});
	}

	/*
	 * Adicionado um par�metro na fun��o para carregar os dados na tableview
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
		// carrega o m�todo para chamar a tela de about
		carregaTela("/gui/About.fxml", x -> {
		});
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {

	}

	/*
	 * Agora a fun��o � do tipo generica que possui um par�metro que ser� passado
	 * como o tipo de express�o lambda ou seja alem de carregar a janela, ser�
	 * carregado tamb�m uma a��o de atualizar a table view
	 */
	private synchronized <T> void carregaTela(String nomeTela, Consumer<T> inicializandoAcao) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(nomeTela));

			// janela que ser� aberta
			VBox novoVBox = loader.load();

			// pega a cena da tela princial main
			Scene cenaPrincipal = Main.obterCenaPrincipal();

			/*
			 * variavel utilizada para converter o scrooplane da janela main em um vbox onde
			 * ser� carregado o vbox da tela about
			 */
			VBox vboxTelaPrincipal = (VBox) ((ScrollPane) cenaPrincipal.getRoot()).getContent();

			/*
			 * variavel criada para guardar uma refer�ncia para o menu, preservando o menu
			 * da tela principal
			 */
			Node menuPrincipal = vboxTelaPrincipal.getChildren().get(0);

			// agora vamos limpar todos os filhos da tela principal
			vboxTelaPrincipal.getChildren().clear();

			/*
			 * agora vamos adicionar os items que foram preservados nas variaveis acima na
			 * tela principal, sendo que o novoVbox ir� carregar a tela que forneceremos
			 * como par�metro do m�todo
			 */
			vboxTelaPrincipal.getChildren().add(menuPrincipal);
			vboxTelaPrincipal.getChildren().addAll(novoVBox.getChildren());

			/*
			 * 
			 * retornando o controlador do tipo que foi atribuido na fun��o
			 */
			T controlador = loader.getController();

			/*
			 * fun��o utilizada executar a a��o, essa variavel foi declarada como par�metro
			 * na metodo
			 */

			inicializandoAcao.accept(controlador);

		} catch (IOException e) {
			Alerts.showAlerts("IOException", "Erro ao carregar a pagina", e.getMessage(), AlertType.ERROR);
		}
	}

}
