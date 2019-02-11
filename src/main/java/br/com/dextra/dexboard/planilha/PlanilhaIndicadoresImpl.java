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

	@Override
	public List<Indicador> criarListaDeIndicadores() {
		List<Indicador> indicadores = new ArrayList<Indicador>();

		Long i = 1l;
		while (true) {
			String nomeIndicador = buscarNomeDoIndicador(i.intValue());
			String descricao = buscarDescricaoDoIndicador(i.intValue());

			if (!StringUtils.isNullOrEmpty(nomeIndicador)) {
				indicadores.add(new Indicador(i, nomeIndicador, descricao));
				i++;
			} else {
				return indicadores;
			}
		}

	}
}
