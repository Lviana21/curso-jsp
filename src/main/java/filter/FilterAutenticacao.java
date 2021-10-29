package filter;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import connection.SingleConnectionBanco;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

//Tudo que vier do sistema ir� passar pelo Filter "Mapear tudo que vier do principal"
@WebFilter(urlPatterns = { "/principal/*" }) // Intercepta todas as requisi��es que vierem do projeto ou mapeamento
public class FilterAutenticacao implements Filter {

	private static Connection connection;

	public FilterAutenticacao() {
	}

	// Encerra os processos quando o servidor � parado
	public void destroy() { // Mataria os processos de conex�o com o banco
		try {
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// Intercepta as requisi��es e as respostas no sistema
	// Tudo que fizer no sistema ir� passar pelo Filter
	// Exemplo: Valida��o de autentica��o
	// Dar commit e rolback de transa��es do banco
	// Validar e fazer redirecionamento de p�ginas
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		try {
			HttpServletRequest req = (HttpServletRequest) request;
			HttpSession session = req.getSession();
	
			String usuarioLogado = (String) session.getAttribute("usuario");// Verifica se est� logado
			String urlParaAutenticar = req.getServletPath(); // Url que est� sendo acessada
	
			// Validar se est� logado sen�o redireciona para a tela de login
			if (usuarioLogado == null && !urlParaAutenticar.equalsIgnoreCase("/principal/ServletLogin")) {// N�o est� logado
	
				RequestDispatcher redireciona = request.getRequestDispatcher("/index.jsp?url=" + urlParaAutenticar);
				request.setAttribute("msq", "Por favor realize o login!");
				redireciona.forward(request, response);
				return;// Para parar a execu��o e redireciona para o login

		} else {
			chain.doFilter(request, response);// chain do Filter deixa o processo do software continuar
		}
			
			connection.commit();//Deu tudo certo ent�o comita as altera��es no banco de dados
			
		}catch (Exception e) {
			e.printStackTrace();
			
			RequestDispatcher redirecionar = request.getRequestDispatcher("/erro.jsp");
			request.setAttribute("msg", e.getMessage());
			redirecionar.forward(request, response);
			
			try {
				connection.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
	}
	

	// Inicia os processos ou recursos quando o servidor sobe o projeto
	public void init(FilterConfig fConfig) throws ServletException {// Iniciar a conex�o com o banco
		connection = SingleConnectionBanco.getConnection();
	}

}
