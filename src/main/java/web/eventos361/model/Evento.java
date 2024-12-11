package web.eventos361.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "evento")
public class Evento {

    private static final long serialVersionUID = 5757384541654785800L; // Gere outro valor

    @Id
    @SequenceGenerator(name="gerador55", sequenceName="usuario_codigo_seq", allocationSize=1)
    @GeneratedValue(generator="gerador55", strategy=GenerationType.SEQUENCE)
    private Long codigo;
    @NotBlank(message = "O nome do usuário é obrigatório")
    private String nome;
    @Column(name = "data_evento")
    @NotNull(message = "A data em que o evento ocorrerá é obrigatória")
    private LocalDate dataEvento;
    @NotBlank(message = "O local do evento é obrigatório")
    private String local;
    @NotNull(message = "A capacidade do evento é obrigatória")
    private Long capacidade;
    @Column(name = "finalizou_em")
    private LocalDate finalizouEm;
    @ManyToOne
    @JoinColumn(name = "codigo_usuario", referencedColumnName = "codigo", nullable = false, foreignKey = @ForeignKey(name = "fk_evento_usuario"))
    private Usuario usuario;
    @OneToMany(mappedBy = "evento", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Participante> participantes = new ArrayList<>();

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

    public LocalDate getDataEvento() {
        return dataEvento;
    }

    public String getDataEventoFormatada() {
        return dataEvento.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }

    public void setDataEvento(LocalDate dataEvento) {
        this.dataEvento = dataEvento;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public Long getCapacidade() {
        return capacidade;
    }

    public void setCapacidade(Long capacidade) {
        this.capacidade = capacidade;
    }

    public LocalDate getFinalizouEm() {
        return finalizouEm;
    }

    public void setFinalizouEm(LocalDate finalizouEm) {
        this.finalizouEm = finalizouEm;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public void finalizar() {
        if(finalizouEm == null) {
            finalizouEm = LocalDate.now();
        }
    }

    public List<Participante> getParticipantes() {
        return participantes;
    }

    public void setParticipantes(List<Participante> participantes) {
        this.participantes = participantes;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Evento evento = (Evento) o;
        return Objects.equals(codigo, evento.codigo) && Objects.equals(nome, evento.nome) && Objects.equals(dataEvento, evento.dataEvento) && Objects.equals(local, evento.local) && Objects.equals(capacidade, evento.capacidade) && Objects.equals(finalizouEm, evento.finalizouEm) && Objects.equals(usuario, evento.usuario) && Objects.equals(participantes, evento.participantes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codigo, nome, dataEvento, local, capacidade, finalizouEm, usuario, participantes);
    }

    @Override
    public String toString() {
        return "Evento{" +
                "codigo=" + codigo +
                ", nome='" + nome + '\'' +
                ", dataEvento=" + dataEvento +
                ", local='" + local + '\'' +
                ", capacidade=" + capacidade +
                ", finalizouEm=" + finalizouEm +
                ", usuario=" + usuario +
                ", participantes=" + participantes +
                '}';
    }
}
