fun main(){
    calcularEdad()
}

fun calcularEdad(){
    println("Cual es tu nombre?")

    val nombre: String? = readLine()


    println("Cual es tu año de nacimiento?")

    var fecha = readln().toInt()
    val anoActual = 2026

    val edad = anoActual - fecha

    if (nombre != null) 
    println("Tu edad es $edad")
}