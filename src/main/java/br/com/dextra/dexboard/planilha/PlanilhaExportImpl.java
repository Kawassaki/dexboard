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
        int coluna = 0;
        this.matrixBuilder.setValueZeroBased(0, coluna++, "Projetos");
        this.matrixBuilder.setValueZeroBased(0, coluna++, "CPI");
        this.matrixBuilder.setValueZeroBased(0, coluna++, "Atrasado");
        for (Indicador indicador: this.indicadores){
            this.matrixBuilder.setValueZeroBased(0,  coluna++, String.format("(Status) %s", indicador.getNome()));
            this.matrixBuilder.setValueZeroBased(0,  coluna++, String.format("(Comentários) %s", indicador.getNome()));
        }
        this.matrixBuilder.setValueZeroBased(0, coluna++, "Id no PMA");
        this.matrixBuilder.setValueZeroBased(0, coluna++, "Equipe");
        this.matrixBuilder.setValueZeroBased(0, coluna++, "tribo");
        this.matrixBuilder.setValueZeroBased(0, coluna++, "email");
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
            this.matrixBuilder.setValueZeroBased(linha, 19, projeto.getIdPma().toString());
            this.matrixBuilder.setValueZeroBased(linha, 20, projeto.getEquipe());
            this.matrixBuilder.setValueZeroBased(linha, 21, projeto.getTribo());
            this.matrixBuilder.setValueZeroBased(linha, 22, projeto.getEmail());
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
