# Sistema de Administración de Encuestas

Sistema completo de gestión de encuestas desarrollado con Java Swing y base de datos H2 embebida.

## Descripción

Aplicación de escritorio para crear, administrar y responder encuestas. Permite a los administradores crear encuestas con múltiples preguntas, configurar opciones personalizadas, y visualizar resultados con estadísticas y gráficos. Los promotores pueden presentar encuestas al público y registrar respuestas.

## Tecnologías

- **Lenguaje**: Java
- **UI**: Swing
- **Base de Datos**: H2 embebida
- **Persistencia**: JDBC con patrón DAO
- **Gráficos**: JFreeChart (opcional)

## Estructura del Proyecto

```
EncuestasSwing/
├── src/
│   ├── com/encuestas/
│   │   ├── dominio/          # 5 Entidades del sistema
│   │   │   ├── Usuario.java
│   │   │   ├── Encuesta.java
│   │   │   ├── Pregunta.java
│   │   │   ├── OpcionRespuesta.java
│   │   │   └── Respuesta.java
│   │   ├── dao/              # 5 DAOs con CRUD
│   │   │   ├── UsuarioDAO.java
│   │   │   ├── EncuestaDAO.java
│   │   │   ├── PreguntaDAO.java
│   │   │   ├── OpcionRespuestaDAO.java
│   │   │   └── RespuestaDAO.java
│   │   ├── ui/               # Interfaces gráficas
│   │   │   ├── LoginFrame.java
│   │   │   ├── AdminFrame.java
│   │   │   ├── PromotorFrame.java
│   │   │   ├── ResponderEncuestaFrame.java
│   │   │   ├── PreguntasDialog.java
│   │   │   ├── OpcionesDialog.java
│   │   │   ├── ResumenDialog.java
│   │   │   ├── DashboardFrame.java
│   │   │   └── GraficosTortaDialog.java
│   │   └── util/             # Utilidades
│   │       └── ConexionDB.java
│   └── Main.java
├── lib/                      # Librerías JAR (descargar manualmente)
│   ├── README.md            # Instrucciones para descargar JARs
│   ├── h2-2.4.240.jar       # H2 Database (descargar)
│   └── jfreechart-1.5.4.jar # JFreeChart (descargar - opcional)
├── data/                     # Base de datos H2 (generada automáticamente)
├── MANUAL_USO.md            # Manual completo de uso
├── JFREECHART_SETUP.md      # Instrucciones para JFreeChart
├── RULES.md                 # Reglas del proyecto
└── README.md
```

## Entidades del Sistema (5)

1. **Usuario**: Representa usuarios del sistema (Admin, Promotor)
2. **Encuesta**: Encuestas creadas por administradores
3. **Pregunta**: Preguntas dentro de una encuesta
4. **OpcionRespuesta**: Opciones con labels personalizables para cada pregunta
5. **Respuesta**: Respuestas registradas por usuarios

## Funcionalidades Implementadas

### Funcionalidades Básicas ✅
- Login por roles (Administrador / Promotor)
- CRUD completo de encuestas
- Gestión de preguntas con escalas variables (1-3, 1-5, etc.)
- Pantalla para responder encuestas completas
- Registro de respuestas en base de datos H2
- Resumen por encuesta con cantidad de respuestas por valor
- Filtrado de resumen por rango de fechas

### Funcionalidades Adicionales ✅
- Escalas variables por pregunta (independientes)
- Labels personalizables para cada opción
- **Respuestas de texto libre** (nuevo campo de texto)

### Bonus Points ✅
- **Dashboard con últimas 4 encuestas** mostrando:
  - Total de respuestas
  - Porcentajes por valor
  - Barras de progreso visuales
- **Gráficos de torta con JFreeChart**
  - Un gráfico por pregunta
  - Distribución de respuestas
  - Porcentajes visuales

## Cómo Ejecutar

### Requisitos

- JDK 8 o superior
- IntelliJ IDEA (recomendado)
- H2 Database JAR (descargar y colocar en `/lib`)
- JFreeChart JAR (opcional, para gráficos)

### Pasos

1. **Clonar el proyecto**
   ```bash
   git clone <url-del-repositorio>
   cd EncuestasSwing
   ```

2. **Descargar las librerías necesarias**

   **H2 Database (OBLIGATORIO)**:
   - Descargar desde: https://repo1.maven.org/maven2/com/h2database/h2/2.4.240/h2-2.4.240.jar
   - Guardar en la carpeta `lib/h2-2.4.240.jar`

   **JFreeChart (OPCIONAL - para gráficos)**:
   - Descargar desde: https://repo1.maven.org/maven2/org/jfree/jfreechart/1.5.4/jfreechart-1.5.4.jar
   - Guardar en la carpeta `lib/jfreechart-1.5.4.jar`

   Ver instrucciones detalladas en `lib/README.md`

