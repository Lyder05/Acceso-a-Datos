import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import kotlinx.serialization.Serializable

@Serializable
data class Videojuego(

        @JacksonXmlProperty(localName = "id")
        val id: Int,

        @JacksonXmlProperty(localName = "titulo")
        val titulo: String,

        @JacksonXmlProperty(localName = "genero")
        val genero: String,

        @JacksonXmlProperty(localName = "anio")
        val anio: Int,

        @JacksonXmlProperty(localName = "nota")
        val nota: Double
)
