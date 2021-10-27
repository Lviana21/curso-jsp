package connection;

import java.sql.Connection;
import java.sql.DriverManager;

public class SingleConnectionBanco {

	private static String banco = "jdbc:postgresql://localhost:5432/curso-jsp?autoReconnect=true";
	private static String user = "postgres";
	private static String senha = "admin";
	private static Connection connection = null;
	
	//Método para retornar a conexão existente 
	public static Connection getConnection() {
		return connection;
	}
	
	
	static {
		conectar();
	}
	
	//Construtor
	public SingleConnectionBanco() {//Quando tiver uma instância vai conectar
		conectar();
	}
	
	// Método Conectar
	private static void conectar() {

		try {

			if (connection == null) {
				Class.forName("org.postgresql.Driver"); // Carrega o driver de conexão com o banco de dados
				connection = DriverManager.getConnection(banco, user, senha);
				connection.setAutoCommit(false);// Para não efetuar alterações no banco sem nosso comando
			}

		} catch (Exception e) {
			e.printStackTrace();// Mostrar qualquer erro no momento de conectar

		}
	}

}
