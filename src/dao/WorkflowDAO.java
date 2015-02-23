package dao;

import java.io.*;
import java.sql.*;
import com.microsoft.sqlserver.jdbc.*;
import utils.Mensagem;

public class WorkflowDAO {
	Connection conn;

	public WorkflowDAO() {
		try {
			conectarAoBanco();
		}
		catch( SQLException | ClassNotFoundException ex ) {
			System.out.println( "Erro ao conectar ao SQL Server!\n" + ex.getMessage() );
		}
	}

	public String getDescricaoTP( String pNumeroTP ) throws SQLException {
		if( this.conn == null ) {
			return( "" );
		}

		Statement st = this.conn.createStatement();

		ResultSet rs = st.executeQuery(
			"SELECT desc_objeto " +
			"FROM crm_objeto (NOLOCK) " +
			"WHERE objeto_id = " + pNumeroTP
		);

		if( rs.next() ) {
			do {
				if( rs.getString( 1 ) != null ) {
					return( rs.getString( "desc_objeto" ) );
				}
			}
			while( rs.next() );
		}
		return( "" );
	}

	private void conectarAoBanco() throws ClassNotFoundException, SQLException {
		String connectionURL = buscarDadosConexao();
		Class.forName( "com.microsoft.sqlserver.jdbc.SQLServerDriver" );
		this.conn = DriverManager.getConnection( connectionURL );
	}
	
	private String buscarDadosConexao() {
		try {
			FileReader f = new FileReader( System.getProperty( "user.home" ) + "\\WFAcess.txt" );
			BufferedReader br = new BufferedReader( f );
			String servidor = "",
				database = "",
				usuario = "",
				senha = "",
				tmp;

			while(( tmp = br.readLine()) != null) {
				if( tmp.startsWith( "SERVER" ) ) {
					servidor = tmp.split("=")[1].trim();
				}
				else if( tmp.startsWith( "DBNAME" ) ) {
					database = tmp.split("=")[1].trim();
				}
				else if( tmp.startsWith( "USER" ) ) {
					usuario = tmp.split("=")[1].trim();
				}
				else if( tmp.startsWith( "PASS" ) ) {
					senha = tmp.split("=")[1].trim();
				}
			}
			
			br.close();
			
			if( servidor.isEmpty() || database.isEmpty() || usuario.isEmpty() || senha.isEmpty() ) {
				return( "" );
			}
			
			return(
				"jdbc:sqlserver://" + servidor + ";" +
				"databaseName=" + database + ";" +
				"user=" + usuario + ";" +
				"password=" + senha
			);
		}
		catch( Exception e ) {
			return( "" );
		}
	}

	public void fecharConexao() throws SQLException {
		this.conn.close();
	}
}