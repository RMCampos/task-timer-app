package task.timer.front;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.DefaultTableModel;

import task.timer.back.Tarefa;

public class TarefaModel extends DefaultTableModel {

    private static final long serialVersionUID = 5776968986470155598L;
    private final List<Tarefa> linhas;
    public static final int COL_COD = 0;
    public static final int COL_NOME = 1;
    public static final int COL_SOL = 2;
    public static final int COL_INICIO = 3;
    public static final int COL_TERMINO = 4;
    public static final int COL_DECORRIDO = 5;
    public static final int COL_ATIVO = 6;
    public static final int NUM_COLUNAS = 7;

    public TarefaModel() {
        this.linhas = new ArrayList<>();
    }

    @Override
    public Class<?> getColumnClass(int column) {
        return String.class;
    }

    @Override
    public String getColumnName(int column) {
        switch (column) {
            case COL_COD: return "Programa";
            case COL_NOME: return "Nome";
            case COL_SOL: return "Cliente";
            case COL_INICIO: return "Início";
            case COL_TERMINO: return "Fim";
            case COL_DECORRIDO: return "Timer";
            case COL_ATIVO: return "Ativo";
            default: return "";
        }
    }

    @Override
    public int getRowCount() {
        if (linhas == null) {
            return 0;
        }

        return linhas.size();
    }

    @Override
    public int getColumnCount() {
        return NUM_COLUNAS;
    }

    public Tarefa getLinha(int linha) {
        try {
            return linhas.get(linha);
        } catch (IndexOutOfBoundsException ix) {
            ix.printStackTrace();
            return null;
        }
    }

    public List<Tarefa> getLinhas() {
        return linhas;
    }

    @Override
    public Object getValueAt(int row, int column) {
        Tarefa dia = getLinha(row);

        if (dia == null) {
            return (null);
        }

        switch (column) {
            case COL_COD: return dia.getDiagramaPrograma();
            case COL_NOME: return dia.getDescricao();
            case COL_SOL: return dia.getCliente();
            case COL_INICIO: return dia.getHoraInicio();
            case COL_TERMINO: return dia.getHoraTermino();
            case COL_DECORRIDO: return dia.getDuracao();
            case COL_ATIVO: return dia.isEmAndamento()? "S" : "N";
            default: return null;
        }
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }

    public void addLinha(Tarefa pDia) {
        linhas.add(pDia);
        fireTableDataChanged();
    }

    public void removeLinha(Tarefa pDia) {
        linhas.remove(pDia);
        fireTableDataChanged();
    }
}
