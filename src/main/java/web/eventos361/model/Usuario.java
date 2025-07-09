package web.eventos361.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import web.eventos361.service.NomeUsuarioUnicoService;
import web.eventos361.validation.UniqueValueAttribute;

@Entity
@Table(name = "usuario")
@UniqueValueAttribute(attribute = "nomeUsuario", service = NomeUsuarioUnicoService.class, message = "Já existe um nome de usuário igual a este cadastrado")
public class Usuario implements Serializable {

	private static final long serialVersionUID = 5757384541654785800L; // Gere outro valor

	@Id
	@SequenceGenerator(name = "gerador55", sequenceName = "usuario_codigo_seq", allocationSize = 1)
	@GeneratedValue(generator = "gerador55", strategy = GenerationType.SEQUENCE)
	private Long codigo;
	@NotBlank(message = "O nome do usuário é obrigatório")
	private String nome;
	@NotBlank(message = "O e-mail do usuário é obrigatório")
	private String email;
	@NotBlank(message = "A senha do usuário é obrigatória")
	private String senha;
	@Column(name = "nome_usuario")
	@NotBlank(message = "O nome de usuário do usuário é obrigatório")
	private String nomeUsuario;
	@Column(name = "data_nascimento")
	@NotNull(message = "A data de nascimento do usuário é obrigatória")
	private LocalDate dataNascimento;
	private boolean ativo;
	@Column(name = "is_palestrante")
	private boolean palestrante;
	@OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Evento> eventos = new ArrayList<>();
	@OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public String getNomeUsuario() {
		return nomeUsuario;
	}

	public void setNomeUsuario(String nomeUsuario) {
		this.nomeUsuario = nomeUsuario;
	}

	public LocalDate getDataNascimento() {
		return dataNascimento;
	}

	public void setDataNascimento(LocalDate dataNascimento) {
		this.dataNascimento = dataNascimento;
	}

	public boolean isAtivo() {
		return ativo;
	}

	public void setAtivo(boolean ativo) {
		this.ativo = ativo;
	}

	public List<Evento> getEventos() {
		return eventos;
	}

	public void setEventos(List<Evento> eventos) {
		this.eventos = eventos;
	}

	public boolean isPalestrante() {
		return palestrante;
	}

	public void setPalestrante(boolean palestrante) {
		this.palestrante = palestrante;
	}

	public List<Participante> getParticipantes() {
		return participantes;
	}

	public void setParticipantes(List<Participante> participantes) {
		this.participantes = participantes;
	}

	@Override
	public boolean equals(Object o) {
		if (o == null || getClass() != o.getClass())
			return false;
		Usuario usuario = (Usuario) o;
		return ativo == usuario.ativo && palestrante == usuario.palestrante && Objects.equals(codigo, usuario.codigo)
				&& Objects.equals(nome, usuario.nome) && Objects.equals(email, usuario.email)
				&& Objects.equals(senha, usuario.senha) && Objects.equals(nomeUsuario, usuario.nomeUsuario)
				&& Objects.equals(dataNascimento, usuario.dataNascimento) && Objects.equals(eventos, usuario.eventos)
				&& Objects.equals(participantes, usuario.participantes);
	}

	@Override
	public int hashCode() {
		return Objects.hash(codigo, nome, email, senha, nomeUsuario, dataNascimento, ativo, palestrante, eventos,
				participantes);
	}
}