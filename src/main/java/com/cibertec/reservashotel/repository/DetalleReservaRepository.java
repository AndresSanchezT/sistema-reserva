package com.cibertec.reservashotel.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cibertec.reservashotel.model.DetalleReserva;

public interface DetalleReservaRepository extends JpaRepository<DetalleReserva, Long> {
}