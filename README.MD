## Microservicio retail desarrollado con Springboot

El microservicio tiene los siguientes endpoints, que se pueden probar en Swagger:

- GET /api/products/{id} -> Devuelve el producto con ese id
- PUT /api/products/{id} -> Para ajuste de descripcion, cambio de precio, cambio de stock
- DELETE /api/products/{id} -> Baja de producto
- GET /api/products -> Devuelve el catalogo de productos con opcion de filtro por categoria/rango de precio
- POST /api/products -> Agregar producto

- GET /api/orders -> Devuelve la lista de pedidos
- POST /api/orders -> Agregar pedido
- GET /api/orders/{id} -> Devuelve el pedido con ese id
- DELETE /api/orders/{id} -> Cancelar pedido
- PATCH /api/orders/{id} -> Actualizar estado de pedido

### Flujo asincrono
- El microservicio manda notificaciones en consola de confirmación de compra y del registro de cambios en el inventario(stock)

### Pruebas Swagger
- Para comenzar a probar los endpoints en Swagger se inicializo la
tabla de Products con datos usando el comando del archivo datos_para_probar.txt 
en la consola de h2
