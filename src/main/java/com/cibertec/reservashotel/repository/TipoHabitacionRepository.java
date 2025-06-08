package com.cibertec.reservashotel.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cibertec.reservashotel.model.TipoHabitacion;

public interface TipoHabitacionRepository extends JpaRepository<TipoHabitacion, Long> {
}