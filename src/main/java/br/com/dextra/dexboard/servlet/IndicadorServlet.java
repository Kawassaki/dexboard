package br.com.dextra.dexboard.servlet;

import br.com.dextra.dexboard.dao.ProjetoDao;
import br.com.dextra.dexboard.domain.IndicadorQuestao;
import br.com.dextra.dexboard.domain.IndicadorResposta;
import br.com.dextra.dexboard.domain.Projeto;
import br.com.dextra.dexboard.domain.RegistroAlteracao;
import br.com.dextra.dexboard.json.ProjetoJson;
import br.com.dextra.dexboard.service.IndicadorService;
import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.rmi.runtime.Log;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.List;

public class IndicadorServlet extends HttpServlet {

    private static final long serialVersionUID = -7416705488396246559L;
    private static final Logger LOG = LoggerFactory.getLogger(IndicadorServlet.class);
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json");

        ProjetoDao dao = new ProjetoDao();
        JSONSerializer serializer = new JSONSerializer();

        Long idProjeto = Long.valueOf(req.getParameter("projeto"));
        Long idIndicador = Long.valueOf(req.getParameter("indicador"));

        JSONDeserializer<RegistroAlteracao> des = new JSONDeserializer<>();
        String json = req.getParameter("registro");
        RegistroAlteracao regAlteracao = des.deserialize(json, RegistroAlteracao.class);

        String respostasJson = req.getParameter("respostas");
        List<IndicadorResposta> respostas = new JSONDeserializer<List<IndicadorResposta>>().use("values", IndicadorResposta.class).deserialize(respostasJson);

        UserService userService = UserServiceFactory.getUserService();

        regAlteracao.setUsuario(userService.getCurrentUser().getEmail());
        regAlteracao.setData(new Date());

        RegistroAlteracao registro = new IndicadorService().salvarAlteracao(idProjeto, idIndicador, regAlteracao, respostas);

        Projeto project = dao.buscarProjeto(idProjeto);
        ProjetoJson projetoJson = project.toProjetoJson();
        MemcacheService memcacheService = MemcacheServiceFactory.getMemcacheService();
        try {
            if (projetoJson.getAtrasado()) {
                memcacheService.put(project.getIdPma(), serializer.deepSerialize(projetoJson));
            } else {
                memcacheService.delete(ProjetoJson.KEY_CACHE);
            }
        } catch (Exception e) {
            LOG.info(e.getMessage());
        }

        resp.getWriter().println(serializer.serialize(registro));
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        doPost(req, resp);
    }
}
