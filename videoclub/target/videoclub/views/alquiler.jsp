<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Gestión de Alquiler de Películas</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 20px;
            background-color: #f5f5f5;
        }
        .container {
            background-color: white;
            padding: 20px;
            border-radius: 5px;
            box-shadow: 0 0 10px rgba(0,0,0,0.1);
            max-width: 800px;
            margin: 0 auto;
        }
        h1 {
            color: #333;
            text-align: center;
        }
        .form-group {
            margin-bottom: 15px;
        }
        label {
            display: block;
            margin-bottom: 5px;
            font-weight: bold;
        }
        select, input {
            width: 100%;
            padding: 8px;
            border: 1px solid #ddd;
            border-radius: 4px;
            box-sizing: border-box;
        }
        button {
            background-color: #4CAF50;
            color: white;
            padding: 10px 15px;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            font-size: 16px;
        }
        button:hover {
            background-color: #45a049;
        }
        .error {
            color: red;
            margin-top: 10px;
            padding: 10px;
            background-color: #ffeeee;
            border: 1px solid #ffcccc;
        }
        .success {
            color: green;
            margin-top: 10px;
            padding: 10px;
            background-color: #eeffee;
            border: 1px solid #ccffcc;
        }
        table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 20px;
        }
        th, td {
            border: 1px solid #ddd;
            padding: 8px;
            text-align: left;
        }
        th {
            background-color: #f2f2f2;
        }
        .remove-btn {
            background-color: #f44336;
            color: white;
            padding: 5px 10px;
            border: none;
            border-radius: 4px;
            cursor: pointer;
        }
        .remove-btn:hover {
            background-color: #d32f2f;
        }
        .cantidad-input {
            width: 60px;
            text-align: center;
        }
    </style>
