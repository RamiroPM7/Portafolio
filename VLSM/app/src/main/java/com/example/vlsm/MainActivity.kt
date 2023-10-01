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

var arrayExponentes = ArrayList<String>()
var arrayExponentesBits = ArrayList<Int>()
var arrayAND = ArrayList<String>()
var arrayANDDecimal = ArrayList<String>()
var arrayAlfabeto = ArrayList<Char>()
var arrayHost = ArrayList<String>()
var IPEnpartes = ArrayList<String>()
var IPEnpartesBinario = ArrayList<String>()
var MaskEnpartes = ArrayList<String>()
var MaskEnpartesBinario = ArrayList<String>()
var arrayHostMayorAMenor = ArrayList<Int>()
var arrayHostMayorAMenor2 = ArrayList<Int>()
var arregloNetwork = ArrayList<Int>()   //este areglo
var resultado = ""
var Nbits = 0

var exponente = 0.0
var NtotalExpo = 0//variable que tiene todos los host sumados
var index = 0

//----------------------------------------------------------------
@Preview
@Composable
fun preview() {
    body()
}//preview

//----------------------------------------------------------------
@Composable
fun body() {

    for (letra in 'A'..'Z') {
        arrayAlfabeto.add(letra)
    }

    var resultado by rememberSaveable { mutableStateOf("") }
    var IPinput by rememberSaveable { mutableStateOf("") }
    var Maskinput by rememberSaveable { mutableStateOf("") }
    var Nhost by rememberSaveable { mutableStateOf("") }
    var showDialog by rememberSaveable { mutableStateOf(false) }

    var modifier = Modifier

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
                boton(IPinput = IPinput, Maskinput = Maskinput, "Calcular") {
                    resultado = calcular(IPinput = IPinput, Maskinput = Maskinput)
                }
//estas llaves representan una parte del código que no es composable, por eso me daba error
                boton(IPinput = IPinput, Maskinput = Maskinput, "Agregar") {
                    showDialog = true

                }
                if (showDialog) {
                    Dialog(
                        show = showDialog,
                        { showDialog = false },
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
@Composable
fun boton(IPinput: String, Maskinput: String, text: String, onClick: () -> Unit) {

    Button(
        onClick = { onClick() },
        // enabled = IPinput.isNotEmpty() && Maskinput.isNotEmpty()
    ) {
        Text(text = text)
    }//Button

}//boton

//----------------------------------------------------------------

fun calcular(IPinput: String, Maskinput: String): String {
    limpiar()

    resultado += "Estos son los hosts que ingresaste: "
    /*estas dos variables me permiten tener la ip o la mask en una cadena ya que anteriormente eran
    * un array en 4 partes. Más adelante estas cadenas serán un array dividido por cada dígito*/
    var maskCadena = ""
    var IPCadena = ""
    IPEnpartes = IPinput.split(".").toTypedArray().toCollection(ArrayList())
    MaskEnpartes = Maskinput.split(".").toTypedArray().toCollection(ArrayList())

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


    //dividimos la mask en digitos independientes y la IP
    var ipEnCaracteresB = IPCadena.map { it.toString() }.toMutableList()//divide la IP completa en cada dígito individual List<String>
    var maskEnCaracteresB = maskCadena.map { it.toString() }.toMutableList()//divide la máscara completa en cada dígito individual List<String>


    //esto nos ayuda a contar la cantidad de bist que hay en un principio
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

    var octeto2 = ""
    var j2 = 0
    for (i in 0 until arrayAND.size) {

        when (i) {
            7, 15, 23, 31 -> {
                octeto2 += arrayAND.get(i)
            //    arrayANDDecimal.add(octeto2)   //aquí el arregloANDDecimal tendrá 4 octetos en binario //aquí en array no es decimal todavía
                arrayANDDecimal.add(binarioAdecimal(octeto2))
                println("AA " +   arrayANDDecimal.get(j2))
                octeto2 = ""
                j2++
            }//8,16,24,32
            else -> {
                octeto2 += arrayAND.get(i)
            }
        }//when
    }//for




    //calculamos los bits que faltan hasta 32 y el resultado lo elevamos con 2
    var tamano = 0
    tamano = 32 - Nbits
    tamano = Math.pow(2.0,tamano.toDouble()).toInt()

    for (i in index until arrayHost.size) {//recorre tod0 el array para sumarlos en NtotalHost y los imprime
        /*INDEX nos recuerda el valor de i en el que nos quedamos porque si el usuario ingresa N cantidad de host y presiona calcular
        el programa imprime dos veces los mismo o se sale de rango y con el index podemos resetear el valor de i a 0
        */

        //con esto calcularemos el exponente de dos que nos permita alojar al host
        while ( Math.pow(2.0, exponente) < arrayHost.get(i).toInt() ) {
            exponente++
        }//while

        arrayExponentesBits.add(exponente.toInt()) //guarda la cantidad de bits que se usaron para cada host. Ejmplo: host 10 -> 2^4=16 -> bits 4
        arrayExponentes.add(Math.pow(2.0, exponente).toInt().toString())//resuelve las potencias de dos y las guarda en un array

        if (index < arrayHost.size) {
            NtotalExpo += arrayExponentes.get(i).toInt()// suma todos las potencias de dos para depués compararlo con el total
          //  println(arrayExponentes.get(i))
        }

        resultado += "\n" + arrayAlfabeto.get(i) + ") " + arrayHost.get(i) + "    " + arrayExponentes.get(i).toInt()//esta linea nos permite darle formato con el alfabeto
        exponente = 0.0
        index++
    } //for



    //verificamos que el tamaño sea el adecuado. No más de
    if (NtotalExpo > tamano) {
        resultado += "\nSuma de cada exponente de dos: $NtotalExpo"
        resultado += "\n\nEste ejercicio no se puede calcular"
    } else {
        resultado += "\n\nIP: $IPinput"
        resultado += "\nMáscara: $Maskinput"


        for (i in 0 until arrayExponentes.size) {
            arrayHostMayorAMenor.add(arrayExponentes.get(i).toInt())
        }
        for (i in 0 until arrayHost.size) {
            arrayHostMayorAMenor2.add(arrayHost.get(i).toInt())
        }

        var arrayhostOrdenados = arrayHostMayorAMenor.sortedDescending()
        var arrayHostParaMostrar = arrayHostMayorAMenor2.sortedDescending()
        var arrayExponentesBitsOdenado = arrayExponentesBits.sortedDescending()

        resultado += "\n\nResultado:  "

       // var arregloBroadcast = ArrayList<Int>()
        var octeto4 = arrayANDDecimal.get(3).toInt()
        var octeto3 = arrayANDDecimal.get(2).toInt()
        var octeto2 = arrayANDDecimal.get(1).toInt()
        var octeto1 = arrayANDDecimal.get(0).toInt()
        var divisionResultado = 0
        var divisionResiduo = 0
        var suma = 0
        var octeto3PrimeraIP = 0

        for (i in 0 until arrayhostOrdenados.size) {
            //se agrega el octeto 4 al array arregloNetwork
            if (i == 0){

                    arregloNetwork.add(octeto4)
                    suma = octeto4 + arrayhostOrdenados.get(0)
                    resultado +="\n\nHOST: " + arrayHostParaMostrar.get(i)
                    if(suma >= 256){
                        octeto3PrimeraIP = arrayhostOrdenados.get(i)/256
                        octeto3PrimeraIP += -1
                        resultado += "\n$octeto1 . $octeto2 . $octeto3 . $octeto4 -> " + "$octeto1 . $octeto2 ."+ octeto3PrimeraIP +". 255 /"+ (32- arrayExponentesBitsOdenado.get(i).toInt()) +"\n"
                    }else{
                        resultado += "\n$octeto1 . $octeto2 . $octeto3 . $octeto4 -> " + "$octeto1 . $octeto2 . $octeto3 . " + (octeto4 + arrayhostOrdenados.get(0) - 1)+"/"+ (32-arrayExponentesBitsOdenado.get(i).toInt()) +"\n"
                    }


            }else if(i != 0){

                resultado +="\nHOST: " + arrayHostParaMostrar.get(i)
                octeto4 = arregloNetwork.get(i-1) +  arrayhostOrdenados.get(i-1)//la suma es el valor del network inicial
              //  octeto4Nuevo = arregloNetwork.get(i-1) +  arrayPerro.get(i-1)
//                println(arregloNetwork.get(i-1))
//                println(arrayPerro.get(i-1))

                if (octeto4 >= 256){

                    divisionResiduo = octeto4 % 256
                    divisionResultado = octeto4 / 256
                    octeto4 = divisionResiduo
                    octeto3 += divisionResultado
                    arregloNetwork.add( octeto4 )

                 //   resultado += "\n$octeto1 . $octeto2 . $octeto3 . $octeto4 -> 255"
                    resultado += "\n$octeto1 . $octeto2 . $octeto3 . $octeto4 -> "  + "$octeto1 . $octeto2 . $octeto3 . " + (octeto4 + arrayhostOrdenados.get(i)-1)+"/"+ (32-arrayExponentesBitsOdenado.get(i).toInt()) +"\n"

                }else if(octeto3 >= 256){

                    divisionResiduo = octeto3 % 256
                    divisionResultado = octeto3 / 256
                    octeto3 = divisionResiduo
                    octeto2 += divisionResultado

                        resultado += "\n$octeto1 . $octeto2 . $octeto3 . $octeto4 -> "  + "$octeto1 . $octeto2 . $octeto3 . " + (octeto4 + arrayhostOrdenados.get(i)-1)+"/"+ (32-arrayExponentesBitsOdenado.get(i).toInt()) +"\n"



                }else if (octeto2 >= 256){

                    divisionResiduo = octeto2 % 256
                    divisionResultado = octeto2 / 256
                    octeto2 = divisionResiduo
                    octeto1 += divisionResultado

                        resultado += "\n$octeto1 . $octeto2 . $octeto3 . $octeto4 -> "  + "$octeto1 . $octeto2 . $octeto3 . " + (octeto4 + arrayhostOrdenados.get(i)-1)+"/"+ (32-arrayExponentesBitsOdenado.get(i).toInt()) +"\n"


                }else if(octeto1 >= 256){

                    divisionResiduo = octeto1 % 256
                    //  divisionResultado = octeto1 / 256
                    octeto1 = divisionResiduo
                    //   octeto1 += divisionResultado
                    //   arregloNetwork.add( octeto1 )

                        resultado += "\n$octeto1 . $octeto2 . $octeto3 . $octeto4 -> "  + "$octeto1 . $octeto2 . $octeto3 . " + (octeto4 + arrayhostOrdenados.get(i)-1)+"/"+ (32-arrayExponentesBitsOdenado.get(i).toInt()) +"\n"


                } else{
                    octeto4 = arregloNetwork.get(i-1) +  arrayhostOrdenados.get(i-1)
                    arregloNetwork.add(octeto4)
                    resultado += "\n$octeto1 . $octeto2 . $octeto3 . $octeto4 -> "  + "$octeto1 . $octeto2 . $octeto3 . " + (octeto4 + arrayhostOrdenados.get(i)-1)+"/"+ (32-arrayExponentesBitsOdenado.get(i).toInt()) +"\n"
                }
            }
        }//for
    }//else
//8,13,25,29

//    for (i in 0 until arregloNetwork.size){
//        println(arregloNetwork.get(i))
//    }

    return resultado
}

//----------------------------------------------------------------

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

fun agregarAlArray(
    name: String,
    //  onValueChange: (String) -> Unit
) {
    arrayHost.add(name)

}

fun limpiar() {
    resultado = ""
    index = 0
    arrayExponentes.clear()
    NtotalExpo = 0

}

fun decimalAbinario(decimal: String): String {

    var numeroBinarioString = Integer.toString(Integer.parseInt(decimal), 2)
    var numeroBinarioRelleno = numeroBinarioString.padStart(8, '0')

    return numeroBinarioRelleno
}//decimalAbinario

fun binarioAdecimal(binario: String): String {
    return Integer.toString(Integer.parseInt(binario, 2))
}//binariodecimal