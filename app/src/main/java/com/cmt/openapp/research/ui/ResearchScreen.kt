package com.cmt.openapp.research.ui

import android.app.DatePickerDialog
import android.widget.DatePicker
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.cmt.openapp.R
import com.cmt.openapp.core.navigation.Routes
import com.cmt.openapp.core.ui.shared.buttonNavigate.MyButton
import com.cmt.openapp.core.ui.shared.dialog.InfoContent
import com.cmt.openapp.core.ui.shared.dialog.TopDialogSheet
import com.cmt.openapp.research.ui.viewmodel.SearchViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Calendar

@Composable
fun ResearchScreen(
    modifier: Modifier = Modifier,
    viewModel: SearchViewModel = hiltViewModel(),
    navigationController: NavHostController,
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadAllIncidents()
    }

    var isBottomSheetVisible by rememberSaveable { mutableStateOf(false) }
    var isTopDialogVisible by rememberSaveable { mutableStateOf(false) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(bottom = 16.dp)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            HeaderResearch {
                if (!isBottomSheetVisible) {
                    isTopDialogVisible = true
                }
            }

            when {
                uiState.isLoading -> {
                    Box(modifier = Modifier.fillMaxSize()) {
                        CircularProgressIndicator(Modifier.align(Alignment.Center))
                    }
                }

                uiState.errorMessage != null -> {
                    uiState.errorMessage?.let { errorMessage ->
                        Box(modifier = Modifier.fillMaxSize()) {
                            Text(
                                text = errorMessage,
                                color = Color.Red,
                                modifier = Modifier.align(Alignment.Center)
                            )
                        }
                    }
                }

                else -> {
                    LazyColumn(
                        Modifier
                            .fillMaxSize()
                            .weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        items(uiState.incidents) { incident ->
                            IncidentBox(
                                {
                                    navigationController.navigate(Routes.DetailIncidentScreen.createRoute(incident.nroIncidente))
                                },
                                incident.nroIncidente,
                                incident.fecha,
                                incident.hora,
                                incident.tipoIncidente
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(75.dp))
        }

        if (isBottomSheetVisible) {
            BottomSheetWithContent(viewModel, onDismiss = { isBottomSheetVisible = false })
        } else if (isTopDialogVisible) { // Verifica que no haya un BottomSheet visible antes de mostrar el diálogo
            TopDialogSheet(onDismissRequest = { isTopDialogVisible = false }) {
                InfoContent()
            }
        }

        MyButton(
            navigate = {
                if (!isTopDialogVisible) {
                    isBottomSheetVisible = true
                }
            },
            textButton = "Mostrar Filtros",
            myIconButton = Icons.Default.Search,
            modifier = Modifier.align(Alignment.BottomCenter)
        )

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheetWithContent(viewModel: SearchViewModel, onDismiss: () -> Unit) {
    ModalBottomSheet(
        onDismissRequest = { onDismiss() },
        modifier = Modifier
            .fillMaxWidth(),
        containerColor = MaterialTheme.colorScheme.primaryContainer,
        contentColor = Color.Black,
        shape = RoundedCornerShape(topStart = 110.dp, topEnd = 110.dp)
    ) {
        BottomSheetContent(viewModel)
    }
}

@Composable
fun BottomSheetContent(viewModel: SearchViewModel) {
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val calendar = Calendar.getInstance()
    val datePickerDialog = remember {

        val maxDate = Calendar.getInstance().apply {
            set(Calendar.YEAR, 2024)
            set(Calendar.MONTH, 3)
            set(Calendar.DAY_OF_MONTH, 1)
        }

        val minDate = Calendar.getInstance().apply {
            set(Calendar.YEAR, 2020)
            set(Calendar.MONTH, 3)
            set(Calendar.DAY_OF_MONTH, 1)
        }

        DatePickerDialog(
            context,
            { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
                viewModel.date = "$dayOfMonth/${month + 1}/$year"
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).apply {
            datePicker.minDate = minDate.timeInMillis
            datePicker.maxDate = maxDate.timeInMillis
        }
    }

    val zoneOptions = remember { listOf("Ayacucho", "El Alambre", "La Noria") }
    val sectorOptions = remember { listOf("Sector A", "Sector B", "Sector C") }
    val accidentTypeOptions = remember { listOf("Manu chipi", "Diego violado", "Flavio penetrado") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(id = R.string.title_filter),
            fontWeight = FontWeight.ExtraBold,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Spacer(modifier = Modifier.height(16.dp))

        MyTextField(
            viewModel.date,
            { viewModel.date = it },
            stringResource(id = R.string.date_field_filter),
            {
                Icon(
                    imageVector = Icons.Default.CalendarMonth,
                    contentDescription = "Abrir selector de fecha",
                    Modifier.clickable { datePickerDialog.show() }
                )
            },
            Modifier.align(Alignment.CenterHorizontally)
        )

        DropdownMenuField(
            selectedOption = viewModel.zone,
            onOptionSelected = { viewModel.zone = it },
            label = stringResource(id = R.string.zone_field_filter),
            options = zoneOptions
        )

        DropdownMenuField(
            selectedOption = viewModel.sect,
            onOptionSelected = { viewModel.sect = it },
            label = stringResource(id = R.string.sector_field_filter),
            options = sectorOptions
        )

        DropdownMenuField(
            selectedOption = viewModel.accidentType,
            onOptionSelected = { viewModel.accidentType = it },
            label = stringResource(id = R.string.incident_type_field_filter),
            options = accidentTypeOptions
        )

        MyButton(
            {
                viewModel.viewModelScope.launch(Dispatchers.IO) {
                    viewModel.searchIncidents(
                        fecha = viewModel.date,
                        zona = viewModel.zone,
                        sector = viewModel.sect,
                        tipoIncidente = viewModel.accidentType
                    )
                }
            },
            stringResource(id = R.string.filter_button),
            Icons.Default.Search
        )
    }
    Spacer(modifier = Modifier.width(56.dp))
}

@Composable
fun DropdownMenuField(
    selectedOption: String,
    onOptionSelected: (String) -> Unit,
    label: String,
    options: List<String>,
    modifier: Modifier = Modifier,
) {
    var expanded by remember { mutableStateOf(false) } // Controla si el menú está desplegado

    Box(modifier = modifier.width(300.dp)) {
        // TextField personalizado
        TextField(
            value = selectedOption,
            onValueChange = { /* No permitido ya que es solo seleccionable */ },
            readOnly = true,
            label = {
                Text(
                    text = label,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 14.sp,
                    lineHeight = 15.sp,
                    color = MaterialTheme.colorScheme.tertiary
                )
            },
            textStyle = TextStyle(
                fontSize = 14.sp,
                lineHeight = 15.sp,
                color = Color.Black
            ),
            trailingIcon = {
                Icon(
                    imageVector = if (expanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                    contentDescription = "arrow selected",
                    tint = MaterialTheme.colorScheme.tertiary
                )
            },
            modifier = modifier
                .fillMaxWidth()
                .padding(bottom = 10.dp)
                .fillMaxWidth()
                .clickable { expanded = !expanded }, // Abre o cierra el menú al hacer clic
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                unfocusedTextColor = MaterialTheme.colorScheme.tertiary,
                unfocusedTrailingIconColor = MaterialTheme.colorScheme.tertiary,
                focusedTrailingIconColor = MaterialTheme.colorScheme.tertiary,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            shape = RoundedCornerShape(24.dp)
        )

        // Menú desplegable personalizado
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .fillMaxWidth()
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option, color = MaterialTheme.colorScheme.onPrimaryContainer) },
                    onClick = {
                        onOptionSelected(option)
                        expanded = false // Cierra el menú al seleccionar una opción
                    })
            }
        }
    }
}

@Composable
fun HeaderResearch(onInfoClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(140.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.open_logo_small),
            contentDescription = "Logo CMT",
            Modifier
                .height(100.dp)
                .padding(top = 10.dp)
                .align(Alignment.Center),
            contentScale = ContentScale.Fit
        )
        IconInfo(onInfoClick, Modifier.align(Alignment.TopEnd))
    }

    Spacer(modifier = Modifier.height(15.dp))
}

