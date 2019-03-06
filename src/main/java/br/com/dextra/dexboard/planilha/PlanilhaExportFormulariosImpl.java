package br.com.dextra.dexboard.planilha;

import br.com.dextra.dexboard.domain.Indicador;
import br.com.dextra.dexboard.domain.IndicadorQuestao;
import br.com.dextra.dexboard.domain.Projeto;
import br.com.dextra.dexboard.domain.RegistroAlteracao;
import br.com.dextra.dexboard.json.IndicadorRespostaJson;
import br.com.dextra.dexboard.service.IndicadorService;
import br.com.dextra.dexboard.service.PlanilhaService;
import com.github.feroult.gapi.spreadsheet.SpreadsheetBatch;
import com.googlecode.objectify.Key;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class PlanilhaExportFormulariosImpl extends PlanilhaDexboard {

    private PlanilhaService service;
    private IndicadorService indicadorService;
    private List<Indicador> indicadores;
    private MatrixBatchBuilder matrixBuilder;

    public PlanilhaExportFormulariosImpl(){
        super("Dexboard Formulários");
        this.service = new PlanilhaService();
        this.indicadorService = new IndicadorService();
        this.indicadores = this.service.obterListaDeIndicadores();
        this.matrixBuilder = new MatrixBatchBuilder();
    }


    private void criarColunasMatriz() {
        int coluna = 0;
        this.matrixBuilder.setValueZeroBased(0, coluna, "Projetos");
        coluna++;
        for (Indicador indicador: this.indicadores) {
            List<IndicadorQuestao> questoes = this.indicadorService.buscarQuestoesPelaKeyDoIndicador(Key.create(Indicador.class, indicador.getId()))
                    .stream().sorted(Comparator.comparing(n -> n.getCategoria())).collect(Collectors.toList());
            for (IndicadorQuestao questao: questoes) {
                this.matrixBuilder.setValueZeroBased(0, coluna, String.format("(%s) %s", indicador.getNome(), questao.getConteudo()));
                coluna++;
            }
        }
    }

    private void preencherMatriz() {
        int linha = 1;
        for (Projeto projeto : this.service.obterListaDeProjetos()) {
            this.matrixBuilder.setValueInColumn("Projetos", linha, projeto.getNome());
            for (Indicador indicador: this.indicadores) {
                List<IndicadorRespostaJson> respostas = this.indicadorService.buscarRespostasDasQuestoesDoIndicador(projeto.getIdPma(), indicador.getId());
                for (IndicadorRespostaJson resposta: respostas) {
                    this.matrixBuilder.setValueInColumn(String.format("(%s) %s", indicador.getNome(), resposta.getQuestao()), linha, resposta.getResposta());
                }
            }
            linha++;
        }
    }

    public SpreadsheetBatch criarPlanilhaDeExportacaoDosFormularios() {
        this.criarColunasMatriz();
        this.preencherMatriz();
        return this.matrixBuilder.build();
    }

    public void exportarPlanilha() {
        salvarBatch(this.criarPlanilhaDeExportacaoDosFormularios());
    }

}
