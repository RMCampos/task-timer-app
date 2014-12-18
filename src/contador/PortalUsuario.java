package contador;

public enum PortalUsuario {
	GERSON_MACEDO ( "gerson.macedo",  "correcaoM"   ),
	MICHELE_BLANCO( "michele.blanco", "correcaoERP" ),
	PAULO_PINHEIRO( "paulo.pinheiro", "correcaoFFC" ),
	RICARDO_CAMPOS( "ricardo.campos", "correcaoFFC" ),
	THIAGO_CORREA ( "thiago.correa",  "correcaoERP" ),
	WELINGTON_LOH ( "welington.loh",  "correcaoM"   );
	
	private String codigoUsuario;
	private String portalUsuario;

	private PortalUsuario( String pCodigoUsuario, String pPortalUsuario ) {
		this.codigoUsuario = pCodigoUsuario;
		this.portalUsuario = pPortalUsuario;
	}

	public String getCodigoUsuario() {
		return( this.codigoUsuario );
	}

	public String getPortalUsuario() {
		return( this.portalUsuario );
	}

	public static PortalUsuario getPortalPorCodigo( String pCodigoUsuario ) {
		for( PortalUsuario portal : PortalUsuario.values() ) {
			if( portal.codigoUsuario.equals( pCodigoUsuario ) ) {
				return( portal );
			}
		}
		return( null );
	}
}