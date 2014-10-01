package contador;

public enum EnumCliente
{
  ARBEITEC    (1, "ARBEITEC"),
  CAPAL       (2, "CAPAL"),
  COOPAGRICOLA(3, "COOPAGRICOLA"),
  DAIRITSU    (4, "DAIRITSU"),
  DENK        (5, "DENK"),
  DIAMANTE    (6, "DIAMANTE"),
  DURMETAL    (7, "DURMETAL"),
  EMBALAMAD   (8, "EMBALAMAD"),
  FRAVI       (9, "FRAVI"),
  FUCK        (10, "FUCK"),
  FUCKCOMP    (11, "FUCKCOMP"),
  FUNDICAOSCH (12, "FUNDICAOSCH"),
  FUTURA      (13, "FUTURA"),
  GDM         (14, "GDM"),
  GEVAS       (15, "GEVAS"),
  GRUBER      (16, "GRUBER"),
  HACKER      (17, "HACKER"),
  HELSTEN     (18, "HELSTEN"),
  IGASA       (19, "IGASA"),
  JOINPAPER   (20, "JOINPAPER"),
  KUGEL       (21, "KUGEL"),
  LUFER       (22, "LUFER"),
  PARNAPLAST  (23, "PARNAPLAST"),
  PERINI      (24, "PERINI"),
  SCHNEIDER   (25, "SCHNEIDER"),
  TECNOPERFIL (26, "TECNOPERFIL"),
  TODOS       (27, "TODOS"),
  YACUY       (28, "YACUY" ),
  WAMA        (29, "WAMA"),
  WOSGRAU     (30, "WOSGRAU");
  
  private final Integer codigo;
  private final String nome;
  
  private EnumCliente( Integer codigoParam, String nomeParam )
  {
    this.nome = nomeParam;
    this.codigo = codigoParam;
  }
  
  public String getNome()
  {
    return( this.nome );
  }
  
  public Integer getCodigo()
  {
    return( this.codigo );
  }
  
  @Override
  public String toString()
  {
    return( this.nome );
  }
  
  public static EnumCliente getPorNome( String nomeParam )
  {
    for( EnumCliente cliente : EnumCliente.values() )
    {
      if( cliente.nome.equals( nomeParam ) )
      {
        return( cliente );
      }
    }
    return( null );
  }
}
