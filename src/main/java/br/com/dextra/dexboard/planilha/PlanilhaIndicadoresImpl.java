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

	private String buscarSituacaoDoIndicador(int linha) { return recuperarConteudoCelula(linha, 5); }

	private Integer buscaIDIndicador(int linha) { return recuperarConteudoCelulaInt(linha, 3); }
	
	private String buscarPlanilhaDeQuestoes(int linha) { return recuperarConteudoCelula(linha, 4); }

	@Override
	public List<Indicador> criarListaDeIndicadores() {
		List<Indicador> indicadores = new ArrayList<>();

		int i = 1;
		while (true) {
			String nomeIndicador = buscarNomeDoIndicador(i);
			if (!StringUtils.isNullOrEmpty(nomeIndicador)) {
				String descricao = buscarDescricaoDoIndicador(i);
				String situacao = buscarSituacaoDoIndicador(i);
				String planilhaDeQuestoes = buscarPlanilhaDeQuestoes(i);
				Long id = (long) buscaIDIndicador(i);
				boolean sitBool = situacao.equalsIgnoreCase("Ativo");
				indicadores.add(new Indicador(id, nomeIndicador, descricao, sitBool, i, planilhaDeQuestoes));
				i++;
			} else {
				return indicadores;
			}
		}

	}
}
