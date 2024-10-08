package com.cmt.openapp.screen

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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController
import com.cmt.openapp.R
import com.cmt.openapp.model.Routes
import com.cmt.openapp.model.SearchViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*

//@Preview(showSystemUi = true)
@Composable
fun ResearchScreen(
    modifier: Modifier,
    viewModel: SearchViewModel = SearchViewModel(),
    navigationController: NavHostController,
) {
    var isBottomSheetVisible by rememberSaveable { mutableStateOf(false) }
    var isTopDialogVisible by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        withContext(Dispatchers.IO) {
//            viewModel.loadIncidents()
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            HeaderSection {
                if (!isBottomSheetVisible) {
                    isTopDialogVisible = true
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            LazyColumn(
                Modifier
                    .fillMaxSize()
                    .weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                items(1) {
                    IncidentBox({
                        navigationController.navigate(Routes.DetailIncidentScreen.route)
                    }, "1999", "03/08/2020", "Consumo de licor en la vía pública")
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

        BottomButton(
            onClick = {
                if (!isTopDialogVisible) {
                    isBottomSheetVisible = true
                }
            },
            Modifier.align(Alignment.BottomCenter)
        )

    }
}

@Composable
fun BottomButton(onClick: () -> Unit, modifier: Modifier) {
    Button(
        onClick = { onClick() },
        modifier = modifier
            .padding(16.dp),
        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.tertiary)
    ) {
        Row(horizontalArrangement = Arrangement.SpaceBetween) {
            Text(
                text = stringResource(id = R.string.message_filter),
                fontSize = 21.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Icon(Icons.Default.Search, contentDescription = "navigate", tint = Color.White)
        }
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
fun TopDialogSheet(onDismissRequest: () -> Unit, content: @Composable () -> Unit) {
    Dialog(onDismissRequest = onDismissRequest) {
        // Este Box asegura que el diálogo se muestre en la parte superior
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(MaterialTheme.colorScheme.secondaryContainer)
                .padding(20.dp)
        ) {
            content()
        }
    }
}

@Composable
fun InfoContent() {
    Box(
        Modifier
            .fillMaxWidth()
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "El presente aplicativo está sujeto de acuerdo a:",
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                modifier = Modifier.align(Alignment.Start)
            )
            Text(
                text = "Artículo 7 de la Ley N° 27806",
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp
            )

            Text(
                text = "Toda persona tiene derecho a solicitar y recibir información de cualquier entidad de la Adminstración Pública. En ningún caso se exige expresión de causa para el ejercicio de este derecho.",
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                textAlign = TextAlign.Justify,
                lineHeight = 12.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Su uso aplica para consulta de incidentes y solicitudes de informes para entrega de copia certificada o simple.",
                color = MaterialTheme.colorScheme.tertiary,
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                textAlign = TextAlign.Justify,
                lineHeight = 12.sp,
                modifier = Modifier.padding(horizontal = 5.dp)
            )
        }
    }
}

@Composable
fun BottomSheetContent(viewModel: SearchViewModel = SearchViewModel()) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()
    val datePickerDialog = rememberDatePicker(context, calendar, viewModel)
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

        MyTextField(
            viewModel.zone,
            { viewModel.zone = it },
            stringResource(id = R.string.zone_field_filter),
            {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = "Seleccionar zona"
                )
            },
            Modifier.align(Alignment.CenterHorizontally)
        )

        MyTextField(
            viewModel.sector,
            { viewModel.sector = it },
            stringResource(id = R.string.sector_field_filter),
            {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = "Seleccionar sector"
                )
            },
            Modifier.align(Alignment.CenterHorizontally)
        )

        MyTextField(
            viewModel.accidentType,
            { viewModel.accidentType = it },
            stringResource(id = R.string.incident_type_field_filter),
            {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = "Seleccionar tipo de incidente"
                )
            },
            Modifier.align(Alignment.CenterHorizontally)
        )

        MyButtonNavigate(
            { },
            stringResource(id = R.string.filter_button),
            Icons.Default.Search
        )
    }
    Spacer(modifier = Modifier.width(56.dp))
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
                .padding(top = 30.dp)
                .align(Alignment.Center),
            contentScale = ContentScale.Fit
        )
    }
}

@Composable
fun IconInfo(onClick: () -> Unit) {
    Icon(
        imageVector = Icons.Default.Info,
        contentDescription = "Información sobre incidentes",
        modifier = Modifier
            .padding(18.dp)
            .clickable { onClick() },
        tint = MaterialTheme.colorScheme.tertiary
    )
}

@Composable
fun IncidentBox(
    navigate: () -> Unit,
    numberIncident: String,
    dateIncident: String,
    motiveIncident: String,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
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
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = dateIncident,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 14.sp,
                    color = Color.Black,
                )
            }
            Text(
                text = motiveIncident,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 14.sp,
                color = Color.Black,
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
    return DatePickerDialog(
        context,
        { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
            viewModel.date = "$dayOfMonth/${month + 1}/$year"
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    ).apply {
        setOnDismissListener {  }
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
            .padding(start = 30.dp, end = 30.dp, bottom = 13.dp)
            .height(50.dp),
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