package br.com.dextra.dexboard.planilha;

import br.com.dextra.dexboard.servlet.Context;

public class PlanilhaExport extends Planilha {

    private static final String CHAVE_PLANILHA_EXPORT_PRODUCAO = System.getProperty("dxb.export.spreadsheet");

    private static final String CHAVE_PLANILHA_EXPORT_TESTE = "1hdlDWls7gWNKfIyKwMI0k-fP4VVlYyyEXL4aG1MXEY8";

    public PlanilhaExport(String nomePlanilha) {
        super(getChavePlanilha(), nomePlanilha);
    }

    private static String getChavePlanilha() {
        if (Context.isIntegrationTestEnvironment()) {
            return CHAVE_PLANILHA_EXPORT_TESTE;
        }
        return CHAVE_PLANILHA_EXPORT_PRODUCAO;
    }

}