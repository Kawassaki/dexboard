package br.com.dextra.dexboard.component;

import br.com.dextra.dexboard.dao.NotificacaoDao;
import br.com.dextra.dexboard.dao.ProjetoDao;
import br.com.dextra.dexboard.domain.Classificacao;
import br.com.dextra.dexboard.domain.Indicador;
import br.com.dextra.dexboard.domain.Projeto;
import br.com.dextra.dexboard.domain.RegistroAlteracao;
import br.com.dextra.dexboard.mock.MockPlanilhaIndicadores;
import br.com.dextra.dexboard.servlet.ReloadProjetosServlet;
import org.junit.Test;

import java.util.Calendar;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ITestNotificacao extends ComponentTest {

	private static final long ID_PROJETO_CONTPLAY = 495L;
	private static final long ID_PROJETO_CONFIDENCE = 565L;

	@Test
	public void testNotificacaoAtraso() {
		(new ReloadProjetosServlet()).doReload();
		registraAlteracoesEmProjetos();

		NotificacaoDao dao = new NotificacaoDao();

		List<Projeto> projetos = dao.buscarProjetosParaNotificar();
		assertEquals(2, projetos.size());

		dao.notificarEquipeProjeto(projetos.get(0));
		projetos = dao.buscarProjetosParaNotificar();
		assertEquals(1, projetos.size());

		dao.notificarEquipeProjeto(projetos.get(0));
		projetos = dao.buscarProjetosParaNotificar();
		assertEquals(0, projetos.size());
	}

	private void registraAlteracoesEmProjetos() {
		ProjetoDao dao = new ProjetoDao();

		List<Projeto> projetos = dao.buscarTodosProjetos();
		List<Indicador> indicarores = new MockPlanilhaIndicadores().criarListaDeIndicadores();

		for (Projeto projeto : projetos) {

			if (projeto.getIdPma().equals(ID_PROJETO_CONFIDENCE)) {

				Calendar c = Calendar.getInstance();
				c.add(Calendar.DAY_OF_MONTH, -17);

				for (Indicador ind : indicarores) {
					dao.salvaAlteracao(projeto.getIdPma(), ind.getId(), criaRegistroAlteracaoNaData(c));
				}
				continue;
			}

			if (projeto.getIdPma().equals(ID_PROJETO_CONTPLAY)) {
				continue;
			}
			for (Indicador ind : indicarores) {
				dao.salvaAlteracao(projeto.getIdPma(), ind.getId(), criaRegistroAlteracao());
			}
		}
	}

	private RegistroAlteracao criaRegistroAlteracao() {
		RegistroAlteracao registro = criaRegistroAlteracaoNaData(Calendar.getInstance());
		registro.setClassificacao(Classificacao.ATENCAO);
		return registro;
	}

	private RegistroAlteracao criaRegistroAlteracaoNaData(Calendar c) {
		RegistroAlteracao registroAlteracao = new RegistroAlteracao();
		registroAlteracao.setClassificacao(Classificacao.OK);
		registroAlteracao.setData(c.getTime());
		registroAlteracao.setComentario("xpto");
		registroAlteracao.setUsuario("john");
		return registroAlteracao;
	}

}
