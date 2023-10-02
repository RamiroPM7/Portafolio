package com.example.vlsm

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.vlsm.ui.theme.VLSMTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            preview()
        }
    }
}

var arrayExponentes = ArrayList<String>() //Nos permite guardar todas las potencias de 2 que alojan a cada host ej. 8,16,32,64,128,512
var arrayExponentesBits = ArrayList<Int>() //Nos permite guardar todas las potencias de 2 que alojan a cada host pero en bits ej: 0,1,2,3,4,5
var arrayAND = ArrayList<String>() //Nos permite guardar el resultado del desarrollo AND entre la IP y la Máscara de subred
var arrayANDDecimal = ArrayList<String>() //Nos permite guardar el resultado del desarrollo AND entre la IP y la Máscara de subred pero en decimal
var arrayAlfabeto = ArrayList<Char>() //Tiene el abecedario guardado para mostrar el host A, B, C, D...
var arrayHost = ArrayList<String>() //Tiene todos los host que digitó el usuario
var IPEnpartes = ArrayList<String>() //Tiene la IP dividida en 4 octetos
var IPEnpartesBinario = ArrayList<String>() //Tiene la IP dividida en 4 octetos pero en binario
var MaskEnpartes = ArrayList<String>() //Tiene la Máscara de subred dividida en 4 octetos
var MaskEnpartesBinario = ArrayList<String>() //Tiene la Máscara de subred dividida en 4 octetos en binario
var arrayExponentesMayorAMenor = ArrayList<Int>() //Guarda todos los resultados de las potencias de dos para más delante ordenarlos
var arrayHostMayorAMenor2 = ArrayList<Int>() //Guarda todos los hosts que digitó el usuario para más delante ordenarlos
var arregloNetwork = ArrayList<Int>()   //guarda los valores del octeto 4 del resultado AND para hacer el procedimiento donde se suman los hosts
var resultado = "" //es la variable que muestra cada impresión en el Text de arriba en la interfaz (textArea())
var Nbits = 0 //es un contador de la cantidad de bits de la máscara de subred

var exponente = 0.0 //es el contador del exponente de dos que podrá alojar a cada host
var NtotalExpo = 0//variable que tiene todos los host sumados
var index = 0 /*nos permite reiniciar un ciclo for cuando el usuario ingresó varios hosts, presionó calcular y de nuevo ingresó más hosts.
así se imprime tod0 se nuevo*/

//----------------------------------------------------------------
@Preview
@Composable
fun preview() { //nos permite mostrar toda la interfaz en android studio
    body()
}//preview

//----------------------------------------------------------------
@Composable
fun body() { //Contiene todos los elementos gráficos de la interfaz y es el cuerpo del proyecto

    for (letra in 'A'..'Z') {//Ciclo que agrega tod0 el abecedario en un array
        arrayAlfabeto.add(letra)
    }
    ///estos son estados que me permiten leer los datos que digitó el usuario en cada TextField y actualizar variables
    var resultado by rememberSaveable { mutableStateOf("") }//muestra tod0 el texto de la app en el método textArea()
    var IPinput by rememberSaveable { mutableStateOf("") } //recibe la IP
    var Maskinput by rememberSaveable { mutableStateOf("") } //recibe la Máscara
    var Nhost by rememberSaveable { mutableStateOf("") } //ribe cada host para después pasarlo al agregarAlArray()
    var showDialog by rememberSaveable { mutableStateOf(false) } //indica si es momento de mostrar el dialogo donde se agregan los hosts

    var modifier = Modifier

/*Aquí se configura cada elemento y se pasan los states como argumento porque se necesitan dentro de los métodos*/
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .fillMaxHeight(0.7f)
        ) {
            textArea(text = resultado, modifier)
        }
        Column(
            modifier = modifier
                .fillMaxWidth()
                .fillMaxHeight(1.0f),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            textFieldFun(
                texto = "Ingresa la IP",
                keyboardType = KeyboardType.Number,
                name = IPinput,
                onValueChange = { IPinput = it },
                singleLine = true
            )
            Spacer(modifier = modifier.padding(6.dp))
            textFieldFun(
                texto = "Ingresa la máscara de subred",
                keyboardType = KeyboardType.Number,
                name = Maskinput,
                onValueChange = { Maskinput = it },
                singleLine = true
            )
            Spacer(modifier = modifier.padding(8.dp))
            Row(
                modifier = modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                /*este botón delega un evento más arriba porque se necesitaba actualizar la variable resultado pero al ser local
                * no podía usarla directamente en el método, por eso la uso aquí. Ya que se calcula tod0 el VLSM se retorna a resultado para
                * mostrarse*/
                boton(text = "Calcular") {
                    resultado = calcular(IPinput = IPinput, Maskinput = Maskinput)
                }
//estas llaves representan una parte del código que no es composable, por eso me daba error
                //esto solo cambia el estado para ver si debe de mostrar el dialog
                boton(text = "Agregar") {
                    showDialog = true

                }
                /*configura el dialog y reinicia el texto del dialog cada que se ingresa un host*/
                if (showDialog) { Dialog(show = showDialog, { showDialog = false },
                        {
                            agregarAlArray(name = Nhost)
                            Nhost = ""
                        },
                        name = Nhost,
                        onValueChange = { Nhost = it })
                }

            }

        }
    }//Box

}//body

