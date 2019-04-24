package br.com.dextra.dexboard.servlet;

import br.com.dextra.dexboard.dao.NotificacaoDao;
import br.com.dextra.dexboard.domain.Projeto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sound.midi.Soundbank;
import java.io.IOException;
import java.util.List;

public class NotificacaoServlet extends HttpServlet {

	private static final long serialVersionUID = -9049212521731856178L;
	private static final Logger LOG = LoggerFactory.getLogger(NotificacaoServlet.class);

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		notificarProjetosAtrasados();

		resp.setContentType("application/json");
		resp.getWriter().print("{status: 'success'}");
	}

	private void notificarProjetosAtrasados() {
		NotificacaoDao dao = new NotificacaoDao();

		List<Projeto> projetos = dao.buscarProjetosParaNotificar();
		LOG.info(String.format("NotificacaoServlet: Enviando email de atraso para o %d projetos", projetos.size()));
		for (Projeto projeto : projetos) {
			dao.notificarEquipeProjeto(projeto);
		}
	}
}
