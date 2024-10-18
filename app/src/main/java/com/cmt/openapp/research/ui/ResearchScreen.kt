package com.cmt.openapp.research.ui

import android.app.DatePickerDialog
import android.content.Context
import android.widget.DatePicker
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.cmt.openapp.R
import com.cmt.openapp.core.navigation.Routes
import com.cmt.openapp.research.ui.viewmodel.SearchViewModel
import com.cmt.openapp.core.ui.shared.buttonNavigate.MyButton
import com.cmt.openapp.core.ui.shared.dialog.InfoContent
import com.cmt.openapp.core.ui.shared.dialog.TopDialogSheet
import java.util.*

@Composable
fun ResearchScreen(
    modifier: Modifier = Modifier,
    viewModel: SearchViewModel = SearchViewModel(),
    navigationController: NavHostController,
) {
    var isBottomSheetVisible by rememberSaveable { mutableStateOf(false) }
    var isTopDialogVisible by rememberSaveable { mutableStateOf(false) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(bottom = 16.dp)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            HeaderSection {
                if (!isBottomSheetVisible) {
                    isTopDialogVisible = true
                }
            }

            LazyColumn(
                Modifier
                    .fillMaxSize()
                    .weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                items(1) {
                    IncidentBox({
                        navigationController.navigate(Routes.DetailIncidentScreen.route)
                    }, "1999", "03/08/2020", "10:29", "Consumo de licor en la vía pública")
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
fun BottomSheetContent(viewModel: SearchViewModel = SearchViewModel()) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()
    val datePickerDialog = rememberDatePicker(context, calendar, viewModel)

    val zoneOptions = listOf("Ayacucho", "El Alambre", "La Noria")
    val sectorOptions = listOf("Sector A", "Sector B", "Sector C")
    val accidentTypeOptions = listOf("Manu chipi", "Diego violado", "Flavio penetrado")

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
        Spacer(modifier = Modifier.height(10.dp))

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
            selectedOption = viewModel.sector,
            onOptionSelected = { viewModel.sector = it },
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
            { /* Acciones cuando se filtra */ },
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

    Box(modifier = modifier.fillMaxWidth()) {
        // TextField personalizado
        TextField(
            value = selectedOption,
            onValueChange = { /* No permitido ya que es solo seleccionable */ },
            readOnly = true,
            label = {
                Text(
                    text = label,
                    fontWeight = FontWeight.ExtraBold,
                    modifier = Modifier.padding(start = 8.dp),
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.tertiary
                )
            },
            trailingIcon = {
                Icon(
                    imageVector = if (expanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                    contentDescription = "arrow selected",
                    tint = MaterialTheme.colorScheme.tertiary
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 30.dp, end = 30.dp, bottom = 13.dp)
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
            shape = RoundedCornerShape(25.dp)
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
fun HeaderSection(onInfoClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(140.dp)
    ) {
        IconInfo(onInfoClick)
        Image(
            painter = painterResource(id = R.drawable.open_logo_small),
            contentDescription = "Logo CMT",
            Modifier
                .height(100.dp)
                .padding(top = 10.dp)
                .align(Alignment.Center),
            contentScale = ContentScale.Fit
        )
    }

    Spacer(modifier = Modifier.height(15.dp))
}

@Composable
fun IconInfo(onClick: () -> Unit) {
    Icon(
        imageVector = Icons.Default.Info,
        contentDescription = "Información sobre incidentes",
        modifier = Modifier
            .padding(22.dp)
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
    motiveIncident: String,
) {
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
                    text = dateIncident,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 14.sp,
                    color = Color.Black,
                    lineHeight = 20.sp
                )
                Text(
                    text = hourIncident,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 14.sp,
                    color = Color.Black,
                    lineHeight = 20.sp,
                    modifier = Modifier.padding(start = 3.dp)
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = motiveIncident,
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
fun rememberDatePicker(
    context: Context,
    calendar: Calendar,
    viewModel: SearchViewModel,
): DatePickerDialog {
    // fecha actual
    val currentDate = Calendar.getInstance()

    // fecha minima
//    val minDate = Calendar.getInstance().apply {
//        set(Calendar.YEAR, 2018)
//        set(Calendar.MONTH, 0)
//        set(Calendar.DAY_OF_MONTH, 1)
//    }

    return DatePickerDialog(
        context,
        { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
            viewModel.date = "$dayOfMonth/${month + 1}/$year"
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    ).apply {
//        setOnDismissListener {  }
        // aplican y establecen fecha minimas y maxima
//        datePicker.minDate = minDate.timeInMillis // metodos (datePicker.minDate)
        datePicker.maxDate = currentDate.timeInMillis // metodos (datePicker.maxDate)
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
                modifier = Modifier.padding(start = 8.dp),
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.tertiary
            )
        },
        readOnly = true,
        modifier = modifier
            .padding(bottom = 13.dp)
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
        shape = RoundedCornerShape(25.dp)
    )
}