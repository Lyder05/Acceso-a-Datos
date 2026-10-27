import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardOpenOption



fun textoPlano(){
    val ruta = Path.of("muestras", "anotacion.txt");

    Files.createDirectories(ruta.parent);

    val anotacion = "Cuidados de orquideas realizados."

    Files.writeString(ruta, anotacion, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);

    val contenidoCompleto = Files.readString(ruta);

    println("--- Contenido del fichero ---");
    println(contenidoCompleto);

    val lineas = listOf(
        "\n1. Regar cuando las raíces se observen de color grisáceo.",
        "2. Mantener en un espacio con luz indirecta.",
        "3. Evitar corrientes de aire."
    )

    Files.write(ruta,
        lineas,
        StandardCharsets.UTF_8,
        StandardOpenOption.APPEND
    );

    val lineasLeidas = Files.readString(ruta);
    println("\n--- 2. Contenido leído con readAllLines: ---")
    println(lineasLeidas);

}

fun main(){
    textoPlano()
}