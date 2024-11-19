package com.example.juegopalabra.model

enum class Estados(val startActivo:Boolean, val enterActivo:Boolean, val textoActivo:Boolean) {
    INICIO(startActivo = true, enterActivo = false, textoActivo = false),
    GENERANDO(startActivo = false, enterActivo = false, textoActivo = false),
    ADIVINANDO(startActivo = false, enterActivo = true, textoActivo = true)
}