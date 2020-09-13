package model.services;

import java.util.ArrayList;
import java.util.List;

import model.entities.Departamento;

public class DepartamentoServicos {
	
	public List<Departamento> encontrarTodosDepartamentos(){
		List<Departamento> lista = new ArrayList<>();
		lista.add(new Departamento(1, "Livros"));
		lista.add(new Departamento(2, "Computador"));
		lista.add(new Departamento(3, "Culin�ria"));
		lista.add(new Departamento(4, "Autom�veis"));
		lista.add(new Departamento(5, "Educa��o"));
		lista.add(new Departamento(6, "Sa�de"));
		lista.add(new Departamento(7, "Lazer"));
		
		return lista;
	}
	
}
