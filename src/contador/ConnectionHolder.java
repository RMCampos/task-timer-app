package contador;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;

public class ConnectionHolder {
	private static BD instance;

	protected ConnectionHolder() {}

	public static Connection getConnection() throws SQLException, ClassNotFoundException {
		if( instance == null ) {
			System.out.println( "LOG: Requesting first connection.." );
			instance = new BD();
		}
		return( instance.getConnection() );
	}

	public static void criarTabelas() throws SQLException, ClassNotFoundException {
		if( instance == null ) {
			System.out.println( "LOG: Requesting conenction.." );
			instance = new BD();
		}

		try {
			instance.criarBancoDeDados( "" );
		}
		catch( ClassNotFoundException cl ){}
	}
}