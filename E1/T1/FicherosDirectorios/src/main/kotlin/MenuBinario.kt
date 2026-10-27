import java.nio.file.Files
import java.nio.file.Path

fun menuBinario() {

    val rutaCSV = Path.of(
        "src", "main", "kotlin", "datos",
        "datos_ini", "videojuegos.csv"
    )

    val rutaBIN = Path.of(
        "src", "main", "kotlin", "datos",
        "datos_fin", "videojuegos.bin"
    )

    var opcion: Int

    do {
        println("\n===== CRUD fichero BIN =====")
        println("1. Importar información desde CSV")
        println("2. Leer información del fichero binario")
        println("3. Añadir un registro nuevo")
        println("4. Modificar un registro existente (por ID)")
        println("5. Eliminar un registro existente (por ID)")
        println("0. Volver")
        print("Elige una opción: ")

        opcion = readlnOrNull()?.toIntOrNull() ?: -1

        when (opcion) {

            1 -> {
                println("\n===== IMPORTAR CSV A BIN =====")
                print("Se reemplazará el BIN actual. ¿Continuar? (S/N): ")
                val confirmar = readlnOrNull()?.trim()?.uppercase()

                if (confirmar == "S") {
                    importarCSVABinario(rutaCSV, rutaBIN)
                } else {
                    println("Importación cancelada.")
                }
            }

            2 -> mostrarVideojuegosBinarios(rutaBIN)

            3 -> agregarRegistroBinario(rutaBIN)

            4 -> modificarRegistroBinario(rutaBIN)

            5 -> eliminarRegistroBinario(rutaBIN)

            0 -> println("Volviendo al menú principal...")

            else -> println("Opción incorrecta.")
        }

    } while (opcion != 0)
}


fun mostrarVideojuegosBinarios(rutaBIN: Path) {

    println("\n===== VIDEOJUEGOS DEL FICHERO BINARIO =====")

    if (!Files.isReadable(rutaBIN)) {
        println("Primero debes importar los videojuegos.")
        return
    }

    val videojuegos = leerVideojuegosBinarios(rutaBIN)

    if (videojuegos.isEmpty()) {
        println("El fichero binario está vacío.")
        return
    }

    for (videojuego in videojuegos) {
        println("ID: ${videojuego.id}")
        println("Título: ${videojuego.titulo}")
        println("Género: ${videojuego.genero}")
        println("Año: ${videojuego.anio}")
        println("Nota: ${videojuego.nota}")
        println("----------------------------")
    }

    println("Total de registros: ${videojuegos.size}")
}


fun agregarRegistroBinario(rutaBIN: Path) {

    println("\n===== AÑADIR VIDEOJUEGO BINARIO =====")

    var id: Int

    while (true) {
        print("Introduce un ID nuevo: ")
        val introducido = readlnOrNull()?.toIntOrNull()

        if (introducido == null || introducido <= 0) {
            println("El ID debe ser un entero positivo.")
            continue
        }

        if (buscarVideojuegoBinario(rutaBIN, introducido) != null) {
            println("Ese ID ya existe. Introduce otro.")
            continue
        }

        id = introducido
        break
    }

    var titulo: String

    while (true) {
        print("Título (máximo 40 bytes): ")
        titulo = readlnOrNull()?.trim().orEmpty()

        if (titulo.isBlank() ||
            titulo.toByteArray(Charsets.ISO_8859_1).size > 40) {
            println("El título es obligatorio y debe ocupar 40 bytes o menos.")
        } else {
            break
        }
    }

    var genero: String

    while (true) {
        print("Género (máximo 20 bytes): ")
        genero = readlnOrNull()?.trim().orEmpty()

        if (genero.isBlank() ||
            genero.toByteArray(Charsets.ISO_8859_1).size > 20) {
            println("El género es obligatorio y debe ocupar 20 bytes o menos.")
        } else {
            break
        }
    }

    var anio: Int

    while (true) {
        print("Año: ")
        val introducido = readlnOrNull()?.toIntOrNull()

        if (introducido == null || introducido <= 0) {
            println("Introduce un año válido.")
        } else {
            anio = introducido
            break
        }
    }

    var nota: Double

    while (true) {
        print("Nota (0-10): ")
        val introducida = readlnOrNull()?.toDoubleOrNull()

        if (introducida == null || introducida !in 0.0..10.0) {
            println("La nota debe estar entre 0 y 10.")
        } else {
            nota = introducida
            break
        }
    }

    val videojuego = Videojuego(
        id, titulo, genero, anio, nota
    )

    anadirVideojuegoBinario(rutaBIN, videojuego)
}


fun modificarRegistroBinario(rutaBIN: Path) {

    println("\n===== MODIFICAR REGISTRO BINARIO =====")

    var id: Int

    while (true) {
        print("ID del videojuego: ")
        val introducido = readlnOrNull()?.toIntOrNull()

        if (introducido == null || introducido <= 0) {
            println("Introduce un ID entero positivo.")
        } else {
            id = introducido
            break
        }
    }

    val videojuego = buscarVideojuegoBinario(rutaBIN, id)

    if (videojuego == null) {
        println("No existe ningún videojuego con ID $id")
        return
    }

    println("Videojuego: ${videojuego.titulo}")
    println("Nota actual: ${videojuego.nota}")

    var nuevaNota: Double

    while (true) {
        print("Nueva nota (0-10): ")
        val introducida = readlnOrNull()?.toDoubleOrNull()

        if (introducida == null || introducida !in 0.0..10.0) {
            println("Introduce una nota válida entre 0 y 10.")
        } else {
            nuevaNota = introducida
            break
        }
    }

    if (!modificarNotaBinaria(rutaBIN, id, nuevaNota)) {
        println("No se ha podido modificar el registro.")
    }
}


fun eliminarRegistroBinario(rutaBIN: Path) {

    println("\n===== ELIMINAR REGISTRO BINARIO =====")

    var id: Int

    while (true) {
        print("ID del videojuego: ")
        val introducido = readlnOrNull()?.toIntOrNull()

        if (introducido == null || introducido <= 0) {
            println("Introduce un ID entero positivo.")
        } else {
            id = introducido
            break
        }
    }

    val videojuego = buscarVideojuegoBinario(rutaBIN, id)

    if (videojuego == null) {
        println("No existe ningún videojuego con ID $id")
        return
    }

    println("Videojuego encontrado: ${videojuego.titulo}")
    print("¿Confirmas que quieres eliminarlo? (S/N): ")

    val confirmacion = readlnOrNull()?.trim()?.uppercase()

    if (confirmacion != "S") {
        println("Eliminación cancelada.")
        return
    }

    if (!eliminarVideojuegoBinario(rutaBIN, id)) {
        println("No se ha podido eliminar el videojuego.")
    }
}
