package com.generation.farmacia.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.generation.farmacia.model.Categoria;
import com.generation.farmacia.repository.CategoriaRepository;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/categorias")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class CategoriaController {

	@Autowired
	private CategoriaRepository categoriaRepository;
	
	//Listar todos
	@GetMapping
	public ResponseEntity<?> getAll(){
		List<Categoria> categorias = categoriaRepository.findAll();
			
		if (categorias.isEmpty()) {
		    Map<String, String> response = new HashMap<>();
		    response.put("mensagem", "Nenhuma categoria encontrada.");
		    return ResponseEntity.status(200).body(response);
		}
		return ResponseEntity.ok(categorias);
	}
	
	//Busca por id
	@GetMapping("/{id}")
	public ResponseEntity<Categoria> getById(@PathVariable Long id){
		return categoriaRepository.findById(id)
				.map(resposta -> ResponseEntity.ok(resposta))
				.orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
	}
	
	//Busca por nome
	@GetMapping("/nome/{nome}")
	public ResponseEntity<List<Categoria>> getAllByNome(@PathVariable String nome) {
	
		return ResponseEntity.ok(categoriaRepository.findAllByNomeContainingIgnoreCase(nome));
	}
	
	@GetMapping("/via/{via}")
	public ResponseEntity<List<Categoria>> getAllByVia(@PathVariable String via) {
	    return ResponseEntity.ok(categoriaRepository.findAllByViaContainingIgnoreCase(via));
	}

	
	//Create
	@PostMapping
	public ResponseEntity<Categoria> post(@Valid @RequestBody Categoria categoria){
		return ResponseEntity.status(HttpStatus.CREATED).body(categoriaRepository.save(categoria));
	}
	
	//Update
	@PutMapping
	public ResponseEntity<?> put(@Valid @RequestBody Categoria categoria){
		
		if(categoria.getId() == null )
			return ResponseEntity.badRequest().body("ID não pode ser nulo");
		
		if(!categoriaRepository.existsById(categoria.getId())) 
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Categoria não encontrada");
		
		Categoria atualizada = categoriaRepository.save(categoria);
	    return ResponseEntity.ok(atualizada);
	}
	
	//Delete
	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable Long id) {
		
		Optional<Categoria> postagem = categoriaRepository.findById(id);
		if(postagem.isEmpty())
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
	
		categoriaRepository.deleteById(id);
		
		//DELETE FROM tb_postagens WHERE id=?;
	}
	
}
