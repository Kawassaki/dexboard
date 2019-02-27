package br.com.dextra.dexboard.domain;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

import java.util.Date;

@Entity
public class IndicadorResposta {
    @Id
    private String composeId;
    @Index
    private String conteudo;
    @Index
    Key<Projeto> projeto;
    @Index
    Key<Indicador> indicador;
    @Index
    Key<IndicadorQuestao> questao;
    @Index
    private Date data = new Date();

    public IndicadorResposta() {
    }

    public IndicadorResposta(String conteudo, Key<Projeto> projeto, Key<Indicador> indicador, Key<IndicadorQuestao> questao) {
        this.conteudo = conteudo;
        this.projeto = projeto;
        this.indicador = indicador;
        this.questao = questao;
    }

    public void setConteudo(String conteudo) {
        this.conteudo = conteudo;
    }

    public String getConteudo() {
        return conteudo;
    }

    public void setProjeto(Key<Projeto> projeto) {
        this.projeto = projeto;
    }

    public Key<Projeto> getProjeto() {
        return projeto;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public Date getData() {
        return data;
    }

    public void setComposeId(String composeId) {
        this.composeId = composeId;
    }

    public void setIndicador(Key<Indicador> indicador) {
        this.indicador = indicador;
    }

    public void setQuestao(Key<IndicadorQuestao> questao) {
        this.questao = questao;
    }

    public String getComposeId() {
        return composeId;
    }

    public Key<Indicador> getIndicador() {
        return indicador;
    }

    public Key<IndicadorQuestao> getQuestao() {
        return questao;
    }

    public void defineComposeId() {
        this.composeId = String.format("%s;%s", this.getQuestao().getName(), this.getProjeto().getId());
    }
}
