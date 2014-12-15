package contador;

public class TarefaResultado {
	private Boolean selecionada;
	private String codigo;
	private String nome;
	private String solicitante;
	private Boolean emAndamento;
	private Boolean finalizada;

	public TarefaResultado() {
		this.selecionada = new Boolean(false);
		this.codigo = "";
		this.nome = "";
		this.solicitante = "";
		this.emAndamento = new Boolean(false);
		this.finalizada = new Boolean(false);
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

	public Boolean emAndamento() {
		return( emAndamento );
	}

	public Boolean finalizada(){
		return( this.finalizada );
	}
	
	public Boolean selecionada(){
		return( this.selecionada );
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

	public void setEmAndamento( Boolean sim ) {
		this.emAndamento = sim;
	}

	public void setFinalizada( Boolean pFin ){
		this.finalizada = pFin;
	}

	public void setSelecionada( Boolean pSel ){
		this.selecionada = pSel;
	}
}