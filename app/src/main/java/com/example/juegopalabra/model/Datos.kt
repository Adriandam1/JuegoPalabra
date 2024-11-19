package com.example.juegopalabra.model

object Datos {
    var palabra = ""
    var palabraJugador = ""
    var sinonimo = ""
    var ronda = 0
    var aciertos = 0
    var fallos = 0
}

enum class Diccionario(val id:Int, val nombre:String, val sinonimo:String){
    PALABRA1(id = 1, "listo", "inteligente"),
    PALABRA2(id = 2, "puerta", "entrada"),
    PALABRA3(id = 3, "luna", "satelite"),
    PALABRA4(id = 4, "rapido", "veloz"),
    PALABRA5(id = 5, "lento", "tortuga"),
    PALABRA6(id = 6, "dificil", "complicado"),
    PALABRA7(id = 7, "tonto", "bobo")

}