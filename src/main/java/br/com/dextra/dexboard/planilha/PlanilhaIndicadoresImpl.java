package br.com.dextra.dexboard.planilha;

import br.com.dextra.dexboard.domain.Indicador;
import br.com.dextra.dexboard.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

class PlanilhaIndicadoresImpl extends PlanilhaDexboard implements PlanilhaIndicadores {

	private final String COLUNA_ID = "ID";
	private final String COLUNA_NOME = "Nome";
	private final String COLUNA_DESCICAO = "Descição";
	private final String COLUNA_QUESTIONARIO = "Questionario";
	private final String COLUNA_SITUACAO = "Situação";


	public PlanilhaIndicadoresImpl() {
		super("Indicadores");
	}

	private String buscarNomeDoIndicador(int linha) {
		try {
			return recuperarConteudoCelula(linha, COLUNA_NOME);
		} catch (Exception e) {
			return null;
		}
	}

	private String buscarDescricaoDoIndicador(int linha) {
		return recuperarConteudoCelula(linha, COLUNA_DESCICAO);
	}

	private String buscarSituacaoDoIndicador(int linha) { return recuperarConteudoCelula(linha, COLUNA_SITUACAO); }

	private Integer buscaIDIndicador(int linha) { return recuperarConteudoCelulaInt(linha, COLUNA_ID); }

	private String buscarPlanilhaDeQuestoes(int linha) { return recuperarConteudoCelula(linha, COLUNA_QUESTIONARIO); }

	@Override
	public List<Indicador> criarListaDeIndicadores() {
		List<Indicador> indicadores = new ArrayList<>();

		int i = 0;
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
