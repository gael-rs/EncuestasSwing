# Librerías del Proyecto

Esta carpeta contiene las librerías JAR necesarias para el proyecto.

## JARs Requeridos

### 1. H2 Database (OBLIGATORIO)

**Archivo**: `h2-2.4.240.jar` (o versión más reciente)

**Descarga**:
- Opción 1: https://repo1.maven.org/maven2/com/h2database/h2/2.4.240/h2-2.4.240.jar
- Opción 2: https://www.h2database.com/html/download.html

**Descripción**: Base de datos embebida H2 para persistencia de datos.

### 2. JFreeChart (OPCIONAL - Para gráficos de torta)

**Archivos**:
- `jfreechart-1.5.6.jar` (o versión más reciente)
- `jcommon-1.0.24.jar` (dependencia requerida)

**Descarga**:
- JFreeChart:
  - Opción 1: https://repo1.maven.org/maven2/org/jfree/jfreechart/1.5.6/jfreechart-1.5.6.jar
  - Opción 2: https://github.com/jfree/jfreechart/releases
- JCommon:
  - https://repo1.maven.org/maven2/org/jfree/jcommon/1.0.24/jcommon-1.0.24.jar

**Descripción**: Librería para crear gráficos de torta en el Dashboard.

## Instrucciones

### Paso 1: Descargar los JARs

1. Descarga `h2-2.4.240.jar` (obligatorio)
2. Descarga `jfreechart-1.5.6.jar` (opcional)
3. Descarga `jcommon-1.0.24.jar` (opcional - solo si usas JFreeChart)
4. Coloca todos los archivos en esta carpeta `/lib`

### Paso 2: Configurar en IntelliJ IDEA

**Opción A: Agregar todos los JARs juntos (Recomendado)**
1. Abre el proyecto en IntelliJ IDEA
2. Ve a **File > Project Structure** (Ctrl+Alt+Shift+S)
3. Selecciona **Libraries** en el panel izquierdo
4. Haz clic en el botón **+** y selecciona **Java**
5. Navega hasta la carpeta `lib/` del proyecto
6. Selecciona **todos** los archivos JAR que descargaste:
   - `h2-2.4.240.jar` (obligatorio)
   - `jfreechart-1.5.6.jar` (opcional)
   - `jcommon-1.0.24.jar` (opcional)
7. Haz clic en **OK**
8. Haz clic en **Apply** y **OK**

**Opción B: Agregar uno por uno**
1. Repite los pasos 2-5 para cada JAR individualmente
2. Asegúrate de agregar tanto JFreeChart como JCommon si quieres usar gráficos

### Paso 3: Habilitar gráficos (solo si instalaste JFreeChart)

Si descargaste JFreeChart:
1. Abre el archivo `src/com/encuestas/ui/GraficosTortaDialog.java`
2. Descomenta las líneas indicadas con `// DESCOMENTA EL SIGUIENTE CODIGO`
3. Guarda el archivo
4. Recompila el proyecto

## Estructura Esperada

```
lib/
├── README.md                 (este archivo)
├── .gitkeep                 (para Git)
├── h2-2.4.240.jar           (descargar - obligatorio)
├── jfreechart-1.5.6.jar     (descargar - opcional)
└── jcommon-1.0.24.jar       (descargar - opcional, solo con JFreeChart)
```

## Notas

- Los archivos JAR NO están incluidos en el repositorio por cuestiones de tamaño
- Debes descargarlos manualmente siguiendo los enlaces arriba
- H2 es obligatorio para que la aplicación funcione
- JFreeChart es opcional, la aplicación funciona sin él (solo no mostrará gráficos)
- Si cambias de versión de los JARs, actualiza la configuración en IntelliJ

## Verificación

Para verificar que las librerías están correctamente configuradas:
1. Ejecuta la clase `Main.java`
2. Si H2 está configurado: la aplicación iniciará correctamente
3. Si JFreeChart está configurado: los gráficos de torta se mostrarán en el Dashboard
