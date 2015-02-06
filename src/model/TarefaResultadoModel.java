package model;

import java.util.ArrayList;
import java.util.List;
import javax.swing.table.DefaultTableModel;
import data.TarefaResultado;

public class TarefaResultadoModel extends DefaultTableModel {

    private final ArrayList<TarefaResultado> linhas;
	public static final int COL_SELECIONADA = 0;
	public static final int COL_CODIGO = 1;
	public static final int COL_NOME = 2;
	public static final int COL_SOLICITANTE = 3;
	public static final int COL_ANDAMENTO = 4;
	public static final int COL_FINALIZADA = 5;
	public static final int NUM_COLUNAS = 6;

	public TarefaResultadoModel() {
		this.linhas = new ArrayList<>();
	}

	@Override
	public Class getColumnClass( int column ) {
		switch( column ) {
	    case COL_SELECIONADA:
		return (Boolean.class);
	    case COL_CODIGO:
		return (String.class);
	    case COL_NOME:
		return (String.class);
	    case COL_SOLICITANTE:
		return (String.class);
	    case COL_ANDAMENTO:
		return (Character.class);
	    case COL_FINALIZADA:
		return (Character.class);
	    default:
		return (String.class);
		}
	}

	@Override
	public String getColumnName( int column ) {
		switch( column ) {
	    case COL_SELECIONADA:
		return ("Sel.");
	    case COL_CODIGO:
		return ("Tarefa");
	    case COL_NOME:
		return ("Nome");
	    case COL_SOLICITANTE:
		return ("Solicitante");
	    case COL_ANDAMENTO:
		return ("Em Andamento");
	    case COL_FINALIZADA:
		return ("Finalizada");
	    default:
		return ("");
		}
	}

	@Override
	public int getRowCount() {
		if( this.linhas != null ) {
			return( this.linhas.size() );
		}
		return( 0 );
	}

	@Override
	public int getColumnCount() {
		return( NUM_COLUNAS );
	}

	public TarefaResultado getLinha( int linha ) {
		if( linha > this.linhas.size()-1 ) {
			return( null );
		}
		return( this.linhas.get( linha ) );
	}

	public ArrayList<TarefaResultado> getLinhas() {
		return( this.linhas );
	}

	@Override
	public Object getValueAt( int row, int column ) {
		TarefaResultado tarefa = getLinha( row );

		if( tarefa == null ) {
			return( null );
		}

		switch( column ) {
	    case COL_SELECIONADA:
		return (tarefa.selecionada());
	    case COL_CODIGO:
		return (tarefa.getCodigo());
	    case COL_NOME:
		return (tarefa.getNome());
	    case COL_SOLICITANTE:
		return (tarefa.getSolicitante());
	    case COL_ANDAMENTO:
		return (tarefa.emAndamento());
	    case COL_FINALIZADA:
		return (tarefa.finalizada());
	    default:
		return (null);
		}
	}

	public void setValueAt( Object pValue, int row, int column ) {
		if( column == 0 && pValue instanceof Boolean ) {
			TarefaResultado tarefa = getLinha( row );
			tarefa.setSelecionada( (Boolean)pValue );
			fireTableCellUpdated(row, column);
		}
	}

	@Override
	public boolean isCellEditable( int row, int column ) {
		return( column == 0 );
	}

	public void limpar() {
		this.linhas.clear();
		fireTableDataChanged();
	}

	public void addLinha( TarefaResultado pDia ) {
		this.linhas.add( pDia );
		fireTableDataChanged();
	}

	public void removeLinha( TarefaResultado pDia ) {
		this.linhas.remove( pDia );
		fireTableDataChanged();
	}

	public void setLinhas( List<TarefaResultado> list ){
		this.linhas.clear();
		this.linhas.addAll( list );
		fireTableDataChanged();
	}
}
