package contador;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFileChooser;

public class Programa {
	private final Tela frame;
	private String tempoTotal;
	private char transacao;
	private String nomeCidade;
	private TarefaDAO tarefaDAO;
	private TarefaResultadoDAO tarefaResultadaoDAO;

	public Programa( String[] s ) {
		// 1.0.0_rc 31/01/2014 - primeiro release
		// 1.0.1_rc 03/02/2014 - modificado layout e adicionado botao excluir
		// 1.0.2_rc 03/02/2014 - incluido funcao exportar
		// 1.0.3_rc 04/02/2014 - corrigido bug do numberFormatException
		// 1.0.4_rc 05/02/2014 - corrigido bug de minutos negativos
		// 1.0.5_rc 12/02/2014 - incluido campo para escolher pasta para exportar
		// 1.0.6_rc 20/02/2014 - incluido contador de tempo total desde que iniciou e pra cada PR
		// 1.0.7    24/06/2014 - incluido cliente Yacuy
		// 1.1.0    30/06/2014 - incluido opção de editar a observação depois de inserir o PR
		// 002      22/08/2014 - alterado para funcionar em linux e windows
		// 003      13/10/2014 - incluido opcao para minizar ao tray
		// 004      04/11/2014 - Removido opcao de parar todas as tarefas ao inserir nova
		//                     - ao incluir uma tarefa, inicia parado
		//                     - alterado o label nome para descrição
		//                     - coluna tarefa agora é um link para o workflow
		// 005      17/11/2014 - adicionado aba lateral para consultar movimentacao
		//          18/11/2014 - parse melhorado
		// 006      20/11/2014 - incluido verificacao do ususario logado no windows
		//                     - colocado o botao de exportar la em baixo
		//                     - incluido botao de alterar no lugar do exportar
		//                     - bug corrigido: ao alterar, dar enter nao cria um novo
		//          03/12/2014 - melhoria no parse do html
		// 007      04/12/2014 - paineis flutuantes possibilitando mudar o tamanho da janela
		// 008      08/12/2014 - melhoria no parse do html
		// 009      10/12/2014 - uso de bd para armazenar dados
		//                     - possibilidade de consultar tp
		// 010      12/12/2014 - opcao de excluir lotes, reativar lotes, buscar tarefa
		//                     - busca o numero do BD do portal
		// 011      16/12/2014 - incluido botao de radio para filtrar nao finalizadas
		obterNomeCidade( s );
		this.frame = new Tela( this.nomeCidade );

		try {
			this.tarefaDAO = new TarefaDAO();
			this.tarefaResultadaoDAO = new TarefaResultadoDAO();
		}
		catch( SQLException ex ){
			Mensagem.informacao( "Erro ao conectar ao banco de dados!\nSaindo...", this.frame );
			System.exit( 1 );
		}
		catch( ClassNotFoundException ex ){
			Mensagem.informacao( "Erro ao conectar ao banco de dados!\nSaindo...", this.frame );
			System.exit( 1 );
		}

		this.frame.setTitle( "Contador de Tarefas - v12.8 (06/01/2015)" );
		this.frame.setVisible( true );
		this.tempoTotal = "00:00:00";
		this.transacao = 'I';
		carregarTarefas();
	}

	private void carregarTarefas() {
		this.frame.limparTarefas();
		List<Tarefa> tarefasList = this.tarefaDAO.selectTarefasEmAberto();

		for( Tarefa tp : tarefasList ){
			this.frame.addTarefa( tp );
		}
	}

	private void obterNomeCidade( String[] s ) {
		this.nomeCidade = "";

		if( s.length >= 1 ) {
			this.nomeCidade = s[0];
		}
	}

