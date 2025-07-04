package com.cibertec.reservashotel.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.Valid;

@Entity
public class Reserva {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate fechaReserva;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private double total;
    private String estado; // EJ: CONFIRMADA, CANCELADA

    @Valid
    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    @OneToMany(mappedBy = "reserva", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetalleReserva> detalles = new ArrayList<>();
    
    @ManyToOne
    @JoinColumn(name = "hotel_id")
    private Hotel hotel;

	public Reserva() {
	}	

	public Reserva(LocalDate fechaReserva, LocalDate fechaInicio, LocalDate fechaFin, double total, String estado,
			Cliente cliente, List<DetalleReserva> detalles) {
		this.fechaReserva = fechaReserva;
		this.fechaInicio = fechaInicio;
		this.fechaFin = fechaFin;
		this.total = total;
		this.estado = estado;
		this.cliente = cliente;
		this.detalles = detalles;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public LocalDate getFechaReserva() {
		return fechaReserva;
	}

	public void setFechaReserva(LocalDate fechaReserva) {
		this.fechaReserva = fechaReserva;
	}

	public LocalDate getFechaInicio() {
		return fechaInicio;
	}

	public void setFechaInicio(LocalDate fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	public LocalDate getFechaFin() {
		return fechaFin;
	}

	public void setFechaFin(LocalDate fechaFin) {
		this.fechaFin = fechaFin;
	}

	public double getTotal() {
		return total;
	}

	public void setTotal(double total) {
		this.total = total;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public List<DetalleReserva> getDetalles() {
		return detalles;
	}

	public void setDetalles(List<DetalleReserva> detalles) {
		this.detalles = detalles;
	}
  
	public Hotel getHotel() {
		return hotel;
	}

	public void setHotel(Hotel hotel) {
		this.hotel = hotel;
	}
	
//	se agrega esta funcion para controlar mejor  la persistencia bidireccional 
	public void addDetalle(DetalleReserva detalle) {
	    detalle.setReserva(this);
	    this.detalles.add(detalle);
	}

}