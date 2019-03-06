package br.com.dextra.dexboard.planilha;

import com.github.feroult.gapi.spreadsheet.SpreadsheetBatch;

import java.util.ArrayList;
import java.util.List;

public class MatrixBatchBuilder {

    private List<List<String>> values;

    public MatrixBatchBuilder() {
        values = new ArrayList<>();
    }

    private List<String> getLine(int i) {
        while (i >= values.size()) {
            values.add(new ArrayList<>());
        }
        return values.get(i);
    }

    public void setValueOneBased(int row, int column, String value) {
        setValueZeroBased(row - 1, column - 1, value);
    }

    public void setValueZeroBased(int row, int column, String value) {
        List<String> line = getLine(row);
        if (column >= line.size()) {
            while (column > line.size()) {
                line.add("");
            }
            line.add(value);
        } else {
            line.set(column, value);
        }
    }

    public void setValueInColumn(String column, int row, String value) {
        int index = values.get(0).indexOf(column);
        if (index == -1) {
            return;
        }
        this.setValueZeroBased(row, index, value);
    }

    public String getValueOneBased(int row, int column) {
        return getValueZeroBased(row - 1, column - 1);
    }

    public String getValueZeroBased(int row, int column) {
        List<String> line = getLine(row);
        if (column < line.size()) {
            return line.get(column);
        }
        return "";
    }

    public SpreadsheetBatch build() {
        return new SpreadsheetBatch() {
            @Override
            public int rows() {
                return values.size();
            }

            @Override
            public int cols() {
                return values.stream().mapToInt(List::size).max().orElse(1);
            }

            @Override
            public String getValue(int row, int column) {
                return getValueOneBased(row, column);
            }
        };
    }
}