package com.libreriag.app.ui.addbook


import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.libreriag.app.viewmodel.BookViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddBookScreen(onSaved: () -> Unit) {
    val vm: BookViewModel = viewModel()
    val title by vm.title.collectAsState()
    val author by vm.author.collectAsState()
    val titleError by vm.titleError.collectAsState()
    val authorError by vm.authorError.collectAsState()

    val snackbar = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    var isSaving by remember { mutableStateOf(false) }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbar) },
        topBar = {
            TopAppBar(
                title = { Text("Agregar Libro") }
            )
        }
    ) { padding ->
        Column(
            Modifier
                .padding(padding)
                .padding(20.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            OutlinedTextField(
                value = title,
                onValueChange = vm::onTitleChange,
                label = { Text("Título") },
                leadingIcon = { Icon(Icons.Default.Book, contentDescription = "Título") },
                isError = titleError != null,
                supportingText = {
                    if (titleError != null)
                        Text(titleError!!, color = MaterialTheme.colorScheme.error)
                },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = author,
                onValueChange = vm::onAuthorChange,
                label = { Text("Autor") },
                leadingIcon = { Icon(Icons.Default.Person, contentDescription = "Autor") },
                isError = authorError != null,
                supportingText = {
                    if (authorError != null)
                        Text(authorError!!, color = MaterialTheme.colorScheme.error)
                },
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = {
                    isSaving = true
                    vm.save()

                    if (titleError == null && authorError == null) {
                        scope.launch { snackbar.showSnackbar("Libro agregado correctamente") }
                        onSaved()
                    } else {
                        scope.launch { snackbar.showSnackbar("Revisa los campos obligatorios") }
                    }

                    isSaving = false
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                AnimatedContent(
                    targetState = isSaving,
                    transitionSpec = { fadeIn() togetherWith fadeOut() }
                ) { saving ->
                    if (saving)
                        CircularProgressIndicator(
                            modifier = Modifier.size(22.dp),
                            strokeWidth = 2.dp
                        )
                    else
                        Text("Guardar")
                }
            }

            AnimatedVisibility(
                visible = titleError == null && authorError == null &&
                        title.isNotEmpty() && author.isNotEmpty(),
                enter = fadeIn() + scaleIn(),
                exit = fadeOut()
            ) {
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(Icons.Default.CheckCircle, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text("Formulario válido")
                }
            }

            AnimatedVisibility(
                visible = titleError != null || authorError != null,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(Icons.Default.Warning, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text("Revisa los campos ingresados")
                }
            }
        }
    }
}
