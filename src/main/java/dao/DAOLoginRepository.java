package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import connection.SingleConnectionBanco;
import model.ModelLogin;

public class DAOLoginRepository {
	
	//Objeto de conex�o
	private Connection connection;
	
	//Construtor
	public DAOLoginRepository() {
		connection = SingleConnectionBanco.getConnection();
	}
	
	//M�todo validar Login
	public boolean validarAutenticacao(ModelLogin modelLogin) throws Exception {
		
		//upper() � utilizando para compara��o de dados Mai�sculo e Min�sculo
		String sql = "select * from model_login where upper(login) = upper(?) and upper(senha) = upper(?)";
		
		PreparedStatement statement = connection.prepareStatement(sql);
		
		statement.setString(1, modelLogin.getLogin());
		statement.setString(2, modelLogin.getSenha());
		
		ResultSet resultSet = statement.executeQuery();
		
		if(resultSet.next()) {
			return true;//Autenticado
		}
		
		return false;//N�o autenticado
	}

}








