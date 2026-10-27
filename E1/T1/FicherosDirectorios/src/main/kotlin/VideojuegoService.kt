
fun buscarPorId(
    lista: List<Videojuego>,
    id: Int
): Videojuego? {
    return lista.find { it.id == id }
}

fun anyadirVideojuego(
    lista: MutableList<Videojuego>,
    titulo: String,
    genero: String,
    anio: Int,
    nota: Double
): Videojuego {

    val nuevoId = (lista.maxOfOrNull { it.id } ?: 0) + 1

    val nuevoVideojuego = Videojuego(
        nuevoId,
        titulo,
        genero,
        anio,
        nota
    )

    lista.add(nuevoVideojuego)

    return nuevoVideojuego
}

fun modificarNota(
    lista: MutableList<Videojuego>,
    id: Int,
    nuevaNota: Double
): Boolean {

    if (nuevaNota !in 0.0..10.0) {
        return false
    }

    val posicion = lista.indexOfFirst { it.id == id }

    if (posicion == -1) {
        return false
    }

    lista[posicion] = lista[posicion].copy(
        nota = nuevaNota
    )

    return true
}

fun eliminarVideojuego(
    lista: MutableList<Videojuego>,
    id: Int
): Boolean {

    return lista.removeIf { it.id == id }
}



fun integrarCatalogo(catalogoActual: MutableList<Videojuego>, nuevosVideojuegos: List<Videojuego>): Int {

    var contador = 0

    for (videojuego in nuevosVideojuegos) {

        val existe = catalogoActual.any {
            it.id == videojuego.id
        }

        if (!existe) {
            catalogoActual.add(videojuego)
            contador++
        }
    }

    return contador
}
