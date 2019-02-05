package br.com.dextra.dexboard.domain;

import flexjson.JSON;

import java.util.List;

public class ListaProjeto {

	private List<Projeto> value;


	public ListaProjeto() {
	}

	public ListaProjeto(List<Projeto> value) {
		this.value = value;
	}

	@JSON
	public List<Projeto> getValue() {
		return value;
	}
}
