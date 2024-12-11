package web.eventos361.model;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "participante")
public class Participante {

    private static final long serialVersionUID = 5757384541654785800L; // Gere outro valor

    @Id
    @SequenceGenerator(name="gerador55", sequenceName="usuario_codigo_seq", allocationSize=1)
    @GeneratedValue(generator="gerador55", strategy=GenerationType.SEQUENCE)
    private Long codigo;
    @ManyToOne
    @JoinColumn(name = "codigo_usuario", referencedColumnName = "codigo", nullable = false, foreignKey = @ForeignKey(name = "fk_participante_usuario"))
    private Usuario usuario;
    @ManyToOne
    @JoinColumn(name = "codigo_evento", referencedColumnName = "codigo", nullable = false, foreignKey = @ForeignKey(name = "fk_participante_evento"))
    private Evento evento;

    public Long getCodigo() {
        return codigo;
    }

    public void setCodigo(Long codigo) {
        this.codigo = codigo;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Evento getEvento() {
        return evento;
    }

    public void setEvento(Evento evento) {
        this.evento = evento;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Participante that = (Participante) o;
        return Objects.equals(codigo, that.codigo) && Objects.equals(usuario, that.usuario) && Objects.equals(evento, that.evento);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codigo, usuario, evento);
    }
}
