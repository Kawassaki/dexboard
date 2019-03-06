package br.com.dextra.dexboard.servlet;

import br.com.dextra.dexboard.dao.ProjetoDao;
import br.com.dextra.dexboard.domain.Projeto;
import br.com.dextra.dexboard.json.ProjetoJson;
import br.com.dextra.dexboard.repository.ProjetoComparator;
import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;
import flexjson.JSONSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class QueryServlet extends HttpServlet {

    public static final int CACHE_EXPIRATION_SECONDS = 60 * 60;
    private static final String EQUIPE_HTTP_PARAMETER = "equipe";
    private static final String TRIBO_HTTP_PARAMETER = "tribo";
    private static final long serialVersionUID = -1248500946944090403L;
    private static final Logger LOG = LoggerFactory.getLogger(QueryServlet.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json");
        resp.getWriter().print(getJsonProjetosWithCache(req.getParameter(EQUIPE_HTTP_PARAMETER),
                req.getParameter(TRIBO_HTTP_PARAMETER)));
    }

    private String getJsonProjetosWithCache(String equipe, String tribo) {
        return getJsonProjetos(equipe, tribo);
    }

    private String getJsonProjetos(String equipe, String tribo) {
        JSONSerializer serializer = new JSONSerializer();
        serializer.exclude("*.class", "*.projeto");
        MemcacheService memcacheService = MemcacheServiceFactory.getMemcacheService();

        ProjetoDao dao = new ProjetoDao();

        if (useCache(equipe, tribo)) {
            List<Long> cache = (List<Long>) memcacheService.get(ProjetoJson.KEY_CACHE);
            if (cache != null) {
                List<String> projetosRetorno = new ArrayList<>();
                cache.forEach(id -> {
                    String proj = (String) memcacheService.get(id);
                    if (proj != null) {
                        projetosRetorno.add(proj);
                    } else {
						Projeto p = dao.buscarProjeto(id);
						projetosRetorno.add(serializer.deepSerialize(p.toProjetoJson()));
                    }
                });
                return String.valueOf((projetosRetorno));
            }
        }

        List<Projeto> projetos = dao.buscarTodosProjetos(equipe, tribo);

        projetos.sort(new ProjetoComparator());
        List<ProjetoJson> projetosJson = Projeto.toProjetoJson(projetos);

        if (useCache(equipe, tribo)) {
            List<Long> ids = new ArrayList<>();
            projetosJson.forEach(projeto -> {
                ids.add(projeto.getIdPma());
                try {
                    memcacheService.put(projeto.getIdPma(), serializer.deepSerialize(projeto));
                } catch (Exception e) {
                    LOG.info(e.getMessage());
                }
            });

            if (projetosJson.size() > 0) {
                try {
                    memcacheService.put(ProjetoJson.KEY_CACHE, ids);
                } catch (Exception e) {
                    LOG.info(e.getMessage());
                }
            }
        }

        return serializer.deepSerialize(projetosJson);
    }

    private boolean useCache(String equipe, String tribo) {
        return ((equipe == null) && (tribo == null));
    }
}