//----------------------------------------------------------------
/*Es la caja de texto que aparece primero en la interfaz y nos permite mostrar los resultados del procedimiento*/
@Composable
fun textArea(text: String, modifier: Modifier) {
    Text(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        text = text,
        fontSize = 16.sp
    )
}//textArea
//----------------------------------------------------------------
/*es la caja de texto que recibe la ip y la máscara de subred y están al fondo de la interfaz*/
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun textFieldFun(
    texto: String,
    keyboardType: KeyboardType,
    name: String,
    onValueChange: (String) -> Unit,
    singleLine: Boolean
) {

    //  userInput = dato
    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = name,
        onValueChange = { newValue -> onValueChange(newValue) },
        label = { Text(text = texto) },
        placeholder = {
            Text(
                text = "ej. 192.168.10.20",
                color = colorResource(id = R.color.ejemplo)
            )
        },
        shape = RoundedCornerShape(20.dp),
        colors = TextFieldDefaults.textFieldColors(
            containerColor = colorResource(id = R.color.Color_textFieldIP)
        ),//TextFieldDefaults.textFieldColors
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType
        ),
        singleLine = singleLine
    )
}//textFieldFun

//----------------------------------------------------------------
/*Son los dos botones del fondo que agregan cada host o calculan el VLSM*/
@Composable
fun boton( text: String, onClick: () -> Unit) {

    Button(
        onClick = { onClick() },

    ) {
        Text(text = text)
    }//Button

}//boton

