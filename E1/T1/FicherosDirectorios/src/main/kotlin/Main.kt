import java.nio.file.Files
import java.nio.charset.StandardCharsets
import java.nio.file.Path
import java.nio.file.StandardOpenOption

fun main() {
    textoPlano()
}


fun textoPlano() {

    // Ruta del fichero con el que vamos a trabajar
    val ruta = Path.of("muestras", "anotacion.txt")

    Files.createDirectories(ruta.parent) // Crea la carpeta "muestras" si no existe

    // Escritura de una línea writeString (si no existe lo crea y si existe lo vacía)
    val anotacion = "Cuidados de orquídeas realizados."
    Files.writeString(ruta, anotacion, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)

    // Lectura rápida de todo el bloque con readString
    val contenidoCompleto = Files.readString(ruta)
    println("--- 1. Contenido del fichero leído con readString: ---")
    println(contenidoCompleto)


    // Escritura de múltiples líneas en un fichero de texto con Files.write
    val lineas = listOf(
        "\n1. Regar cuando las raíces se observen de color grisáceo.",
        "2. Mantener en un espacio con luz indirecta.",
        "3. Evitar corrientes de aire."
    )
    Files.write(ruta, lineas, StandardCharsets.UTF_8, StandardOpenOption.APPEND)

    // Lectura con readAllLines (devuelve una lista línea por línea)
    val lineasLeidas = Files.readAllLines(ruta)
    println("\n--- 2. Contenido leído con readAllLines: ---")
    for (linea in lineasLeidas) {
        println(linea)
    }

    Files.newBufferedWriter(ruta, StandardOpenOption.APPEND).use { writer ->
        writer.write("(SISTEMA) Invernadero automatizado iniciado...\n")
        writer.write("(SENSOR) Nivel de humedad óptimo detectado (75%).\n")
    }

    // Lectura secuencial eficiente con newBufferedReader
    println("\n--- 3. Contenido leído con newBufferedReader: ---")
    Files.newBufferedReader(ruta).use { reader ->
        reader.lineSequence().forEach { linea ->
            println(linea)
        }
    }

}