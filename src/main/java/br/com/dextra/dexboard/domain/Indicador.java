package br.com.dextra.dexboard.domain;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Ignore;
import com.googlecode.objectify.annotation.Index;

import java.util.List;

@Entity
public class Indicador {

	@Id
	private String composeId;
	@Index
	private Long id;
	@Index
	private Key<Projeto> projeto;
	@Index
	private String nome;
	private String descricao;
	@Index
	private boolean ativo;
	private int posicao;
    private String planilhaQuestoes;
    @Ignore
    private List<IndicadorQuestao> questoes;

    public Indicador() {}

    public Indicador(Long id, String nomeIndicador, String descricao, boolean ativo, int posicao, String planilhaQuestoes) {
        this.id = id;
        this.nome = nomeIndicador;
        this.descricao = descricao;
        this.ativo = ativo;
        this.posicao = posicao;
        this.planilhaQuestoes = planilhaQuestoes;
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Key<Projeto> getProjeto() {
        return projeto;
    }

    public void setProjeto(Key<Projeto> projeto) {
        this.projeto = projeto;
    }

    public String getDescricao() { return this.descricao; }

    public boolean getAtivo() { return ativo; }

    public void setAtivo(boolean ativo) { this.ativo = ativo; }

    public int getPosicao() {
        return posicao;
    }

    public void setPosicao(int posicao) {
        this.posicao = posicao;
    }

    public void defineComposeId() {
        String value = String.format("%s;%s", this.getProjeto().getId(), this.getId().toString());
        this.composeId = value;
    }

    public String getPlanilhaQuestoes() {
        return planilhaQuestoes;
    }

    public void setPlanilhaQuestoes(String planilhaQuestoes) {
        this.planilhaQuestoes = planilhaQuestoes;
    }

    public void setQuestoes(List<IndicadorQuestao> questoes) {
        this.questoes = questoes;
    }

    public List<IndicadorQuestao> getQuestoes() {
        return questoes;
    }
}