	private String somarTempoTotal( String tempo1, String tempo2 ) {
		try {
			int hora1 = Integer.parseInt( tempo1.substring( 0, 2 ) );
			int min1  = Integer.parseInt( tempo1.substring( 3, 5 ) );
			int seg1  = Integer.parseInt( tempo1.substring( 6, 8 ) );

			int hora2 = Integer.parseInt( tempo2.substring( 0, 2 ) );
			int min2  = Integer.parseInt( tempo2.substring( 3, 5 ) );
			int seg2  = Integer.parseInt( tempo2.substring( 6, 8 ) );

			int hora3 = hora1+hora2;
			int min3  = min1+min2;
			int seg3  = seg1+seg2;

			if( seg3 >= 60 ) {
				min3++;
				seg3 -= 60;
			}

			if( min3 >= 60 ) {
				hora3++;
				min3 -= 60;
			}

			String horaS = (hora3 < 10)? "0"+hora3+":" : hora3+":";
			String minS = (min3 < 10)? "0"+min3+":" : min3+":";
			String segS = (seg3 < 10)? "0"+seg3 : seg3+"";

			return( horaS+minS+segS );
		}
		catch( NumberFormatException e ) {
			System.out.println( "Erro no formato do numero." );
			return( "00:00:00" );
		}
	}

	public void exec() {
		processar();
	}

	private void processar() {
		try {
			do {
				this.frame.acessar();

				if( this.frame.getComandoTela().equals( "ADICIONAR_TAREFA" ) ) {
					adicionarTarefa();
				}
				if( this.frame.getComandoTela().equals( "ALTERAR" ) ) {
					alterar();
				}
				if( this.frame.getComandoTela().equals( "BUSCAR" ) ) {
					buscarTarefas();
				}
				if( this.frame.getComandoTela().equals( "CANCELAR" ) ) {
					cancelar();
				}
				if( this.frame.getComandoTela().equals( "CARREGAR_TAREFA" ) ) {
					carregarTarefaSelecionado();
				}
				if( this.frame.getComandoTela().equals( "CARREGAR_PROXIMA_LINHA" ) ) {
					carregarLinha( "PROXIMA" );
				}
				if( this.frame.getComandoTela().equals( "CARREGAR_LINHA_ANTERIOR" ) ) {
					carregarLinha( "ANTERIOR" );
				}
				if( this.frame.getComandoTela().equals( "CONTINUAR" ) ) {
					continuar();
				}
				if( this.frame.getComandoTela().equals( "EXPORTAR" ) ) {
					exportar();
				}
				if( this.frame.getComandoTela().equals( "EXCLUIR_SELECAO" ) ) {
					excluirSelecao();
				}
				if( this.frame.getComandoTela().equals( "EXCLUIR" ) ) {
					excluir();
				}
				if( this.frame.getComandoTela().equals( "FINALIZAR" ) ) {
					finalizar();
				}
				if( this.frame.getComandoTela().equals( "PARAR" ) ) {
					parar( null );
				}
				if( this.frame.getComandoTela().equals( "PROCURAR_DIRETORIO" ) ) {
					procurarDiretorio();
				}
				if( this.frame.getComandoTela().equals( "REATIVAR" ) ) {
					reativarSelecao();
				}
			}
			while( true );
		}
		catch( Exception ex ) {
			ex.printStackTrace();
		}
	}

