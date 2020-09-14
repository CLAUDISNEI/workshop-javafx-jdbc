package model.services;

import java.util.List;

import model.DAO.FabricaDAO;
import model.DAO.VendedorDAO;
import model.entities.Vendedor;

public class VendedorServicos {
	
	private VendedorDAO dao = FabricaDAO.criarVendedorDao();
	
	public List<Vendedor> encontrarTodosVendedores(){
		return dao.findAll();
	}
	
	public void salvarOUAtualizar(Vendedor vendedor) {
		if(vendedor.getId() == null) {
			dao.insert(vendedor);
		}else {
			dao.update(vendedor);
		}
	}
	
	public void remover(Vendedor vendedor) {
		dao.deleteById(vendedor.getId());
	}
}
