package database;

import java.sql.Connection;
import java.sql.SQLException;

public class ConnectionHolder {

	private static BD instance;

    protected ConnectionHolder() {
    }

	public static Connection getConnection() throws SQLException, ClassNotFoundException {
		if( instance == null ) {
			System.out.println( "LOG: Requesting first connection.." );
			instance = new BD();
			instance.setConnection( instance.getConnection() );
			criarTabelas();
		}
		return( instance.getConnection() );
	}

	public static void criarTabelas() throws SQLException, ClassNotFoundException {
		try {
			if( !instance.existsDataBase() ) {
				instance.criarBancoDeDados( "" );
			}
	} catch (ClassNotFoundException cl) {
			System.out.println( "ClassNotFoundException: " + cl.getMessage() );
		}
	}
}
