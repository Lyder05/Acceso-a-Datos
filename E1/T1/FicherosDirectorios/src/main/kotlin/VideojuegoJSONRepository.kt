import kotlinx.serialization.json.Json
import java.nio.file.Files
import java.nio.file.Path

fun leerVideojuegosJSON(ruta: Path): List<Videojuego> {

    if (!Files.isReadable(ruta)) {
        println("No se puede leer el archivo JSON: $ruta")
        return emptyList()
    }

    return try {

        val contenido = Files.readString(ruta)

        val videojuegos = Json.decodeFromString<List<Videojuego>>(
            contenido
        )

        println("JSON leído correctamente: $ruta")

        videojuegos

    } catch (e: Exception) {
        println("Error al leer JSON: ${e.message}")
        emptyList()
    }
}


fun escribirVideojuegosJSON(
    ruta: Path,
    lista: List<Videojuego>
) {
    try {
        Files.createDirectories(ruta.parent)

        val json = Json {
            prettyPrint = true
        }

        val contenido = json.encodeToString(lista)

        Files.writeString(ruta, contenido)

        println("JSON guardado correctamente en: $ruta")

    } catch (e: Exception) {
        println("Error al escribir JSON: ${e.message}")
        throw e
    }
}

