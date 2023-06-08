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
fun FormPencatatanPromoScreen(navController : NavHostController, id: String? = null, modifier: Modifier = Modifier) {
    val isLoading = remember { mutableStateOf(false) }
    val buttonLabel = if (isLoading.value) "Mohon tunggu..." else
        "Simpan"
    val awalDialogState = rememberMaterialDialogState()
    val akhirDialogState = rememberMaterialDialogState()
    val viewModel = hiltViewModel<PengelolaanPromoViewModel>()
    val scope = rememberCoroutineScope()
    val model = remember { mutableStateOf(TextFieldValue("")) }
    val tanggal_awal = remember { mutableStateOf(TextFieldValue("")) }
    val tanggal_akhir = remember { mutableStateOf(TextFieldValue("")) }
    val persentase = remember { mutableStateOf(TextFieldValue("")) }

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
            label = { Text(text = "Tanggal Awal") },
            value = tanggal_awal.value,
            enabled = false,
            onValueChange = {
                tanggal_awal.value = it
            },
            modifier = Modifier
                .padding(4.dp)
                .fillMaxWidth()
                .clickable {
                    awalDialogState.show()
                },
            textStyle = TextStyle(color = Color.Black)
        )
        OutlinedTextField(
            label = { Text(text = "Tanggal Akhir") },
            value = tanggal_akhir.value,
            enabled = false,
            onValueChange = {
                tanggal_akhir.value = it
            },
            modifier = Modifier
                .padding(4.dp)
                .fillMaxWidth()
                .clickable {
                    akhirDialogState.show()
                },
            textStyle = TextStyle(color = Color.Black)
        )
        OutlinedTextField(
            label = { Text(text = "Persentase") },
            value = persentase.value,
            onValueChange = {
                persentase.value = it
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
                    if (model.value.text.isNotBlank() && tanggal_awal.value.text.isNotBlank() && tanggal_akhir.value.text.isNotBlank() && persentase.value.text.isNotBlank()) {
                        if (id == null) {
                            scope.launch {
                                viewModel.insert(model.value.text, tanggal_awal.value.text, tanggal_akhir.value.text,
                                    Integer.parseInt(persentase.value.text))
                            }
                        } else {
                            scope.launch {
                                viewModel.update(id, model.value.text, tanggal_awal.value.text, tanggal_akhir.value.text,
                                    Integer.parseInt(persentase.value.text))
                            }
                        }
                        if (!isLoading.value) {
                            navController.navigate("pengelolaan-promo")
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
                    tanggal_awal.value = TextFieldValue("")
                    tanggal_akhir.value = TextFieldValue("")
                    persentase.value = TextFieldValue("")
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
            viewModel.loadItem(id) { Promo ->
                Promo?.let {
                    model.value = TextFieldValue(Promo.model)
                    tanggal_awal.value = TextFieldValue(Promo.tanggal_awal)
                    tanggal_akhir.value = TextFieldValue(Promo.tanggal_akhir)
                    persentase.value = TextFieldValue(Promo.persentase.toString())
                }
            }
        }
    }

    MaterialDialog(dialogState = awalDialogState, buttons = {
        positiveButton("OK")
        negativeButton("Batal")
    }) {
        datepicker { date ->
            tanggal_awal.value =
                TextFieldValue(date.format(DateTimeFormatter.ISO_LOCAL_DATE))
        }
    }

    MaterialDialog(dialogState = akhirDialogState, buttons = {
        positiveButton("OK")
        negativeButton("Batal")
    }) {
        datepicker { date ->
            tanggal_akhir.value =
                TextFieldValue(date.format(DateTimeFormatter.ISO_LOCAL_DATE))
        }
    }
}