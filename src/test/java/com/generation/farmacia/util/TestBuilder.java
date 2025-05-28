package com.generation.farmacia.util;

import java.time.LocalDate;
import java.util.Optional;

import com.generation.farmacia.model.Usuario;
import com.generation.farmacia.model.UsuarioLogin;
import com.generation.farmacia.repository.UsuarioRepository;

public class TestBuilder {

	public static Usuario criarUsuario(Long id, String nome, String email, String senha) {
	    Usuario usuario = new Usuario();
	    if (id != null) {
	        usuario.setId(id);
	    }
	    usuario.setNome(nome);
	    usuario.setUsuario(email);
	    usuario.setSenha(senha);
	    usuario.setDataNascimento(LocalDate.of(2000, 1, 1));
	    return usuario;
	}


    public static Usuario criarUsuarioRoot() {
        return criarUsuario(null, "Root", "root@email.com", "rootroot", LocalDate.of(2000, 1, 1));
    }


    public static Usuario criarUsuario(Long id, String nome, String email, String senha, LocalDate dataNascimento) {
        Usuario usuario = new Usuario();
        usuario.setId(id);
        usuario.setNome(nome);
        usuario.setUsuario(email);
        usuario.setSenha(senha);
        usuario.setDataNascimento(dataNascimento);
        return usuario;
    }


    // Versão opcional mais completa (caso precise de mais campos no login)
    public static UsuarioLogin criarUsuarioLoginCompleto(Long id, String nome, String email, String senha, String token) {
        UsuarioLogin usuarioLogin = new UsuarioLogin();
        usuarioLogin.setId(id);
        usuarioLogin.setNome(nome);
        usuarioLogin.setUsuario(email);
        usuarioLogin.setSenha(senha);
        usuarioLogin.setToken(token);
        return usuarioLogin;
    }

    // Método utilitário para evitar duplicação em testes
    public static void garantirUsuarioNaoExiste(String email, UsuarioRepository repository) {
        Optional<Usuario> usuarioExistente = repository.findByUsuario(email);
        usuarioExistente.ifPresent(usuario -> repository.deleteById(usuario.getId()));
    }
    
    
}
