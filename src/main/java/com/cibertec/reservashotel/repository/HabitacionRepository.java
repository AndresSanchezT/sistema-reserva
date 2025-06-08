package com.cibertec.reservashotel.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cibertec.reservashotel.model.Habitacion;

public interface HabitacionRepository extends JpaRepository<Habitacion, Long> {
    List<Habitacion> findByHotelId(Long hotelId);
    
    @Query("SELECT h FROM Habitacion h WHERE h.hotel.id = :hotelId AND h.estado = 'DISPONIBLE'")
    List<Habitacion> findDisponiblesByHotelId(@Param("hotelId") Long hotelId);
}