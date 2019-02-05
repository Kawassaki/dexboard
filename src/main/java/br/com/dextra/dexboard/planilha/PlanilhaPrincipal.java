package br.com.dextra.dexboard.planilha;

import br.com.dextra.dexboard.domain.Projeto;

import java.util.Map;

public interface PlanilhaPrincipal {

	Map<Long, Projeto> buscarDadosDosProjetos();
}
