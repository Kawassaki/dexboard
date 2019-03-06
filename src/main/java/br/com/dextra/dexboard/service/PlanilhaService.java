package br.com.dextra.dexboard.service;

import br.com.dextra.dexboard.dao.ProjetoDao;
import br.com.dextra.dexboard.domain.Indicador;
import br.com.dextra.dexboard.domain.Projeto;
import br.com.dextra.dexboard.domain.RegistroAlteracao;
import br.com.dextra.dexboard.planilha.PlanilhaExportFormulariosImpl;
import br.com.dextra.dexboard.planilha.PlanilhaExportImpl;
import br.com.dextra.dexboard.servlet.ReloadProjetosServlet;
import com.googlecode.objectify.Key;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;


public class PlanilhaService {

    private static final Logger LOG = LoggerFactory.getLogger(ReloadProjetosServlet.class);
    private ProjetoDao dao;

    public  PlanilhaService() {
        dao = new ProjetoDao();
    }

    public List<Projeto> obterListaDeProjetos() {
        return dao.buscarTodosProjetos();
    }

    public RegistroAlteracao obterUltimoRegistroDeAlteracaoDoIndicadorDoProjeto(Key<Indicador> indicadorKey) {
        RegistroAlteracao registro = dao.obterUltimoRegistroDeAlteracaoDoIndicadorDoProjeto(indicadorKey);
        return registro;
    }

    public List<Indicador> obterListaDeIndicadores() {
        return dao.buscarTodosIndicadores();
    }

    public void exportarPlanilha() {
        LOG.info("Exportando Dexboard atual para planilha...");
        new PlanilhaExportImpl().exportarPlanilha();
        LOG.info("Exportando respostas dos formulários do Dexboard atual para planilha...");
        new PlanilhaExportFormulariosImpl().exportarPlanilha();
        LOG.info("Dexboard exportado com Sucesso!");
    }


}

