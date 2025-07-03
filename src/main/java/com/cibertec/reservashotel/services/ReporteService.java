package com.cibertec.reservashotel.services;

import java.io.File;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import com.cibertec.reservashotel.model.Reserva;
import com.cibertec.reservashotel.repository.ReservaRepository;

import jakarta.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@Service
public class ReporteService {

    @Autowired
    private ReservaRepository reservaRepository;
    
    public void exportarReporteReservas(LocalDate inicio, LocalDate fin, HttpServletResponse response) throws Exception {
        List<Reserva> reservasOriginal = reservaRepository.findByFechaReservaBetween(inicio, fin);

        // Convertimos a lista de mapas (datos planos compatibles con Jasper)
        List<Map<String, Object>> reservas = reservasOriginal.stream().map(r -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id", r.getId());
            map.put("fechaReserva", java.sql.Date.valueOf(r.getFechaReserva()));
            map.put("cliente", r.getCliente().getNombre());
            map.put("hotel", r.getHotel().getNombre());
            map.put("fechaInicio", java.sql.Date.valueOf(r.getFechaInicio()));
            map.put("fechaFin", java.sql.Date.valueOf(r.getFechaFin()));
            map.put("total", r.getTotal());
            map.put("estado", r.getEstado());
            return map;
        }).toList();

        Map<String, Object> parametros = new HashMap<>();
        parametros.put("fechaInicio", inicio.toString());
        parametros.put("fechaFin", fin.toString());

        // Cargar el archivo .jrxml desde resources
        File file = ResourceUtils.getFile("classpath:reservasrango.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());

        // Usar JRBeanCollectionDataSource con la lista de mapas
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(reservas);

        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parametros, dataSource);

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "inline; filename=reservas-fechas.pdf");

        JasperExportManager.exportReportToPdfStream(jasperPrint, response.getOutputStream());
    }

    	
    public void exportarHistorialReservas(HttpServletResponse response) throws Exception {
    	List<Reserva> reservas = reservaRepository.findAll();
    	
    	   // Convertimos a lista de mapas (datos planos compatibles con Jasper)
        List<Map<String, Object>> reservasMapeada = reservas.stream().map(r -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id", r.getId());
            map.put("fechaReserva", java.sql.Date.valueOf(r.getFechaReserva()));
            map.put("cliente", r.getCliente().getNombre());
            map.put("hotel", r.getHotel().getNombre());
            map.put("fechaInicio", java.sql.Date.valueOf(r.getFechaInicio()));
            map.put("fechaFin", java.sql.Date.valueOf(r.getFechaFin()));
            map.put("total", r.getTotal());
            map.put("estado", r.getEstado());
            return map;
        }).toList();
    	
        // Cargar el archivo .jrxml desde resources
        File file = ResourceUtils.getFile("classpath:historialReservas.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());

        // Usar JRBeanCollectionDataSource con la lista de mapas
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(reservasMapeada);

        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, null, dataSource);

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "inline; filename=reservas-fechas.pdf");

        JasperExportManager.exportReportToPdfStream(jasperPrint, response.getOutputStream());
        
    }
}
