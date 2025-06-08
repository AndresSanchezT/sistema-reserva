package com.cibertec.reservashotel.model;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

@Entity
public class Habitacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String numero;
    private String estado; // Ej: DISPONIBLE, OCUPADA, MANTENIMIENTO
    private double precio;

    @ManyToOne
    @JoinColumn(name = "tipo_id")
    private TipoHabitacion tipoHabitacion;

    @ManyToOne
    @JoinColumn(name = "hotel_id")
    private Hotel hotel;

    @OneToMany(mappedBy = "habitacion", cascade = CascadeType.ALL)
    private List<DetalleReserva> detalles;

	public Habitacion() {
	}

	public Habitacion(String numero, String estado, double precio, TipoHabitacion tipoHabitacion, Hotel hotel,
			List<DetalleReserva> detalles) {
		this.numero = numero;
		this.estado = estado;
		this.precio = precio;
		this.tipoHabitacion = tipoHabitacion;
		this.hotel = hotel;
		this.detalles = detalles;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public double getPrecio() {
		return precio;
	}

	public void setPrecio(double precio) {
		this.precio = precio;
	}

	public TipoHabitacion getTipoHabitacion() {
		return tipoHabitacion;
	}

	public void setTipoHabitacion(TipoHabitacion tipoHabitacion) {
		this.tipoHabitacion = tipoHabitacion;
	}

	public Hotel getHotel() {
		return hotel;
	}

	public void setHotel(Hotel hotel) {
		this.hotel = hotel;
	}

	public List<DetalleReserva> getDetalles() {
		return detalles;
	}

	public void setDetalles(List<DetalleReserva> detalles) {
		this.detalles = detalles;
	}
    
}