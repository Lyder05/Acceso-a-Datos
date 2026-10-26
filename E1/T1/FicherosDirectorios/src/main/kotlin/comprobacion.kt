import java.nio.file.Files
import java.nio.file.Path
<<<<<<< HEAD
import kotlin.io.path.exists

fun main() {

    val datosIni = Path.of("src/main/kotlin/datos/datos_ini")
    val datosFin = Path.of("src/main/kotlin/datos/datos_fin")

    println("CREACIÓN DE RUTAS PROYECTO")
    println("Creando rutas...")

    if (!datosIni.exists()) {
        Files.createDirectories(datosIni)
        println("Creando ruta para datosIni")
    }

    if (!datosFin.exists()) {
        Files.createDirectories(datosFin)
        println("Creando ruta para datosFin")
=======

fun crearDirectorios(){
    val rutaDatos: Path = Path.of("datos, datos.ini");

    val rutaDatosFin: Path = Path.of("datos, datos.fin");

    if (!Files.exists(rutaDatosFin)){
        Files.createDirectories(rutaDatosFin);
    }
    if (!Files.exists(rutaDatos)){
        Files.createFile(rutaDatos);
>>>>>>> 3ae5f4aecd6cfb954f29a13f4ef58a6a0cb8aa43
    }

}