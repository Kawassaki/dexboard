package br.com.dextra.dexboard.servlet;

import br.com.dextra.dexboard.dao.ProjetoDao;
import br.com.dextra.dexboard.domain.Indicador;
import br.com.dextra.dexboard.domain.Projeto;
import br.com.dextra.dexboard.domain.RegistroAlteracao;
import br.com.dextra.dexboard.json.HistoricoJson;
import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;
import com.googlecode.objectify.Key;
import flexjson.JSONSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

public class HistoryServlet extends HttpServlet {

	public static final int CACHE_EXPIRATION_SECONDS = 60 * 60 * 6;
	private static final long serialVersionUID = 892806850164024145L;
	private Map<Key<Indicador>, Object> cacheIndicador = new HashMap<Key<Indicador>, Object>();
	private Map<Key<Projeto>, Object> cacheProjeto = new HashMap<Key<Projeto>, Object>();
	private static final Logger LOG = LoggerFactory.getLogger(IndicadorServlet.class);

	private ProjetoDao dao;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setCharacterEncoding("UTF-8");
		resp.setContentType("application/json");
		resp.getWriter().print(getJsonHistoryWithCache());
	}

	private String getJsonHistoryWithCache() {
		MemcacheService memcacheService = MemcacheServiceFactory.getMemcacheService();

		String cacheJson = (String) memcacheService.get(ProjetoDao.HISTORY_CACHE);
		if (cacheJson != null) {
			return cacheJson;
		}

		String json = getJsonHistory();

		try {
			memcacheService.put(ProjetoDao.HISTORY_CACHE, json);
		} catch (Exception e) {
			LOG.info(e.toString());
		}

		return json;
	}

	private ProjetoDao getDao() {
		if (dao == null)
			dao = new ProjetoDao();
		return dao;
	}

	private String getJsonHistory() {
		Calendar cal = GregorianCalendar.getInstance();
		cal.setTime(new Date());
		cal.add(Calendar.DAY_OF_MONTH, -30);

		List<RegistroAlteracao> buscarHistoricoAlteracoes = getDao().buscarHistoricoAlteracoes(cal.getTime(), 16);
		List<HistoricoJson> historico = new ArrayList<>();

		for (RegistroAlteracao h : buscarHistoricoAlteracoes) {
			historico.add(new HistoricoJson(h, getProjeto(h.getProjeto()), getIndicador(h.getIndicador())));
		}

		JSONSerializer serializer = new JSONSerializer();
		serializer.exclude("*.class", "*.projeto");
		return serializer.deepSerialize(historico);
	}

	private Indicador getIndicador(Key<Indicador> key) {
		Object cache = cacheIndicador.get(key);
		if (cache == null) {
			cache = getDao().buscarIndicadorByKey(key);
			cacheIndicador.put(key, cache);
		}
		return (Indicador) cache;
	}

	private Projeto getProjeto(Key<Projeto> key) {
		Object cache = cacheProjeto.get(key);
		if (cache == null) {
			cache = getDao().buscarProjetoByKey(key);
			cacheProjeto.put(key, cache);
		}
		return (Projeto) cache;
	}

}
