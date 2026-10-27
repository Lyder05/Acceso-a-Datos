
import java.nio.file.Files
import java.nio.file.Path

fun main() {


    val rutaInicial = Path.of("src", "main", "kotlin", "datos", "datos_ini", "videojuegos.csv")

    val rutaSalida = Path.of("src", "main", "kotlin", "datos", "datos_fin", "videojuegos.csv")

    val rutaXML = Path.of("src", "main", "kotlin", "datos", "datos_ini", "videojuegos.xml")

    val rutaJSON = Path.of("src", "main", "kotlin", "datos", "datos_ini", "videojuegos.json")


    val rutaLectura = if (Files.exists(rutaSalida)) {
        rutaSalida
    } else {
        rutaInicial
    }

    if (!Files.isReadable(rutaLectura)) {
        println("No se encuentra el catálogo CSV: $rutaLectura")
        return
    }

    val videojuegos = leerVideojuegosCSV(rutaLectura).toMutableList()

    var opcion: Int

    do {
        println("\n===== CATÁLOGO DE VIDEOJUEGOS =====")
        println("1. Gestión CSV")
        println("2. Leer catálogo desde XML")
        println("3. Leer catálogo desde JSON")
        println("4. Conversiones entre formatos")
        println("5. Gestión fichero BIN")
        println("0. Salir")
        println("====================================")
        print("Elige una opción: ")

        opcion = readlnOrNull()?.toIntOrNull() ?: -1

        when (opcion) {

            1 -> menuCSV(videojuegos, rutaSalida)

            2 -> importarVideojuegosXML(
                videojuegos,
                rutaXML,
                rutaSalida
            )

            3 -> importarVideojuegosJSON(
                videojuegos,
                rutaJSON,
                rutaSalida
            )

            4 -> menuConversiones()

            5 -> menuBinario()

            0 -> println("Saliendo del programa...")

            else -> println("Opción incorrecta.")
        }

    } while (opcion != 0)
}


fun menuCSV(
    lista: MutableList<Videojuego>,
    rutaSalida: Path
) {

    var opcion: Int

    do {
        println("\n--- Gestión CSV ---")
        println("1. Listar todos los videojuegos")
        println("2. Buscar videojuego por ID")
        println("3. Añadir nuevo videojuego")
        println("4. Modificar nota de un videojuego")
        println("5. Eliminar videojuego")
        println("0. Volver al menú principal")
        print("Elige una opción: ")

        opcion = readlnOrNull()?.toIntOrNull() ?: -1

        when (opcion) {
            1 -> listarVideojuegos(lista)
            2 -> buscarVideojuego(lista)
            3 -> agregarVideojuego(lista, rutaSalida)
            4 -> modificarNotaVideojuego(lista, rutaSalida)
            5 -> eliminarVideojuegoMenu(lista, rutaSalida)
            0 -> println("Volviendo al menú principal...")
            else -> println("Opción incorrecta.")
        }

    } while (opcion != 0)
}


fun listarVideojuegos(lista: List<Videojuego>) {

    if (lista.isEmpty()) {
        println("No hay videojuegos en el catálogo.")
        return
    }

    println("\n===== LISTADO DE VIDEOJUEGOS =====")

    for (videojuego in lista) {
        println("ID: ${videojuego.id}")
        println("Título: ${videojuego.titulo}")
        println("Género: ${videojuego.genero}")
        println("Año: ${videojuego.anio}")
        println("Nota: ${videojuego.nota}")
        println("-----------------------------")
    }
}


fun buscarVideojuego(lista: List<Videojuego>) {

    print("Introduce el ID del videojuego: ")
    val id = readlnOrNull()?.toIntOrNull()

    if (id == null) {
        println("Debes introducir un ID válido.")
        return
    }

    val videojuego = buscarPorId(lista, id)

    if (videojuego != null) {
        println("\n===== VIDEOJUEGO ENCONTRADO =====")
        println("ID: ${videojuego.id}")
        println("Título: ${videojuego.titulo}")
        println("Género: ${videojuego.genero}")
        println("Año: ${videojuego.anio}")
        println("Nota: ${videojuego.nota}")
    } else {
        println("No existe ningún videojuego con ID $id")
    }
}


