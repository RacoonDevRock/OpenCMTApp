package com.cmt.openapp.research.ui

import android.app.DatePickerDialog
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.cmt.openapp.R
import com.cmt.openapp.core.navigation.Routes
import com.cmt.openapp.core.ui.FAB
import com.cmt.openapp.core.ui.HeaderSection
import com.cmt.openapp.core.ui.shared.buttonNavigate.MyButton
import com.cmt.openapp.core.ui.shared.dialog.InfoContent
import com.cmt.openapp.core.ui.shared.dialog.TopDialogSheet
import com.cmt.openapp.core.ui.shared.loading.LoadingScreen
import com.cmt.openapp.research.ui.viewmodel.SearchViewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import java.util.Calendar

@Composable
fun ResearchScreen(
    modifier: Modifier = Modifier,
    viewModel: SearchViewModel = hiltViewModel(),
    navigationController: NavController,
    onThemeChange: (Int) -> Unit,
    onTypographyChange: (Typography) -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()
    var isBottomSheetVisible by rememberSaveable { mutableStateOf(false) }
    var isTopDialogVisible by rememberSaveable { mutableStateOf(false) }
    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing = uiState.isLoading)
    val listState = rememberLazyListState()

    LaunchedEffect(Unit) {
        viewModel.searchIncidents()
        viewModel.obtenerTiposDeIncidente()
    }

    ConstraintLayout(modifier = modifier.fillMaxSize()) {
        val (header, list, button) = createRefs()

        HeaderSection(
            modifier = Modifier.constrainAs(header) { top.linkTo(parent.top) },
            isInfo = true,
            onInfoClick = {
                if (!isBottomSheetVisible) {
                    isTopDialogVisible = true
                }
            }, { navigationController })

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .constrainAs(list) {
                    top.linkTo(header.bottom)
                    bottom.linkTo(button.top)
                    height = Dimension.fillToConstraints
                },
            verticalArrangement = Arrangement.Center
        ) {

            SwipeRefresh(
                state = swipeRefreshState,
                onRefresh = { viewModel.searchIncidents() }) {

                when {
                    uiState.isLoading && !swipeRefreshState.isRefreshing -> {
                        LoadingScreen()
                    }

                    uiState.errorMessage != null -> {
                        uiState.errorMessage?.let { errorMessage ->
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = errorMessage,
                                    color = Color.Red,
                                    modifier = Modifier.align(Alignment.Center)
                                )
                            }
                        }
                    }

                    uiState.incidents.isEmpty() -> { // Mostrar mensaje cuando no hay datos
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "No se encontraron incidentes.",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }

                    else -> {

                        LazyColumn(
                            state = listState,
                            modifier = Modifier.fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(uiState.incidents) { incident ->
                                IncidentBox(
                                    {
                                        navigationController.navigate(
                                            Routes.DetailIncidentScreen.createRoute(
                                                incident.nroIncidente
                                            )
                                        )
                                    },
                                    incident.nroIncidente,
                                    incident.fecha,
                                    incident.hora,
                                    incident.tipoIncidente
                                )
                            }
                        }

                        LaunchedEffect(listState) {
                            snapshotFlow {
                                listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index
                            }.collect { lastVisibleItem ->
                                lastVisibleItem?.let {
                                    if (it == uiState.incidents.size - 1 && !uiState.isLoading) {
                                        viewModel.loadNextPage()
                                    }
                                }
                            }
                        }

                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(80.dp))

        FAB(
            isDarkTheme = AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES,
            onThemeChange = onThemeChange,
            onMainFabClick = { isBottomSheetVisible = !isBottomSheetVisible },
            currentTypography = MaterialTheme.typography,
            onTypographyChange = onTypographyChange
        )

        if (isBottomSheetVisible) {
            BottomSheetWithContent(
                viewModel,
                onDismiss = { isBottomSheetVisible = false })
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
            textButton = stringResource(id = R.string.message_filter),
            myIconButton = Icons.Default.Search,
            modifier = Modifier
                .padding(bottom = 10.dp)
                .constrainAs(button) {
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
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
        BottomSheetContent(viewModel, onDismiss)
    }
}

@Composable
fun BottomSheetContent(viewModel: SearchViewModel, onDismiss: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(id = R.string.title_filter),
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.align(Alignment.CenterHorizontally),
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(10.dp))

        DateDropDown(viewModel.date) { viewModel.date = it }

        ZoneDropDown(viewModel.zone) { viewModel.zone = it }

        SectorDropDown(viewModel.sect, { viewModel.sect = it }, viewModel)

        IncidentTypeDropDown(viewModel.accidentType, { viewModel.accidentType = it }, viewModel)

        MyButton(
            {
                viewModel.searchIncidents()
                onDismiss()
            },
            stringResource(id = R.string.filter_button),
            Icons.Default.Search
        )
    }
    Spacer(modifier = Modifier.width(56.dp))
}

@Composable
fun IncidentTypeDropDown(
    selectedIncidentType: String?, onIncidentTypeSelected: (String?) -> Unit,
    viewModel: SearchViewModel,
) {
    var expanded by rememberSaveable { mutableStateOf(false) }
    val incidentOptions by viewModel.tiposDeIncidente.collectAsState()

    Box {

        MyTextField(
            selectedIncidentType ?: "Tipo de Incidente",
            {},
            placeholder = stringResource(id = R.string.incident_type_field_filter),
            trailingIcon = {
                Icon(
                    imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = "",
                    modifier = Modifier.clickable { expanded = true }
                )
            },
            Modifier.fillMaxWidth()
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .background(color = MaterialTheme.colorScheme.primary)
                .align(Alignment.Center),
        ) {
            incidentOptions.forEach { tipoIncidente ->
                DropdownMenuItem(onClick = {
                    onIncidentTypeSelected(tipoIncidente.nombre)
                    expanded = false
                },
                    text = {
                        Text(
                            text = tipoIncidente.nombre,
                            color = MaterialTheme.colorScheme.onTertiary,
                            style = MaterialTheme.typography.bodySmall,
                        )
                    }
                )
            }
        }
    }

}