	private void buscarTarefas() {
		this.frame.limparResultadoBusca();

		String querySQL =
			"SELECT * " +
			"FROM tarefa " +
			"WHERE 1 = 1";

		if( !this.frame.getTxfTarefaBuscar().isEmpty() ) {
			querySQL += " AND codigo = " + this.frame.getTxfTarefaBuscar();
		}

		if( this.frame.getStatusTarefaBuscar() == StatusTarefa.FINALIZADAS ) {
			querySQL += " AND finalizada = 'S'";

			if( this.frame.getDtcPeriodoInicial() != null ) {
				DateFormat df = new SimpleDateFormat( "dd/MM/yyyy" );
				String dataInicio = df.format( this.frame.getDtcPeriodoInicial() ) + " 00:00:00";
				querySQL += " AND dataHoraTermino >= '" + dataInicio + "'";
			}

			if( this.frame.getDtcPeriodoFinal() != null ) {
				DateFormat df = new SimpleDateFormat( "dd/MM/yyyy" );
				String dataFinal = df.format( this.frame.getDtcPeriodoFinal() ) + " 23:59:59";
				querySQL += " AND dataHoraTermino <= '" + dataFinal + "'";
			}
		}
		else if( this.frame.getStatusTarefaBuscar() == StatusTarefa.NAO_FINALIZADAS ) {
			querySQL += " AND finalizada = 'N'";
		}
		else if( this.frame.getStatusTarefaBuscar() == StatusTarefa.EM_ANDAMENTO ) {
			querySQL += " AND emAndamento = 'S'";
		}
		else {
				if( this.frame.getDtcPeriodoInicial() != null ) {
				DateFormat df = new SimpleDateFormat( "dd/MM/yyyy" );
				String dataInicio = df.format( this.frame.getDtcPeriodoInicial() ) + " 00:00:00";
				querySQL += " AND dataHoraInclusao >= '" + dataInicio + "'";
			}

			if( this.frame.getDtcPeriodoFinal() != null ) {
				DateFormat df = new SimpleDateFormat( "dd/MM/yyyy" );
				String dataFinal = df.format( this.frame.getDtcPeriodoFinal() ) + " 23:59:59";
				querySQL += " AND dataHoraInclusao <= '" + dataFinal + "'";
			}
		}

		if( !this.frame.getTxfTextoLivre().isEmpty() ) {
			String anotacoes = "anotacoes LIKE ";
			String textoLivre = this.frame.getTxfTextoLivre();
			String termos[] = textoLivre.split( "," );

			for( int index=0; index<termos.length; index++) {
				String textoBusca = termos[index].trim();
				anotacoes += "'%" + textoBusca + ((index == termos.length-1)? "%')" : "%' OR anotacoes LIKE ");
			}

			querySQL += " AND (" + anotacoes ;
		}

		this.frame.setTarefasResultado( this.tarefaResultadaoDAO.executarQuery( querySQL ) );
	}

	private void finalizar(){
		Tarefa tarefa = this.frame.getTarefa();

		if( tarefa == null ){
			return;
		}

		tarefa.setFinalizada( true );
		DateFormat df = new SimpleDateFormat( "dd/MM/yyyy HH:mm:ss" );
		tarefa.setDataHoraTermino( df.format( new java.util.Date() ) );

		try {
			this.tarefaDAO.alterar( tarefa );
			carregarTarefas();
			Mensagem.informacao( "Tarefa finalizada!", this.frame );
		}
		catch( SQLException e ){
			System.out.println( "SQLException: " + e.getMessage() );
		}
	}

	private void procurarDiretorio() {
		JFileChooser folder = new JFileChooser();
		folder.setCurrentDirectory( new File( "." ) );
		folder.setDialogTitle( "Selecione uma pasta" );
		folder.setFileSelectionMode( JFileChooser.DIRECTORIES_ONLY );
		folder.setAcceptAllFileFilterUsed( false );

		if( folder.showOpenDialog( this.frame ) == JFileChooser.APPROVE_OPTION ){
			String caminho = folder.getSelectedFile().toString();
			if( OS.isWindows() ) {
				caminho += "\\";
			}
			System.out.println( "Selecionado: " + caminho );
			this.frame.setTxfDiretorio( folder.getSelectedFile().toString() + "\\" );
		}
	}

	private void carregarLinha( String pProximoAnterior ) {
		Tarefa tarefa = this.frame.getTarefa();

		if( pProximoAnterior.equals( "ANTERIOR" ) ) {
			this.frame.carregarTarefaPosicao( tarefa, '-' );
		}
		else if( pProximoAnterior.equals( "PROXIMA" ) ) {
			this.frame.carregarTarefaPosicao( tarefa, '+' );
		}

		this.frame.habilitarContinuar( tarefa.emAndamento() );
		this.frame.habilitarBotaoInserir( false );
		this.frame.habilitarExcluir( true );
		this.frame.setLblTotalTarefa( tarefa.getCronometro() );
		this.frame.setLblTotalTempo( obterTempoTotalDecorrido() );
		this.frame.habilitarBtnAlterar();
	}

