package br.com.dextra.dexboard.service;

import br.com.dextra.dexboard.dao.ProjetoDao;
import br.com.dextra.dexboard.domain.*;
import br.com.dextra.dexboard.json.IndicadorRespostaJson;
import com.googlecode.objectify.Key;

import java.util.List;

public class IndicadorService {

    ProjetoDao dao;

    public IndicadorService() {
        this.dao = new ProjetoDao();
    }

    public RegistroAlteracao salvarAlteracao(Long idProjeto, Long idIndicador, RegistroAlteracao regAlteracao, List<IndicadorResposta> respostas){
        if(respostas != null && respostas.size() > 0){
            for (IndicadorResposta resposta: respostas){
               IndicadorResposta indicadorResposta = new IndicadorResposta(
                    resposta.getConteudo(),
                    Key.create(Projeto.class,idProjeto),
                    Key.create(Indicador.class, idIndicador),
                    Key.create(IndicadorQuestao.class, resposta.getComposeId())
                );
                indicadorResposta.defineComposeId();
                dao.salvarRespostasDoIndicador(indicadorResposta);
            }
        }

        return dao.salvaAlteracao(idProjeto, idIndicador, regAlteracao);
    }

    public List<IndicadorRespostaJson> buscarRespostasDasQuestoesDoIndicador(Long projetoId, Long indicadorId){
        return dao.buscarRespostasDasQuestoesDoIndicador(
                Key.create(Projeto.class, projetoId),
                Key.create(Indicador.class, indicadorId)
        );
    }

    public List<IndicadorQuestao> buscarQuestoesPelaKeyDoIndicador(Key<Indicador> key){
        return dao.buscarQuestoesPelaKeyDoIndicador(key);
    }

    public List<RegistroAlteracao> buscarRegistrosDeAlteracoes(Indicador indicador) {
        return dao.buscarRegistrosDeAlteracoes(indicador);
    }

}
