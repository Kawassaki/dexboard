package br.com.dextra.dexboard.planilha;

import br.com.dextra.dexboard.mock.MockPlanilhaIndicadores;
import br.com.dextra.dexboard.mock.MockPlanilhaPrincipal;

public class PlanilhaFactory {

//	private static final boolean MOCK = Context.isDevelopmentEnvironment();
	private static final boolean MOCK = false;

	public static PlanilhaIndicadores indicadores() {
		return (MOCK) ? new MockPlanilhaIndicadores() : new PlanilhaIndicadoresImpl();
	}

	public static PlanilhaPrincipal principal() {
		return (MOCK) ? new MockPlanilhaPrincipal() : new PlanilhaPrincipalImpl();

	}

	public static PlanilhaQuestoes questoes(String nomeIndicador) {
		return (MOCK) ? null : new PlanilhaQuestoesImpl(nomeIndicador); // TODO MOCK
	}

}
