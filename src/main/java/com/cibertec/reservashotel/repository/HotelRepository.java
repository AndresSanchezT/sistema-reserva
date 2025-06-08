package com.cibertec.reservashotel.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cibertec.reservashotel.model.Hotel;

public interface HotelRepository extends JpaRepository<Hotel, Long> {
    List<Hotel> findByDepartamentoId(Long departamentoId);
}
