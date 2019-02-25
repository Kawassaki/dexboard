package br.com.dextra.dexboard.domain;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

@Entity
public class Indicador {

	@Id
	private String composeId;
	private Long id;
	@Index
	private Key<Projeto> projeto;
	private String nome;
	private String descricao;
	private boolean ativo;
	private int posicao;
	public Indicador() {
		super();
	}

	public Indicador(Long id, String nomeIndicador, String descricao, boolean ativo, int posicao) {
		this.id = id;
		this.nome = nomeIndicador;
		this.descricao = descricao;
		this.ativo = ativo;
		this.posicao = posicao;
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
}
