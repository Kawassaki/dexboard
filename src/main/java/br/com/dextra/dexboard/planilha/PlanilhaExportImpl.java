package br.com.dextra.dexboard.planilha;

import br.com.dextra.dexboard.domain.Indicador;
import br.com.dextra.dexboard.domain.Projeto;
import br.com.dextra.dexboard.domain.RegistroAlteracao;
import br.com.dextra.dexboard.json.ProjetoJson;
import br.com.dextra.dexboard.service.PlanilhaService;
import com.github.feroult.gapi.spreadsheet.SpreadsheetBatch;
import com.googlecode.objectify.Key;
import java.util.*;
import java.util.stream.Collectors;

public class PlanilhaExportImpl extends PlanilhaExport {

    private PlanilhaService service;
    private List<Indicador> indicadores;
    private MatrixBatchBuilder matrixBuilder;

    public PlanilhaExportImpl(){
        super("Dexboard");
        this.service = new PlanilhaService();
        this.indicadores = this.service.obterListaDeIndicadores();
        this.matrixBuilder = new MatrixBatchBuilder();
    }

    private void criarColunasMatriz() {
        this.matrixBuilder.setValueZeroBased(0, 0, "Projetos");
        this.matrixBuilder.setValueZeroBased(0,  1, "CPI");
        this.matrixBuilder.setValueZeroBased(0,  2, "Atrasado");
        int coluna = 2;
        for (Indicador indicador: this.indicadores){
            this.matrixBuilder.setValueZeroBased(0, coluna + 1, String.format("(Status) %s", indicador.getNome()));
            this.matrixBuilder.setValueZeroBased(0, coluna + 2, String.format("(Comentários) %s", indicador.getNome()));
            coluna = coluna + 2;
        }
    }

    private void preencherMatriz() {
        int linha = 1;
        for (Projeto projeto : this.service.obterListaDeProjetos()) {
            ProjetoJson p = new ProjetoJson(projeto);
            this.matrixBuilder.setValueZeroBased(linha, 0, projeto.getNome());
            this.matrixBuilder.setValueZeroBased(linha, 1, "'" +projeto.getCpi().toString());
            this.matrixBuilder.setValueZeroBased(linha, 2, p.getAtrasado() ? "Sim" : "Não");
            for (Indicador indicador: this.indicadores) {
                RegistroAlteracao registro = this.service.obterUltimoRegistroDeAlteracaoDoIndicadorDoProjeto(
                        Key.create(Indicador.class, String.format("%s;%s",projeto.getIdPma(), indicador.getId())));
                if (registro == null){
                    continue;
                }
                this.matrixBuilder.setValueInColumn(String.format("(Status) %s", indicador.getNome()), linha, registro.getClassificacao().toString());
                this.matrixBuilder.setValueInColumn(String.format("(Comentários) %s", indicador.getNome()), linha, registro.getComentario());
            }
            linha++;
        }
    }

    public SpreadsheetBatch criarPlanilhaDeExportacao() {
        this.criarColunasMatriz();
        this.preencherMatriz();
        return this.matrixBuilder.build();
    }

    public void exportarPlanilha() {
        salvarBatch(this.criarPlanilhaDeExportacao());
    }

}
