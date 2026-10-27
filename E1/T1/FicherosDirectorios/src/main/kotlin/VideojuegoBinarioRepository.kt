import java.nio.ByteBuffer
import java.nio.channels.FileChannel
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardCopyOption
import java.nio.file.StandardOpenOption

private const val BIN_ID = 4
private const val BIN_TITULO = 40
private const val BIN_GENERO = 20
private const val BIN_ANIO = 4
private const val BIN_NOTA = 8

private const val BIN_REGISTRO =
    BIN_ID + BIN_TITULO + BIN_GENERO + BIN_ANIO + BIN_NOTA

private val BIN_CHARSET = Charsets.ISO_8859_1

private fun escribirTexto(
    buffer: ByteBuffer,
    texto: String,
    longitud: Int
) {
    val bytes = texto.toByteArray(BIN_CHARSET)

    require(bytes.size <= longitud) {
        "El texto supera los $longitud bytes permitidos."
    }

    val campo = ByteArray(longitud) { 32 }
    bytes.copyInto(campo)

    buffer.put(campo)
}

private fun leerTexto(
    buffer: ByteBuffer,
    longitud: Int
): String {
    val bytes = ByteArray(longitud)
    buffer.get(bytes)

    return String(bytes, BIN_CHARSET).trimEnd()
}

private fun crearBuffer(videojuego: Videojuego): ByteBuffer {

    val buffer = ByteBuffer.allocate(BIN_REGISTRO)

    buffer.putInt(videojuego.id)
    escribirTexto(buffer, videojuego.titulo, BIN_TITULO)
    escribirTexto(buffer, videojuego.genero, BIN_GENERO)
    buffer.putInt(videojuego.anio)
    buffer.putDouble(videojuego.nota)

    buffer.flip()
    return buffer
}

private fun leerRegistro(buffer: ByteBuffer): Videojuego {

    val id = buffer.getInt()
    val titulo = leerTexto(buffer, BIN_TITULO)
    val genero = leerTexto(buffer, BIN_GENERO)
    val anio = buffer.getInt()
    val nota = buffer.getDouble()

    return Videojuego(id, titulo, genero, anio, nota)
}

private fun leerBufferCompleto(
    canal: FileChannel,
    buffer: ByteBuffer
): Boolean {

    while (buffer.hasRemaining()) {
        val leidos = canal.read(buffer)

        if (leidos == -1) {
            return false
        }

        if (leidos == 0) {
            error("No se ha podido completar la lectura.")
        }
    }

    buffer.flip()
    return true
}

private fun escribirBufferCompleto(
    canal: FileChannel,
    buffer: ByteBuffer
) {
    while (buffer.hasRemaining()) {
        val escritos = canal.write(buffer)

        if (escritos <= 0) {
            error("No se ha podido completar la escritura.")
        }
    }
}


fun importarCSVABinario(
    rutaCSV: Path,
    rutaBIN: Path
): Int {

    if (!Files.isReadable(rutaCSV)) {
        println("No se encuentra el CSV: $rutaCSV")
        return -1
    }

    val lista = leerVideojuegosCSV(rutaCSV)

    if (lista.isEmpty()) {
        println("El CSV no contiene videojuegos válidos.")
        return -1
    }

    try {
        for (videojuego in lista) {
            crearBuffer(videojuego)
        }

        if (lista.map { it.id }.distinct().size != lista.size) {
            println("El CSV contiene IDs duplicados.")
            return -1
        }

        Files.createDirectories(rutaBIN.parent)

        FileChannel.open(
            rutaBIN,
            StandardOpenOption.CREATE,
            StandardOpenOption.WRITE,
            StandardOpenOption.TRUNCATE_EXISTING
        ).use { canal ->

            for (videojuego in lista) {
                escribirBufferCompleto(
                    canal,
                    crearBuffer(videojuego)
                )
            }
        }

        println("Importación completada correctamente.")
        println("Registros importados: ${lista.size}")
        return lista.size

    } catch (e: Exception) {
        println("Error al importar: ${e.message}")
        return -1
    }
}


fun leerVideojuegosBinarios(rutaBIN: Path): List<Videojuego> {

    val lista = mutableListOf<Videojuego>()

    if (!Files.isReadable(rutaBIN)) {
        println("No existe el fichero binario.")
        return lista
    }

    try {
        FileChannel.open(
            rutaBIN,
            StandardOpenOption.READ
        ).use { canal ->

            require(canal.size() % BIN_REGISTRO == 0L) {
                "El fichero tiene un tamaño incorrecto."
            }

            val buffer = ByteBuffer.allocate(BIN_REGISTRO)

            while (canal.position() < canal.size()) {
                buffer.clear()

                if (!leerBufferCompleto(canal, buffer)) {
                    break
                }

                lista.add(leerRegistro(buffer))
            }
        }
    } catch (e: Exception) {
        println("Error al leer BIN: ${e.message}")
    }

    return lista
}


