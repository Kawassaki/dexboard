package br.com.dextra.dexboard.service;

import br.com.dextra.dexboard.domain.Projeto;
import br.com.dextra.dexboard.planilha.PlanilhaFactory;
import br.com.dextra.dexboard.planilha.PlanilhaPrincipal;
import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;

import java.util.Map;

public class ProjetoPlanilhaService {

	public static Map<Long, Projeto> buscarDadosProjetosAtivos() {
		PlanilhaPrincipal planilhaPrincipal = PlanilhaFactory.principal();
		return planilhaPrincipal.buscarDadosDosProjetos();
	}

	public static void limparCache() {
		MemcacheService cache = MemcacheServiceFactory.getMemcacheService("dados-projetos");
		cache.delete("dados-projetos");
	}

}