3. **Abrir el proyecto en IntelliJ IDEA**
   - File > Open > Seleccionar la carpeta del proyecto

4. **Configurar las librerías en IntelliJ**
   - File > Project Structure > Libraries
   - Clic en **+** > Java
   - Navegar a la carpeta `lib/`
   - Seleccionar `h2-2.4.240.jar` > OK
   - Repetir para `jfreechart-1.5.4.jar` si lo descargaste
   - Apply > OK

5. **Habilitar gráficos (solo si descargaste JFreeChart)**
   - Abrir `src/com/encuestas/ui/GraficosTortaDialog.java`
   - Descomentar las líneas de código indicadas
   - Build > Rebuild Project

6. **Ejecutar la aplicación**
   - Ejecutar la clase `Main.java`
   - Se abrirá la ventana de Login

7. **Usuarios de prueba**
   - **Admin** (rol: ADMIN) - para administrar encuestas
   - **Promotor** (rol: PROMOTOR) - para responder encuestas

## Manual de Uso

Para instrucciones detalladas paso a paso de cómo usar el sistema, consulta **[MANUAL_USO.md](MANUAL_USO.md)**

El manual incluye:
- Flujo completo como Administrador
- Flujo completo como Promotor
- Creación de encuestas
- Gestión de preguntas (opciones y texto libre)
- Personalización de labels
- Responder encuestas
- Ver resultados y estadísticas
- Usar el Dashboard
- Ver gráficos de torta

## Arquitectura y Diseño

### Principios Aplicados
- **Bajo acoplamiento**: Cada componente es independiente
- **Alta cohesión**: Responsabilidades bien definidas
- **Patrón DAO**: Separación de acceso a datos
- **Separación de capas**: Dominio, Persistencia, UI

### Patrón DAO
Cada entidad tiene su propio DAO con operaciones:
- `crear()` - Insertar nuevos registros
- `buscarPorId()` - Buscar por ID
- `listarTodos()` - Listar todos los registros
- `actualizar()` - Modificar registros
- `eliminar()` - Eliminar registros

Los DAOs NO contienen lógica de negocio, solo operaciones de persistencia.

## Base de Datos

- **Motor**: H2 Database (embebida)
- **Ubicación**: `./data/encuestas.mv.db`
- **Modo**: Archivo local
- **Inicialización**: Automática al iniciar la aplicación

### Tablas
- `Usuario` - Usuarios del sistema
- `Encuesta` - Encuestas creadas
- `Pregunta` - Preguntas de encuestas
- `OpcionRespuesta` - Opciones con labels
- `Respuesta` - Respuestas registradas

### Datos Iniciales
Se crean automáticamente al inicio:
- Usuario Admin (id=1, rol=ADMIN)
- Usuario Promotor (id=2, rol=PROMOTOR)

## Capturas de Pantalla (Funcionalidades)

### Panel de Administrador
- Tabla de encuestas con estado (Activa/Inactiva)
- Botones: Nueva, Editar, Gestionar Preguntas, Ver Resumen, Dashboard, Eliminar

### Gestión de Preguntas
- Tabla de preguntas con tipo (Opciones/Texto Libre)
- Crear preguntas con escalas variables
- Checkbox para preguntas de texto libre
- Gestionar opciones y editar labels

### Responder Encuesta
- Preguntas con radio buttons (para opciones)
- Campos de texto (para texto libre)
- Validación de respuestas completas

### Dashboard
- Últimas 4 encuestas
- Total de respuestas
- Porcentajes por valor con barras de progreso
- Botón para ver gráficos

### Gráficos de Torta (con JFreeChart)
- Un gráfico por pregunta
- Sectores con porcentajes
- Labels personalizados

## Cumplimiento de Requerimientos

✅ Exactamente 5 entidades
✅ Patrón DAO implementado correctamente
✅ Separación de capas (dominio, DAO, UI)
✅ H2 embebida con creación automática de tablas
✅ UI Swing funcional y completa
✅ ABM de encuestas
✅ Gestión de preguntas
✅ Responder encuestas
✅ Resumen con filtro de fechas
✅ Escalas variables
✅ Labels personalizables
✅ **Respuestas de texto libre** (adicional)
✅ **Dashboard con porcentajes** (bonus)
✅ **Gráficos de torta** (bonus)

## Autor

Proyecto académico - Trabajo Práctico
Sistema de Administración de Encuestas con Java Swing

## Notas

- La base de datos se crea automáticamente en la primera ejecución
- Los datos persisten entre ejecuciones
- Para resetear, eliminar la carpeta `data/`
- JFreeChart es opcional pero recomendado para visualizar gráficos
- El sistema cumple estrictamente con las restricciones del proyecto (5 entidades, sin frameworks externos)
