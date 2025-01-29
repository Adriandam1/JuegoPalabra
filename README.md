## JuegoPalabra ENUNCIADO

Implementa una app que es un juego de adivinar una palabra, dando pistas  
Se genera una palabra random de un diccionario.  
Las pistas son sinónimos de esta palabra  
En cada ronda la app muestra un sinónimo.  
Si al tercer intento el jugador no acierta, pierde y el juego termina  
Si en algún intento el jugador acierta, gana y el juego termina  

Se valorará el uso de Singleton y Enum (1 punto)  
Se valorará el uso de estados (1 punto)  
Se valorará el uso del patrón observer (1 punto)  
Se valorará el esquema MVVM (1 punto)  
Se valorará el uso de métodos con parámetros y retorno de valores (0,5 punto)  
El funcionamiento de la aplicación (0,5 punto).  
Crea un Readme explicando lo que realizaste (1 punto)  

-----------------------------------------------------
## 1. Explicación de los datos:  
Como vamos a utilizar estados durante el uso de la aplicación hago un resumen de los mismos. Tenemos nuestra clase **enum Estados** que recoge 3 Boolean:  
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
Así mismo tnemos nuestro **Datos.kt** en el que tenemos nuestro objeto singleton Datos con las var que vamos a usar en la App. 
```bash
object Datos {
    var palabra = ""
    var palabraJugador = ""
    var sinonimo = ""
    var ronda = 0
    var aciertos = 0
    var fallos = 0
}
```
Y tamnbién dentro de **Datos.kt** tenemos nuetra clase Diccionario en la que tenemos nuestra lista de sinónimos.
```bash
enum class Diccionario(val id:Int, val nombre:String, val sinonimo:String){
    PALABRA1(id = 1, "listo", "inteligente"),
    PALABRA2(id = 2, "puerta", "entrada"),
    PALABRA3(id = 3, "luna", "satelite"),
    PALABRA4(id = 4, "rapido", "veloz"),
    PALABRA5(id = 5, "lento", "tortuga"),
    PALABRA6(id = 6, "dificil", "complicado"),
    PALABRA7(id = 7, "tonto", "bobo")
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
## 3. **Rondas y Aciertos (ViewModel)**

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
## 4. **Vista de la app (UI)**  
La función **MyApp** es la función principal que organiza todos los componentes en la interfaz de usuario.  
Tenemos un Box con una Column dentro de la que tenemos 6 Row en la que se encuentran todos los elementos de la vista.  
- En la primera Row tenemos ShowRondas que es un texto que indica el numero de ronda.
- En la segunda Row tenem ShowSinonimo que es otro texto en el que se muestra al jugador el sinńomi que debe buscar.  
- En la tercera Row tenemos TextNombreEscribir con el Textfield para que escriba el jugador.  
- En la cuarta Row tenemos nuestro boton ButtonEnter, para que el usuario introduzca su palabra.  
- En la quinta Row tenemos los textos ShowAciertos y ShowFallos que indican al jugador cuantas palabras acerto y cuantos fallos lleva en la ronda actual.  
- En la ultima Row tenemos nuestro boton ButtonStart para que el jugador inicie las rondas.  
![fotoSinonimos](https://github.com/user-attachments/assets/f8044369-0e3b-4512-b5c2-2c44d0d4f9a2)











