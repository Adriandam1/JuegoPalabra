## JuegoPalabra ENUNCIADO
-  El jugador inicia la partida.
-  Se le facilitará una palabra sinonima para que acierte la palabra oculta.
-  Contadores con aciertos, fallo y rondas.
-  Cuando llegue a 3 fallos, o 2 si ya acerto una palabra o 1 si ya acerto 2 palabras habra perdido.
-  Mientras no se cumpla la condición para perder el jugador podrá jugar indefinidamente.
-  Cuando el jugador pierda la aplicación se reiniciará.

-----------------------------------------------------
### comienzo explicacion del flujo

Esta aplicación es un juego en el que el jugador debe adivinar una palabra a partir de sus sinónimos. A continuación, te explico el flujo de la aplicación, basado en el código del `ViewModel`.  

## 1. **Inicialización del Juego**  
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

