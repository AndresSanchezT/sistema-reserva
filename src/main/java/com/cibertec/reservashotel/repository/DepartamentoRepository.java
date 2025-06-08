package com.cibertec.reservashotel.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cibertec.reservashotel.model.Departamento;

public interface DepartamentoRepository extends JpaRepository<Departamento, Long> {
}