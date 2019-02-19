package br.com.dextra.dexboard.mock;

import br.com.dextra.dexboard.domain.Indicador;
import br.com.dextra.dexboard.planilha.PlanilhaIndicadores;

import java.util.Arrays;
import java.util.List;

public class MockPlanilhaIndicadores implements PlanilhaIndicadores {
	@Override
	public List<Indicador> criarListaDeIndicadores() {
		return Arrays.asList(
				new Indicador(1L, "Satisfa\u00E7\u00E3o do cliente", "Como está a satisfação do cliente com o projeto?"),
				new Indicador(2L, "Satisfa\u00E7\u00E3o da equipe", "Como está a satisfação da equipe com o projeto?"),
				new Indicador(3L, "Foco em entrega do valor", "Descrição 3"),
				new Indicador(4L, "Qualidade funcional", "Como está a visão da equipe sobre a qualidade técnica do projeto? O time tem orgulho do que está produzindo?"),
				new Indicador(5L, "Qualidade do c\u00F3digo", "Como está a qualidade da experiência do usuário com o sistema?"),
				new Indicador(6L, "CPI", "Avalia como está o custo do projeto, em relação ao orçamento previsto. O ideal é que o indicador seja maior ou igual a 1. Um número menor do que 1 indica que estamos gastando mais do que o previsto, como funciona com um testo grande"),
				new Indicador(7L, "Indicador 7", "Descricao do indicador 7"),
				new Indicador(8L, "Indicador 8", "Descricao do indicador 8")
		);
	}
}
