package com.generation.farmacia.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.generation.farmacia.model.Usuario;
import com.generation.farmacia.model.UsuarioDTO;
import com.generation.farmacia.model.UsuarioLogin;
import com.generation.farmacia.security.BasicSecurityConfig;
import com.generation.farmacia.service.UsuarioService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/usuarios")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class UsuarioController {

    private final BasicSecurityConfig basicSecurityConfig;
	
	@Autowired
	private UsuarioService usuarioService;


    UsuarioController(BasicSecurityConfig basicSecurityConfig) {
        this.basicSecurityConfig = basicSecurityConfig;
    }
	
    @GetMapping("/listarUsuarios")
    public ResponseEntity<List<UsuarioDTO>> getAll(){
        return ResponseEntity.ok(usuarioService.listarUsuarios());
    }
    
    @GetMapping("/buscarPorId/{id}")
    public ResponseEntity<UsuarioDTO> getById(@PathVariable Long id){
        return usuarioService.buscarUsuarioId(id)
        		.map(ResponseEntity::ok)
        		.orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/cadastrar")
    public ResponseEntity<Usuario> post(@Valid @RequestBody Usuario usuario) {
        Usuario novoUsuario = usuarioService.cadastrarUsuario(usuario).orElseThrow();
        return ResponseEntity.status(HttpStatus.CREATED).body(novoUsuario);
    }

	
	@PutMapping("/atualizar")
	public ResponseEntity<Usuario> put(@Valid @RequestBody Usuario usuario){
		return usuarioService.atualizarUsuario(usuario)
				.map(resposta -> ResponseEntity.status(HttpStatus.OK).body(resposta))
				.orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
	}
	
	@PostMapping("/logar")
	public ResponseEntity<UsuarioLogin> autenticar(@Valid @RequestBody Optional<UsuarioLogin> usuarioLogin){
		return usuarioService.autenticarUsuario(usuarioLogin)
				.map(resposta -> ResponseEntity.status(HttpStatus.OK).body(resposta))
				.orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
	}	
	
	
	@DeleteMapping("/deletar/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
	    if (usuarioService.deletarUsuario(id)) {
	        return ResponseEntity.noContent().build(); // 204 No Content
	    } else {
	        return ResponseEntity.notFound().build(); // 404 Not Found
	    }
	}
	
}
