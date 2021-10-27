package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import connection.SingleConnectionBanco;
import model.ModelLogin;

public class DAOLoginRepository {
	
	//Objeto de conexão
	private Connection connection;
	
	//Construtor
	public DAOLoginRepository() {
		connection = SingleConnectionBanco.getConnection();
	}
	
	//Método validar Login
	public boolean validarAutenticacao(ModelLogin modelLogin) throws Exception {
		
		//upper() é utilizando para comparação de dados Maiúsculo e Minúsculo
		String sql = "select * from model_login where upper(login) = upper(?) and upper(senha) = upper(?)";
		
		PreparedStatement statement = connection.prepareStatement(sql);
		
		statement.setString(1, modelLogin.getLogin());
		statement.setString(2, modelLogin.getSenha());
		
		ResultSet resultSet = statement.executeQuery();
		
		if(resultSet.next()) {
			return true;//Autenticado
		}
		
		return false;//Não autenticado
	}

}