</head>
<body>
    <div class="container">
        <h1>Gestión de Alquiler de Películas</h1>
        
        <form action="${pageContext.request.contextPath}/alquiler" method="POST" id="formAlquiler">
            <!-- Selección de Cliente -->
            <div class="form-group">
                <label for="cliente">Cliente:</label>
                <select id="cliente" name="cliente" required>
                    <option value="">Seleccione un cliente</option>
                    <c:forEach items="${clientes}" var="cliente">
                        <option value="${cliente.idCliente}">
                            ${cliente.nombre} - ${cliente.email}
                        </option>
                    </c:forEach>
                </select>
            </div>
            
            <!-- Selección de Película -->
            <div class="form-group">
                <label for="pelicula">Película:</label>
                <select id="pelicula">
                    <option value="">Seleccione una película</option>
                    <c:forEach items="${peliculas}" var="pelicula">
                        <option value="${pelicula.idPelicula}" 
                                data-precio="${pelicula.genero eq 'Nuevo' ? 5.00 : 3.00}"
                                data-stock="${pelicula.stock}">
                            ${pelicula.titulo} - ${pelicula.genero} (Stock: ${pelicula.stock})
                        </option>
                    </c:forEach>
                </select>
                <button type="button" onclick="agregarPelicula()">Agregar Película</button>
            </div>
            
            <!-- Tabla de Películas Seleccionadas -->
            <div class="form-group">
                <table id="peliculasSeleccionadas">
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Película</th>
                            <th>Precio</th>
                            <th>Cantidad</th>
                            <th>Subtotal</th>
                            <th>Acción</th>
                        </tr>
                    </thead>
                    <tbody>
                        <!-- Las películas agregadas aparecerán aquí -->
                    </tbody>
                </table>
            </div>
            
            <!-- Total y Estado -->
            <div class="form-group">
                <label for="total">Total:</label>
                <input type="text" id="total" name="total" readonly value="0.00">
            </div>
            
            <div class="form-group">
                <label for="estado">Estado del Alquiler:</label>
                <select id="estado" name="estado" required>
                    <option value="">-- Seleccione estado --</option>
                    <c:forEach items="${estadosAlquiler}" var="estado">
                        <option value="${estado.name()}">${estado}</option>
                    </c:forEach>
                </select>
            </div>
            
            <button type="submit">Registrar Alquiler</button>
            
            <c:if test="${not empty error}">
                <div class="error">${error}</div>
            </c:if>
            
            <c:if test="${not empty success}">
                <div class="success">${success}</div>
            </c:if>
        </form>
    </div>

    <script>
        // Función para agregar películas al formulario
        function agregarPelicula() {
            const select = document.getElementById('pelicula');
            const selectedOption = select.options[select.selectedIndex];
            
            if (!selectedOption || !selectedOption.value) {
                alert('Por favor seleccione una película válida');
                return;
            }
            
            const peliculaId = selectedOption.value;
            const peliculaTexto = selectedOption.text.split(' (Stock:')[0].trim();
            const precio = parseFloat(selectedOption.getAttribute('data-precio'));
            const stock = parseInt(selectedOption.getAttribute('data-stock'));
            
            // Verificar si ya existe
            const existe = Array.from(document.querySelectorAll('input[name="peliculaId"]'))
                .some(input => input.value === peliculaId);
            
            if (existe) {
                alert('Esta película ya fue agregada');
                return;
            }
            
            if (stock <= 0) {
                alert('No hay stock disponible para esta película');
                return;
            }
            
             const tbody = document.querySelector('#peliculasSeleccionadas tbody');
            const tr = document.createElement('tr');
            tr.dataset.peliculaId = peliculaId;
            
            // Crear elementos individualmente para evitar problemas con innerHTML
            const tdId = document.createElement('td');
            tdId.textContent = peliculaId;
            tr.appendChild(tdId);
            
            const tdTexto = document.createElement('td');
            tdTexto.textContent = peliculaTexto;
            tr.appendChild(tdTexto);
            
            const tdPrecio = document.createElement('td');
            tdPrecio.textContent = precio.toFixed(2);
            tr.appendChild(tdPrecio);
            
            const tdCantidad = document.createElement('td');
            const inputCantidad = document.createElement('input');
            inputCantidad.type = 'number';
            inputCantidad.name = 'cantidad';
            inputCantidad.value = '1';
            inputCantidad.min = '1';
            inputCantidad.max = stock;
            inputCantidad.className = 'cantidad-input';
            inputCantidad.addEventListener('change', calcularTotal);
            inputCantidad.required = true;
            tdCantidad.appendChild(inputCantidad);
            tr.appendChild(tdCantidad);
            
            const tdSubtotal = document.createElement('td');
            tdSubtotal.setAttribute('data-subtotal', '');
            tdSubtotal.textContent = precio.toFixed(2);
            tr.appendChild(tdSubtotal);
            
            const tdAccion = document.createElement('td');
            const btnEliminar = document.createElement('button');
            btnEliminar.type = 'button';
            btnEliminar.className = 'remove-btn';
            btnEliminar.textContent = 'Eliminar';
            btnEliminar.addEventListener('click', function() {
                tr.remove();
                calcularTotal();
            });
            tdAccion.appendChild(btnEliminar);
            tr.appendChild(tdAccion);
            
            // Campo oculto para el ID de la película
            const hiddenInput = document.createElement('input');
            hiddenInput.type = 'hidden';
            hiddenInput.name = 'peliculaId';
            hiddenInput.value = peliculaId;
            tr.appendChild(hiddenInput);
            
            tbody.appendChild(tr);
            calcularTotal();
            select.selectedIndex = 0;
        }
        
        // Función para calcular el total
        function calcularTotal() {
            const rows = document.querySelectorAll('#peliculasSeleccionadas tbody tr');
            let total = 0;
            
            rows.forEach(row => {
                const precio = parseFloat(row.querySelector('td:nth-child(3)').textContent);
                const cantidad = parseInt(row.querySelector('input[name="cantidad"]').value) || 0;
                const subtotal = precio * cantidad;
                
                row.querySelector('td[data-subtotal]').textContent = subtotal.toFixed(2);
                total += subtotal;
            });
            
            document.getElementById('total').value = total.toFixed(2);
        }
        
        // Validación antes de enviar
        document.getElementById('formAlquiler').addEventListener('submit', function(e) {
            const cliente = document.getElementById('cliente').value;
            const estado = document.getElementById('estado').value;
            const peliculas = document.querySelectorAll('input[name="peliculaId"]');
            
            if (!cliente) {
                alert('Debe seleccionar un cliente');
                e.preventDefault();
                return;
            }
            
            if (peliculas.length === 0) {
                alert('Debe agregar al menos una película');
                e.preventDefault();
                return;
            }
            
            if (!estado) {
                alert('Debe seleccionar un estado para el alquiler');
                e.preventDefault();
                return;
            }
            
            // Validar cantidades
            let valid = true;
            document.querySelectorAll('input[name="cantidad"]').forEach(input => {
                if (input.value <= 0 || isNaN(input.value)) {
                    alert('La cantidad debe ser mayor a 0 para todas las películas');
                    valid = false;
                }
            });
            
            if (!valid) {
                e.preventDefault();
            }
        });
    </script>
</body>
</html>