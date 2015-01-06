package contador;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class TarefaDAO {
	private Connection connection;

	public TarefaDAO() throws SQLException, ClassNotFoundException {
		this.connection = ConnectionHolder.getConnection();
	}

	public List<Tarefa> selectTarefasEmAberto() {
		ArrayList<Tarefa> TarefaList = new ArrayList<>();

		try {
			Statement st = this.connection.createStatement();
			String sQuery =
				"SELECT" +
				" codigo," +
				" descricao," +
				" solicitante," +
				" obs," +
				" dataHoraInclusao," +
				" dataHoraInicio," +
				" dataHoraTermino," +
				" tempoDecorrido," +
				" emAndamento," +
				" finalizada," +
				" anotacoes " +
				"FROM" +
				" tarefa " +
				"WHERE" +
				" finalizada = 'N' " +
				"ORDER BY" +
				" codigo";

			System.out.println( "SQL: " + sQuery );

			ResultSet rs = st.executeQuery( sQuery );
			DateFormat df = new SimpleDateFormat( "dd/MM/yyyy HH:mm:ss" );

			if( rs.next() ) {
				do {
					if( rs.getString( 2 ) != null ) {
						Tarefa tarefa = new Tarefa();
						Date dt;

						tarefa.setCodigo( String.valueOf( rs.getInt( "codigo" ) ) );
						tarefa.setNome( rs.getString( "descricao" ) );
						tarefa.setSoliciante( rs.getString( "solicitante" ) );
						tarefa.setObs( rs.getString( "obs" ) );

						try {
							dt = df.parse( rs.getString( "dataHoraInclusao" ) );
							tarefa.setDataHoraInclusao( df.format(dt) );
						}
						catch( ParseException pe ){
							dt = new Date();
							tarefa.setDataHoraInclusao( "00:00:00" );
						}

						try {
							dt = df.parse( rs.getString( "dataHoraInicio" ) );
							tarefa.setHoraInicio( df.format(dt) );
						}
						catch( ParseException pe ){
							dt = new Date();
							tarefa.setHoraInicio( "00:00:00" );
						}

						try {
							tarefa.setDuracao( rs.getString( "tempoDecorrido" ) );
						}
						catch( Exception pe ){
							tarefa.setDuracao( "00:00:00" );
						}
						tarefa.setEmAndamento( rs.getString( "emAndamento" ).charAt(0) == 'S' );
						tarefa.setFinalizada( rs.getString( "finalizada" ).charAt(0) == 'S' );

						if( rs.getString( "anotacoes" ) != null ) {
							tarefa.setAnotacoes( rs.getString( "anotacoes" ) );
						}

						TarefaList.add( tarefa );
					}
				}
				while( rs.next() );

				System.out.println( "SQL: " + TarefaList.size() + " Row(s)" );
			}
			else {
				System.out.println( "SQL: 0 Rows" );
			}
			return( TarefaList );
		}
		catch( SQLException ex ){
			System.out.println( "SQLException: " + ex.getMessage() );
			return( TarefaList );
		}
	}
	public void inserir( Tarefa pTarefa ) throws SQLException {
		Statement st = this.connection.createStatement();

		this.connection.setAutoCommit( false );

		String sQuery =
			"INSERT INTO tarefa (" +
			"	codigo" +
			"	,descricao" +
			"	,solicitante" +
			"   ,obs" +
			"	,dataHoraInclusao" +
			"	,dataHoraInicio" +
			"	,dataHoraTermino" +
			"	,tempoDecorrido" +
			"	,emAndamento" +
			"	,finalizada" +
			"	,anotacoes" +
			")" +
			" VALUES" +
			" (" +
			"  " + pTarefa.getCodigo() +
			"  ,'" + pTarefa.getNome() + "' " +
			"  ,'" + pTarefa.getSolicitante() + "' " +
			"  ,'" + pTarefa.getObs() + "' " +
			"  ,'" + pTarefa.getDataHoraInclusao() + "' " +
			"  ,'" + pTarefa.getHoraInicio() + "' " +
			"  ,'" + pTarefa.getDataHoraTermino() + "' " +
			"  ,'" + pTarefa.getDuracao() + "' " +
			"  ,'" + (pTarefa.emAndamento()? "S" : "N") + "' " +
			"  ,'" + (pTarefa.finalizada()? "S" : "N") + "' ";

		if( !pTarefa.getAnotacoes().isEmpty() ) {
			sQuery += "  ,'" + pTarefa.getAnotacoes() + "'";
		}
		else {
			sQuery += "  ,null";
		}

		sQuery += " )";

		System.out.println( "SQL: " + sQuery );

		if( st.executeUpdate( sQuery ) == Statement.EXECUTE_FAILED ) {
			this.connection.rollback();
			System.out.println( "SQL: 0 Rows affecteds" );
		}
		else {
			this.connection.commit();
			System.out.println( "SQL: 1 Row affected" );
		}

		this.connection.setAutoCommit( true );
	}
	public void alterar( Tarefa pTarefa ) throws SQLException {
		Statement st = this.connection.createStatement();

		this.connection.setAutoCommit( false );

		String sQuery =
			"UPDATE tarefa SET " +
			"	descricao = '" + pTarefa.getNome() + "' " +
			"	,solicitante = '" + pTarefa.getSolicitante() + "' " +
			"	,obs = '" + pTarefa.getObs() + "' " +
			"	,dataHoraInclusao = '" + pTarefa.getDataHoraInclusao() + "' " +
			"	,dataHoraInicio = '" + pTarefa.getHoraInicio() + "' " +
			"	,dataHoraTermino = '" + pTarefa.getDataHoraTermino() + "' " +
			"	,tempoDecorrido = '" + pTarefa.getDuracao() + "' " +
			"	,emAndamento = '" + (pTarefa.emAndamento()? "S" : "N") + "' " +
			"	,finalizada = '" + (pTarefa.finalizada()? "S" : "N") + "' ";

		if( !pTarefa.getAnotacoes().isEmpty() ) {
			sQuery += "  ,anotacoes = '" + pTarefa.getAnotacoes() + "' ";
		}
		else {
			sQuery += "  ,anotacoes = null ";
		}

		sQuery +=
			"WHERE" +
			" codigo = " + pTarefa.getCodigo();

		System.out.println( "SQL: " + sQuery );

		if( st.executeUpdate( sQuery ) == Statement.EXECUTE_FAILED ) {
			this.connection.rollback();
			System.out.println( "SQL: 0 Rows affecteds" );
		}
		else {
			this.connection.commit();
			System.out.println( "SQL: 1 Row affected" );
		}

		this.connection.setAutoCommit( true );
	}
	public void excluir( Tarefa pTarefa ) throws SQLException {
		Statement st = this.connection.createStatement();

		this.connection.setAutoCommit( false );

		String sQuery =
			"DELETE FROM tarefa " +
			"WHERE codigo = " + pTarefa.getCodigo();

		System.out.println( "SQL: " + sQuery );

		if( st.executeUpdate( sQuery ) == Statement.EXECUTE_FAILED ) {
			this.connection.rollback();
			System.out.println( "SQL: 0 Rows affecteds" );
		}
		else {
			this.connection.commit();
			System.out.println( "SQL: 1 Row affected" );
		}

		this.connection.setAutoCommit( true );
	}
}