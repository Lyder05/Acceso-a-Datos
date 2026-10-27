# Catálogo de Videojuegos - Unidad 2

Proyecto desarrollado en Kotlin para el módulo de Acceso a Datos de 2.º de Desarrollo de Aplicaciones Multiplataforma (DAM).

## 1. Descripción del proyecto

En este proyecto he desarrollado una aplicación de consola para gestionar un catálogo de videojuegos, utilizando diferentes tipos de ficheros para almacenar y recuperar la información.

A lo largo de las prácticas he trabajado con directorios, ficheros de texto, CSV, XML, JSON y ficheros binarios de acceso aleatorio.

La aplicación permite consultar, añadir, modificar y eliminar videojuegos, importar información desde diferentes formatos y realizar conversiones entre ellos.

## 2. Prácticas realizadas

### Práctica 1. Creación del proyecto

He creado y configurado el proyecto Kotlin utilizando Gradle en IntelliJ IDEA, preparando la estructura inicial para desarrollar las actividades de la unidad.

### Práctica 2. Gestión de ficheros y directorios

He trabajado con rutas y directorios utilizando las clases `Path` y `Files`.

También he utilizado métodos para comprobar la existencia de archivos, crear directorios y trabajar con las carpetas de entrada y salida.

### Práctica 3. CRUD con CSV

He desarrollado un menú de gestión de videojuegos utilizando un archivo CSV.

Las operaciones implementadas son:

- Listar todos los videojuegos.
- Buscar videojuegos por ID.
- Añadir nuevos videojuegos con un ID automático.
- Modificar la nota de un videojuego.
- Eliminar videojuegos con confirmación.

Los cambios se guardan en un fichero CSV para mantener la información después de cerrar el programa.

### Práctica 4. Lectura desde XML

He añadido la posibilidad de leer videojuegos desde un fichero XML utilizando Jackson XML.

Los videojuegos se convierten en objetos Kotlin y se incorporan al catálogo, evitando duplicar registros que tengan el mismo ID.

### Práctica 5. Lectura desde JSON

He implementado la lectura de videojuegos desde archivos JSON utilizando `kotlinx.serialization`.

Los datos se convierten en objetos de la clase `Videojuego` y se integran en el catálogo existente, comprobando que no se repitan los identificadores.

### Práctica 6. Conversiones entre formatos

He añadido un menú que permite convertir archivos entre CSV, XML y JSON.

Las conversiones disponibles son:

- CSV a JSON.
- CSV a XML.
- JSON a CSV.
- JSON a XML.
- XML a CSV.
- XML a JSON.

Para evitar repetir código, he utilizado una función genérica `convertir()`, que recibe las funciones necesarias para leer y escribir cada formato.

Los ficheros generados se almacenan en la carpeta `datos_fin`.

### Práctica 7. Gestión de ficheros binarios

He incorporado un nuevo menú para gestionar videojuegos mediante un fichero binario de acceso aleatorio.

Las operaciones que permite realizar son:

- Importar videojuegos desde CSV.
- Leer todos los registros del fichero binario.
- Añadir nuevos registros.
- Modificar la nota de un videojuego buscando por ID.
- Eliminar registros con confirmación.

Para trabajar con el fichero binario he utilizado `FileChannel` y `ByteBuffer`.

## 3. Estructura de datos

La aplicación utiliza una clase de datos llamada `Videojuego`, que representa cada registro del catálogo.

```kotlin
data class Videojuego(
    val id: Int,
    val titulo: String,
    val genero: String,
    val anio: Int,
    val nota: Double
)
```

Cada videojuego contiene un identificador, un título, un género, el año de lanzamiento y una nota.

### Estructura del registro binario

Para trabajar con acceso aleatorio he definido registros de longitud fija.

| Campo | Tipo | Tamaño |
|---|---|---|
| ID | Int | 4 bytes |
| Título | String | 40 bytes |
| Género | String | 20 bytes |
| Año | Int | 4 bytes |
| Nota | Double | 8 bytes |
| **Total** | | **76 bytes** |

Los campos de texto se guardan con longitud fija, rellenando con espacios cuando es necesario.

