package model.DAO;

import db.DB;
import model.DAO.impl.DepartamentoDaoJDBC;
import model.DAO.impl.VendedorDaoJDBC;

public class FabricaDAO {

	public static VendedorDAO criarVendedorDao() {
		return new VendedorDaoJDBC(DB.getConnection());
	}

	public static DepartamentoDAO criarDepartmentoDao() {
		return new DepartamentoDaoJDBC(DB.getConnection());
	}
}
