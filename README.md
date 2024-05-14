![logo_ironhack_blue 7](https://user-images.githubusercontent.com/23629340/40541063-a07a0a8a-601a-11e8-91b5-2f13e4e6b441.png)

# Final Project | Hospital-AU

## Project Description

Este proyecto consiste en la creación de una Aplicación Backend con Java y Spring Boot, destinada a ser utilizada como aplicación para el sistema de gestión de un hospital. Integra la implementación de una correspondiente API REST, con la configuración de múltiples rutas que permiten añadir, actualizar y borrar información a la base de datos conectada.
La aplicación contempla múltiples tipos de entidades, siendo las principales los pacientes y los médicos. Ambos comparten algunos atributos comunes como el nombre completo o el número de teléfono, y toman como identificador único un id generado con la siguiente lógica


- Los identificadores de médicos y pacientes se componen de las letras iniciales del nombre y el apellido (extraídos ambos de la variable fullName, a la que se fuerza tener, al menos, dos palabras), seguidas de un número que permite diferenciar entre distintos nombres con las mismas iniciales. Por ejemplo, a una primera paciente o doctora Laura Roman se le asignaría el id LR1. Una vez guardada en el repositorio y bbdd, al añadir por ejemplo Leo Ramirez se le asociaría el id LR2.

El atributo más importante de cada doctor es la especialidad, que consiste en otro tipo de objeto, entidades a las cuales también se les asigna un código de manera similar a pacientes y doctores. Además de este tipo de lógicas y validaciones de valores de atributos, la principal funcionalidad de la app es la gestión de citas.
La lógica seguida para programar una cita a un paciente depende de si dicho paciente ha tenido ya revisiones médicas anteriores o no. En caso negativo, al paciente se le asigna una revisión en la fecha deseada con un doctor de la especialidad Medicina General. En caso contrario, si el paciente ha tenido citas médicas anteriores, para asignar la siguiente cita se debe consultar la última para verificar si el paciente ha sido derivado a otra especialidad o no, con tal de asignar un médico con la especialidad de la derivación (o en caso contrario continuar en Medicina General).


## Class Diagram

Diagrama de clases con sus atributos, métodos y relaciones entre ellas adjunto como archivo.
![alt text](<Hospital AU - UML Class Diagram-1.png>)

## Setup

Para poder inicializar la aplicación es necesario tener el puerto 8080 (http://localhost:8080) libre de cualquier otra ejecución. También es necesario crear la base de datos llamada "hospital" en MySQL, así como configurar la conexión a la base de datos en el archivo application.properties, especificando usuario y contraseña (asegurando que dicho usuario tenga acceso a la bbdd creada).

Con tal de acceder a los endpoints para hacer peticiones a la API es necesario iniciar sesión con un usuario existente mediante una llamada de tipo GET a la API, con tal de obtener un token de autenticación. Esto debe hacerse a través del endpoint http://localhost:8080/api/login al cual tiene acceso cualquier usuario.
- Ejemplo de inicio de sesión: http://localhost:8080/api/login?username=john&password=1234

Una vez hecha la llamada a la API, se recibe un token de autenticación como respuesta si el login es correcto. Este token debe introducirse en la cabecera de las peticiones al resto de endpoints, para que pueda verificarse si el usuario que ha hecho login tiene el rol necesario que le permita realizar la acción específica de cada endpoint.


## Technologies used

Las tecnologías usadas para este proyecto han sido Java como lenguaje de programación, haciendo uso de IntelliJ como IDE.

Se ha usado **Maven** como gestor de paquetes para inyectar las dependencias, bajadas de la nube, necesarias para realizar y compilar el proyecto, o para facilitar la codificación. Por ejemplo, se ha usado la librería Lombok para reducir la cantidad de código boilerplate y agilizar código repetitivo.

También se ha usado el conjunto de librerías de **Spring Boot** que configura el framework de Spring y así permite establecer la estructura del proyecto por capas (modelo, controlador, repositorio, servicio). Este contexto de Spring facilita la creación de la aplicación mediante la gestión de, por ejemplo, la conexión y las consultas a la base de datos a través de peticiones a los controladores construidos.
En este caso se utiliza **MySQL** como el sistema de gestión de bases de datos relacional para almacenar y recuperar datos.

Por último, se ha usado JPA (Java Persistance API) para mapear de manera muy simple objetos Java a entidades de la base de datos relacional, sin tener que escribir consultas SQL directamente.



## Controllers and Routes structure

Descripción de las rutas de tipo POST, GET, PUT y DELETE:



### Endpoints de tipo POST

1. **Añadir una nueva especialidad**

* Ruta: /specialty
* Descripción: Permite añadir una nueva especialidad introduciendo el nombre de la especialidad en el cuerpo de la solicitud. Devuelve un código de estado HTTP 201 (CREATED) si se añade correctamente, o un código de estado HTTP 409 (CONFLICT) si una especialidad con el nombre dado ya existe.

2. **Añadir un nuevo paciente**

* Ruta: /patient
* Descripción: Permite añadir un nuevo paciente proporcionando la información del paciente en el cuerpo de la solicitud en formato JSON. Retorna un código de estado HTTP 200 (OK) si se añade correctamente.
* Ejemplo body request:
```json
{
  "fullName" : "Andrea Contreras",
  "address" : {
    "streetAddress": "street Doctor Bergos 54",
    "city": "Barcelona",
    "postalCode" : "08932"
  },
  "phone" : "633569042",
  "email": "aksf@gmail.com",
  "bloodType": "A"
}
```

3. **Añadir un nuevo médico**

* Ruta: /doctor
* Descripción: Permite añadir un nuevo médico proporcionando la información del médico en el cuerpo de la solicitud en formato JSON. Retorna un código de estado HTTP 200 (OK) si se añade correctamente.
* Ejemplo body request:
```json
{
  "fullName" : "Andrea Pacheco",
  "address" : {
    "streetAddress": "street Doctor Bergos 54",
    "city": "Barcelona",
    "postalCode" : "08290"
  },
  "phone" : "655894673",
  "email": "aksf@gmail.com",
  "specialty" : {
    "code": "MG1",
    "name": "Medicina General"
  }
}
```

4. **Añadir un nuevo medicamento**

* Ruta: /medicine
* Descripción: Permite añadir un nuevo medicamento proporcionando el nombre del medicamento en el cuerpo de la solicitud en formato JSON. Retorna un código de estado HTTP 200 (OK) si se añade correctamente.

5. **Añadir una nueva cita médica**

* Ruta: /appointment/{patientId}
* Descripción: Permite añadir una nueva cita médica asociada a un paciente específico, proporcionando el ID del paciente en el camino de la URL y la fecha de la cita en el cuerpo de la solicitud. Retorna un código de estado HTTP 400 (BAD SYNTAX) si el formato de la fecha no es el correcto (dd/MM/yyyy) o si la fecha es anterior a la actual, o un HTTP 200 (OK) si se añade correctamente.
* Ejemplo body request:
```json
07/09/2024
```

6. **Guardar un nuevo usuario**

* Ruta: /api/users
* Descripción: Permite guardar un nuevo usuario en el sistema, proporcionando el objeto JSON representando el usuario a guardar. Devuelve un código de estado 201 (CREATED).

7. **Guardar un nuevo rol**

* Ruta: /api/roles
* Descripción: Permite guardar un nuevo rol en el sistema, proporcionando el objeto JSON representando el rol a guardar. Devuelve un código de estado 201 (CREATED).

8. **Añadir un rol a un usuario**
* Ruta: /api/roles/addtouser
* Descripción: Permite añadir un rol específico a un usuario existente en el sistema, proporcionando el DTO (Objeto de Transferencia de Datos) que contiene el nombre de usuario y el nombre del rol a añadir. Devuelve un código de estado 204 (NO CONTENT).

### Endpoints de tipo GET

9. **Obtener una especialidad por código**

* Ruta: /specialty/{code}
* Descripción: Permite obtener la especialidad buscada proporcionando el código de la especialidad en el camino de la URL. Retorna un código de estado HTTP 404 (NOT FOUND) si no se encuentra la especialidad con el código proporcionado, y un código de estado HTTP 200 (OK) si la operación se realiza correctamente.

10. **Obtener un medicamento por ID**

* Ruta: /medicine/{id}
* Descripción: Permite obtener el medicamento buscado proporcionando el ID del medicamento en el camino de la URL. Retorna un código de estado HTTP 404 (NOT FOUND) si no se encuentra el medicamento con el ID proporcionado, y un código de estado HTTP 200 (OK) si la operación se realiza correctamente.

11. **Obtener todas las especialidades**

* Ruta: /specialties
* Descripción: Permite obtener todas las especialidades disponibles en el sistema. Retorna un código de estado HTTP 200 (OK) si la operación se realiza correctamente.

12. **Obtener todos los medicamentos**

* Ruta: /medicines
* Descripción: Permite obtener todos los medicamentos disponibles en el sistema. Retorna un código de estado HTTP 200 (OK) si la operación se realiza correctamente.

13. **Obtener todos los pacientes**

* Ruta: /patients
* Descripción: Permite obtener todos los pacientes registrados en el sistema. Retorna un código de estado HTTP 200 (OK) si la operación se realiza correctamente.

14. **Obtener todos los médicos**

* Ruta: /doctors
* Descripción: Permite obtener todos los médicos registrados en el sistema. Retorna un código de estado HTTP 200 (OK) si la operación se realiza correctamente.

15. **Obtener médicos por especialidad**

* Ruta: /doctors/specialty/{code}
* Descripción: Permite obtener todos los médicos que pertenecen a una especialidad específica, proporcionando el código de la especialidad en el camino de la URL. Retorna un código de estado HTTP 404 (NOT FOUND) si la especialidad con el código dado no existe o si no hay médicos con la especialidad encontrada, o un HTTP 200 (OK) si la operación se realiza correctamente.

16. **Obtener citas médicas por ID de médico**

* Ruta: /appointments/{doctorId}
* Esta es la única ruta que requiere autenticación como usuario con rol de tipo doctor.
* Descripción: Permite obtener todas las citas médicas asociadas a un médico específico, proporcionando el ID del médico en el camino de la URL. También se puede proporcionar un parámetro de consulta adicional para filtrar por fecha. Retorna un código de estado HTTP 403 (FORBIDDEN) si el usuario no tiene permisos para acceder a la ruta (si no tiene rol de tipo doctor), 404 (NOT FOUND) si el doctor con el id especificado no existe o si no tiene citas médicas asociadas, HTTP 200 (OK) si la operación se realiza correctamente.

17. **Obtener usuarios**

* Ruta: /api/users
* Descripción: Permite obtener todos los usuarios registrados en el sistema. Retorna un código de estado HTTP 200 (OK) si la operación se realiza correctamente.

###  Endpoints de tipo PUT

18. **Actualizar una especialidad por código**

* Ruta: /specialty/{code}
* Descripción: Permite actualizar el nombre de la especialidad correspondiente al código proporcionado en el camino de la URL, proporcionando el nuevo nombre de la especialidad en el cuerpo de la solicitud. Retorna un código de estado HTTP 404 (NOT FOUND) si no se encuentra la especialidad con el código proporcionado, y un código de estado HTTP 200 (OK) si la operación se realiza correctamente.

19. **Actualizar un medicamento por ID**

* Ruta: /medicine/{id}
* Descripción: Permite actualizar el nombre de un medicamento proporcionando el ID del medicamento en el camino de la URL y el nuevo nombre en el cuerpo de la solicitud. Retorna un código de estado HTTP 404 (NOT FOUND) si no se encuentra el medicamento con el ID proporcionado, y un código de estado HTTP 200 (OK) si la operación se realiza correctamente.

### Endpoints de tipo DELETE

20. **Eliminar un paciente por ID**

* Ruta: /patient/{id}
* Descripción: Permite eliminar un paciente proporcionando el ID del paciente en el camino de la URL. Retorna un código de estado HTTP 200 (OK) si se elimina correctamente.

21. **Eliminar un médico por ID**

* Ruta: /doctor/{id}
* Descripción: Permite eliminar un médico proporcionando el ID del médico en el camino de la URL. Retorna un código de estado HTTP 200 (OK) si se elimina correctamente.

22. **Eliminar un usuario por ID**

* Ruta: /api/users/{id}
* Descripción: Permite eliminar un usuario proporcionando el ID del usuario en el camino de la URL. Retorna un código de estado HTTP 200 (OK) si se elimina correctamente.



## Extra links
- Google Slides Presentation: https://docs.google.com/presentation/d/1MH3PY4GE_angRsA9AcBq8tqWXCnlIYHfjJmd2qaEhTo/edit#slide=id.g2dc23ea6569_2_281
- Task management app: https://lausr.planio.com/projects/website-redesign/issues


## Future work

Este proyecto ha permitido crear una primera versión inicial, simple pero funcional, del sistema de gestión de un hospital. Las funcionalidades implementadas son básicas y pretenden ser mejoradas y ampliadas. El futuro paso imprescindible a seguir para completar el proyecto, que no ha podido ser completado por limitaciones de tiempo, sería el siguiente:
* Implementar el feature de realizar una revisión médica. Crear métodos con la lógica de la realización de citas por parte de médicos a sus pacientes, y gestionar estas citas a través de endpoints, contemplando ambos casos: que el paciente sea derivado, o no.

* Relacionado con esto, la intención era también implementar una lógica respecto a los medicamentos que puedan recetar los médicos a sus pacientes, comprobando que dicho medicamento sea "compatible" en cada caso con el paciente en cuestión (mediante el uso del atributo de Grupo Sanguíneo del paciente, que debía también ser integrado como atributo en la clase Medicine para indicar con que grupos es compatible cada medicamento).

A parte de esto, muchas otras funcionalidades podrían ser añadidas, como por ejemplo:
* Introducción de una nueva clase Enfermera que represente otro rol con sus correspondientes permisos para realizar determinadas acciones (llamadas a la API), como por ejemplo la gestión de citas, sin poder realizar revisiones (esto solo podría ejecutarlo un médico).
* Tener en cuenta que los diagnósticos solo pueden ser vistos por los médicos, y no por ningún otro tipo de rol de usuario (ni administradores, ni enfermeras...).
* Modificar los permisos de acceso a información de un médico a las citas médicas. Actualmente cualquier médico puede ver las citas de cualquier médico, e idealmente solo debería poder ver las suyas.
* Añadir un nuevo método de consulta de datos para los usuarios con rol doctor para que puedan ver la información de la última cita de un paciente, para poder así realizar la cita actual con esa información.
* Gestión de pruebas diagnósticas como parte de la "receta" de un médico, a través de la creación de un nuevo tipo de cita médica.

La gestión de un hospital es muy extensa y por ello este proyecto plantea muchas oportunidades. Otro punto interesante para aplicar podría ser analizar de alguna forma las faltas o fallos de algún sistema actual en funcionamiento e intentar plantear la estructura de implementación como reto. Por último, también sería interesante continuar este proyecto creando una interfaz de usuario o integrando la app en una web programando con frontend.