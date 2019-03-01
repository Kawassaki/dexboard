package br.com.dextra.dexboard.servlet;

import br.com.dextra.dexboard.dao.ProjetoDao;
import br.com.dextra.dexboard.domain.Projeto;
import br.com.dextra.dexboard.json.ProjetoJson;
import br.com.dextra.dexboard.repository.ProjetoComparator;
import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;
import com.google.gson.JsonDeserializer;
import flexjson.JSONSerializer;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class QueryServlet extends HttpServlet {

	public static final int CACHE_EXPIRATION_SECONDS = 60 * 60;
	private static final String EQUIPE_HTTP_PARAMETER = "equipe";
	private static final String TRIBO_HTTP_PARAMETER = "tribo";
	private static final long serialVersionUID = -1248500946944090403L;

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
		List cache = (List) memcacheService.get(ProjetoJson.KEY_CACHE);
		if (cache != null) {
			List<String> projetosRetorno = new ArrayList<>();
			cache.forEach(id -> projetosRetorno.add( (String) memcacheService.get(id)));
			return String.valueOf((projetosRetorno));
		}
		List<Projeto> projetos = dao.buscarTodosProjetos(equipe, tribo);

		Collections.sort(projetos, new ProjetoComparator());
		List<ProjetoJson> projetosJson = Projeto.toProjetoJson(projetos);

		List<Long> i = new ArrayList<>();
		projetosJson.forEach(projeto -> {
			i.add(projeto.getIdPma());
			memcacheService.put(projeto.getIdPma(), serializer.serialize(projeto));
		});

		if (projetosJson.size() > 0) { memcacheService.put(ProjetoJson.KEY_CACHE, i); }


		return serializer.deepSerialize(projetosJson);
	}

	private boolean useCache(String equipe, String tribo) {
		return ((equipe == null) && (tribo == null));
	}
}
