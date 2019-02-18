package br.com.dextra.dexboard.mock;

import br.com.dextra.dexboard.domain.Indicador;
import br.com.dextra.dexboard.planilha.PlanilhaIndicadores;

import java.util.Arrays;
import java.util.List;

public class MockPlanilhaIndicadores implements PlanilhaIndicadores {

    @Override
    public List<Indicador> criarListaDeIndicadores() {
        return Arrays.asList(
                new Indicador(1L, "Satisfa\u00E7\u00E3o do cliente"),
                new Indicador(2L, "Satisfa\u00E7\u00E3o da equipe"),
                new Indicador(3L, "Foco em entrega do valor"),
                new Indicador(5L, "Qualidade"),
                new Indicador(6L, "CPI"),
                new Indicador(7L, "Processo Ágil: Scrum Master"),
                new Indicador(8L, "Processo Ágil: Retrospectiva"),
                new Indicador(9L, "Processo Ágil: Backlog & Estimativas")
        );
    }

}
