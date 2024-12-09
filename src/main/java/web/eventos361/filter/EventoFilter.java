package web.eventos361.filter;

public class EventoFilter {
    private Long codigo;
    private String nome;
    private String local;

    public Long getCodigo() {
        return codigo;
    }

    public void setCodigo(Long codigo) {
        this.codigo = codigo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    @Override
    public String toString() {
        return "EventoFilter{" +
                "codigo=" + codigo +
                ", nome='" + nome + '\'' +
                ", local='" + local + '\'' +
                '}';
    }
}
