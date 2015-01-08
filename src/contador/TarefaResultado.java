package contador;

public class TarefaResultado {
	private Boolean selecionada;
	private String codigo;
	private String nome;
	private String solicitante;
	private Character emAndamento;
	private Character finalizada;
	private String dataHoraFinalizacao;
	private String tempoDecorrido;

	public TarefaResultado() {
		this.selecionada = new Boolean(false);
		this.codigo = "";
		this.nome = "";
		this.solicitante = "";
		this.emAndamento = new Character( ' ' );
		this.finalizada = new Character( ' ' );
		this.dataHoraFinalizacao = "";
		this.tempoDecorrido = "";
	}

	public TarefaResultado( final String pNome ) {
		this.codigo = "";
		this.nome = pNome;
		this.solicitante = "";
		this.emAndamento = new Character( ' ' );
		this.finalizada = new Character( ' ' );
		this.dataHoraFinalizacao = "";
		this.tempoDecorrido = "";
	}

	public String getCodigo() {
		return( this.codigo );
	}

	public String getNome() {
		return( this.nome );
	}

	public String getSolicitante() {
		return( this.solicitante );
	}

	public Character emAndamento() {
		return( emAndamento );
	}

	public Character finalizada(){
		return( this.finalizada );
	}

	public Boolean selecionada(){
		return( this.selecionada );
	}

	public String getDataHoraFinalizacao() {
		return( this.dataHoraFinalizacao );
	}

	public String getTempoDecorrido() {
		return( this.tempoDecorrido );
	}

	public void setCodigo(String pCodigo) {
		this.codigo = pCodigo;
	}

	public void setNome(String pNome) {
		this.nome = pNome;
	}

	public void setSoliciante(String cliente) {
		this.solicitante = cliente;
	}

	public void setEmAndamento( Character sim ) {
		this.emAndamento = sim;
	}

	public void setFinalizada( Character pFin ){
		this.finalizada = pFin;
	}

	public void setSelecionada( Boolean pSel ){
		this.selecionada = pSel;
	}

	public void setDataHoraFinalizacao( String pDataHoraFinalizacao ) {
		this.dataHoraFinalizacao = pDataHoraFinalizacao;
	}

	public void setTempoDecorrido( String pTempoDecorrido ) {
		this.tempoDecorrido = pTempoDecorrido;
	}
}