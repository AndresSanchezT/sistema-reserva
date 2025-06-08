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
public class Hotel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String direccion;

    @ManyToOne
    @JoinColumn(name = "departamento_id")
    private Departamento departamento;

    @OneToMany(mappedBy = "hotel", cascade = CascadeType.ALL)
    private List<Habitacion> habitaciones;

	public Hotel() {
	}

	public Hotel(String nombre, String direccion, Departamento departamento, List<Habitacion> habitaciones) {
		this.nombre = nombre;
		this.direccion = direccion;
		this.departamento = departamento;
		this.habitaciones = habitaciones;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public Departamento getDepartamento() {
		return departamento;
	}

	public void setDepartamento(Departamento departamento) {
		this.departamento = departamento;
	}

	public List<Habitacion> getHabitaciones() {
		return habitaciones;
	}

	public void setHabitaciones(List<Habitacion> habitaciones) {
		this.habitaciones = habitaciones;
	}
	
	public double getPrecioMinimo() {
	    if (habitaciones == null || habitaciones.isEmpty()) {
	        return 0.0;
	    }

	    return habitaciones.stream()
	        .mapToDouble(Habitacion::getPrecio)
	        .min()
	        .orElse(0.0);
	}
	
	public int cantidadHabitaciones() {
		return habitaciones.size();
	}

}