	private void alterar() {
		if( this.transacao == 'A' ) {
			Tarefa tarefa = this.frame.getTarefa();

			tarefa.setNome( this.frame.getTxfNome() );
			tarefa.setSoliciante( this.frame.getTxfSolicitante() );
			tarefa.setObs( this.frame.getTxfObs() );
			tarefa.setAnotacoes( this.frame.getTxaAnotacoes() );


			try {
				this.tarefaDAO.alterar( tarefa );
				cancelar();
			}
			catch( SQLException e ){
				System.out.println( "SQLException: " + e.getMessage() );
			}
		}
	}

	private boolean validar() {
		boolean flValidar = true;
		String strMsg = "";

		if( this.frame.getTxfCodigo().isEmpty() ) {
			flValidar = false;
			strMsg += "->Código da tarefa não informado.\n";
		}

		if( this.frame.getTxfNome().isEmpty() ) {
			flValidar = false;
			strMsg += "->Nome da tarefa não informado.\n";
		}

		if( !flValidar ) {
			Mensagem.informacao( "Erro ao criar tarefa:\n" + strMsg, this.frame );
		}

		return( flValidar );
	}

	private Tarefa criarTarefa() {
		Tarefa tarefa = new Tarefa();

		tarefa.setCodigo( this.frame.getTxfCodigo() );
		tarefa.setNome( this.frame.getTxfNome() );
		tarefa.setSoliciante( this.frame.getTxfSolicitante() );
		tarefa.setObs( this.frame.getTxfObs() );

		return( tarefa );
	}

	private void adicionarTarefa() {
		if( validar() && this.transacao == 'I' ) {
				Tarefa tarefa = criarTarefa();

				try {
					DateFormat df = new SimpleDateFormat( "dd/MM/yyyy HH:mm:ss" );

					tarefa.setDataHoraInclusao( df.format( new java.util.Date() ) );
					this.tarefaDAO.inserir( tarefa );
					this.frame.addTarefa( tarefa );
					this.frame.limpar();
				}
				catch( SQLException ex ){
					System.out.println( "SQLException: " + ex.getMessage() );
				}
		}
		else {
			cancelar();
		}
	}

	private void carregarTarefaSelecionado() {
		Tarefa tarefa = this.frame.getLinhaSelecionada();

		this.frame.setTarefa( tarefa );
		this.transacao = 'A';
		this.frame.mudarEstado( "EDICAO" );
		this.frame.setTxfCodigo( tarefa.getCodigo() );
		this.frame.setTxfNome( tarefa.getNome() );
		this.frame.setTxfSolicitante( tarefa.getSolicitante() );
		this.frame.setTxfObs( tarefa.getObs() );
		this.frame.setTxaAnotacoes( tarefa.getAnotacoes() );
		this.frame.habilitarContinuar( tarefa.emAndamento() );
		this.frame.habilitarBotaoInserir( false );
		this.frame.habilitarExcluir( true );
		this.frame.setLblTotalTarefa( tarefa.getDuracao() );
		this.frame.setLblTotalTempo( obterTempoTotalDecorrido() );
		this.frame.habilitarBtnAlterar();
		this.frame.carregarPaginaEvolucao();
	}

	private String obterTempoTotalDecorrido() {
		Collection<Tarefa> TarefaList = this.frame.contadorModel.getLinhas();

		this.tempoTotal = "00:00:00";
		for( Tarefa tarefa : TarefaList ) {
			this.tempoTotal = somarTempoTotal( this.tempoTotal, tarefa.getCronometro() );
		}
		return( tempoTotal );
	}

