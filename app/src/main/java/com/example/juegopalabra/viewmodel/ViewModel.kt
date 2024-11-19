package com.example.juegopalabra.viewmodel

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.juegopalabra.model.Datos
import com.example.juegopalabra.model.Diccionario
import com.example.juegopalabra.model.Estados
import kotlin.random.Random

class ViewModel:ViewModel() {

    val random = Random

    private var _palabraJugadorLiveData = MutableLiveData<String>()
    val palabraJugadorLiveData : LiveData<String> get() = _palabraJugadorLiveData

    private val _sinonimoLiveData = MutableLiveData<String>()
    val sinonimoLiveData : LiveData<String> get() = _sinonimoLiveData

    private var _rondasLiveData = MutableLiveData<Int>()
    val rondasLiveData: LiveData<Int> get() = _rondasLiveData

    private var _aciertosLiveData = MutableLiveData<Int>()
    val aciertosLiveData: LiveData<Int> get() = _aciertosLiveData

    private var _fallosLiveData = MutableLiveData<Int>()
    val fallosLiveData: LiveData<Int> get() = _fallosLiveData

    val estadoLiveData : MutableLiveData<Estados> = MutableLiveData(Estados.INICIO)

    init {
        _palabraJugadorLiveData.value = Datos.palabraJugador
        _sinonimoLiveData.value = Datos.sinonimo
        _rondasLiveData.value = Datos.ronda
        _aciertosLiveData.value = Datos.aciertos
        _fallosLiveData.value = Datos.fallos
    }



    fun setPalabraAdivinar(context: Context){
        estadoLiveData.value = Estados.GENERANDO
        var numeroAleatorioDiccionario = random.nextInt(1,8)
        var palabraAdivinar = checkPalabra(numeroAleatorioDiccionario)
        var sinonimoAdivinar = checkSinonimo(palabraAdivinar)

        Log.d("Comprobando", palabraAdivinar)
        Log.d("Comprobando", sinonimoAdivinar)

        // Mostrar el Toast con el sinónimo
        //Toast.makeText(context, "Sinónimo: $sinonimoAdivinar", Toast.LENGTH_SHORT).show()

        estadoLiveData.value = Estados.ADIVINANDO
    }

    fun addPalabraJugador(palabraJugador: String, palabraMaquina: String, context: Context) {
        Log.d("palabraJ", getPalabraJugador())
        Log.d("Comprobando", palabraJugador)
        Log.d("Comprobando", palabraMaquina)

        // Pasa el contexto al llamar a winOrLose
        ganar(palabraJugador, palabraMaquina, context)
    }


    private fun checkPalabra(id: Int): String {
        val palabra = Diccionario.entries.find { it.id == id }
        if (palabra != null) {
           setPalabraMaquina(palabra.nombre)
            return palabra.nombre
        }
        return ""
    }

     fun checkSinonimo(palabra:String):String{
        val sinonimoAdivinar = Diccionario.entries.find { it.nombre == palabra }
        if(sinonimoAdivinar != null){
            setSinonimo(sinonimoAdivinar.sinonimo)
            return sinonimoAdivinar.sinonimo
        }
        return ""
    }

    fun ganar(palabraJugador: String, palabraMaquina: String, context: Context) {
        if (palabraJugador == palabraMaquina) {
            Log.d("GanarOPerder", "Has ganado")
            // Usa el context proporcionado para mostrar el Toast
            Toast.makeText(context, "¡Has ganado!", Toast.LENGTH_SHORT).show()

            setRondas()
            setAciertos()
            restartFallos()
            restartSinonimo()
            estadoLiveData.value = Estados.INICIO
        } else {
            setFallos()
            perder(getAciertos(), getFallos(), context)
        }
    }

    private fun perder(aciertos: Int, fallos: Int, context: Context) {
        if (aciertos == 0 && fallos == 3) {
            estadoLiveData.value = Estados.INICIO
            Log.d("GanarOPerder", "Has perdido")
            // Usa el context proporcionado para mostrar el Toast
            Toast.makeText(context, "¡Has perdido!", Toast.LENGTH_LONG).show()

            restartRondas()
            restartAciertos()
            restartFallos()
            restartSinonimo()
        } else if (aciertos == 1 && fallos == 2) {
            estadoLiveData.value = Estados.INICIO
            Log.d("GanarOPerder", "Has perdido")
            Toast.makeText(context, "¡Has perdido!", Toast.LENGTH_LONG).show()

            restartRondas()
            restartAciertos()
            restartFallos()
            restartSinonimo()
        } else if (aciertos > 1 && fallos == 1) {
            estadoLiveData.value = Estados.INICIO
            Log.d("GanarOPerder", "Has perdido")
            Toast.makeText(context, "¡Has perdido!", Toast.LENGTH_LONG).show()

            restartRondas()
            restartAciertos()
            restartFallos()
            restartSinonimo()
        }
    }



    fun setPalabraMaquina(palabraMaquina: String){
        Datos.palabra = palabraMaquina
    }

    fun setSinonimo(sinonimoAdivinar:String){
        Datos.sinonimo = sinonimoAdivinar
        _sinonimoLiveData.value = Datos.sinonimo
    }

    fun setPalabraJugador(texto:String){
        Datos.palabraJugador = texto
        _palabraJugadorLiveData.value = Datos.palabraJugador
    }

    fun setRondas(){
        Datos.ronda += 1
        _rondasLiveData.value = Datos.ronda
    }

    fun setAciertos(){
        Datos.aciertos += 1
        _aciertosLiveData.value = Datos.aciertos
    }

    fun setFallos(){
        Datos.fallos += 1
        _fallosLiveData.value  = Datos.fallos
    }

    fun getPalabraMaquina():String{
        return Datos.palabra
    }

    fun getPalabraJugador():String{
        return Datos.palabraJugador
    }

    fun getSinonimo():String{
        return Datos.sinonimo
    }

    fun getRonda():Int{
        return Datos.ronda
    }

    fun getAciertos():Int{
        return Datos.aciertos
    }

    fun getFallos():Int{
        return Datos.fallos
    }

    fun restartRondas(){
        Datos.ronda = 0
        _rondasLiveData.value = Datos.ronda
    }

    fun restartAciertos(){
        Datos.aciertos = 0
        _aciertosLiveData.value = Datos.aciertos
    }

    fun restartFallos(){
        Datos.fallos = 0
        _fallosLiveData.value  = Datos.fallos
    }

    fun restartSinonimo(){
        Datos.sinonimo = ""
        _sinonimoLiveData.value = ""
    }




}