fun buscarVideojuegoBinario(
    rutaBIN: Path,
    idBuscado: Int
): Videojuego? {

    if (!Files.isReadable(rutaBIN)) {
        return null
    }

    FileChannel.open(
        rutaBIN,
        StandardOpenOption.READ
    ).use { canal ->

        require(canal.size() % BIN_REGISTRO == 0L)

        val buffer = ByteBuffer.allocate(BIN_REGISTRO)

        while (canal.position() < canal.size()) {
            buffer.clear()

            if (!leerBufferCompleto(canal, buffer)) {
                break
            }

            val videojuego = leerRegistro(buffer)

            if (videojuego.id == idBuscado) {
                return videojuego
            }
        }
    }

    return null
}


fun anadirVideojuegoBinario(
    rutaBIN: Path,
    videojuego: Videojuego
): Boolean {

    try {
        if (buscarVideojuegoBinario(rutaBIN, videojuego.id) != null) {
            println("Ya existe un videojuego con ese ID.")
            return false
        }

        val buffer = crearBuffer(videojuego)

        Files.createDirectories(rutaBIN.parent)

        FileChannel.open(
            rutaBIN,
            StandardOpenOption.CREATE,
            StandardOpenOption.WRITE,
            StandardOpenOption.APPEND
        ).use { canal ->

            escribirBufferCompleto(canal, buffer)
        }

        println("Videojuego añadido al fichero binario.")
        return true

    } catch (e: Exception) {
        println("Error al añadir: ${e.message}")
        return false
    }
}


fun modificarNotaBinaria(
    rutaBIN: Path,
    idBuscado: Int,
    nuevaNota: Double
): Boolean {

    if (nuevaNota !in 0.0..10.0) {
        return false
    }

    if (!Files.isReadable(rutaBIN)) {
        return false
    }

    try {
        FileChannel.open(
            rutaBIN,
            StandardOpenOption.READ,
            StandardOpenOption.WRITE
        ).use { canal ->

            require(canal.size() % BIN_REGISTRO == 0L)

            val buffer = ByteBuffer.allocate(BIN_REGISTRO)

            while (canal.position() < canal.size()) {

                val inicioRegistro = canal.position()
                buffer.clear()

                if (!leerBufferCompleto(canal, buffer)) {
                    break
                }

                val id = buffer.getInt()

                if (id == idBuscado) {

                    val posicionNota = inicioRegistro +
                            BIN_ID + BIN_TITULO + BIN_GENERO + BIN_ANIO

                    canal.position(posicionNota)

                    val bufferNota = ByteBuffer.allocate(BIN_NOTA)
                    bufferNota.putDouble(nuevaNota)
                    bufferNota.flip()

                    escribirBufferCompleto(canal, bufferNota)

                    println("Nota modificada correctamente.")
                    return true
                }
            }
        }

    } catch (e: Exception) {
        println("Error al modificar: ${e.message}")
    }

    return false
}


fun eliminarVideojuegoBinario(
    rutaBIN: Path,
    idBuscado: Int
): Boolean {

    if (!Files.isReadable(rutaBIN)) {
        return false
    }

    if (buscarVideojuegoBinario(rutaBIN, idBuscado) == null) {
        return false
    }

    val temporal = rutaBIN.resolveSibling(
        rutaBIN.fileName.toString() + ".tmp"
    )

    try {

        FileChannel.open(
            rutaBIN,
            StandardOpenOption.READ
        ).use { lectura ->

            require(lectura.size() % BIN_REGISTRO == 0L)

            FileChannel.open(
                temporal,
                StandardOpenOption.CREATE,
                StandardOpenOption.WRITE,
                StandardOpenOption.TRUNCATE_EXISTING
            ).use { escritura ->

                val buffer = ByteBuffer.allocate(BIN_REGISTRO)

                while (lectura.position() < lectura.size()) {

                    buffer.clear()

                    if (!leerBufferCompleto(lectura, buffer)) {
                        break
                    }

                    val id = buffer.getInt()

                    if (id != idBuscado) {
                        buffer.rewind()
                        escribirBufferCompleto(escritura, buffer)
                    }
                }
            }
        }

        Files.move(
            temporal,
            rutaBIN,
            StandardCopyOption.REPLACE_EXISTING
        )

        println("Videojuego eliminado correctamente.")
        return true

    } catch (e: Exception) {
        println("Error al eliminar: ${e.message}")
        return false

    } finally {
        Files.deleteIfExists(temporal)
    }
}
