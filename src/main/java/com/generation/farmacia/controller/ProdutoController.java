package com.generation.farmacia.controller;

import java.math.BigDecimal;
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

import com.generation.farmacia.model.Produto;
import com.generation.farmacia.repository.CategoriaRepository;
import com.generation.farmacia.repository.ProdutoRepository;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/produtos")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ProdutoController {

	@Autowired
	private ProdutoRepository produtoRepository;
	
	@Autowired
	private CategoriaRepository categoriaRepository;
	
	//Mostrar todos os produtos
	@GetMapping
	public ResponseEntity<?> getAll(){
		List<Produto> produtos = produtoRepository.findAll();
			
		if (produtos.isEmpty()) {
		    Map<String, String> response = new HashMap<>();
		    response.put("mensagem", "Nenhum produto encontrado.");
		    return ResponseEntity.status(200).body(response);
		}
		return ResponseEntity.ok(produtos);
	}
	
	//Buscar por id
	@GetMapping("/{id}")
	public ResponseEntity<Produto> getById(@PathVariable Long id){
		return produtoRepository.findById(id)
				.map(resposta -> ResponseEntity.ok(resposta))
				.orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
	}
	
	//Buscar por titulo
	@GetMapping("/titulo/{titulo}")
	public ResponseEntity<List<Produto>> getAllByTitulo(@PathVariable String titulo) {
		return ResponseEntity.ok(produtoRepository.findAllByTituloContainingIgnoreCase(titulo));
	}
	
	//Buscar por categoria
	@GetMapping("/categoriaID/{categoriaID}")
	public ResponseEntity<List<Produto>>  getByCategoriaId(@PathVariable Long categoriaID) {
		return ResponseEntity.ok(produtoRepository.findByCategoriaId(categoriaID));
	}
	
	//Maior Preço
	@GetMapping("/preco/maiorque/{preco}")
	public ResponseEntity<List<Produto>> getByPrecoMaiorQue(@PathVariable BigDecimal preco) {
	    return ResponseEntity.ok(produtoRepository.findByPrecoGreaterThan(preco));
	}

	
	//Menor Preço
	@GetMapping("/preco/menorque/{preco}")
	public ResponseEntity<List<Produto>> getByPrecoMenorQue(@PathVariable BigDecimal preco) {
	    return ResponseEntity.ok(produtoRepository.findByPrecoLessThan(preco));
	}

	
	//Create
	@PostMapping
	public ResponseEntity<Produto> post(@Valid @RequestBody Produto produto) {
		validarCategoriaExistente(produto.getCategoria().getId());
		return ResponseEntity.status(HttpStatus.CREATED).body(produtoRepository.save(produto));
	}
	
	
	//Update
	@PutMapping
	public ResponseEntity<Produto> put(@Valid @RequestBody Produto produto) {
		if (produto.getId() == null)
			return ResponseEntity.badRequest().build();
 
		if (!produtoRepository.existsById(produto.getId()))
	    return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

		validarCategoriaExistente(produto.getCategoria().getId());
		return ResponseEntity.ok(produtoRepository.save(produto));
	}
	
	//Delete
	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable Long id) {
		
		Optional<Produto> produto = produtoRepository.findById(id);
		if(produto.isEmpty())
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		
		produtoRepository.deleteById(id);

	}
	
	//Auxiliares
	private void validarCategoriaExistente(Long categoriaId) {
	    if (!categoriaRepository.existsById(categoriaId)) {
	        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A categoria não existe!");
	    }
	}
	
	

}
