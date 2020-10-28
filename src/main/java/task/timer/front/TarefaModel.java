package task.timer.front;

import task.timer.back.Tarefa;

import java.util.ArrayList;
import javax.swing.table.DefaultTableModel;

public class TarefaModel extends DefaultTableModel {

    private final ArrayList<Tarefa> linhas;
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
            case COL_COD:
                return ("Name/code");
            case COL_NOME:
                return ("Description");
            case COL_SOL:
                return ("Client");
            case COL_INICIO:
                return ("Started at");
            case COL_TERMINO:
                return ("Finished at");
            case COL_DECORRIDO:
                return ("Timer");
            case COL_ATIVO:
                return ("Active");
            default:
                return ("");
        }
    }

    @Override
    public int getRowCount() {
        if (this.linhas != null) {
            return (this.linhas.size());
        }
        return (0);
    }

    @Override
    public int getColumnCount() {
        return (NUM_COLUNAS);
    }

    public Tarefa getLinha(int linha) {
        if (linha > this.linhas.size() - 1 || linha < 0) {
            return (null);
        }
        return (this.linhas.get(linha));
    }

    public ArrayList<Tarefa> getLinhas() {
        return (this.linhas);
    }

    @Override
    public Object getValueAt(int row, int column) {
        Tarefa dia = getLinha(row);

        if (dia == null) {
            return (null);
        }

        switch (column) {
            case COL_COD:
                return (dia.getDiagramaPrograma());
            case COL_NOME:
                return (dia.getDescricao());
            case COL_SOL:
                return (dia.getCliente());
            case COL_INICIO:
                return (dia.getHoraInicio());
            case COL_TERMINO:
                return (dia.getHoraTermino());
            case COL_DECORRIDO:
                return (dia.getDuracao());
            case COL_ATIVO:
                return (dia.isEmAndamento()? "S" : "N");
            default:
                return (null);
        }
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return (false);
    }

    public void addLinha(Tarefa pDia) {
        this.linhas.add(pDia);
        fireTableDataChanged();
    }

    public void removeLinha(Tarefa pDia) {
        this.linhas.remove(pDia);
        fireTableDataChanged();
    }
}
