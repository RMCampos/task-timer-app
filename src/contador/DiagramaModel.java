package contador;

import java.util.ArrayList;
import javax.swing.table.DefaultTableModel;

public class DiagramaModel extends DefaultTableModel {
  private ArrayList<Diagrama> linhas;
  public static final int COL_COD = 0;
  public static final int COL_NOME = 1;
  public static final int COL_SOL = 2;
  public static final int COL_INICIO = 3;
  public static final int COL_TERMINO = 4;
  public static final int COL_DECORRIDO = 5;
  public static final int COL_ATIVO = 6;
  public static final int NUM_COLUNAS = 7;
  
  public DiagramaModel() {
    this.linhas = new ArrayList<>();
  }
  
  @Override
  public Class getColumnClass( int column ) {
    return( String.class );
  }
  
  @Override
  public String getColumnName( int column ) {
    switch( column ) {
      case COL_COD: return( "Código" );
      case COL_NOME: return( "Nome" );
      case COL_SOL: return( "Solicitante" );
      case COL_INICIO: return( "Início" );
      case COL_TERMINO: return( "Término" );
      case COL_DECORRIDO: return( "Decorrido" );
      case COL_ATIVO: return( "Ativo" );
      default: return( "" );
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
  
  public Diagrama getLinha( int linha ) {
    if( linha > this.linhas.size()-1 ) {
      return( null );
    }
    return( this.linhas.get( linha ) );
  }
  
  public ArrayList<Diagrama> getLinhas() {
    return( this.linhas );
  }
  
  @Override
  public Object getValueAt( int row, int column ) {
    Diagrama dia = getLinha( row );
    
    if( dia == null ) {
      return( null );
    }
    
    switch( column ) {
      case COL_COD: return( dia.getCodigo() );
      case COL_NOME: return( dia.getNome() );
      case COL_SOL: return( dia.getSolicitante() );
      case COL_INICIO: return( dia.getHoraInicio() );
      case COL_TERMINO: return( dia.getHoraTermino() );
      case COL_DECORRIDO: return( dia.getDuracao() );
      case COL_ATIVO: return( dia.getEmAndamento() );
      default: return( null );
    }
  }
  
  @Override
  public boolean isCellEditable( int row, int column ) {
    return( false );
  }
  
  public void limpar() {
    this.linhas.clear();
    fireTableDataChanged();
  }
  
  public void addLinha( Diagrama pDia ) {
    this.linhas.add( pDia );
    fireTableDataChanged();
  }
  
  public void removeLinha( Diagrama pDia ) {
    this.linhas.remove( pDia );
    fireTableDataChanged();
  }
}
