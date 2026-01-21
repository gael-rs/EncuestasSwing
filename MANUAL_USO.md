# Manual de Uso - Sistema de Administración de Encuestas

## Inicio de la Aplicación

1. **Ejecutar la aplicación**
   - En IntelliJ IDEA, abre el proyecto
   - Ejecuta la clase `Main.java`
   - Se abrirá la ventana de Login

## Flujo de Trabajo Completo

### Parte 1: Como Administrador

#### 1. Login como Administrador

- En la ventana de login, selecciona el usuario **"Admin"**
- Haz clic en **"Ingresar"**
- Se abrirá el Panel de Administrador

#### 2. Crear una Nueva Encuesta

- Haz clic en el botón **"Nueva Encuesta"**
- Completa los campos:
  - **Título**: Por ejemplo, "Encuesta de Satisfacción del Curso"
  - **Estado**: Marca "Activa" si quieres que esté disponible inmediatamente
- Haz clic en **OK**
- La encuesta aparecerá en la tabla principal

#### 3. Agregar Preguntas a la Encuesta

- Selecciona la encuesta recién creada en la tabla
- Haz clic en **"Gestionar Preguntas"**

##### Agregar Pregunta con Opciones Numéricas:

- Haz clic en **"Nueva Pregunta"**
- Completa los campos:
  - **Texto**: Por ejemplo, "¿Qué tan satisfecho está con el curso?"
  - **Tipo**: NO marques "Es respuesta de texto libre"
  - **Valor Mínimo**: 1
  - **Valor Máximo**: 5
- Haz clic en **OK**
- La pregunta se creará con opciones del 1 al 5

##### Agregar Pregunta de Texto Libre:

- Haz clic en **"Nueva Pregunta"**
- Completa los campos:
  - **Texto**: Por ejemplo, "¿Qué sugerencias tiene para mejorar?"
  - **Tipo**: MARCA "Es respuesta de texto libre"
  - Los campos de Valor Mínimo y Máximo se deshabilitarán
- Haz clic en **OK**

#### 4. Personalizar Labels de Opciones (Opcional)

- En la ventana de Preguntas, selecciona una pregunta con opciones numéricas
- Haz clic en **"Gestionar Opciones"**
- Selecciona una opción (por ejemplo, "Opción 1")
- Haz clic en **"Editar Label"**
- Cambia el label (por ejemplo, de "Opción 1" a "Muy insatisfecho")
- Repite para todas las opciones:
  - Valor 1: "Muy insatisfecho"
  - Valor 2: "Insatisfecho"
  - Valor 3: "Neutral"
  - Valor 4: "Satisfecho"
  - Valor 5: "Muy satisfecho"
- Haz clic en **"Cerrar"**

#### 5. Crear Múltiples Preguntas

Repite el paso 3 para crear varias preguntas. Por ejemplo:
- "¿El contenido fue claro?" (opciones 1-5)
- "¿El instructor fue profesional?" (opciones 1-5)
- "¿Qué temas le parecieron más interesantes?" (texto libre)

#### 6. Cerrar Sesión

- Haz clic en **"Cerrar Sesión"** para volver al login

### Parte 2: Como Promotor (Responder Encuestas)

#### 1. Login como Promotor

- En la ventana de login, selecciona el usuario **"Promotor"**
- Haz clic en **"Ingresar"**
- Se abrirá el Panel de Promotor

#### 2. Iniciar una Encuesta

- Verás la lista de encuestas activas
- Selecciona la encuesta "Encuesta de Satisfacción del Curso"
- Haz clic en **"Iniciar Encuesta"**

#### 3. Responder las Preguntas

- Para preguntas con opciones:
  - Selecciona un radio button con tu respuesta
  - Por ejemplo, selecciona "Muy satisfecho" para la primera pregunta
- Para preguntas de texto libre:
  - Escribe tu respuesta en el campo de texto
  - Por ejemplo: "Me gustaría más ejemplos prácticos"
- Completa TODAS las preguntas (es obligatorio)
- Haz clic en **"Guardar Respuestas"**
- Verás un mensaje de confirmación

#### 4. Responder Múltiples Veces

- Puedes volver a iniciar la misma encuesta
- Responde con diferentes valores para generar datos
- Esto simulará múltiples personas respondiendo
- Repite este proceso 5-10 veces para tener datos significativos

### Parte 3: Ver Resultados (Como Administrador)

#### 1. Login nuevamente como Admin

- Cierra sesión del Promotor
- Inicia sesión como **"Admin"**

#### 2. Ver Resumen Individual de una Encuesta

- Selecciona la encuesta en la tabla
- Haz clic en **"Ver Resumen"**
- Verás:
  - Total de respuestas recibidas
  - Para cada pregunta con opciones: cantidad de respuestas por valor
  - Las preguntas de texto libre se muestran como "Respuesta de texto libre"

##### Filtrar por Fechas:

- En el resumen, ingresa fechas en formato YYYY-MM-DD
  - **Fecha Inicio**: Por ejemplo, 2026-01-01
  - **Fecha Fin**: Por ejemplo, 2026-12-31
