package web.eventos361.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import web.eventos361.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
	
	Usuario findByNomeUsuarioIgnoreCase(String nomeUsuario);
	
}