fun agregarVideojuego(lista: MutableList<Videojuego>, rutaSalida: Path) {

    println("\n===== AÑADIR VIDEOJUEGO =====")

    print("Título: ")
    val titulo = readlnOrNull()?.trim().orEmpty()

    print("Género: ")
    val genero = readlnOrNull()?.trim().orEmpty()

    print("Año: ")
    val anio = readlnOrNull()?.toIntOrNull()

    print("Nota (0-10): ")
    val nota = readlnOrNull()?.toDoubleOrNull()

    if (titulo.isBlank() || genero.isBlank() ||
        anio == null || anio <= 0 ||
        nota == null || nota !in 0.0..10.0) {

        println("Error: Los datos introducidos no son válidos.")
        return
    }

    val nuevoVideojuego = anyadirVideojuego(lista, titulo, genero, anio, nota)

    println("Videojuego añadido con ID ${nuevoVideojuego.id}")

    guardarVideojuegosCSV(rutaSalida, lista)
}


fun modificarNotaVideojuego(lista: MutableList<Videojuego>, rutaSalida: Path) {

    println("\n===== MODIFICAR NOTA =====")

    print("Introduce el ID del videojuego: ")
    val id = readlnOrNull()?.toIntOrNull()

    if (id == null) {
        println("Debes introducir un ID válido.")
        return
    }

    val videojuego = buscarPorId(lista, id)

    if (videojuego == null) {
        println("No existe ningún videojuego con ID $id")
        return
    }

    println("Videojuego: ${videojuego.titulo}")
    println("Nota actual: ${videojuego.nota}")

    print("Nueva nota (0-10): ")
    val nuevaNota = readlnOrNull()?.toDoubleOrNull()

    if (nuevaNota == null || nuevaNota !in 0.0..10.0) {
        println("La nota debe estar entre 0 y 10.")
        return
    }

    val modificado = modificarNota(lista, id, nuevaNota)

    if (modificado) {
        println("Nota modificada correctamente.")
        guardarVideojuegosCSV(rutaSalida, lista)
    } else {
        println("No se ha podido modificar la nota.")
    }
}


fun eliminarVideojuegoMenu(lista: MutableList<Videojuego>, rutaSalida: Path ){

    println("\n===== ELIMINAR VIDEOJUEGO =====")

    print("Introduce el ID del videojuego: ")
    val id = readlnOrNull()?.toIntOrNull()

    if (id == null) {
        println("Debes introducir un ID válido.")
        return
    }

    val videojuego = buscarPorId(lista, id)

    if (videojuego == null) {
        println("No existe ningún videojuego con ID $id")
        return
    }

    println("Videojuego encontrado: ${videojuego.titulo}")
    print("¿Seguro que quieres eliminarlo? (S/N): ")

    val confirmacion = readlnOrNull()?.trim()?.uppercase()

    if (confirmacion != "S") {
        println("Eliminación cancelada.")
        return
    }

    val eliminado = eliminarVideojuego(lista, id)

    if (eliminado) {
        println("Videojuego eliminado correctamente.")
        guardarVideojuegosCSV(rutaSalida, lista)
    } else {
        println("No se ha podido eliminar el videojuego.")
    }
}


fun importarVideojuegosXML(catalogo: MutableList<Videojuego>, rutaXML: Path, rutaSalida: Path) {

    println("\n===== IMPORTAR VIDEOJUEGOS DESDE XML =====")

    if (!Files.isReadable(rutaXML)) {
        println("No se encuentra el archivo XML: $rutaXML")
        return
    }

    val juegosXML = leerVideojuegosXML(rutaXML)

    if (juegosXML.isEmpty()) {
        println("No se han encontrado videojuegos para importar.")
        return
    }

    println("\n--- VIDEOJUEGOS LEÍDOS DESDE XML ---")
    listarVideojuegos(juegosXML)

    val cantidadAntes = catalogo.size

    val nuevos = integrarCatalogo(catalogo, juegosXML)

    println("\nVideojuegos leídos desde XML: ${juegosXML.size}")
    println("Videojuegos nuevos integrados: $nuevos")
    println("Total de videojuegos: ${catalogo.size}")

    val guardado = guardarVideojuegosCSV(rutaSalida, catalogo)

    if (!guardado) {
        while (catalogo.size > cantidadAntes) {
            catalogo.removeAt(catalogo.lastIndex)
        }
        println("No se ha podido completar la importación.")
    } else {
        println("Importación XML completada correctamente.")
    }
}


