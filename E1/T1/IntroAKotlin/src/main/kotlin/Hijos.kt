class Hijos(edad1: IntArray) {
    val edad: IntArray = IntArray(5)

    fun cargar(edad: IntArray){
        for (i in edad.indices){
            println("Ingresa edad:")
            edad[i] = readln()!!.toInt()
        }
    }
}