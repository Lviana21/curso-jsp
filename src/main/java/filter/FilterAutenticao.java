package filter;

import java.io.IOException;

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
import model.ModelLogin;

/**
 * Servlet Filter implementation class FilterAutenticao
 */
@WebFilter(urlPatterns = {"/principal/*"}) /*Intercepta todas as requisi�oes que vierem das paginas dentro da pasta principal*/
public class FilterAutenticao implements Filter {

    public FilterAutenticao() {
    }

    /*Mata recursos ao encerrar o sistema ou parar o servidor*/
	public void destroy() {
	}

	/*Interceptar todas as requisi�oes e dar a resposta*/
	//Tudo que fizer o no sistema vai passar por essse filtro
	// Faz a validadao de usu�rio autenticadao
	// Dar commit ou rollback em um transa��o com banco de dados
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		
		HttpServletRequest req = (HttpServletRequest) request;
		HttpSession session = req.getSession(); /*Navegador do usu�rio aberto*/
		
		String urlParaAutenticar = req.getServletPath(); /*Url que est� sendo acessada*/
		
		ModelLogin modelLogin = (ModelLogin) session.getAttribute("usuario");
		
		if (modelLogin == null && !urlParaAutenticar.equalsIgnoreCase("/principal/ServletLogin")) { /*Usu�rio n�o autenticado*/
			
			RequestDispatcher redireciona = request.getRequestDispatcher("/index.jsp?url=" + urlParaAutenticar);
			request.setAttribute("msg", "Por favor realize o login!");
			redireciona.forward(request, response);
			return; /*Para a execu��o e redireciona para o login do sistema*/
			
		}else {
		 chain.doFilter(request, response);/*Continua o fluxo do sistema*/
		}
	}

	/*Inicia processos ou recursos quando o servidor est� levantando o sistema*/
	public void init(FilterConfig fConfig) throws ServletException {
	}

}
