package br.com.dextra.dexboard.mock;

import br.com.dextra.dexboard.domain.Projeto;
import br.com.dextra.dexboard.planilha.PlanilhaPrincipal;

import java.util.HashMap;
import java.util.Map;

public class MockPlanilhaPrincipal implements PlanilhaPrincipal {

	private static Projeto p(long id, String nome, String equipe, String email, double cpi, String apresentacao) {
		Projeto projeto = new Projeto();
		projeto.setIdPma(id);
		projeto.setNome(nome);
		projeto.setEquipe(equipe);
		projeto.setEmail(email);
		projeto.setCpi(cpi);
		projeto.setApresentacao(apresentacao);
		return projeto;
	}

	@Override
	public Map<Long, Projeto> buscarDadosDosProjetos() {
		Map<Long, Projeto> map = new HashMap<>();

		// TODO: trazer da build
		String email = "fernando@dextra-sw.com";
		String apresentacao = "https://docs.google.com/presentation/d/1ID6Oh3Dm0HHFQb9h8A32WJYy5Z-gufbLiTEcrdKAtM8/embed";
		map.put(495L, p(495L, "A4C", "Chaos", email, 1.01d, apresentacao));
		map.put(565L, p(565L, "Confidence", "Rocket", email, 0.99d, apresentacao));
		map.put(530L, p(530L, "DPaschoal", "Heisenberg", email, 1.05d, apresentacao));
		map.put(568L, p(568L, "FAB SDAB", "Mustache", email, 0.92d, apresentacao));
		map.put(521L, p(521L, "Certics", "Buzz", email, 0.99d, apresentacao));
		map.put(537L, p(537L, "Globosat Scrum", "Walking", email, 1.00d, apresentacao));
		map.put(441L, p(441L, "ICTS: Manutenção", "Mustache", email, 1.05d, apresentacao));
		map.put(571L, p(571L, "Trópico", "Tropico", email, 1.07d, apresentacao));
		map.put(517L, p(517L, "Marinha", "Chuck", email, 0.59d, apresentacao));
		map.put(542L, p(542L, "Portal Doc", "Buzz", email, 1.08d, apresentacao));
		map.put(543L, p(543L, "Poli.TIC.Sys", "Buzz", email, 1.00d, apresentacao));
		map.put(549L, p(549L, "Jequiti", "SS", email, 1.02d, apresentacao));
		map.put(555L, p(555L, "Unimed CG", "Unimed", email, 0.95d, apresentacao));
		map.put(556L, p(556L, "Pósitron", "Positron", email, 0.85d, apresentacao));
		map.put(564L, p(564L, "CMB - Autor. Cert.", "Buzz", email, 1.01d, apresentacao));
		map.put(563L, p(563L, "Integral: Busca Multimídia", "Walking", email, 0.99d, apresentacao));
		map.put(553L, p(553L, "Move+ Suporte", "Minions", email, 1.20d, apresentacao));
		map.put(566L, p(566L, "CMB - Insumos", "Minions", email, 0.97d, apresentacao));
		map.put(569L, p(569L, "SMARAPD", "Heisenberg", email, 0.98d, apresentacao));
		map.put(579L, p(579L, "Movile", "Rocket", email, 1.70d, apresentacao));
		map.put(583L, p(583L, "Wareline: Editor", "Mustache", email, 1.18d, apresentacao));
		map.put(585L, p(585L, "ADV: Fase II", "Mustache", email, 1.39d, apresentacao));
		map.put(24L, p(24L, "TI Dextra", "TI", email, 1.00d, apresentacao));

		return map;
	}

}
