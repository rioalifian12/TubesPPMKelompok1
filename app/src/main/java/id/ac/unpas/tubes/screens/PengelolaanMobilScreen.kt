package id.ac.unpas.tubes.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import kotlinx.coroutines.launch
import id.ac.unpas.tubes.model.Mobil

@Composable
fun PengelolaanMobilScreen(navController : NavHostController, modifier: Modifier = Modifier, snackbarHostState: SnackbarHostState) {
    val viewModel = hiltViewModel<PengelolaanMobilViewModel>()
    val scope = rememberCoroutineScope()
    val items: List<Mobil> by viewModel.listData.observeAsState(initial = listOf())

    Column(modifier = modifier.fillMaxWidth()) {
        Button(modifier = Modifier
            .padding(15.dp)
            .fillMaxWidth(),
            onClick = {
                navController.navigate("tambah-pengelolaan-mobil")
            }) {
            Text(text = "Tambah Data", modifier = Modifier.padding(4.dp))
        }
        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            items(items = items, itemContent = { item ->
                MobilItem(item = item, navController = navController) {
                    scope.launch {
                        viewModel.delete(it)
                    }
                }
            })
        }
    }

    LaunchedEffect(scope) {
        viewModel.loadItems()
    }

    viewModel.success.observe(LocalLifecycleOwner.current) {
        if (it) {
            scope.launch {
                viewModel.loadItems()
            }
        }
    }

    viewModel.toast.observe(LocalLifecycleOwner.current) {
        scope.launch {
            snackbarHostState.showSnackbar(it, actionLabel =
            "Tutup", duration = SnackbarDuration.Short)
        }
    }
}