	private void continuar() {
		Tarefa tarefa = this.frame.getLinhaSelecionada();

		if( tarefa == null ) {
			Mensagem.informacao( "Nenhuma linha selecionada.", frame);
			return;
		}

		DateFormat df = new SimpleDateFormat( "HH:mm:ss" );
		tarefa.setHoraInicio( df.format( new java.util.Date() ) );

		tarefa.setHoraIntervalo( new Date() );
		tarefa.setEmAndamento( true );
		tarefa.iniciarTempo();

		this.frame.limpar();
		this.frame.contadorTable.repaint();
		this.frame.contadorTable.getSelectionModel().clearSelection();
		this.frame.habilitarBotaoInserir( true );
		this.frame.habilitarBotoes( false );
		this.frame.setTarefa( null );
		this.frame.limparTempoDecorrido();
		this.frame.mudarEstado( "" );
		this.transacao = 'I';

		try {
			this.tarefaDAO.alterar( tarefa );
		}
		catch( SQLException e ){
			System.out.println( "SQLException: " + e.getMessage() );
		}
	}

	private void parar( Tarefa pTarefa ) {
		this.frame.setTarefa( null );

		Tarefa tarefa = pTarefa;

		if( tarefa == null ) {
			tarefa = this.frame.getLinhaSelecionada();
		}

		DateFormat data = new SimpleDateFormat( "HH:mm:ss" );
		tarefa.setHoraTermino( data.format( new Date() ) );

		DateFormat df = new SimpleDateFormat( "dd/MM/yyyy HH:mm:ss" );
		tarefa.setDataHoraTermino( df.format( new java.util.Date() ) );

		if( tarefa.getDuracao().isEmpty() ) {
			String duracao = obterDuracao( tarefa.getHoraTermino(), data.format( tarefa.getHoraIntervalo() ) );
			tarefa.setDuracao( duracao );
		}
		else {
			String duracaoAtual = tarefa.getDuracao();
			String novaDuracao = obterDuracao( tarefa.getHoraTermino(), data.format( tarefa.getHoraIntervalo() ) );
			tarefa.setDuracao( somarDuracao( duracaoAtual, novaDuracao ) );
		}

		tarefa.setEmAndamento( false );
		tarefa.setHoraIntervalo( new Date() );
		tarefa.pararTempo();

		if( pTarefa == null ) {
			this.frame.limpar();
		}

		this.frame.contadorTable.repaint();
		this.frame.contadorTable.getSelectionModel().clearSelection();
		this.frame.habilitarBotaoInserir( true );
		this.frame.habilitarBotoes( false );
		this.frame.limparTempoDecorrido();
		this.frame.mudarEstado( "" );
		this.transacao = 'I';

		try {
			this.tarefaDAO.alterar( tarefa );
		}
		catch( SQLException e ){
			System.out.println( "SQLException: " + e.getMessage() );
		}
	}

	private void cancelar() {
		this.frame.limpar();
		this.frame.setTarefa( null );
		this.frame.limparTempoDecorrido();
		this.frame.habilitarBotoes( false );
		this.frame.habilitarBotaoInserir( true );
		this.frame.mudarEstado( "" );
		this.transacao = 'I';
		this.frame.contadorTable.getSelectionModel().clearSelection();
	}

