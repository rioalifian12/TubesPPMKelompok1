package id.ac.unpas.tubes.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import id.ac.unpas.tubes.ui.theme.Purple700
import id.ac.unpas.tubes.ui.theme.Teal200
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import kotlinx.coroutines.launch
import java.time.format.DateTimeFormatter

@Composable
fun FormPencatatanMotorScreen(navController : NavHostController, id: String? = null, modifier: Modifier = Modifier) {
    val isLoading = remember { mutableStateOf(false) }
    val buttonLabel = if (isLoading.value) "Mohon tunggu..." else
        "Simpan"
    val tanggalDialogState = rememberMaterialDialogState()
    val viewModel = hiltViewModel<PengelolaanMotorViewModel>()
    val scope = rememberCoroutineScope()
    val model = remember { mutableStateOf(TextFieldValue("")) }
    val warna = remember { mutableStateOf(TextFieldValue("")) }
    val kapasitas = remember { mutableStateOf(TextFieldValue("")) }
    val tanggal_rilis = remember { mutableStateOf(TextFieldValue("")) }
    val harga = remember { mutableStateOf(TextFieldValue("")) }

    Column(modifier = modifier
        .padding(10.dp)
        .fillMaxWidth()) {
        OutlinedTextField(
            label = { Text(text = "Model") },
            value = model.value,
            onValueChange = {
                model.value = it
            },
            modifier = Modifier
                .padding(4.dp)
                .fillMaxWidth(),
            placeholder = { Text(text = "Model") }
        )
        OutlinedTextField(
            label = { Text(text = "Warna") },
            value = warna.value,
            onValueChange = {
                warna.value = it
            },
            modifier = Modifier
                .padding(4.dp)
                .fillMaxWidth(),
            placeholder = { Text(text = "Warna") }
        )
        OutlinedTextField(
            label = { Text(text = "Kapasitas") },
            value = kapasitas.value,
            onValueChange = {
                kapasitas.value = it
            },
            modifier = Modifier
                .padding(4.dp)
                .fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType =
            KeyboardType.Decimal),
            placeholder = { Text(text = "5") }
        )
        OutlinedTextField(
            label = { Text(text = "Tanggal Rilis") },
            value = tanggal_rilis.value,
            enabled = false,
            onValueChange = {
                tanggal_rilis.value = it
            },
            modifier = Modifier
                .padding(4.dp)
                .fillMaxWidth()
                .clickable {
                    tanggalDialogState.show()
                },
            textStyle = TextStyle(color = Color.Black)
        )
        OutlinedTextField(
            label = { Text(text = "Harga") },
            value = harga.value,
            onValueChange = {
                harga.value = it
            },
            modifier = Modifier
                .padding(4.dp)
                .fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType =
            KeyboardType.Decimal),
            placeholder = { Text(text = "5") }
        )
        val loginButtonColors = ButtonDefaults.buttonColors(
            backgroundColor = Purple700,
            contentColor = Teal200
        )
        val resetButtonColors = ButtonDefaults.buttonColors(
            backgroundColor = Teal200,
            contentColor = Purple700
        )
        Row (modifier = Modifier
            .padding(horizontal = 4.dp, vertical = 8.dp)
            .fillMaxWidth()) {
            Button(modifier = Modifier
                .weight(1f)
                .padding(end = 4.dp),
                onClick = {
                    if (model.value.text.isNotBlank() && warna.value.text.isNotBlank() && kapasitas.value.text.isNotBlank() && tanggal_rilis.value.text.isNotBlank() && harga.value.text.isNotBlank()) {
                        if (id == null) {
                            scope.launch {
                                viewModel.insert(model.value.text, warna.value.text, Integer.parseInt(kapasitas.value.text),
                                    tanggal_rilis.value.text, Integer.parseInt(harga.value.text))
                            }
                        } else {
                            scope.launch {
                                viewModel.update(id, model.value.text, warna.value.text, Integer.parseInt(kapasitas.value.text),
                                    tanggal_rilis.value.text, Integer.parseInt(harga.value.text))
                            }
                        }
                        if (!isLoading.value) {
                            navController.navigate("pengelolaan-motor")
                        }
                    }
                }, colors = loginButtonColors) {
                Text(
                    text =  buttonLabel,
                    style = TextStyle(
                        color = Color.White,
                        fontSize = 18.sp
                    ), modifier = Modifier.padding(8.dp)
                )
            }
            Button(modifier = Modifier
                .weight(1f)
                .padding(start = 4.dp),
                onClick = {
                    model.value = TextFieldValue("")
                    warna.value = TextFieldValue("")
                    kapasitas.value = TextFieldValue("")
                    tanggal_rilis.value = TextFieldValue("")
                    kapasitas.value = TextFieldValue("")
                }, colors = resetButtonColors) {
                Text(
                    text = "Reset",
                    style = TextStyle(
                        color = Color.White,
                        fontSize = 18.sp
                    ), modifier = Modifier.padding(8.dp)
                )
            }
        }
    }
    viewModel.isLoading.observe(LocalLifecycleOwner.current) {
        isLoading.value = it
    }

    if (id != null) {
        LaunchedEffect(scope) {
            viewModel.loadItem(id) { SepedaMotor ->
                SepedaMotor?.let {
                    model.value = TextFieldValue(SepedaMotor.model)
                    warna.value = TextFieldValue(SepedaMotor.warna)
                    kapasitas.value = TextFieldValue(SepedaMotor.kapasitas.toString())
                    tanggal_rilis.value = TextFieldValue(SepedaMotor.tanggal_rilis)
                    harga.value = TextFieldValue(SepedaMotor.harga.toString())
                }
            }
        }
    }

    MaterialDialog(dialogState = tanggalDialogState, buttons = {
        positiveButton("OK")
        negativeButton("Batal")
    }) {
        datepicker { date ->
            tanggal_rilis.value =
                TextFieldValue(date.format(DateTimeFormatter.ISO_LOCAL_DATE))
        }
    }
}