//----------------------------------------------------------------
/*Este método contiene tod0 el procedimiento del VLSM*/
fun calcular(IPinput: String, Maskinput: String): String { //recibe la IP y la Máscara

    limpiar()//limpia el texto por si se agrega más hosts y limpia algunas variables

/* durante el código se verá mucho la linea 'resultado +=' y se trata de al variable que muestra o imprime TOD0*/

    resultado += "Estos son los hosts que ingresaste: "

    IPEnpartes = IPinput.split(".").toTypedArray().toCollection(ArrayList())
    MaskEnpartes = Maskinput.split(".").toTypedArray().toCollection(ArrayList())

    /*estas dos variables me permiten tener la ip o la mask en una cadena ya que anteriormente eran
      un array en 4 partes. Más adelante estas cadenas serán un array dividido por cada dígito*/
    var maskCadena = ""
    var IPCadena = ""


    //transformamos el array IP decimal a binario
    for (i in 0 until IPEnpartes.size){
        IPEnpartesBinario.add(decimalAbinario(IPEnpartes.get(i)))
        IPCadena += IPEnpartesBinario.get(i)

    }//for

    //transformamos el array mask decimal a binario
    for(i in 0 until MaskEnpartes.size){
        MaskEnpartesBinario.add(decimalAbinario(MaskEnpartes.get(i))) //transforma cada octeto a binario y lo guarda en un array
        maskCadena += MaskEnpartesBinario.get(i)

    }//for


    //dividimos la mask en digitos independientes y la IP también
    var ipEnCaracteresB = IPCadena.map { it.toString() }.toMutableList()//divide la IP completa en cada dígito individual List<String>
    var maskEnCaracteresB = maskCadena.map { it.toString() }.toMutableList()//divide la máscara completa en cada dígito individual List<String>


    //esto nos ayuda a contar la cantidad de bist que hay en un principio en la máscara de subred
    for(i in 0 until 32){
            if (maskEnCaracteresB[i] == "1") {
               Nbits++
            } else {
              break
            }
    }//for

    //AND
    //quí hacemos el and y el resultado lo colocamos en un arreglo
    for (i in 0 until 32){
        if (ipEnCaracteresB[i] == "1" && maskEnCaracteresB[i]== "1"){
            arrayAND.add("1")
        }else{
            arrayAND.add("0")
        }

    }//for

    /*esto nos permite tener el resultado and en 4 partes para después pasar esas 4 partes a un array y convertirlo a decimal*/
    var octeto2 = ""
    var j2 = 0
    for (i in 0 until arrayAND.size) {

        when (i) {
            7, 15, 23, 31 -> {
                octeto2 += arrayAND.get(i)
                arrayANDDecimal.add(binarioAdecimal(octeto2))
                octeto2 = ""
                j2++
            }//8,16,24,32
            else -> {
                octeto2 += arrayAND.get(i)
            }
        }//when
    }//for




    /*calculamos los bits que faltan hasta 32 y el resultado lo elevamos con 2, esto nos permite saber el tamaño
    * que más adelante será comparado con la suma de los hosts*/
    var tamano = 0
    tamano = 32 - Nbits
    tamano = Math.pow(2.0,tamano.toDouble()).toInt()

    for (i in index until arrayHost.size) {//recorre tod0 el array para sumarlos en NtotalHost y los imprime

        /*INDEX nos recuerda el valor de i en el que nos quedamos porque si el usuario ingresa N cantidad de host y presiona calcular
        el programa imprime dos veces los mismo o se sale de rango y con el index podemos resetear el valor de i a 0
        */

        /*con esto calcularemos el exponente de dos que nos permita alojar al host. El ciclo avanza si la potencia de dos
        * es menor al host*/
        while ( Math.pow(2.0, exponente) < arrayHost.get(i).toInt() ) {
            exponente++
        }//while

        arrayExponentesBits.add(exponente.toInt()) //guarda la cantidad de bits que se usaron para cada host. Ejmplo: host 10 -> 2^4=16 -> bits 4
        arrayExponentes.add(Math.pow(2.0, exponente).toInt().toString())//resuelve las potencias de dos que fueron válidas y las guarda en un array

        if (index < arrayHost.size) {
            NtotalExpo += arrayExponentes.get(i).toInt()// suma todos las potencias de dos
        }

        resultado += "\n" + arrayAlfabeto.get(i) + ") " + arrayHost.get(i) + "    " + arrayExponentes.get(i).toInt()//esta linea nos permite darle formato a la impresión
        exponente = 0.0 // reinicia el exponente de dos para empezar de 0 con otro host
        index++ // aumenta el indice
    } //for



    //verificamos si el problema se puede resolver verificando el tamaño con la suma de los hosts
    /*si la suma es menor el programa de dice que no se puede realizar*/
    if (NtotalExpo > tamano) {
        resultado += "\nSuma de cada exponente de dos: $NtotalExpo"
        resultado += "\n\nEste ejercicio no se puede calcular"
    } else {
        //muestra la ip y la máscara
        resultado += "\n\nIP: $IPinput"
        resultado += "\nMáscara: $Maskinput"

        /*guarda el array de potencias de dos en otro array. Hace lo mismo con el de hosts. Solo se están duplicando*/
        for (i in 0 until arrayExponentes.size) {
            arrayExponentesMayorAMenor.add(arrayExponentes.get(i).toInt())
        }
        for (i in 0 until arrayHost.size) {
            arrayHostMayorAMenor2.add(arrayHost.get(i).toInt())
        }

        /*Ordena los hosts,los bits de cada potencia y las potencias resueltas de dos de mayor a menor*/
        var arrayExponentesOrdenados = arrayExponentesMayorAMenor.sortedDescending()
        var arrayHostParaMostrar = arrayHostMayorAMenor2.sortedDescending()
        var arrayExponentesBitsOdenado = arrayExponentesBits.sortedDescending()

        //imprime "resultado"
        resultado += "\n\nResultado:  "

       /*colocamos cada octeto del array en un variable y declaramos otras variables para el procedimiento*/
        var octeto4 = arrayANDDecimal.get(3).toInt()
        var octeto3 = arrayANDDecimal.get(2).toInt()
        var octeto2 = arrayANDDecimal.get(1).toInt()
        var octeto1 = arrayANDDecimal.get(0).toInt()
        var divisionResultado = 0
        var divisionResiduo = 0
        var suma = 0
        var octeto3PrimeraIP = 0

        for (i in 0 until arrayExponentesOrdenados.size) { //vamos a repetir el bucle según la cantidad de hosts

            if (i == 0){
            /*la primera vez calculamos si el octeto 4 + el primer host (el más grande) son iguales o mayores a 256.
            * si es verdadero imprimimos el host que nos digitaron, luego dividimos el primer host o la primer potencia de dos
            * que puede alojar al primer host ej. 400 -> 2^9 = 521 -> 521/256 y al resultado de restamos -1 porque será de ip de
            * broadcast,luego se imprime.
            * Si las suma no cumplecon esto se imprime cada octeto pero el octeto 4 se calcula sumando el octeto4 + el host más grande.
            * Al final de cada impresión se coloca la cantidad de bits de ese resultado, esto se hace con los bits que se usaron para cada potencia de
            * dos restándolos con 32.
            * */
                    arregloNetwork.add(octeto4)
                    suma = octeto4 + arrayExponentesOrdenados.get(0)
                    resultado +="\n\nHOST: " + arrayHostParaMostrar.get(i)
                    if(suma >= 256){
                        octeto3PrimeraIP = arrayExponentesOrdenados.get(i)/256
                        octeto3PrimeraIP += -1
                        resultado += "\n$octeto1 . $octeto2 . $octeto3 . $octeto4 -> " + "$octeto1 . $octeto2 ."+ octeto3PrimeraIP +". 255 /"+ (32- arrayExponentesBitsOdenado.get(i).toInt()) +"\n"
                    }else{
                        resultado += "\n$octeto1 . $octeto2 . $octeto3 . $octeto4 -> " + "$octeto1 . $octeto2 . $octeto3 . " + (octeto4 + arrayExponentesOrdenados.get(0) - 1)+"/"+ (32-arrayExponentesBitsOdenado.get(i).toInt()) +"\n"
                    }


            }else if(i != 0){

                /*Cuando el ciclo da su segunda vuelta se imprime el host que se está calculando, luego se suma el octeto 4 anterior
                * con la potencia resuelta de dos anterior, si el octeto 4 es mayor o igual a 256 se divide la potencia resuelta entre 256 y el resultado pasa
                * pasa sumando con el octeto 3. El residuo pasa a sere el octeto4 y se guarda el valor del octeto4 en un array, luego se muestra en pantalla.
                *
                * Si la suma no es mayor igual a 256 se suma el octeto 4 con la potencia resuelta anterior y se guarda en un arregloNetwork, luego se imprime.
                *
                * si mientras se hace la suma algún octeto que no sea el 4 se pasa o es igual a 256 se le hace el mismo procedimiento, el octeto 3 o 2 se
                * dividen y el residuo pasa a ser el valor actual se octeto que se está culculando, mientras que el resultado se suma con el octeto anterior
                * */

                resultado +="\nHOST: " + arrayHostParaMostrar.get(i)
                octeto4 = arregloNetwork.get(i-1) +  arrayExponentesOrdenados.get(i-1)//la suma es el valor del network inicial

                if (octeto4 >= 256){

                    divisionResiduo = octeto4 % 256
                    divisionResultado = octeto4 / 256
                    octeto4 = divisionResiduo
                    octeto3 += divisionResultado
                    arregloNetwork.add( octeto4 )

                 //   resultado += "\n$octeto1 . $octeto2 . $octeto3 . $octeto4 -> 255"
                    resultado += "\n$octeto1 . $octeto2 . $octeto3 . $octeto4 -> "  + "$octeto1 . $octeto2 . $octeto3 . " + (octeto4 + arrayExponentesOrdenados.get(i)-1)+"/"+ (32-arrayExponentesBitsOdenado.get(i).toInt()) +"\n"

                }else if(octeto3 >= 256){

                    divisionResiduo = octeto3 % 256
                    divisionResultado = octeto3 / 256
                    octeto3 = divisionResiduo
                    octeto2 += divisionResultado

                        resultado += "\n$octeto1 . $octeto2 . $octeto3 . $octeto4 -> "  + "$octeto1 . $octeto2 . $octeto3 . " + (octeto4 + arrayExponentesOrdenados.get(i)-1)+"/"+ (32-arrayExponentesBitsOdenado.get(i).toInt()) +"\n"



                }else if (octeto2 >= 256){

                    divisionResiduo = octeto2 % 256
                    divisionResultado = octeto2 / 256
                    octeto2 = divisionResiduo
                    octeto1 += divisionResultado

                        resultado += "\n$octeto1 . $octeto2 . $octeto3 . $octeto4 -> "  + "$octeto1 . $octeto2 . $octeto3 . " + (octeto4 + arrayExponentesOrdenados.get(i)-1)+"/"+ (32-arrayExponentesBitsOdenado.get(i).toInt()) +"\n"


                }else if(octeto1 >= 256){

                    divisionResiduo = octeto1 % 256
                    octeto1 = divisionResiduo


                        resultado += "\n$octeto1 . $octeto2 . $octeto3 . $octeto4 -> "  + "$octeto1 . $octeto2 . $octeto3 . " + (octeto4 + arrayExponentesOrdenados.get(i)-1)+"/"+ (32-arrayExponentesBitsOdenado.get(i).toInt()) +"\n"


                } else{
                    octeto4 = arregloNetwork.get(i-1) +  arrayExponentesOrdenados.get(i-1)
                    arregloNetwork.add(octeto4)
                    resultado += "\n$octeto1 . $octeto2 . $octeto3 . $octeto4 -> "  + "$octeto1 . $octeto2 . $octeto3 . " + (octeto4 + arrayExponentesOrdenados.get(i)-1)+"/"+ (32-arrayExponentesBitsOdenado.get(i).toInt()) +"\n"
                }
            }
        }//for
    }//else
//8,13,25,29

    return resultado
}

