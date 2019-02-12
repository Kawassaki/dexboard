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
                new Indicador(4L, "Qualidade funcional"),
                new Indicador(5L, "Qualidade do c\u00F3digo"),
                new Indicador(6L, "CPI")
        );
    }

}
