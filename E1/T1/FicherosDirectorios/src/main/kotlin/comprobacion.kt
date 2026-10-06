import java.nio.file.Files
import java.nio.file.Path

fun crearDirectorios(){
    val rutaDatos: Path = Path.of("datos, datos.ini");

    val rutaDatosFin: Path = Path.of("datos, datos.fin");

    if (!Files.exists(rutaDatosFin)){
        Files.createDirectories(rutaDatosFin);
    }
    if (!Files.exists(rutaDatos)){
        Files.createFile(rutaDatos);
    }

}