	private void exportar() {
		Collection<Tarefa> tarefaList = this.frame.contadorModel.getLinhas();
		String tempoTotalTarefas = obterTempoTotalDecorrido();

		if( tarefaList == null || tarefaList.isEmpty() ) {
			Mensagem.informacao( "Não existe nada para ser exportado.", this.frame );
			return;
		}

		final String cabecalho = "CODIGO;NOME;SOLICITANTE;TEMPO TOTAL;DATA;HORA INICIO;HORA FIM;OBS";
		DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		String dataAtual = formatter.format( new Date() );
		String camArquivo = this.frame.getTxfDiretorio();

		if( camArquivo.isEmpty() ) {
			if( OS.isWindows() ) {
				camArquivo = System.getProperty( "user.home" ) + "\\Desktop\\";
			}
			else {
				camArquivo = System.getProperty( "user.home" ) + "/";
			}
		}
		String nomeArquivo = (this.frame.getTxfNomeArquivo().isEmpty())? "Contador.csv" : this.frame.getTxfNomeArquivo();

		if( !nomeArquivo.endsWith( ".csv" ) ) {
			nomeArquivo += ".csv";
		}

		File desktop = new File( camArquivo );

		if( !desktop.exists() ) {
			desktop.mkdir();
		}

		File arquivoExportado = new File( camArquivo+nomeArquivo );
		FileWriter arquivo;

		try {
			arquivo = new FileWriter( arquivoExportado );
			System.out.println( "Arquivo criado em: " + camArquivo+nomeArquivo );
		}
		catch( IOException e ) {
			System.out.println( "IOException: " + e.getMessage() );
			arquivo = null;
		}

		PrintWriter gravarArq = new PrintWriter(arquivo);

		gravarArq.printf( cabecalho + "\r\n" );

		for( Tarefa tarefa : tarefaList ) {
			String linha =
				((tarefa.getCodigo().isEmpty())? " N " : tarefa.getCodigo()) + ";" +
				tarefa.getNome() + ";" +
				((tarefa.getSolicitante().isEmpty())? " N " : tarefa.getSolicitante()) + ";" +
				tarefa.getDuracao() + ";" +
				dataAtual + ";" +
				tarefa.getHoraInicio() + ";" +
				tarefa.getHoraTermino() + ";" +
				((tarefa.getObs().isEmpty())? "Nenhuma." : tarefa.getObs()) + "\r\n";

			System.out.println( "gravando a linha: " + linha );

			gravarArq.printf( linha );
		}

		// Grava o tempo Total
		gravarArq.printf( "\r\n \r\nTempo total das tarefas: " + tempoTotalTarefas + "\r\n" );

		try {
			arquivo.close();
		}
		catch( IOException e ){
			System.out.println( "IOException: " + e.getMessage() );
		}

		Mensagem.informacao( "Contador exportado com sucesso!\nLocal: " + camArquivo + "\nNome: " + nomeArquivo, this.frame );
	}

	private void excluir() {
		Tarefa tarefa = this.frame.getLinhaSelecionada();

		if( tarefa == null ) {
			Mensagem.informacao( "Nenhuma tarefa selecionada.", this.frame );
		}

		if( Mensagem.confirmar( "Confirma a exclusão da tarefa?", this.frame ) ) {
			try {
				parar( tarefa );
				this.tarefaDAO.excluir( tarefa );
				this.frame.contadorModel.removeLinha( tarefa );
				this.frame.contadorTable.getSelectionModel().clearSelection();
				this.frame.habilitarBotaoInserir( true );
				this.frame.habilitarBotoes( false );
				this.frame.limpar();
				this.frame.limparTempoDecorrido();
			}
			catch( SQLException e ){
				System.out.println( "SQLException: " + e.getMessage() );
			}
		}
	}

	private void excluirSelecao() {
		List<TarefaResultado> list = this.frame.getTarefasBusca();

		if( list.isEmpty() ){
			return;
		}

		String lista = "";
		for( int i=0; i<list.size(); i++ ) {
			lista += list.get(i).getCodigo();

			if( i < list.size()-1 ) {
				lista += ",";
			}
		}
		this.tarefaResultadaoDAO.apagarLista( lista );
		buscarTarefas();
		carregarTarefas();
	}

	private void reativarSelecao() {
		List<TarefaResultado> list = this.frame.getTarefasBusca();

		if( list.isEmpty() ){
			return;
		}

		String lista = "";
		for( int i=0; i<list.size(); i++ ) {
			lista += list.get(i).getCodigo();

			if( i < list.size()-1 ) {
				lista += ",";
			}
		}
		this.tarefaResultadaoDAO.reativarLista( lista );
		buscarTarefas();
		carregarTarefas();
	}

