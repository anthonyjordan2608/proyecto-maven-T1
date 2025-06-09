package com.empresa.videoclub.Servlets;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

import com.empresa.videoclub.enums.EstadoAlquiler;
import com.empresa.videoclub.model.Alquiler;
import com.empresa.videoclub.model.Cliente;
import com.empresa.videoclub.model.Pelicula;
import com.empresa.videoclub.repository.ClienteRepository;
import com.empresa.videoclub.service.AlquilerService;
import com.empresa.videoclub.service.PeliculaService;

@WebServlet(name = "AlquilerServlet", urlPatterns = {"/alquiler"}, loadOnStartup = 1)
public class AlquilerServlet extends HttpServlet {
    
    private final AlquilerService alquilerService = new AlquilerService();
    private final PeliculaService peliculaService = new PeliculaService();
    private final ClienteRepository clienteRepository = new ClienteRepository();
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        try {
            List<Cliente> clientes = clienteRepository.findAll();
            List<Pelicula> peliculas = peliculaService.listarPeliculasDisponibles();
            
            request.setAttribute("clientes", clientes);
            request.setAttribute("peliculas", peliculas);
            request.setAttribute("estadosAlquiler", EstadoAlquiler.values());
            
            request.getRequestDispatcher("/views/alquiler.jsp").forward(request, response);
        } catch (Exception e) {
            request.setAttribute("error", "Error al cargar datos: " + e.getMessage());
            request.getRequestDispatcher("/views/alquiler.jsp").forward(request, response);
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        request.setCharacterEncoding("UTF-8");
        
        try {
            // 1. Validar cliente
            String clienteIdStr = request.getParameter("cliente");
            if (clienteIdStr == null || clienteIdStr.trim().isEmpty()) {
                throw new IllegalArgumentException("Debe seleccionar un cliente");
            }
            int idCliente = Integer.parseInt(clienteIdStr.trim());
            
            // 2. Validar películas
            String[] peliculaIds = request.getParameterValues("peliculaId");
            String[] cantidades = request.getParameterValues("cantidad");
            
            if (peliculaIds == null || peliculaIds.length == 0) {
                throw new IllegalArgumentException("Debe seleccionar al menos una película");
            }
            
            if (cantidades == null || peliculaIds.length != cantidades.length) {
                throw new IllegalArgumentException("Cantidad de películas no coincide con cantidades");
            }
            
            // Validar cada película individualmente
            Map<Integer, Integer> peliculasMap = new HashMap<>();
            for (int i = 0; i < peliculaIds.length; i++) {
                String idStr = peliculaIds[i] != null ? peliculaIds[i].trim() : "";
                String cantidadStr = cantidades[i] != null ? cantidades[i].trim() : "";
                
                if (idStr.isEmpty()) {
                    throw new IllegalArgumentException("ID de película no puede estar vacío en la posición " + (i+1));
                }
                
                if (cantidadStr.isEmpty()) {
                    throw new IllegalArgumentException("Cantidad no puede estar vacía para la película " + idStr);
                }
                
                try {
                    int idPelicula = Integer.parseInt(idStr);
                    int cantidad = Integer.parseInt(cantidadStr);
                    
                    if (cantidad <= 0) {
                        throw new IllegalArgumentException("Cantidad debe ser mayor a 0 para la película " + idPelicula);
                    }
                    
                    Pelicula pelicula = peliculaService.buscarPorId(idPelicula);
                    if (pelicula == null) {
                        throw new IllegalArgumentException("No existe película con ID: " + idPelicula);
                    }
                    
                    if (pelicula.getStock() < cantidad) {
                        throw new IllegalArgumentException("Stock insuficiente para: " + pelicula.getTitulo());
                    }
                    
                    peliculasMap.put(idPelicula, cantidad);
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Formato inválido para película o cantidad en la posición " + (i+1));
                }
            }
            
            // 3. Validar estado
            String estadoStr = request.getParameter("estado");
            if (estadoStr == null || estadoStr.trim().isEmpty()) {
                throw new IllegalArgumentException("Debe seleccionar un estado");
            }
            EstadoAlquiler estado = EstadoAlquiler.valueOf(estadoStr.trim());
            
            // 4. Validar total
            String totalStr = request.getParameter("total");
            if (totalStr == null || totalStr.trim().isEmpty()) {
                throw new IllegalArgumentException("El total no puede estar vacío");
            }
            BigDecimal total = new BigDecimal(totalStr.trim());
            
            // 5. Crear alquiler
            Alquiler alquiler = new Alquiler();
            alquiler.setFecha(LocalDateTime.now());
            
            Cliente cliente = clienteRepository.findById(idCliente);
            if (cliente == null) {
                throw new IllegalArgumentException("No existe cliente con ID: " + idCliente);
            }
            alquiler.setCliente(cliente);
            
            alquiler.setTotal(total.doubleValue());
            alquiler.setEstado(estado);
            
            // 6. Registrar alquiler
            alquilerService.registrarAlquiler(alquiler, peliculasMap);
            
            request.setAttribute("success", "Alquiler registrado exitosamente. ID: " + alquiler.getIdAlquiler());
            
        } catch (IllegalArgumentException e) {
            request.setAttribute("error", "Error de validación: " + e.getMessage());
        } catch (Exception e) {
            request.setAttribute("error", "Error interno: " + e.getMessage());
            e.printStackTrace();
        }
        
        // Recargar datos
        doGet(request, response);
    }
}