- Haz clic en **"Generar Resumen"**
- El resumen se actualizará con solo las respuestas en ese rango

#### 3. Ver Dashboard de Últimas 4 Encuestas

- En el Panel de Administrador, haz clic en **"Dashboard"**
- Verás un resumen visual de las últimas 4 encuestas creadas
- Para cada encuesta:
  - Título y fecha de creación
  - Total de respuestas
  - Para cada pregunta con opciones:
    - Cantidad de respuestas por valor
    - Porcentaje de cada valor
    - Barra de progreso visual mostrando el porcentaje

#### 4. Ver Gráficos de Torta (Bonus - Requiere JFreeChart)

- En la ventana del Dashboard, haz clic en **"Ver Gráficos de Torta"**
- Si JFreeChart está instalado:
  - Verás gráficos de torta para cada pregunta con opciones
  - Cada sector mostrará el porcentaje de respuestas
- Si JFreeChart NO está instalado:
  - Verás un mensaje indicando que se requiere JFreeChart
  - Consulta el archivo **JFREECHART_SETUP.md** para instrucciones de instalación

### Parte 4: Gestión de Encuestas

#### Editar una Encuesta

- Selecciona una encuesta en la tabla
- Haz clic en **"Editar Encuesta"**
- Modifica el título o el estado (Activa/Inactiva)
- Haz clic en **OK**

#### Editar Preguntas

- Selecciona una encuesta
- Haz clic en **"Gestionar Preguntas"**
- Selecciona una pregunta
- Haz clic en **"Editar"**
- Modifica el texto o los valores mínimo/máximo
- Haz clic en **OK**

#### Eliminar Pregunta

- En la ventana de Preguntas, selecciona una pregunta
- Haz clic en **"Eliminar"**
- Confirma la eliminación
- Nota: Se eliminarán también todas las respuestas asociadas

#### Eliminar Encuesta

- Selecciona una encuesta en el Panel de Administrador
- Haz clic en **"Eliminar"**
- Confirma la eliminación
- Nota: Se eliminarán la encuesta, todas sus preguntas y todas las respuestas

## Resumen de Funcionalidades Implementadas

### Funcionalidades Básicas
- ✅ Login por rol (Admin/Promotor)
- ✅ ABM de encuestas
- ✅ Gestión de preguntas con escalas variables (1-3, 1-5, etc.)
- ✅ Pantalla para responder encuestas completas
- ✅ Registro de respuestas en base de datos
- ✅ Resumen por encuesta con conteo de respuestas
- ✅ Filtrado de resumen por rango de fechas

### Funcionalidades Adicionales
- ✅ Escalas variables por pregunta (1-3, 1-4, 1-5, etc.)
- ✅ Labels personalizables por opción
- ✅ **Respuestas de texto libre**

### Bonus Points
- ✅ **Dashboard con últimas 4 encuestas con porcentajes**
- ✅ **Gráficos de torta con JFreeChart** (requiere instalación de librería)

## Estructura de la Base de Datos

La base de datos H2 se crea automáticamente en:
- **Ubicación**: `./data/encuestas.mv.db`
- **Tablas**: Usuario, Encuesta, Pregunta, OpcionRespuesta, Respuesta
- **Usuarios precargados**: Admin (ADMIN), Promotor (PROMOTOR)

## Solución de Problemas

### Error: "Driver H2 no encontrado"
- Verifica que el JAR de H2 esté agregado a las librerías del proyecto
- Ve a File > Project Structure > Libraries
- Asegúrate de que h2-2.4.240.jar (o similar) esté presente

### No se muestran los gráficos de torta
- Los gráficos requieren JFreeChart
- Sigue las instrucciones en **JFREECHART_SETUP.md**
- Descarga e instala la librería JFreeChart
- Descomenta el código en `GraficosTortaDialog.java`

### No aparecen encuestas en el Panel de Promotor
- Verifica que la encuesta esté marcada como "Activa"
- Como Admin, edita la encuesta y marca "Activa"

### Error al responder encuesta
- Asegúrate de responder TODAS las preguntas
- Para preguntas con opciones: selecciona un radio button
- Para preguntas de texto libre: escribe algo en el campo

## Notas Importantes

1. La base de datos se crea automáticamente al iniciar la aplicación
2. Los datos persisten entre ejecuciones
3. Para reiniciar la base de datos, elimina la carpeta `data/`
4. Los usuarios Admin y Promotor se crean automáticamente
5. Las respuestas incluyen fecha y hora automáticamente
6. El sistema cumple estrictamente con las 5 entidades requeridas

## Arquitectura del Sistema

### Paquetes
- **com.encuestas.dominio**: 5 entidades (Usuario, Encuesta, Pregunta, OpcionRespuesta, Respuesta)
- **com.encuestas.dao**: 5 DAOs con CRUD básico
- **com.encuestas.util**: Conexión a base de datos H2
- **com.encuestas.ui**: Interfaces gráficas Swing

### Principios Aplicados
- Bajo acoplamiento
- Alta cohesión
- Patrón DAO
- Separación de capas (Dominio, Persistencia, UI)
