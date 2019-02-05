package br.com.dextra.dexboard.servlet;

import br.com.dextra.dexboard.dao.NotificacaoDao;
import br.com.dextra.dexboard.domain.Projeto;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class NotificacaoServlet extends HttpServlet {

	private static final long serialVersionUID = -9049212521731856178L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		resp.setContentType("application/json");
		notificarProjetosAtrasados();
		resp.getWriter().print("{status: 'success'}");
	}

	private void notificarProjetosAtrasados() {
		NotificacaoDao dao = new NotificacaoDao();

		List<Projeto> projetos = dao.buscarProjetosParaNotificar();
		for (Projeto projeto : projetos) {
			dao.notificarEquipeProjeto(projeto);
		}
	}
}
