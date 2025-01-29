## JuegoPalabra ENUNCIADO
-  El jugador inicia la partida.
-  Se le facilitará una palabra sinonima para que acierte la palabra oculta.
-  Contadores con aciertos, fallo y rondas.
-  Cuando llegue a 3 fallos, o 2 si ya acerto una palabra o 1 si ya acerto 2 palabras habra perdido.
-  Mientras no se cumpla la condición para perder el jugador podrá jugar indefinidamente.
-  Cuando el jugador pierda la aplicación se reiniciará.

-----------------------------------------------------
## 1. Explicación de los estados:  
Como vamos a utilizar estados durante el uso de la aplicación hago un resumen de los mismos. En nuestro Datos.kt tenemos nuestra clase enum Estados que recoge 3 Boolean:  
El boolean **startActivo** es un boolean que utilizaremos para activar o desactivar nuestro boton "ButtonStart".  
El boolean **enterActivo** es el boolean que utilizaremos para activar o desactivar nuestro boton "ButtonEnter" que el jugador clicará para introducir las palabras
El boolean **textoActivo** es el boolean que utilizaremos para habilitar nuestro TextField, en el que el jugador podrá escribir las palabras.
```bash
enum class Estados(val startActivo:Boolean, val enterActivo:Boolean, val textoActivo:Boolean) {
    INICIO(startActivo = true, enterActivo = false, textoActivo = false), // estado inicial y entre rondas
    GENERANDO(startActivo = false, enterActivo = false, textoActivo = false), // estado para cuando se esten llevando las operacion de setPalabraAdivinar
    ADIVINANDO(startActivo = false, enterActivo = true, textoActivo = true) // estado en el que el jugador esta introduciendo su palabra
}
```



### comienzo explicacion del flujo

Esta aplicación es un juego en el que el jugador debe adivinar una palabra a partir de sus sinónimos. A continuación, te explico el flujo de la aplicación, basado en el código del `ViewModel`.  

## 2. **Inicialización del Juego**  
Iniciamos el juego, como es lígoc en el estado INICIO, que tenemos en nuestro LiveData para ir actualizandolo:
```bash
    val estadoLiveData : MutableLiveData<Estados> = MutableLiveData(Estados.INICIO)
```
Cuando se inicia el juego, se establece una palabra aleatoria a adivinar y su correspondiente sinónimo. El flujo comienza con la llamada a `setPalabraAdivinar()`:  
- **Cambio de estado**: Se cambia el estado a **GENERANDO**
- **Generación de palabra aleatoria**: Se genera un número aleatorio que selecciona una palabra del diccionario.
- **Obtención del sinónimo**: Una vez que se tiene la palabra, se busca su sinónimo.
```bash
    fun setPalabraAdivinar(context: Context){
        estadoLiveData.value = Estados.GENERANDO // Cambia el estado del juego a GENERANDO
        var numeroAleatorioDiccionario = random.nextInt(1,8) // Se genera un número aleatorio entre 1 y 7 (inclusive)
        var palabraAdivinar = checkPalabra(numeroAleatorioDiccionario) // Se recoge el numero aleatorio en palabraAdivinar() y se emplea en checkPalabra para buscar en el diccionario la palabra.
        var sinonimoAdivinar = checkSinonimo(palabraAdivinar) // Se llama a la funcion checkSinonimo() para buscar la palabra sinonimo correspondiente
        estadoLiveData.value = Estados.ADIVINANDO // Se actualiza el estado a ADIVINANDO
    }
```
----------------------------------------------------
## 3. **Rondas y Aciertos**

Cada ronda del juego tiene un número limitado de intentos (3 intentos). El jugador puede introducir una palabra en un campo de texto para intentar adivinar la palabra correcta.

- **Función `addPalabraJugador(palabraJugador: String, palabraMaquina: String, context: Context)`**: Esta función compara la palabra ingresada por el jugador con la palabra correcta (la palabra de la máquina).
  
  - Si el jugador acierta la palabra (`palabraJugador == palabraMaquina`), se aumenta el número de aciertos y se genera una nueva ronda.
  - Si el jugador falla, se incrementa el contador de fallos.
  
### Condiciones de Acierto:  
- Si el jugador acierta, se genera una nueva palabra y sinónimo para la próxima ronda.
```bash
    fun ganar(palabraJugador: String, palabraMaquina: String, context: Context) {
        if (palabraJugador == palabraMaquina) {
            Log.d("GanarOPerder", "Has ganado")
            // Usa el context proporcionado para mostrar el Toast
            Toast.makeText(context, "¡Has ganado!", Toast.LENGTH_SHORT).show() // mostramos toast informativo

            setRondas()
            setAciertos()
            restartFallos()
            restartSinonimo()
            estadoLiveData.value = Estados.INICIO // cambiamos el estado a inicio
        } else {
            setFallos()
            perder(getAciertos(), getFallos(), context)
        }
    }
```  
### Condiciones de para Perder:
- Si el jugador llega a 3 fallos, pierde la ronda y se reinician los contadores de rondas, aciertos, fallos y sinónimos. 
- Cuando el jugador falla 3 veces, la función `perder(aciertos: Int, fallos: Int, context: Context)` es la encargada de determinar el fin de la partida:  
```bash
    fun perder(aciertos: Int, fallos: Int, context: Context) {
        if (fallos == 3) {
            estadoLiveData.value = Estados.INICIO // volvemos al estado INICIO
            Log.d("GanarOPerder", "Has perdido")
            Toast.makeText(context, "¡Has perdido!", Toast.LENGTH_LONG).show() // Mostramos un toast informativo
            // Reiniciar los contadores
            restartRondas()
            restartAciertos()
            restartFallos()
            restartSinonimo()
        }
    }
```
