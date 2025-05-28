package com.generation.farmacia.service;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.generation.farmacia.model.Usuario;
import com.generation.farmacia.model.UsuarioDTO;
import com.generation.farmacia.model.UsuarioLogin;
import com.generation.farmacia.repository.UsuarioRepository;
import com.generation.farmacia.security.JwtService;

@Service
public class UsuarioService {
	
	//Usuarios verificados no sistema
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	//Faz o token
	@Autowired
	private JwtService jwtService;
	
	//Banco de dados
	@Autowired
	private AuthenticationManager authenticationManager;
	
	public List<UsuarioDTO> listarUsuarios() {
	    List<Usuario> usuarios = usuarioRepository.findAll();
	    return usuarios.stream()
	                   .map(usuario -> new UsuarioDTO(
	                       usuario.getId(),
	                       usuario.getNome(),
	                       usuario.getUsuario(),
	                       usuario.getDataNascimento()))
	                   .collect(Collectors.toList());
	}

	public Optional<UsuarioDTO> buscarUsuarioId(Long id) {
		 return usuarioRepository.findById(id)
	                   .map(usuario -> new UsuarioDTO(
	                		  usuario.getId(),
		                      usuario.getNome(),
		                      usuario.getUsuario(),
		                      usuario.getDataNascimento()));
	}

	
	public Optional<Usuario> cadastrarUsuario(Usuario usuario) {
	    if (!isMaiorDeIdade(usuario.getDataNascimento())) {
	        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Erro: Usuário deve ser maior de 18 anos.");
	    }

	    if (usuarioRepository.findByUsuario(usuario.getUsuario()).isPresent()) {
	        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Erro: Já existe um usuário com este e-mail cadastrado.");
	    }

	    usuario.setSenha(criptografarSenha(usuario.getSenha()));
	    return Optional.of(usuarioRepository.save(usuario));
	}
	
	
	public Optional<Usuario> atualizarUsuario(Usuario usuario) {
	    if (!isMaiorDeIdade(usuario.getDataNascimento())) {
	        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Erro: Usuário deve ser maior de 18 anos.");
	    }

	    // Verifica se o usuário existe pelo e-mail e não é o mesmo usuário que está sendo atualizado
	    Optional<Usuario> usuarioExistente = usuarioRepository.findByUsuario(usuario.getUsuario());
	    
	    if (usuarioExistente.isPresent() && !usuarioExistente.get().getId().equals(usuario.getId())) {
	        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Erro: Já existe um usuário com este e-mail cadastrado.");
	    }

	    // Verifica se a senha já está criptografada (bcrypt começa com "$2a$")
	    if (usuario.getSenha() != null && !usuario.getSenha().startsWith("$2a$")) {
	        usuario.setSenha(criptografarSenha(usuario.getSenha()));
	    }
	    
	    return Optional.ofNullable(usuarioRepository.save(usuario));
	}
	
	public boolean deletarUsuario(Long id) {
	    Optional<Usuario> usuario = usuarioRepository.findById(id);
	    if (usuario.isPresent()) {
	        usuarioRepository.deleteById(id);
	        return true;
	    }
	    return false;
	}

	
	public Optional<UsuarioLogin> autenticarUsuario(Optional<UsuarioLogin> usuarioLogin){
		
		var credenciais = new UsernamePasswordAuthenticationToken(
				usuarioLogin.get().getUsuario(),
				usuarioLogin.get().getSenha()
		);
		
		Authentication authentication = authenticationManager.authenticate(credenciais);
		
		if(authentication.isAuthenticated()) {
			Optional<Usuario> usuario = usuarioRepository.findByUsuario(usuarioLogin.get().getUsuario()) ;
			
			if(usuario.isPresent()) {
				usuarioLogin.get().setId(usuario.get().getId());
				usuarioLogin.get().setNome(usuario.get().getNome());
				usuarioLogin.get().setSenha("");
				usuarioLogin.get().setToken(gerarToken(usuarioLogin.get().getUsuario()));
				
				return usuarioLogin;
			}
		}
		return Optional.empty();
	}
	
	private String gerarToken(String usuario) {
		return "Bearer " + jwtService.generateToken(usuario);
	}
	
	
	private String criptografarSenha(String senha) {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		return encoder.encode(senha);
	}
	
	private boolean isMaiorDeIdade(LocalDate dataNascimento) {
	    return Period.between(dataNascimento, LocalDate.now()).getYears() >= 18;
	}
	
	

	
}
