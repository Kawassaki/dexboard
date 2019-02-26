package br.com.dextra.dexboard.planilha;

import br.com.dextra.dexboard.domain.IndicadorQuestao;

import java.util.ArrayList;
import java.util.List;

public class PlanilhaQuestoesImpl extends PlanilhaDexboard implements PlanilhaQuestoes{

    private final String COLUNA_ID = "Id";
    private final String COLUNA_CATEGORIA = "Categoria";
    private final String COLUNA_QUESTAO = "Questão";

    public PlanilhaQuestoesImpl(String nomePlanilha) {
        super(nomePlanilha);
    }

    private Long buscarId(int indice){
        return Long.parseLong(recuperarConteudoCelula(indice, COLUNA_ID));
    }

    private String buscarCategoria(int indice){
        try {
            return recuperarConteudoCelula(indice, this.COLUNA_CATEGORIA);
        }catch(Exception ex){
            return null;
        }
    }

    private String buscarQuestao(int indice){
        return recuperarConteudoCelula(indice, this.COLUNA_QUESTAO);
    }

    @Override
    public List<IndicadorQuestao> criarListaDeQuestoes() {
        List<IndicadorQuestao> questoes = new ArrayList<>();
        int indice = 0;
        while(true){
            final String nomeCategoria = this.buscarCategoria(indice);
            if(nomeCategoria == null){
                break;
            }
            questoes.add(new IndicadorQuestao(this.buscarId(indice), this.buscarQuestao(indice), nomeCategoria));
            indice++;
        }
        return questoes;
    }

}
