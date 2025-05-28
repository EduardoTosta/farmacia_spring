package com.generation.farmacia.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "td_categoria") // CREATE TABLE tb_categoriaa;
public class Categoria {
	
	//Atributos
	@Id //Primary Key
	@GeneratedValue(strategy = GenerationType.IDENTITY) //AUTO_INCREMENT
	private Long id; 
	
	//Nome
	@Column(length = 1000)
	@NotBlank(message = "O atributo 'Nome' é obrigatório")
	@Pattern(regexp = "^[^0-9].*", message = "O Nome não pode ser apenas numérico")
	@Size(min = 3, max = 1000, message = "O atributo 'Nome' deve ter entre 3 e 1000 caracteres")
	private String nome;
	

	//Via de administração
	@NotBlank(message = "O atributo 'Via de administração' é obrigatório")
	@Size(min = 3, max = 100, message = "O atributo 'Via de administração' deve ter entre 3 e 100 caracteres")
	private String via;

	
	/*
	//Produto
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "categoria", cascade = CascadeType.REMOVE)
	@JsonIgnoreProperties("categoria")
	private List<Produto> produto;
	*/
	
	
	//Getters e Setters
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getVia() {
		return via;
	}

	public void setVia(String via) {
		this.via = via;
	}

	
	/*
	public List<Produto> getProduto() {
		return produto;
	}

	public void setProduto(List<Produto> produto) {
		this.produto = produto;
	}
	*/	
	
}
