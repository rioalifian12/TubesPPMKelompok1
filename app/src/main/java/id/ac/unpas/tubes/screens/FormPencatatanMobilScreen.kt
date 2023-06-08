package id.ac.unpas.tubes.screens

import android.graphics.Paint.Align
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import id.ac.unpas.tubes.ui.theme.Purple700
import id.ac.unpas.tubes.ui.theme.Teal200
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import kotlinx.coroutines.launch
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import kotlin.math.exp

@Composable
fun FormPencatatanMobilScreen(navController : NavHostController, id: String? = null, modifier: Modifier = Modifier) {
    val dijualOptions = listOf(0, 1);
    val bahanBakarOptions = listOf("--Pilih--", "Bensin" ,"Solar", "Listrik")
    var expandDropdown by remember { mutableStateOf(false) }
    val isLoading = remember { mutableStateOf(false) }
    val buttonLabel = if (isLoading.value) "Mohon tunggu..." else
        "Simpan"

    val viewModel = hiltViewModel<PengelolaanMobilViewModel>()
    val scope = rememberCoroutineScope()

    val merk = remember { mutableStateOf(TextFieldValue("")) }
    val model = remember { mutableStateOf(TextFieldValue("")) }
    val (bahan_bakar, setBahanBakar) = remember { mutableStateOf(bahanBakarOptions[0]) }
    val (dijual, setDijual) = remember { mutableStateOf(dijualOptions[0]) }
    val deskripsi = remember { mutableStateOf(TextFieldValue("")) }

    val icon = if (expandDropdown)
        Icons.Filled.KeyboardArrowUp
    else
        Icons.Filled.KeyboardArrowDown

    Column(modifier = modifier
        .padding(10.dp)
        .fillMaxWidth()) {
        OutlinedTextField(
            label = { Text(text = "Merk") },
            value = merk.value,
            onValueChange = {
                merk.value = it
            },
            modifier = Modifier
                .padding(4.dp)
                .fillMaxWidth(),
            placeholder = { Text(text = "Merk") }
        )

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

        Box(
            modifier = Modifier.padding(top = 8.dp)
        ){
            OutlinedTextField(
                onValueChange = {},
                enabled = false,
                value = bahan_bakar,
                modifier = Modifier
                    .padding(4.dp)
                    .fillMaxWidth()
                    .clickable { expandDropdown = !expandDropdown },
                trailingIcon = {
                    Icon(icon, "dropdown icon")
                },
                textStyle = TextStyle(color = Color.Black)
            )

            DropdownMenu(
                expanded = expandDropdown,
                onDismissRequest = { expandDropdown = false },
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                bahanBakarOptions.forEach { label ->
                    DropdownMenuItem(
                        onClick = {
                            setBahanBakar(label)
                            expandDropdown = false
                        },
                        enabled = label != bahanBakarOptions[0])
                    {
                        Text(text = label)
                    }
                }
            }
        }

        Row(
            Modifier
                .selectableGroup()
                .padding(top = 8.dp)
        ) {
            dijualOptions.forEach { text ->
                Row(
                    Modifier
                        .selectable(
                            selected = (text == dijual),
                            onClick = { setDijual(text) },
                            role = Role.RadioButton
                        )
                        .padding(end = 20.dp)
                ) {
                    RadioButton(
                        selected = (text == dijual),
                        onClick = { setDijual(text) })

                    val dijualText = when (text) {
                        0 -> "Tidak"
                        else -> "Ya"
                    }

                    Text(
                        text = dijualText,
                        fontSize = 18.sp,
                        modifier = Modifier.padding(top = 12.dp)
                    )
                }
            }
        }

        OutlinedTextField(
            label = { Text(text = "Deskripsi") },
            value = deskripsi.value,
            onValueChange = {
                deskripsi.value = it
            },
            modifier = Modifier
                .padding(4.dp)
                .fillMaxWidth(),
            placeholder = { Text(text = "Deskripsi") }
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
            .fillMaxWidth()
        ) {
            Button(modifier = Modifier
                .weight(1f)
                .padding(end = 4.dp),
                onClick = {
                    if (merk.value.text.isNotBlank() && merk.value.text.isNotBlank() && bahan_bakar != bahanBakarOptions[0] && deskripsi.value.text.isNotBlank()) {
                        if (id == null) {
                            scope.launch {
                                viewModel.insert(model.value.text, merk.value.text, bahan_bakar, dijual, deskripsi.value.text)
                            }
                        } else {
                            scope.launch {
                                viewModel.update(id, model.value.text, merk.value.text, bahan_bakar, dijual, deskripsi.value.text)
                            }
                        }
                        if (!isLoading.value) {
                            navController.navigate("pengelolaan-mobil")
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
                    merk.value = TextFieldValue("")
                    model.value = TextFieldValue("")
                    setBahanBakar(bahanBakarOptions[0])
                    deskripsi.value = TextFieldValue("")
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
            viewModel.loadItem(id) { Mobil ->
                Mobil?.let {
                    merk.value = TextFieldValue(Mobil.merk)
                    model.value = TextFieldValue(Mobil.model)
                    setBahanBakar(Mobil.bahan_bakar)
                    setDijual(Mobil.dijual)
                    deskripsi.value = TextFieldValue(Mobil.deskripsi)
                }
            }
        }
    }
}