Cada registro ocupa 76 bytes, lo que permite calcular su posición dentro del fichero.

## 4. Organización del proyecto

El código fuente se encuentra en `src/main/kotlin`.

Los archivos de datos se organizan en las siguientes carpetas:

- `src/main/kotlin/datos/datos_ini`: contiene los ficheros originales de entrada.
- `src/main/kotlin/datos/datos_fin`: contiene los ficheros generados y modificados por el programa.

Los principales archivos Kotlin del proyecto son:

| Archivo | Función |
|---|---|
| `Main.kt` | Menú principal y gestión CSV |
| `Videojuego.kt` | Clase de datos |
| `VideojuegoRepository.kt` | Lectura y escritura CSV |
| `VideojuegoService.kt` | Operaciones del catálogo |
| `VideojuegosXML.kt` | Contenedor para XML |
| `VideojuegoXMLRepository.kt` | Lectura y escritura XML |
| `VideojuegoJSONRepository.kt` | Lectura y escritura JSON |
| `Conversiones.kt` | Conversiones entre formatos |
| `VideojuegoBinarioRepository.kt` | Gestión de registros binarios |
| `MenuBinario.kt` | Menú del CRUD binario |

## 5. Instrucciones de ejecución

Para ejecutar el proyecto se necesita tener instalado IntelliJ IDEA y un JDK compatible con la versión de Kotlin utilizada.

1. Abrir el proyecto en IntelliJ IDEA.
2. Sincronizar las dependencias de Gradle.
3. Comprobar que los archivos iniciales CSV, JSON y XML se encuentran en la carpeta `datos_ini`.
4. Ejecutar la función `main()` del archivo `Main.kt`.
5. Seleccionar la operación que se quiera realizar desde el menú principal.

El programa permite regresar al menú principal desde los submenús y salir utilizando la opción `0`.

Los archivos nuevos o modificados se guardan en la carpeta `datos_fin`.

## 6. Decisiones de diseño

He utilizado la clase `Videojuego` para representar los datos de manera uniforme, independientemente del formato del fichero.

También he separado las funciones de lectura y escritura de las operaciones del catálogo para mantener el código más organizado.

En el CRUD de CSV he utilizado una lista mutable, ya que permite añadir, modificar y eliminar videojuegos antes de guardar los cambios.

Para las importaciones XML y JSON he comprobado los identificadores antes de añadir nuevos videojuegos, evitando que se generen duplicados.

En las conversiones he utilizado funciones como parámetros para no tener que repetir la misma lógica en cada formato.

Para el fichero binario he elegido registros de 76 bytes, utilizando 40 bytes para el título y 20 para el género. Esto permite conocer la posición de cada registro y acceder directamente a sus campos.

La modificación de notas se realiza escribiendo sobre la posición correspondiente del fichero. Para eliminar un registro se utiliza un fichero temporal con los registros que deben conservarse.

## 7. Tecnologías utilizadas

- Kotlin.
- Gradle.
- IntelliJ IDEA.
- Java NIO (`Path`, `Files`, `FileChannel`, `ByteBuffer`).
- Kotlin CSV.
- Jackson XML.
- Kotlinx Serialization JSON.

## 8. Control de errores y validaciones

Durante el desarrollo he incluido comprobaciones para evitar errores habituales:

- Verificación de que los ficheros existen y pueden leerse.
- Control de opciones incorrectas en los menús.
- Validación de identificadores numéricos.
- Comprobación de IDs duplicados.
- Validación de notas entre 0 y 10.
- Confirmación antes de eliminar videojuegos.
- Control de errores durante la lectura y escritura de archivos.

Estas comprobaciones permiten informar al usuario cuando introduce datos incorrectos o cuando no se puede completar alguna operación.

## 9. Conclusión

Con estas prácticas he aprendido a gestionar la persistencia de datos utilizando diferentes formatos de ficheros en Kotlin.

He podido comprobar las diferencias entre trabajar con archivos de texto estructurados y utilizar ficheros binarios de acceso aleatorio.

También he aprendido a organizar las operaciones de lectura y escritura, validar los datos introducidos y mantener la información guardada después de cerrar la aplicación.