@Composable
fun IconInfo(onClick: () -> Unit, modifier: Modifier) {
    Icon(
        imageVector = Icons.Default.Info,
        contentDescription = "Información sobre incidentes",
        modifier = modifier
            .padding(24.dp)
            .clickable { onClick() },
        tint = MaterialTheme.colorScheme.tertiary
    )
}

@Composable
fun IncidentBox(
    navigate: () -> Unit,
    numberIncident: String,
    dateIncident: String,
    hourIncident: String,
    typeIncident: String,
) {
    val rememberedDateIncident = remember { dateIncident }
    val rememberedHourIncident = remember { hourIncident }
    val rememberedTypeIncident = remember { typeIncident }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(85.dp)
            .padding(start = 26.dp, end = 26.dp, bottom = 16.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.secondaryContainer)
            .clickable { navigate() }
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .padding(12.dp)
        ) {
            Row(
                Modifier
                    .fillMaxWidth()
            ) {
                Text(
                    text = "Incidente N° $numberIncident",
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 14.sp,
                    color = Color.Black,
                    lineHeight = 20.sp
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = rememberedDateIncident,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 14.sp,
                    color = Color.Black,
                    lineHeight = 20.sp
                )
                Text(
                    text = rememberedHourIncident,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 14.sp,
                    color = Color.Black,
                    lineHeight = 20.sp,
                    modifier = Modifier.padding(start = 3.dp)
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = rememberedTypeIncident,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 14.sp,
                color = Color.Black,
                lineHeight = 20.sp,
                modifier = Modifier
                    .fillMaxWidth()
            )
        }
    }
}

@Composable
fun MyTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    trailingIcon: @Composable () -> Unit,
    modifier: Modifier,
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = {
            Text(
                text = placeholder,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 14.sp,
                lineHeight = 15.sp,
                color = MaterialTheme.colorScheme.tertiary
            )
        },
        textStyle = TextStyle(
            fontSize = 14.sp,
            lineHeight = 15.sp,
            color = Color.Black
        ),
        readOnly = true,
        modifier = modifier
            .padding(bottom = 10.dp)
            .width(300.dp),
        trailingIcon = trailingIcon,
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White,
            unfocusedTextColor = MaterialTheme.colorScheme.tertiary,
            unfocusedTrailingIconColor = MaterialTheme.colorScheme.tertiary,
            focusedTrailingIconColor = MaterialTheme.colorScheme.tertiary,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        ),
        shape = RoundedCornerShape(24.dp)
    )
}