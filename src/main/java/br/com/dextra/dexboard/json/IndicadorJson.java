package br.com.dextra.dexboard.json;

import br.com.dextra.dexboard.dao.NotificacaoDao;
import br.com.dextra.dexboard.dao.ProjetoDao;
import br.com.dextra.dexboard.domain.*;
import br.com.dextra.dexboard.service.IndicadorService;
import com.googlecode.objectify.Key;
import flexjson.JSON;

import java.util.*;

public class IndicadorJson {

	private Indicador indicador;
	private List<RegistroAlteracao> registros;
	private List<IndicadorQuestao> questoes;

	public IndicadorJson() {
		super();
	}

	public IndicadorJson(Indicador indicador) {
		this.indicador = indicador;

		IndicadorService service = new IndicadorService();
		this.registros = service.buscarRegistrosDeAlteracoes(indicador);
		this.questoes = service.buscarQuestoesPelaKeyDoIndicador(Key.create(Indicador.class, indicador.getId()));

		Collections.sort(this.registros, (r1, r2) -> r2.getData().compareTo(r1.getData()));
	}

	public Long getId() {
		return this.indicador.getId();
	}

	public String getNome() {
		return this.indicador.getNome();
	}

	public String getDescricao() {
		return this.indicador.getDescricao();
	}

	public boolean getAtivo() {
		return this.indicador.getAtivo();
	}

	public int getPosicao() { return this.indicador.getPosicao(); }

	public boolean getAtrasado() {
		if (this.registros.isEmpty()) {
			return true;
		}
		RegistroAlteracao ultimaAlteracao = this.registros.get(0);

		if (!isValido(ultimaAlteracao.getData())) {
			return true;
		}

		return false;
	}

	public Classificacao getClassificacao() {
		if (this.registros.isEmpty()) {
			return Classificacao.PERIGO;
		}
		RegistroAlteracao ultimaAlteracao = this.registros.get(0);
		return ultimaAlteracao.getClassificacao();
	}

	@JSON
	public List<RegistroAlteracao> getRegistros() {
		return registros;
	}

	private boolean isValido(Date data) {
		Calendar calendar = GregorianCalendar.getInstance();
		calendar.setTime(data);
		calendar.add(Calendar.DAY_OF_MONTH, NotificacaoDao.getValidadeAlteracao());

		return calendar.getTime().compareTo(new Date()) > -1;
	}

	public List<IndicadorQuestao> getQuestoes() {
		return questoes;
	}

	public void setQuestoes(List<IndicadorQuestao> questoes) {
		this.questoes = questoes;
	}
}
