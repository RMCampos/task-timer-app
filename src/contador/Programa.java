package contador;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import javax.swing.JFileChooser;

public class Programa {
  private final Tela frame;
  private String tempoTotal;
  private char transacao;
  private String nomeCidade;

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
    obterNomeCidade( s );
    this.frame = new Tela( this.nomeCidade );
    this.frame.setTitle( "Contador de Tarefas - Versão 002 (22/08/2014)" );
    this.frame.setVisible( true );
    this.tempoTotal = "00:00:00";
    this.transacao = 'I';
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
        
        if( this.frame.getComandoTela().equals( "ADICIONAR_DIAGRAMA" ) ) {
          adicionarDiagrama();
        }
        if( this.frame.getComandoTela().equals( "ALTERAR_OBS" ) ) {
          alterarObs();
        }
        if( this.frame.getComandoTela().equals( "CANCELAR" ) ) {
          cancelar();
        }
        if( this.frame.getComandoTela().equals( "CARREGAR_DIAGRAMA" ) ) {
          carregarDiagramaSelecionado();
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
        if( this.frame.getComandoTela().equals( "EXCLUIR" ) ) {
          excluir();
        }
        if( this.frame.getComandoTela().equals( "PARAR" ) ) {
          parar();
        }
        if( this.frame.getComandoTela().equals( "PROCURAR_DIRETORIO" ) ) {
          procurarDiretorio();
        }
      }
      while( !this.frame.getComandoTela().equals( "SAIR" ) );
    }
    catch( Exception ex ) {
      ex.printStackTrace();
    }
  }
  private void procurarDiretorio() {
    JFileChooser folder = new JFileChooser();
    folder.setCurrentDirectory( new File( "." ) );
    folder.setDialogTitle( "Selecione uma pasta" );
    folder.setFileSelectionMode( JFileChooser.DIRECTORIES_ONLY );
    folder.setAcceptAllFileFilterUsed( false );
    
    if( folder.showOpenDialog( this.frame ) == JFileChooser.APPROVE_OPTION ){
      System.out.println( "Selecionado: " + folder.getSelectedFile());
      this.frame.setTxfDiretorio( folder.getSelectedFile().toString() );
    }
  }
  private void carregarLinha( String pParam ) {
    Diagrama dia = this.frame.getDiagrama();

    if( pParam.equals( "ANTERIOR" ) ) {
      this.frame.carregarDiagramaPosicaoA( dia, '-' );
    }
    else if( pParam.equals( "PROXIMA" ) ) {
      this.frame.carregarDiagramaPosicaoA( dia, '+' );
    }
  }
  private void alterarObs() {
    if( this.transacao == 'A' ) {
      Diagrama dia = this.frame.getDiagrama();
      dia.setObs( this.frame.getTxfObs() );
    }
  }
  private boolean validar() {
    try {
      boolean flValidar = true;
      String strMsg = "";

      /* Desabilitado a validacao do codigo */
      /*
      if( this.frame.getTxfCodPr().isEmpty() ) {
        flValidar = false;
        strMsg += "->Cód do diagrama não informado.\n";
      }
      */
      
      if( this.frame.getTxfNome().isEmpty() )
      {
        flValidar = false;
        strMsg += "->Nome da tarefa não informado.\n";
      }
      
      /* Desabilitado validacao para o solicitante */
      /*
      if( this.frame.getCbxNomeCliente() == null )
      {
        flValidar = false;
        strMsg += "->Nome do cliente não informado.\n";
      }
      */
      
      if( !flValidar ) {
        Mensagem.informacao( "Erro ao criar tarefa:\n" + strMsg, this.frame );
      }
      
      return( flValidar );
    }
    catch( Exception e ) {
      e.printStackTrace();
      return( false );
    }
  }
  private Diagrama criarDiagrama() {
    try {
      Diagrama d = new Diagrama();
  
      d.setCodigo( this.frame.getTxfCodigo() );
      d.setNome( this.frame.getTxfNome() );
      d.setSoliciante( this.frame.getTxfSolicitante() );
      d.setObs( this.frame.getTxfObs() );
      
      DateFormat hora = new SimpleDateFormat( "HH:mm:ss" );
      
      d.setHoraInicio( hora.format( new Date() ) );
      
      return( d );
    }
    catch( Exception e ) {
      e.printStackTrace();
      return( null );
    }
  }
  private void adicionarDiagrama() {
    try {
      if( validar() ) {
        Diagrama diagrama = criarDiagrama();
        diagrama.iniciarTempo();
        this.frame.addDiagrama( diagrama );
        this.frame.limpar();
      }
    }
    catch( Exception e ) {
      e.printStackTrace();
    }
  }
  private void carregarDiagramaSelecionado() {
    try {
      Diagrama d = this.frame.getLinhaSelecionada();
      
      this.frame.setDiagrama( d );
      this.transacao = 'A';
      this.frame.mudarEstado( "EDICAO" );
      this.frame.setTxfCodigo( d.getCodigo() );
      this.frame.setTxfNome( d.getNome() );
      this.frame.setTxfSolicitante( d.getSolicitante() );
      this.frame.setTxfObs( d.getObs() );
      this.frame.habilitarContinuar( d.emAndamento() );
      this.frame.habilitarBotaoInserir( false );
      this.frame.habilitarExcluir( true );
      this.frame.setLblTotalPr( d.getCronometro() );
      this.frame.setLblTotalTempo( obterTempoTotalDecorrido() );
    }
    catch( Exception e ) {
      e.printStackTrace();
    }
  }
  private String obterTempoTotalDecorrido() {
    Collection<Diagrama> diagramaList = this.frame.contadorModel.getLinhas();
    
    this.tempoTotal = "00:00:00";
    for( Diagrama d : diagramaList ) {
      this.tempoTotal = somarTempoTotal( this.tempoTotal, d.getCronometro() );
    }
    return( tempoTotal );
  }
  private void continuar() {
    Diagrama d = this.frame.getLinhaSelecionada();
    
    if( d == null ) {
      Mensagem.informacao( "Nenhuma linha selecionada.", frame);
      return;
    }
      
    d.setHoraIntervalo( new Date() );
    d.setEmAndamento( true );
    d.iniciarTempo();
      
    this.frame.limpar();
    this.frame.contadorTable.repaint();
    this.frame.contadorTable.getSelectionModel().clearSelection();
    this.frame.habilitarBotaoInserir( true );
    this.frame.habilitarBotoes( false );
    this.frame.setDiagrama( null );
    this.frame.limparTempoDecorrido();
    this.frame.mudarEstado( "" );
    this.transacao = 'I';
  }
  private void parar() {
    try {
      this.frame.setDiagrama( null );
      Diagrama d = this.frame.getLinhaSelecionada();
      
      DateFormat data = new SimpleDateFormat( "HH:mm:ss" );
      d.setHoraTermino( data.format( new Date() ) );

      if( d.getDuracao().isEmpty() ) {
        String duracao = obterDuracao( d.getHoraTermino(), data.format( d.getHoraIntervalo() ) );
        d.setDuracao( duracao );
      }
      else {
        String duracaoAtual = d.getDuracao();
        String novaDuracao = obterDuracao( d.getHoraTermino(), data.format( d.getHoraIntervalo() ) );
        d.setDuracao( somarDuracao( duracaoAtual, novaDuracao ) );
      }

      d.setEmAndamento( false );
      d.setHoraIntervalo( new Date() );
      d.pararTempo();
      
      this.frame.limpar();
      this.frame.contadorTable.repaint();
      this.frame.contadorTable.getSelectionModel().clearSelection();
      this.frame.habilitarBotaoInserir( true );
      this.frame.habilitarBotoes( false );
      this.frame.limparTempoDecorrido();
      this.frame.mudarEstado( "" );
      this.transacao = 'I';
    }
    catch( Exception e ) {
      e.printStackTrace();
    }
  }
  private void cancelar() {
    this.frame.limpar();
    this.frame.setDiagrama( null );
    this.frame.limparTempoDecorrido();
    this.frame.habilitarBotoes( false );
    this.frame.habilitarBotaoInserir( true );
    this.frame.mudarEstado( "" );
    this.transacao = 'I';
    this.frame.contadorTable.getSelectionModel().clearSelection();
  }
  private void exportar() {
    try {
      Collection<Diagrama> dList = this.frame.contadorModel.getLinhas();
      String tempoTotalTarefas = obterTempoTotalDecorrido();
      
      if( dList == null || dList.isEmpty() ) {
        System.out.println( "Não existe nada para ser exportado." );
        return;
      }
      
      final String cabecalho = "CODIGO;NOME;SOLICITANTE;TEMPO TOTAL;DATA;HORA INICIO;HORA FIM;OBS";
      DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
      String dataAtual = formatter.format( new Date() );
      String camArquivo;

      if( OS.isWindows() ) {
       camArquivo = System.getProperty( "user.home" ) + "\\Desktop\\";
      }
      else {
        camArquivo = System.getProperty( "user.home" ) + "/";
      }
      String nomeArquivo = (this.frame.getTxfNomeArquivo().isEmpty())? "Contador.csv" : this.frame.getTxfNomeArquivo();

      if( !nomeArquivo.endsWith( ".csv" ) )
        nomeArquivo += ".csv";

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
        arquivo = new FileWriter( "Contador.csv" );
      }

      PrintWriter gravarArq = new PrintWriter(arquivo);

      gravarArq.printf( cabecalho + "\r\n" );

      for( Diagrama dia : dList ) {
        /* Formato:
        * CODIGO - NOME - SOLICITANTE - TEMPO_TOTAL - DATA - HORA_INICIO - HORA_FIM - OBS
        */
        String linha =
          ((dia.getCodigo().isEmpty())? " N " : dia.getCodigo()) + ";" +
          dia.getNome() + ";" +
          ((dia.getSolicitante().isEmpty())? " N " : dia.getSolicitante()) + ";" +
          dia.getDuracao() + ";" +
          dataAtual + ";" +
          dia.getHoraInicio() + ";" +
          dia.getHoraTermino() + ";" +
          //dia.getDuracao() + ";" +
          ((dia.getObs().isEmpty())? "Nenhuma." : dia.getObs()) + "\r\n";

        System.out.println( "gravando a linha: " + linha );

        gravarArq.printf( linha );
      }

      // Grava o tempo Total
      gravarArq.printf( "\r\n \r\nTempo total das tarefas: " + tempoTotalTarefas + "\r\n" );

      arquivo.close();

      Mensagem.informacao( "Contador exportado com sucesso!\nLocal: " + camArquivo + "\nNome: " + nomeArquivo, this.frame );
    }
    catch( IOException e ) {
      e.printStackTrace();
    }
  }
  private void excluir() {
    Diagrama d = this.frame.getLinhaSelecionada();

    if( d == null ) {
      Mensagem.informacao( "Nenhuma tarefa selecionada.", this.frame );
    }
    
    if( Mensagem.confirmar( "Confirma a exclusão da tarefa?", this.frame ) ) {
      this.frame.contadorModel.removeLinha( d );
      this.frame.contadorTable.getSelectionModel().clearSelection();
      this.frame.habilitarBotaoInserir( true );
      this.frame.habilitarBotoes( false );
    }
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
