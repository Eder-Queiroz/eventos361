package web.eventos361.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import web.eventos361.model.Usuario;
import web.eventos361.repository.UsuarioRepository;

@Service
public class CadastroUsuarioService {

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Transactional
	public void salvar(Usuario usuario) {
		usuarioRepository.save(usuario);
	}

	@Transactional
	public Usuario pesquisarPorNome(String nomeUsuario) {
		return usuarioRepository.findByNomeUsuarioIgnoreCase(nomeUsuario);
	}
}
