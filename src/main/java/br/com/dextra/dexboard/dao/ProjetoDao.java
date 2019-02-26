package br.com.dextra.dexboard.dao;

import br.com.dextra.dexboard.domain.Indicador;
import br.com.dextra.dexboard.domain.Projeto;
import br.com.dextra.dexboard.domain.RegistroAlteracao;
import com.google.appengine.api.memcache.MemcacheServiceFactory;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.LoadResult;
import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.cmd.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import static java.util.stream.Collectors.toList;

public class ProjetoDao {

	public static final String KEY_CACHE = "dexboard.cache.key";
	public static final String HISTORY_CACHE = "dexlife.cache.key";
	private static final Logger LOG = LoggerFactory.getLogger(ProjetoDao.class);
	private Objectify ofy;

	public ProjetoDao() {
		RegisterClasses.register();
		ofy = ObjectifyService.ofy();
	}

	public void salvarProjeto(Projeto p) {
		MemcacheServiceFactory.getMemcacheService().delete(KEY_CACHE);
		MemcacheServiceFactory.getMemcacheService().delete(HISTORY_CACHE);
		ofy.save().entity(p).now();
	}

	public Projeto buscarProjeto(Long idProjeto) {
		return ofy.load().type(Projeto.class).id(idProjeto).now();
	}

	public Projeto buscarProjetoByKey(Key<Projeto> key) {
		LoadResult<Projeto> first = ofy.load().type(Projeto.class).filterKey(key).first();
		return first.now();
	}

	public Indicador buscarIndicadorByKey(Key<Indicador> key) {
		LoadResult<Indicador> first = ofy.load().type(Indicador.class).filterKey(key).first();
		return first.now();
	}

	public List<RegistroAlteracao> buscarHistoricoAlteracoes(Date minDate, Integer limit) {
		List<RegistroAlteracao> list;

		Query<RegistroAlteracao> queryByDate = ofy.load().type(RegistroAlteracao.class).filter("data >=", minDate);
		list = queryByDate.list();

		if (list == null || list.size() == 0) {
			return new ArrayList<>();
		}

		Collections.reverse(list);

		if (list.size() <= limit - 1) {
			return list;
		} else {
			return list.subList(0, limit);
		}
	}

	public List<Projeto> buscarTodosProjetos() {
		Query<Projeto> query = ofy.load()
				.type(Projeto.class)
				.filter("ativo", true);
		return query.list();
	}

	public List<Projeto> buscarProjetosInativos() {
		Query<Projeto> query = ofy.load()
				.type(Projeto.class)
				.filter("ativo", false);
		return query.list();
	}

	public List<Projeto> buscarProjetosEquipe(String equipe) {
		Query<Projeto> query = ofy.load()
				.type(Projeto.class)
				.filter("equipe", equipe.toUpperCase());

		List<Projeto> list = query.list();
		return list.stream().filter(Projeto::isAtivo).collect(toList());
	}

	public List<Projeto> buscarProjetosTribo(String tribo) {
		Query<Projeto> query = ofy.load()
				.type(Projeto.class)
				.filter("tribo", tribo.toUpperCase());

		List<Projeto> list = query.list();

		return list.stream().filter(Projeto::isAtivo).collect(toList());
	}

	public List<Projeto> buscarTodosProjetos(String equipe, String tribo) {

		boolean hasEquipe = equipe != null;
		boolean hasTribo = tribo != null;

		if (hasEquipe) return buscarProjetosEquipe(equipe);
		if (hasTribo) return buscarProjetosTribo(tribo);
		return buscarTodosProjetos();
	}

	public List<Indicador> buscarIndicadoresDoProjeto(Long idPma) {
		Projeto projeto = buscarProjeto(idPma);
		return ofy.load().type(Indicador.class).filter("projeto", projeto).list();
	}

	public List<RegistroAlteracao> buscarRegistrosDeAlteracoes(Indicador indicador) {
		List<RegistroAlteracao> list = ofy.load().type(RegistroAlteracao.class).filter("indicador", indicador).list();
		if (list == null) {
			list = new ArrayList<>();
		}

		if (indicador.getProjeto().getId() == 619) {
			for (RegistroAlteracao reg : list) {
				LOG.info(indicador.getNome() + "-" + reg.getUsuario() + "-" + reg.getData());
			}
		}

		return list;
	}

	public void salvaIndicador(Long idProjetoPma, Indicador indicador) {
		MemcacheServiceFactory.getMemcacheService().delete(KEY_CACHE);
		MemcacheServiceFactory.getMemcacheService().delete(HISTORY_CACHE);
		Key<Projeto> keyProjeto = Key.create(Projeto.class, idProjetoPma);
		indicador.setProjeto(keyProjeto);
		indicador.defineComposeId();
		ofy.save().entity(indicador);
	}

	public RegistroAlteracao salvaAlteracao(Long idProjetoPma, Long idIndicador, RegistroAlteracao registroAlteracao) {
		MemcacheServiceFactory.getMemcacheService().delete(KEY_CACHE);
		MemcacheServiceFactory.getMemcacheService().delete(HISTORY_CACHE);
		Key<Projeto> keyProjeto = Key.create(Projeto.class, idProjetoPma);
		Key<Indicador> keyIndicador = Key.create(Indicador.class, idProjetoPma + ";" + idIndicador);

		registroAlteracao.setIndicador(keyIndicador);
		registroAlteracao.setProjeto(keyProjeto);
		registroAlteracao.defineId();
		ofy.save().entity(registroAlteracao).now();

		return registroAlteracao;
	}

}
