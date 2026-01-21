# Sistema de Administración de Encuestas
(Java + Swing + H2 embebida + DAO)

## Contexto
Este proyecto es un trabajo práctico académico.  
El objetivo es cumplir **exactamente** los requerimientos solicitados, sin agregar funcionalidades extra ni complejidad innecesaria.

El énfasis está en:
- Diseño orientado a objetos
- Bajo acoplamiento
- Alta cohesión
- Correcto uso del patrón DAO
- Separación clara entre dominio, persistencia y UI

NO se evalúa SQL avanzado ni diseño complejo de base de datos.

---

## Tecnologías
- Lenguaje: Java
- UI: Swing
- Base de datos: H2 embebida (JAR ya agregado al proyecto)
- Persistencia: JDBC
- Patrón: DAO
- Build system: IntelliJ (NO Maven, NO Gradle)

---

## Estructura de paquetes (ya creada)

com.encuestas.dominio
com.encuestas.dao
com.encuestas.ui
com.encuestas.util


---

## Restricciones obligatorias
- El sistema debe tener **exactamente 5 entidades**.
- No agregar entidades extra.
- No usar frameworks externos.
- No agregar dashboards ni gráficos.
- Cumplir únicamente la funcionalidad solicitada.

---

## Entidades del sistema (EXACTAMENTE 5)

### 1. Usuario
Responsabilidad: representar un usuario del sistema y su rol.
- id
- nombre
- rol (ADMIN, PROMOTOR)

---

### 2. Encuesta
Responsabilidad: representar una encuesta.
- id
- titulo
- fechaCreacion
- activa

---

### 3. Pregunta
Responsabilidad: representar una pregunta dentro de una encuesta.
- id
- texto
- valorMin
- valorMax
- encuestaId

---

### 4. OpcionRespuesta
Responsabilidad: definir el label de cada valor de una pregunta.
- id
- preguntaId
- valor
- label

---

### 5. Respuesta
Responsabilidad: registrar una respuesta del usuario.
- id
- encuestaId
- preguntaId
- valorSeleccionado
- fechaRespuesta

---

## Funcionalidad básica obligatoria

- Administrar usuarios por rol.
- Administrar encuestas.
- Cada encuesta tiene múltiples preguntas.
- Cada pregunta tiene opciones numéricas (por defecto 1–5).
- Permitir responder una encuesta completa desde una UI Swing.
- Registrar respuestas en la base de datos.
- Mostrar resumen por encuesta:
    - cantidad de respuestas por valor
    - filtrado por rango de fechas

---

## Funcionalidades adicionales permitidas
(sin excederse)

- Escalas variables por pregunta (1–3, 1–4, etc.).
- Labels personalizables por opción.
- No incluir gráficos ni dashboards.

---

## Patrón DAO (obligatorio)

- Un DAO por entidad:
    - UsuarioDAO
    - EncuestaDAO
    - PreguntaDAO
    - OpcionRespuestaDAO
    - RespuestaDAO

- Cada DAO debe encargarse solo de:
    - crear
    - buscar
    - listar
    - eliminar (si aplica)

- No incluir lógica de negocio en los DAO.

---

## Base de datos

- Usar H2 embebida.
- Crear una clase de conexión en `com.encuestas.util`.
- Crear tablas solo con los campos necesarios.
- Mantener SQL simple.

---

## Interfaz gráfica (Swing)

Pantallas mínimas:
1. Login simple (selección de usuario y rol).
2. Pantalla Administrador:
    - ABM de encuestas
    - Ver resumen de encuesta con filtro por fechas
3. Pantalla Promotor:
    - Seleccionar encuesta activa
    - Iniciar encuesta
4. Pantalla Responder Encuesta:
    - Mostrar preguntas
    - Mostrar opciones con radio buttons
    - Guardar respuestas

---

## Primera entrega (Segundo Parcial)

- Implementar CRUD completo de la entidad **Encuesta**.
- Incluir:
    - DAO
    - manejo de excepciones
    - pantallas Swing
- No es necesario que el sistema esté completo en esta etapa.

---

## Objetivo final
Generar un sistema simple, claro, bien diseñado, defendible académicamente y que cumpla estrictamente con los requerimientos del trabajo práctico.
