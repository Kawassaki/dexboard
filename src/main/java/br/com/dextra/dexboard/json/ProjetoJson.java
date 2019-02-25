package br.com.dextra.dexboard.json;

import br.com.dextra.dexboard.dao.ProjetoDao;
import br.com.dextra.dexboard.domain.Classificacao;
import br.com.dextra.dexboard.domain.Indicador;
import br.com.dextra.dexboard.domain.Projeto;
import flexjson.JSON;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ProjetoJson {

	private Projeto projeto;
	private List<IndicadorJson> indicadores = new ArrayList<>();
	private Classificacao classificacao;
	private boolean atrasado;

	public ProjetoJson() {
	}

	public ProjetoJson(Projeto projeto) {
		this.projeto = projeto;

		inicializaIndicadores();
		// ordenaIndicadores();

		this.classificacao = defineClassificacao();
		this.atrasado = defineAtrasado();
	}

	private void inicializaIndicadores() {
		ProjetoDao dao = new ProjetoDao();

		List<Indicador> indicadoresDataStore = dao.buscarIndicadoresDoProjeto(getIdPma());
		for (Indicador i : indicadoresDataStore) {
			if (i.getAtivo()) {
				this.indicadores.add(new IndicadorJson(i));
			}
		}
	}

	private void ordenaIndicadores() {
		this.indicadores.sort(Comparator.comparing(IndicadorJson::getNome));
	}

	public boolean getAtrasado() {
		return atrasado;
	}

	@JSON
	public List<IndicadorJson> getIndicadores() {
		return indicadores;
	}

	public Long getIdPma() {
		return this.projeto.getIdPma();
	}

	public String getNome() {
		return this.projeto.getNome();
	}

	public Double getCpi() {
		return this.projeto.getCpi();
	}

	public String getEquipe() {
		return this.projeto.getEquipe();
	}

	public Classificacao getClassificacao() {
		return classificacao;
	}

	public String getApresentacao() {
		return this.projeto.getApresentacao();
	}

	private boolean defineAtrasado() {
		for (IndicadorJson indicador : this.getIndicadores()) {
			if (indicador.getAtrasado()) {
				return true;
			}
		}
		return false;
	}

	private Classificacao defineClassificacao() {
		Classificacao retorno = Classificacao.OK;

		for (IndicadorJson indicador : this.getIndicadores()) {
			Classificacao classificacao = indicador.getClassificacao();

			if (classificacao.equals(Classificacao.PERIGO)) {
				return Classificacao.PERIGO;
			}

			if (classificacao.equals(Classificacao.ATENCAO)) {
				retorno = Classificacao.ATENCAO;
			}
		}

		return retorno;
	}

}
