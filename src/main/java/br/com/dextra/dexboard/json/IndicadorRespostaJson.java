package br.com.dextra.dexboard.json;

import java.util.Date;

public class IndicadorRespostaJson {
    private String categoria;
    private String questao;
    private String resposta;
    private Date data;

    public IndicadorRespostaJson(String categoria, String questao, String resposta) {
        this.categoria = categoria;
        this.questao = questao;
        this.resposta = resposta;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setQuestao(String questao) {
        this.questao = questao;
    }

    public void setResposta(String resposta) {
        this.resposta = resposta;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public String getQuestao() {
        return questao;
    }

    public String getResposta() {
        return resposta;
    }

    public Date getData() {
        return data;
    }
}