	private String somarDuracao( String duracao1Param, String duracao2Param ) {
		try {
			Integer hor1 = Integer.parseInt( String.valueOf( duracao1Param.substring( 0, 2 ).replaceAll(":", "") ) );
			Integer min1 = Integer.parseInt( String.valueOf( duracao1Param.substring( 3, 5 ).replaceAll(":", "") ) );
			Integer seg1 = Integer.parseInt( String.valueOf( duracao1Param.substring( 6, 8 ).replaceAll(":", "") ) );

			Integer hor2 = Integer.parseInt( String.valueOf( duracao2Param.substring( 0, 2 ).replaceAll(":", "") ) );
			Integer min2 = Integer.parseInt( String.valueOf( duracao2Param.substring( 3, 5 ).replaceAll(":", "") ) );
			Integer seg2 = Integer.parseInt( String.valueOf( duracao2Param.substring( 6, 8 ).replaceAll(":", "") ) );

			String duracaoTotal;
			Integer horTotal = hor1+hor2;
			Integer minTotal = min1+min2;
			Integer segTotal = seg1+seg2;

			if( segTotal > 60 ) {
				segTotal -= 60;
				minTotal++;
			}

			if( minTotal > 60 ) {
				minTotal -= 60;
				horTotal++;
			}

			duracaoTotal = (horTotal.toString().length() == 1)? "0" + horTotal.toString() + ":" : horTotal.toString() + ":";
			duracaoTotal += (minTotal.toString().length() == 1)? "0" + minTotal.toString() + ":" : minTotal.toString() + ":";
			duracaoTotal += (segTotal.toString().length() == 1)? "0" + segTotal.toString() : segTotal.toString();

			return( duracaoTotal );
		}
		catch( NumberFormatException e ) {
			e.printStackTrace();
			return( null );
		}
	}

	private String obterDuracao( String fimParam, String inicParam ) {
		try {
			Integer horFim = Integer.parseInt( String.valueOf( fimParam.substring( 0, 2 ) ) );
			Integer minFim = Integer.parseInt( String.valueOf( fimParam.substring( 3, 5 ) ) );
			Integer segFim = Integer.parseInt( String.valueOf( fimParam.substring( 6, 8 ) ) );

			Integer horIni = Integer.parseInt( String.valueOf( inicParam.substring( 0, 2 ) ) );
			Integer minIni = Integer.parseInt( String.valueOf( inicParam.substring( 3, 5 ) ) );
			Integer segIni = Integer.parseInt( String.valueOf( inicParam.substring( 6, 8 ) ) );

			Integer horaFinal = 0;
			Integer minFinal;
			Integer segFinal;

			if( horFim > horIni ) {
				horaFinal = horFim - horIni;
			}

			minFinal = minFim - minIni;
			segFinal = segFim - segIni;

			if( segFinal < 0 ) {
				segFinal += 60;
				minFinal -= 1;
			}

			if( minFinal < 0 ) {
				minFinal += 60;
				horaFinal -= 1;
			}

			// constroi a hora final
			String duracao = "";

			if( horaFinal == 0 ) {
				duracao = "00:";
			}
			else if( horaFinal <= 9 ) {
				duracao = "0" + horaFinal + ":";
			}
			else {
				duracao = horaFinal + ":";
			}

			duracao += (minFinal <= 9)? "0" + minFinal + ":" : minFinal + ":";
			duracao += (segFinal <= 9)? "0" + segFinal : segFinal;
			return( duracao );
		}
		catch( NumberFormatException e ) {
			e.printStackTrace();
			return( null );
		}
	}

	public static void main( String[] s ) {
		Programa p = new Programa( s );
		p.exec();
	}
}