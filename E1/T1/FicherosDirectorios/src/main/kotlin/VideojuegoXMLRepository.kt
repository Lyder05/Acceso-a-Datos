import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import java.nio.file.Files
import java.nio.file.Path

fun leerVideojuegosXML(ruta: Path): List<Videojuego> {

    if (!Files.isReadable(ruta)) {
        println("No se puede leer el archivo XML: $ruta")
        return emptyList()
    }

    return try {

        val xmlMapper = XmlMapper().registerKotlinModule()

        val contenedor: VideojuegosXML = xmlMapper.readValue(
            ruta.toFile()
        )

        println("XML leído correctamente: $ruta")

        contenedor.listaVideojuegos

    } catch (e: Exception) {
        println("Error al leer XML: ${e.message}")
        emptyList()
    }
}


fun escribirVideojuegosXML(
    ruta: Path,
    lista: List<Videojuego>
) {
    try {
        Files.createDirectories(ruta.parent)

        val xmlMapper = XmlMapper().registerKotlinModule()

        xmlMapper.enable(SerializationFeature.INDENT_OUTPUT)

        val contenedor = VideojuegosXML(lista)

        xmlMapper.writeValue(
            ruta.toFile(),
            contenedor
        )

        println("XML guardado correctamente en: $ruta")

    } catch (e: Exception) {
        println("Error al escribir XML: ${e.message}")
        throw e
    }
}

