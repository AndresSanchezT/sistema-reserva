package com.cibertec.reservashotel.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cibertec.reservashotel.model.Departamento;

public interface DepartamentoRepository extends JpaRepository<Departamento, Long> {
	Optional<Departamento> findByNombre(String nombre);
}