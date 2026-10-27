
import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import com.github.doyaaaaaken.kotlincsv.dsl.csvWriter
import java.nio.file.Files
import java.nio.file.Path

fun leerVideojuegosCSV(ruta: Path): List<Videojuego> {

    if (!Files.isReadable(ruta)) {
        println("No se puede leer el archivo: $ruta")
        return emptyList()
    }

    val reader = csvReader {
        delimiter = ';'
    }

    val filas = reader.readAll(ruta.toFile())

    return filas.mapNotNull { columnas ->

        if (columnas.size < 5) {
            println("Fila incompleta ignorada: $columnas")
            null
        } else {
            try {
                Videojuego(
                    columnas[0].toInt(),
                    columnas[1],
                    columnas[2],
                    columnas[3].toInt(),
                    columnas[4].toDouble()
                )
            } catch (e: Exception) {
                println("Fila inválida ignorada: $columnas")
                null
            }
        }
    }
}

fun guardarVideojuegosCSV(
    ruta: Path,
    lista: List<Videojuego>
): Boolean {

    return try {
        Files.createDirectories(ruta.parent)

        val writer = csvWriter {
            delimiter = ';'
        }

        val filas = lista.map { videojuego ->
            listOf(
                videojuego.id.toString(),
                videojuego.titulo,
                videojuego.genero,
                videojuego.anio.toString(),
                videojuego.nota.toString()
            )
        }

        writer.writeAll(filas, ruta.toFile())

        println("Videojuegos guardados correctamente.")
        true

    } catch (e: Exception) {
        println("Error al guardar el CSV: ${e.message}")
        false
    }
}