//----------------------------------------------------------------
/*Este dialog es el que se muestra después de presionar "Agregar" */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Dialog(
    show: Boolean,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    name: String,
    onValueChange: (String) -> Unit,
) {


    if (show) {
        AlertDialog(modifier = Modifier.fillMaxWidth(),
            onDismissRequest = { onDismiss() },
            dismissButton = {
                TextButton(onClick = { onDismiss() }) {
                    Text(text = "Cancelar")
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    onConfirm()
                }) {
                    Text(text = "Aceptar")
                }
            },
            title = { Text(text = "Agregar Subred") },
            text = {
                Column {
                    Text(text = "¿Cuántos hosts deseas ingresar?")
                    Spacer(modifier = Modifier.padding(4.dp))
                    TextField(
                        value = name, onValueChange = { newValue -> onValueChange(newValue) },
                        singleLine = true,
                        //    shape = RoundedCornerShape(20.dp),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number
                        ),
//                        colors = TextFieldDefaults.textFieldColors(
//                            containerColor = colorResource(id = R.color.DialogColor)
//                        )
                    )
                }
            }
        )
    }//if
}//agregarHost
//--------------------------------------------------------------
/*agrega un host al arrayHost cuando se presiona aceptar dentro del Dialog*/
fun agregarAlArray(
    name: String,
) {
    arrayHost.add(name)

}
//--------------------------------------------------------------
/*se encarga de limpiar el text principal y algunas variables*/
fun limpiar() {
    resultado = ""
    index = 0
    arrayExponentes.clear()
    NtotalExpo = 0

}
//--------------------------------------------------------------
/*transforma deciamal a binario*/
fun decimalAbinario(decimal: String): String {

    var numeroBinarioString = Integer.toString(Integer.parseInt(decimal), 2)
    var numeroBinarioRelleno = numeroBinarioString.padStart(8, '0')

    return numeroBinarioRelleno
}//decimalAbinario
//--------------------------------------------------------------

/*transforma  binario a deciamal*/
fun binarioAdecimal(binario: String): String {
    return Integer.toString(Integer.parseInt(binario, 2))
}//binariodecimal