package br.com.dextra.dexboard.planilha;

import br.com.dextra.dexboard.domain.Indicador;
import br.com.dextra.dexboard.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

class PlanilhaIndicadoresImpl extends PlanilhaDexboard implements PlanilhaIndicadores {

    public PlanilhaIndicadoresImpl() {
        super("Indicadores");
    }

    private String buscarNomeDoIndicador(int linha) {
        return recuperarConteudoCelula(linha, 1);
    }

    private String buscarDescricaoDoIndicador(int linha) {
        return recuperarConteudoCelula(linha, 2);
    }

    private String buscarPlanilhaDeQuestoes(int linha) {
        return recuperarConteudoCelula(linha, 4);
    }

    @Override
    public List<Indicador> criarListaDeIndicadores() {
        List<Indicador> indicadores = new ArrayList<>();

        Long i = 1L;
        while (true) {
            String nomeIndicador = buscarNomeDoIndicador(i.intValue());
            String descricao = buscarDescricaoDoIndicador(i.intValue());
            String planilhaDeQuestoes = buscarPlanilhaDeQuestoes(i.intValue());

            if (!StringUtils.isNullOrEmpty(nomeIndicador)) {
                indicadores.add(new Indicador(i, nomeIndicador, descricao, planilhaDeQuestoes));
                i++;
            } else {
                return indicadores;
            }
        }

    }
}
