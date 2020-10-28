package task.timer.back;

public enum StatusTarefa {
    TODAS(0),
    FINALIZADAS(1),
    NAO_FINALIZADAS(2),
    EM_ANDAMENTO(3);

    private final int codigo;

    StatusTarefa(int pCodigo) {
        this.codigo = pCodigo;
    }

    public int getCodigo() {
        return codigo;
    }
}
