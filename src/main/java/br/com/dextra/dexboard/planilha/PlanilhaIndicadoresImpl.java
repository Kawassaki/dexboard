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

	@Override
	public List<Indicador> criarListaDeIndicadores() {
		List<Indicador> indicadores = new ArrayList<>();

		Long i = 1L;
		while (true) {
			String nomeIndicador = buscarNomeDoIndicador(i.intValue());
			if (!StringUtils.isNullOrEmpty(nomeIndicador)) {
				String descricao = buscarDescricaoDoIndicador(i.intValue());
				String situacao = buscarSituacaoDoIndicador(i.intValue());
				Boolean sitBool = (situacao.equalsIgnoreCase("Ativo")) ? true : false;

				indicadores.add(new Indicador(i, nomeIndicador, descricao, sitBool));
				i++;
			} else {
				return indicadores;
			}
		}

	}
}
