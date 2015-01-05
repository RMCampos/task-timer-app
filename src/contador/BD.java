package contador;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;

public class BD {
	private Connection connection;

	public BD() throws SQLException, ClassNotFoundException {
		conectar();
	}

	public String getDatabaseURL() {
		if( OS.isWindows() ) {
			String base = System.getenv( "APPDATA" );
			File f = new File( base );

			if( f.exists() && f.isDirectory() ) {
				return( "jdbc:sqlite:" + f.getAbsolutePath() + "\\Contador.db" );
			}
			else {
				return( "jdbc:sqlite:" + System.getProperty("user.home").substring( 0, System.getProperty("user.home").length()-1 ) + "\\Contador.db" );
			}
		}
		else {
			File f = new File( System.getProperty( "user.home" ) );
			return( "jdbc:sqlite:" + f.getAbsolutePath() + "/.Contador/Contador.db" );
		}
	}

	private void conectar() throws SQLException, ClassNotFoundException {
		Class.forName( "org.sqlite.JDBC" );
		System.out.println( "LOG: Driver: " + getDatabaseURL() );
		this.connection = DriverManager.getConnection( getDatabaseURL(), "", "" );
		System.out.println( "LOG: Connection Established!" );
	}

	public Connection getConnection(){
		return( this.connection );
	}

	public void fecharConexao() {
		try {
			this.connection.close();
		}
		catch( SQLException ex ) {
			System.out.println( "SQL Exception: " + ex.getLocalizedMessage() );
		}
	}

	public void criarBancoDeDados( String psCaminhoCompleto ) throws ClassNotFoundException {
		try {
			if( this.connection == null ) {
				conectar();
			}

			this.connection.setAutoCommit( false );

			String comandoSQL =
				"CREATE TABLE IF NOT EXISTS tarefa " +
				"(" +
				" codigo INTEGER NOT NULL," +
				" descricao VARCHAR(100) NOT NULL," +
				" solicitante VARCHAR(50) NOT NULL," +
				" obs VARCHAR(500) NOT NULL," +
				" dataHoraInclusao DATETIME NOT NULL," +
				" dataHoraInicio DATETIME NOT NULL," +
				" dataHoraTermino DATETIME NOT NULL," +
				" tempoDecorrido TIME NOT NULL," +
				" emAndamento CHAR NOT NULL," +
				" finalizada CHAR NOT NULL," +
				" anotacoes VARCHAR(5000) NULL," +
				" PRIMARY KEY(codigo) " +
				")";

			Statement st = this.connection.createStatement();
			System.out.println( "SQL: Creating tables.." );

			System.out.println( "SQL: " + comandoSQL );

			if( st.executeUpdate( comandoSQL ) == Statement.EXECUTE_FAILED ) {
				System.out.println( "SQL Error: " + comandoSQL );
				this.connection.rollback();
			}
			this.connection.commit();
		}
		catch( SQLException e ) {
			System.out.println( "SQLException: " + e.getLocalizedMessage() );
		}
	}
}