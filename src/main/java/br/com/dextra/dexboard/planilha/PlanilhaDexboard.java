package br.com.dextra.dexboard.planilha;

import br.com.dextra.dexboard.servlet.Context;

class PlanilhaDexboard extends Planilha {

    // TODO: trazer de propriedades do sistema

    private static final String CHAVE_PLANILHA_DEXBOARD_PRODUCAO = System.getProperty("dxb.spreadhseet");

    private static final String CHAVE_PLANILHA_DEXBOARD_TESTE = "11z7PwH4sADaiXxshNPYr-88u3flqewCcGRGJok9cl6o";

    public PlanilhaDexboard(String nomePlanilha) {
        super(getChavePlanilha(), nomePlanilha);
    }

    private static String getChavePlanilha() {
        if (Context.isIntegrationTestEnvironment()) {
            return CHAVE_PLANILHA_DEXBOARD_TESTE;
        }
        return CHAVE_PLANILHA_DEXBOARD_PRODUCAO;
    }

}
