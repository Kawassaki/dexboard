package br.com.dextra.dexboard.mock;

import br.com.dextra.dexboard.domain.IndicadorQuestao;
import br.com.dextra.dexboard.planilha.PlanilhaQuestoes;

import java.util.Arrays;
import java.util.List;

public class MockPlanilhaIndicadoresQuestoes implements PlanilhaQuestoes {
    @Override
    public List<IndicadorQuestao> criarListaDeQuestoes(){
        return Arrays.asList(
                new IndicadorQuestao(1L, "Existe mock?", "Mock", true),
                new IndicadorQuestao(2L, "Existe mock 2?", "Mock", true),
                new IndicadorQuestao(3L, "Existe mock 3?", "Mock 2", true),
                new IndicadorQuestao(4L, "Existe mock 4?", "Mock 2", true)
        );
    }

}
