package contador;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class TarefaResultadoDAO {
	private Connection connection;

	public TarefaResultadoDAO() throws SQLException, ClassNotFoundException {
		this.connection = ConnectionHolder.getConnection();
		ConnectionHolder.criarTabelas();
	}

	public List<TarefaResultado> executarQuery( String pQuery ) {
		ArrayList<TarefaResultado> tarefaList = new ArrayList<>();

		try {
			Statement st = this.connection.createStatement();

			System.out.println( "SQL: " + pQuery );

			ResultSet rs = st.executeQuery( pQuery );

			if( rs.next() ) {
				do {
					if( rs.getString( 2 ) != null ) {
						TarefaResultado tarefa = new TarefaResultado();

						tarefa.setCodigo( String.valueOf( rs.getInt( "codigo" ) ) );
						tarefa.setNome( rs.getString( "descricao" ) );
						tarefa.setSoliciante( rs.getString( "solicitante" ) );
						tarefa.setEmAndamento( rs.getString( "emAndamento" ).charAt(0) == 'S' );
						tarefa.setFinalizada( rs.getString( "finalizada" ).charAt(0) == 'S' );

						tarefaList.add( tarefa );
					}
				}
				while( rs.next() );

				System.out.println( "SQL: " + tarefaList.size() + " Row(s)" );
			}
			else {
				System.out.println( "SQL: 0 Rows" );
			}
			return( tarefaList );
		}
		catch( SQLException ex ){
			System.out.println( "SQLException: " + ex.getMessage() );
			return( tarefaList );
		}
	}
	
	public void apagarLista( String pLista ) {
		try {
			Statement st = this.connection.createStatement();
			
			this.connection.setAutoCommit( false );
			
			String querySQL =
				"DELETE FROM tarefa " +
				"WHERE codigo IN (" + pLista + ")";

			System.out.println( "SQL: " + querySQL );
			
			int rowsAffecteds = st.executeUpdate( querySQL );
			
			if( rowsAffecteds == Statement.EXECUTE_FAILED ) {
				this.connection.rollback();
				System.out.println( "SQL: 0 Rows affecteds" );
			}
			else {
				this.connection.commit();
				System.out.println( "SQL: " + rowsAffecteds + " Row(s) affected(s)" );
			}

		this.connection.setAutoCommit( true );
		}
		catch( SQLException ex ){
			System.out.println( "SQLException: " + ex.getMessage() );
		}
	}
	
	public void reativarLista( String pLista ) {
		try {
			Statement st = this.connection.createStatement();
			
			this.connection.setAutoCommit( false );
			
			String querySQL =
				"UPDATE tarefa " +
				"SET finalizada = 'N' " +
				"WHERE codigo IN (" + pLista + ")";

			System.out.println( "SQL: " + querySQL );
			
			int rowsAffecteds = st.executeUpdate( querySQL );
			
			if( rowsAffecteds == Statement.EXECUTE_FAILED ) {
				this.connection.rollback();
				System.out.println( "SQL: 0 Rows affecteds" );
			}
			else {
				this.connection.commit();
				System.out.println( "SQL: " + rowsAffecteds + " Row(s) affected(s)" );
			}

		this.connection.setAutoCommit( true );
		}
		catch( SQLException ex ){
			System.out.println( "SQLException: " + ex.getMessage() );
		}
	}
}