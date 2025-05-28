package com.generation.farmacia.model;

import java.time.LocalDate;

public class UsuarioDTO {
    private Long id;
    private String nome;
    private String email;
    private LocalDate dataNascimento;

    // Construtor
    public UsuarioDTO(Long id, String nome, String email, LocalDate dataNascimento ) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.dataNascimento = dataNascimento;
    }
    // Getters e setters

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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public LocalDate getDataNascimento() {
		return dataNascimento;
	}

	public void setDataNascimento(LocalDate dataNascimento) {
		this.dataNascimento = dataNascimento;
	}

    
    
}
