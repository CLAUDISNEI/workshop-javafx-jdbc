package gui.util;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.stage.Stage;


public class Utilidades {
	/*
	 * capturando o palco de um evento de um controle qualquer transformando para
	 * Node e fazendo um dowcast para Stage, uma vez que o Node captrou uma cena do
	 * tipo window que e super classe de Stage *
	 */
	public static Stage palcoAtual(ActionEvent evento) {
		return (Stage) ((Node) evento.getSource()).getScene().getWindow();
	}

	// metodo para retornar a conversão de string para inteiro
	public static Integer converterParaInteiro(String valor) {
		/*
		 * caso ocorra um erro no formato informado, será lançado a exção do tipo
		 * NumberFormatException e valor será retornado null;
		 */
		try {
			return Integer.parseInt(valor);
		} catch (NumberFormatException e) {
			return null;
		}
	}
	
	
	/*
	 * public static <T> void formatTableColumnDate(TableColumn<T, Date> tableColumn, String format) {
tableColumn.setCellFactory(column -> {
TableCell<T, Date> cell = new TableCell<T, Date>() {
private SimpleDateFormat sdf = new SimpleDateFormat(format);
@Override
protected void updateItem(Date item, boolean empty) {
super.updateItem(item, empty);
if (empty) {
setText(null);
} else {
setText(sdf.format(item));
}
}
};
return cell;
});
}
	 */

	public static <T> void formatarTipoDataEmColunas(TableColumn<T,Date> colunaTabela, String stringFormato) {
		colunaTabela.setCellFactory(coluna -> {
			TableCell<T,Date> celula = new TableCell<T,Date>() {
				private SimpleDateFormat sdf = new SimpleDateFormat(stringFormato);

				@Override
				protected void updateItem(Date valor, boolean empty) {
					super.updateItem(valor, empty);
					if (empty) {
						setText(null);
					} else {
						setText(sdf.format(valor));
					}

				}
			};
			return celula;
		});
	}

	public static <T> void formatarTipoDoubleColunas(TableColumn< T , Double> colunaTabela, int precisaoDecimal) {
		colunaTabela.setCellFactory(coluna -> {
			TableCell<T, Double> celula = new TableCell<T, Double>() {

				@Override
				protected void updateItem(Double valor, boolean empty) {
					if (empty) {
						setText(null);
					} else {
						Locale.setDefault(Locale.US);
						setText(String.format("%." + precisaoDecimal + "f", valor));
					}

				}
			};
			return celula;
		});
	}

}
