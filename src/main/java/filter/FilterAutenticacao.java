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

//Tudo que vier do sistema irá passar pelo Filter "Mapear tudo que vier do principal"
@WebFilter(urlPatterns = { "/principal/*" }) // Intercepta todas as requisições que vierem do projeto ou mapeamento
public class FilterAutenticacao implements Filter {

	private static Connection connection;

	public FilterAutenticacao() {
	}

	// Encerra os processos quando o servidor é parado
	public void destroy() { // Mataria os processos de conexão com o banco
		try {
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// Intercepta as requisições e as respostas no sistema
	// Tudo que fizer no sistema irá passar pelo Filter
	// Exemplo: Validação de autenticação
	// Dar commit e rolback de transações do banco
	// Validar e fazer redirecionamento de páginas
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		try {
			HttpServletRequest req = (HttpServletRequest) request;
			HttpSession session = req.getSession();
	
			String usuarioLogado = (String) session.getAttribute("usuario");// Verifica se está logado
			String urlParaAutenticar = req.getServletPath(); // Url que está sendo acessada
	
			// Validar se está logado senão redireciona para a tela de login
			if (usuarioLogado == null && !urlParaAutenticar.equalsIgnoreCase("/principal/ServletLogin")) {// Não está logado
	
				RequestDispatcher redireciona = request.getRequestDispatcher("/index.jsp?url=" + urlParaAutenticar);
				request.setAttribute("msq", "Por favor realize o login!");
				redireciona.forward(request, response);
				return;// Para parar a execução e redireciona para o login

		} else {
			chain.doFilter(request, response);// chain do Filter deixa o processo do software continuar
		}
			
			connection.commit();//Deu tudo certo então comita as alterações no banco de dados
			
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
	public void init(FilterConfig fConfig) throws ServletException {// Iniciar a conexão com o banco
		connection = SingleConnectionBanco.getConnection();
	}

}
