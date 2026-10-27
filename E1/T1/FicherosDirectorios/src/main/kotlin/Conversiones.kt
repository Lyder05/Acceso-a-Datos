import java.nio.file.Files
import java.nio.file.Path

fun convertir(origen: Path, destino: Path, leer: (Path) -> List<Videojuego>, escribir: (Path, List<Videojuego>) -> Unit) {

    if (!Files.isReadable(origen)) {
        println("Error: No existe o no se puede leer: $origen")
        return
    }

    try {
        val videojuegos = leer(origen)

        if (videojuegos.isEmpty()) {
            println("No hay videojuegos para convertir.")
            return
        }

        Files.createDirectories(destino.parent)

        escribir(destino, videojuegos)

        println("\n===== CONVERSIÓN COMPLETADA =====")
        println("Origen: $origen")
        println("Destino: $destino")
        println("Registros convertidos: ${videojuegos.size}")

    } catch (e: Exception) {
        println("Error durante la conversión: ${e.message}")
    }
}

fun menuConversiones() {

    val carpetaInicial = Path.of(
        "src", "main", "kotlin", "datos", "datos_ini"
    )

    val carpetaFinal = Path.of(
        "src", "main", "kotlin", "datos", "datos_fin"
    )

    val csvInicial = carpetaInicial.resolve("videojuegos.csv")
    val jsonInicial = carpetaInicial.resolve("videojuegos.json")
    val xmlInicial = carpetaInicial.resolve("videojuegos.xml")

    val csvFinal = carpetaFinal.resolve("videojuegos.csv")
    val jsonFinal = carpetaFinal.resolve("videojuegos.json")
    val xmlFinal = carpetaFinal.resolve("videojuegos.xml")

    val escribirCSV: (Path, List<Videojuego>) -> Unit =
        { ruta, lista ->
            if (!guardarVideojuegosCSV(ruta, lista)) {
                error("No se ha podido guardar el archivo CSV")
            }
        }

    var opcion: Int

    do {
        println("\n--- Conversiones ---")
        println("1. CSV  -> JSON")
        println("2. CSV  -> XML")
        println("3. JSON -> CSV")
        println("4. JSON -> XML")
        println("5. XML  -> CSV")
        println("6. XML  -> JSON")
        println("0. Volver")
        print("Elige una opción: ")

        opcion = readlnOrNull()?.toIntOrNull() ?: -1

        when (opcion) {

            1 -> convertir(
                csvInicial, jsonFinal,
                ::leerVideojuegosCSV,
                ::escribirVideojuegosJSON
            )

            2 -> convertir(
                csvInicial, xmlFinal,
                ::leerVideojuegosCSV,
                ::escribirVideojuegosXML
            )

            3 -> convertir(
                jsonInicial, csvFinal,
                ::leerVideojuegosJSON,
                escribirCSV
            )

            4 -> convertir(
                jsonInicial, xmlFinal,
                ::leerVideojuegosJSON,
                ::escribirVideojuegosXML
            )

            5 -> convertir(
                xmlInicial, csvFinal,
                ::leerVideojuegosXML,
                escribirCSV
            )

            6 -> convertir(
                xmlInicial, jsonFinal,
                ::leerVideojuegosXML,
                ::escribirVideojuegosJSON
            )

            0 -> println("Volviendo al menú principal...")

            else -> println("Opción incorrecta.")
        }

    } while (opcion != 0)
}
