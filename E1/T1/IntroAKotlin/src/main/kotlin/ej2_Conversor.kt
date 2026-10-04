fun askQuantity(){
    println("Insert km to converse:")

    var km = readLine()?.toFloat()

    var miles = km?.times(0.621371)

    println("%.2f".format(miles))
}
fun main(){
    askQuantity()
}