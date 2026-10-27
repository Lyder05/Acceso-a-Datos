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
