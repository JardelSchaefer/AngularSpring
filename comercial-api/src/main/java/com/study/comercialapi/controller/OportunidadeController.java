package com.study.comercialapi.controller;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.study.comercialapi.model.Oportunidade;
import com.study.comercialapi.repository.OportunidadeRepository;

@CrossOrigin
@RestController
@RequestMapping("/oportunidades")
public class OportunidadeController {
	
	@Autowired
	private OportunidadeRepository oportunidadeRepository;
	
	@GetMapping
	public List<Oportunidade> listar(){
		List<Oportunidade> oportunidades = oportunidadeRepository.findAll();
		return oportunidades;
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Oportunidade> buscar(@PathVariable Long id) {
		Optional<Oportunidade> oportunidade = oportunidadeRepository.findById(id);
		if (oportunidade.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		
		return ResponseEntity.ok(oportunidade.get());
	}
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Oportunidade adicionar(@Valid @RequestBody Oportunidade oportunidade) {
		Optional<Oportunidade> oportunidadeExistente = oportunidadeRepository.findByDescricaoAndNomeProspecto
				(oportunidade.getDescricao(), oportunidade.getNomeProspecto());
		
		if (oportunidadeExistente.isPresent()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Já tem uma oportunidade"
					+ " igual, sorry :)");
		}
		
		return oportunidadeRepository.save(oportunidade);
		
	}
	
	@DeleteMapping("/{id}")
	public String deletar (@PathVariable Long id) {
		Optional <Oportunidade> oportunidade = oportunidadeRepository.findById(id);
	
		if (oportunidade.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Oportunidade inexistente ou ja exluída");
		}
	
		oportunidadeRepository.delete(oportunidade.get());
		
		return "Oportunidade excluida com sucesso";
	}
	
	


}