@Composable
fun SectorDropDown(
    selectedSector: String?,
    onSectorSelected: (String?) -> Unit,
    viewModel: SearchViewModel,
) {
    var expanded by rememberSaveable { mutableStateOf(false) }
    val sectores by viewModel.sectores.collectAsState()
    val context = LocalContext.current

    Box {

        MyTextField(
            sectores.find { it.titulo == selectedSector }?.titulo ?: "Todos los sectores",
            {},
            placeholder = stringResource(id = R.string.sector_field_filter),
            trailingIcon = {
                Icon(
                    imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = "",
                    modifier = Modifier.clickable {
                        if (!viewModel.zone.isNullOrEmpty()) {
                            expanded = true
                            viewModel.obtenerSectoresPorZona(viewModel.zone!!)
                        } else {
                            Toast.makeText(context, "Seleccione una zona", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                )
            },
            Modifier.fillMaxWidth()
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .background(color = MaterialTheme.colorScheme.primary)
                .align(Alignment.Center),
        ) {
            sectores.forEach { sector ->
                DropdownMenuItem(onClick = {
                    onSectorSelected(sector.titulo)
                    expanded = false
                },
                    text = {
                        Text(
                            text = sector.titulo,
                            color = MaterialTheme.colorScheme.onTertiary,
                            style = MaterialTheme.typography.bodySmall,
                        )
                    }
                )
            }
        }
    }
}

@Composable
fun ZoneDropDown(selectedZone: String?, onZoneSelected: (String?) -> Unit) {
    var expanded by rememberSaveable { mutableStateOf(false) }
    val zoneOptions = mapOf(
        "Todas las zonas" to null,
        "El Alambre" to "ALAMBRE",
        "Ayacucho" to "AYACUCHO",
        "La Noria" to "NORIA"
    )

    Box {

        MyTextField(
            zoneOptions.entries.find { it.value == selectedZone }?.key ?: "Zona",
            {},
            placeholder = stringResource(id = R.string.zone_field_filter),
            trailingIcon = {
                Icon(
                    imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = "",
                    modifier = Modifier.clickable { expanded = true }
                )
            },
            Modifier.fillMaxWidth()
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .background(color = MaterialTheme.colorScheme.primary)
                .align(Alignment.Center),
        ) {
            zoneOptions.forEach { (displayText, value) ->
                DropdownMenuItem(onClick = {
                    onZoneSelected(value)
                    expanded = false
                },
                    text = {
                        Text(
                            text = displayText,
                            color = MaterialTheme.colorScheme.onTertiary,
                            style = MaterialTheme.typography.bodySmall,
                        )
                    }
                )
            }
        }
    }

}

@Composable
fun DateDropDown(selectedDate: String?, onDateSelected: (String?) -> Unit) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()
    val datePickerDialog = remember {
        DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                val formattedDate = "$year-${(month + 1).toString().padStart(2, '0')}-${
                    dayOfMonth.toString().padStart(2, '0')
                }"
                onDateSelected(formattedDate)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).apply {
            datePicker.minDate = Calendar.getInstance().apply {
                set(Calendar.YEAR, 2020)
                set(Calendar.MONTH, 2)
                set(Calendar.DAY_OF_MONTH, 1)
            }.timeInMillis

            datePicker.maxDate = Calendar.getInstance().apply {
                set(Calendar.YEAR, 2024)
                set(Calendar.MONTH, 3)
                set(Calendar.DAY_OF_MONTH, 1)
            }.timeInMillis
        }
    }

    MyTextField(
        selectedDate ?: "Fecha",
        {},
        placeholder = stringResource(id = R.string.date_field_filter),
        trailingIcon = {
            Icon(
                imageVector = Icons.Default.CalendarMonth,
                contentDescription = "",
                Modifier.clickable { datePickerDialog.show() }
            )
        },
        Modifier.fillMaxWidth()
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
            .padding(horizontal = 25.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surfaceContainer)
            .clickable { navigate() }
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .padding(12.dp)
        ) {
            Row(
                Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Incidente N° $numberIncident",
                    fontWeight = FontWeight.ExtraBold,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary,

                    )

                Row {
                    Text(
                        text = rememberedDateIncident,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = rememberedHourIncident,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = rememberedTypeIncident,
                fontWeight = FontWeight.ExtraBold,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary,
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
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onTertiary
            )
        },
        textStyle = MaterialTheme.typography.bodySmall,
        readOnly = true,
        modifier = modifier
            .padding(start = 30.dp, end = 30.dp, bottom = 13.dp)
            .height(50.dp),
        trailingIcon = trailingIcon,
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White,
            unfocusedTextColor = MaterialTheme.colorScheme.onTertiary,
            unfocusedTrailingIconColor = MaterialTheme.colorScheme.onTertiary,
            focusedTextColor = MaterialTheme.colorScheme.onTertiaryContainer,
            focusedTrailingIconColor = MaterialTheme.colorScheme.onTertiaryContainer,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            cursorColor = MaterialTheme.colorScheme.onTertiaryContainer,
        ),
        shape = RoundedCornerShape(24.dp)
    )
}