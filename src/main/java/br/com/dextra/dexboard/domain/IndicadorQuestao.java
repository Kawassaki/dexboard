package br.com.dextra.dexboard.domain;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

@Entity
public class IndicadorQuestao {
    @Id
    private String composeId;
    private Long id;
    private String conteudo;
    private String categoria;
    @Index
    private boolean ativo;
    @Index
    private Key<Indicador> indicador;

    public IndicadorQuestao() {
        super();
    }

    public IndicadorQuestao(Long id, String conteudo, String categoria, boolean ativo) {
        this.id = id;
        this.conteudo = conteudo;
        this.categoria = categoria;
        this.ativo = ativo;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setConteudo(String conteudo) {
        this.conteudo = conteudo;
    }

    public String getConteudo() {
        return conteudo;
    }

    public void setId(Long id) { this.id = id; }

    public Long getId() { return this.id; }

    public void setIndicador(Key<Indicador> indicador) {
        this.indicador = indicador;
    }

    public Key<Indicador> getIndicador() {
        return indicador;
    }

    public void setComposeId(String composeId) {
        this.composeId = composeId;
    }

    public String getComposeId() {
        return composeId;
    }

    public void defineComposeId() {
        String value = String.format("%s;%s", this.getIndicador().getId(), this.getId().toString());
        this.composeId = value;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    public boolean isAtivo() {
        return ativo;
    }
}