fun importarVideojuegosJSON(catalogo: MutableList<Videojuego>, rutaJSON: Path, rutaSalida: Path) {

    println("\n===== IMPORTAR VIDEOJUEGOS DESDE JSON =====")

    if (!Files.isReadable(rutaJSON)) {
        println("No se encuentra el archivo JSON: $rutaJSON")
        return
    }

    val juegosJSON = leerVideojuegosJSON(rutaJSON)

    if (juegosJSON.isEmpty()) {
        println("No se han encontrado videojuegos para importar.")
        return
    }

    println("\n--- VIDEOJUEGOS LEÍDOS DESDE JSON ---")

    listarVideojuegos(juegosJSON)

    val cantidadAntes = catalogo.size

    val nuevos = integrarCatalogo(catalogo, juegosJSON)

    println("\nVideojuegos leídos desde JSON: ${juegosJSON.size}")
    println("Videojuegos nuevos integrados: $nuevos")
    println("Total de videojuegos: ${catalogo.size}")

    val guardado = guardarVideojuegosCSV(
        rutaSalida,
        catalogo
    )

    if (!guardado) {
        while (catalogo.size > cantidadAntes) {
            catalogo.removeAt(catalogo.lastIndex)
        }

        println("No se ha podido completar la importación.")
    } else {
        println("Importación JSON completada correctamente.")
    }
}


/*fun main() {

    val plantasPath = Path.of(
        "src", "main", "kotlin", "datos", "plantas.csv"
    )

    val plantas2Path = Path.of(
        "src", "main", "kotlin", "datos", "plantas2.csv"
    )

    val plantas = leerDatosCSV(plantasPath)

    for (planta in plantas) {
        println(planta)
    }

    escribirCSV(plantas2Path, plantas)

    println("Archivo plantas2.csv creado correctamente")
}



fun escribirCSV(ruta: Path, plantas: List<Planta>) {

    val writer = csvWriter {
        delimiter = ';'
    }

    val filas = plantas.map { planta ->
        listOf(
            planta.idPlanta.toString(),
            planta.nombreComun,
            planta.nombreCientifico,
            planta.stock.toString(),
            planta.precio.toString()
        )
    }

    writer.writeAll(filas, ruta.toFile())
}


fun leerDatosCSV(ruta: Path): List<Planta> {
    var plantas: List<Planta> = emptyList()

    if (!Files.isReadable(ruta)) {
        println("Error: No se puede leer el fichero en la ruta: $ruta")
    }
    else{
        val reader = csvReader{
            delimiter = ';'
        }

        val filas: List<List<String>> = reader.readAll(ruta.toFile())

        plantas = filas.mapNotNull { columnas ->
            if (columnas.size >= 5){
                try {
                    val idPlanta = columnas[0].toInt()
                    val nombreComun = columnas[1]
                    val nombreCientifico = columnas[2]
                    val stock = columnas[3].toInt()
                    val precio = columnas[4].toDouble()

                    Planta(
                        idPlanta,
                        nombreComun,
                        nombreCientifico,
                        stock,
                        precio
                    )
                } catch (e: Exception){
                    println("Fila inválida ignorada: $columnas")
                    null
                }
            }else{
                println("Fila incompleta ignorada: $columnas")
                null
            }
        }

    }
    return (plantas)
}



fun textoPlano() {

    val ruta = of("muestras", "anotacion.txt")

    Files.createDirectories(ruta.parent)

    val anotacion = "Cuidados de orquídeas realizados."
    Files.writeString(ruta, anotacion, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)

    val contenidoCompleto = Files.readString(ruta)
    println("--- 1. Contenido del fichero leído con readString: ---")
    println(contenidoCompleto)

    val lineas = listOf(
        "\n1. Regar cuando las raíces se observen de color grisáceo.",
        "2. Mantener en un espacio con luz indirecta.",
        "3. Evitar corrientes de aire."
    )
    Files.write(ruta, lineas, StandardCharsets.UTF_8, StandardOpenOption.APPEND)

    val lineasLeidas = Files.readAllLines(ruta)
    println("\n--- 2. Contenido leído con readAllLines: ---")
    for (linea in lineasLeidas) {
        println(linea)
    }

    Files.newBufferedWriter(ruta, StandardOpenOption.APPEND).use { writer ->
        writer.write("(SISTEMA) Invernadero automatizado iniciado...\n")
        writer.write("(SENSOR) Nivel de humedad óptimo detectado (75%).\n")
    }

    println("\n--- 3. Contenido leído con newBufferedReader: ---")
    Files.newBufferedReader(ruta).use { reader ->
        reader.lineSequence().forEach { linea ->
            println(linea)
        }
    }

}*/