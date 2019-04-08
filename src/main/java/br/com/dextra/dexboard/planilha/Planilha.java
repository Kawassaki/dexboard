package br.com.dextra.dexboard.planilha;

import com.github.feroult.gapi.GoogleAPI;
import com.github.feroult.gapi.SpreadsheetAPI;
import com.github.feroult.gapi.spreadsheet.SpreadsheetBatch;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

abstract class Planilha {

	private final String sheetName;

	private SpreadsheetAPI spreadSheet;

	private List<Map<String, String>> spreadSheetMap;

	protected Planilha(String chave, String sheet) {
		this.sheetName = sheet;
		this.spreadSheet = new GoogleAPI().spreadsheet(chave);

		if (this.spreadSheet == null) {
			throw new RuntimeException("SpreadSheet " + chave + " does not exist or certificate lacks permission to view it.");
		}

		if (!spreadSheet.hasWorksheet(sheetName)) {
			throw new RuntimeException("worksheet " + sheetName + " does not exist.");
		}

		this.spreadSheetMap = spreadSheet.worksheet(sheetName).asMap();
	}

	protected void salvarConteudoCelular(int linha, int coluna, String value) {
		this.spreadSheet.worksheet(sheetName).setValue(linha, coluna, value);
	}

	protected void salvarBatch(SpreadsheetBatch batch){
		this.spreadSheet.worksheet(sheetName).batch(batch);
	}

	protected String recuperarConteudoCelula(int linha, int coluna) {
		if (!spreadSheet.hasWorksheet(sheetName)) {
			throw new RuntimeException("worksheet " + sheetName + " does not exist.");
		}
		return spreadSheet.worksheet(sheetName).getValue(linha, coluna);
	}

	protected String recuperarConteudoCelula(int linha, String nomeColuna) {
		return spreadSheetMap.get(linha).get(normalizarNomeColuna(nomeColuna));
	}

	private String normalizarNomeColuna(String nomeColuna) {
		return nomeColuna.replaceAll(" ", "").toLowerCase();
	}

	protected Integer recuperarConteudoCelulaInt(int linha, int coluna) {
		String conteudo = recuperarConteudoCelula(linha, coluna);
		return Integer.valueOf(conteudo);
	}

	protected Integer recuperarConteudoCelulaInt(int linha, String nomeColuna) {
		String conteudo = recuperarConteudoCelula(linha, nomeColuna);
		return Integer.valueOf(conteudo);
	}

	protected Double recuperarConteudoCelulaDouble(int linha, int coluna) {
		String value = recuperarConteudoCelula(linha, coluna);
		return parseDouble(value);
	}

	protected Double recuperarConteudoCelulaDouble(int linha, String nomeColuna) {
		String value = recuperarConteudoCelula(linha, nomeColuna);
		return parseDouble(value);
	}

	private Double parseDouble(String value) {
		DecimalFormat df = new DecimalFormat();
		DecimalFormatSymbols symbols = new DecimalFormatSymbols();
		symbols.setDecimalSeparator(',');
		symbols.setGroupingSeparator('.');
		df.setDecimalFormatSymbols(symbols);
		try {
			return df.parse(value).doubleValue();
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}

}