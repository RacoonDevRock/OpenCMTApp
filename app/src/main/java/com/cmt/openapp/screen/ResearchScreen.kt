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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.cmt.openapp.R
import com.cmt.openapp.model.Routes
import com.cmt.openapp.model.SearchViewModel
import java.util.*

//@Preview(showSystemUi = true)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResearchScreen(
    modifier: Modifier,
    viewModel: SearchViewModel = SearchViewModel(),
    navigationController: NavHostController,
) {
    var isBottomSheetVisible by remember { mutableStateOf(false) }

    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            HeaderSection()

            Spacer(modifier = Modifier.height(20.dp))

            LazyColumn(
                Modifier
                    .fillMaxSize()
                    .weight(1f), horizontalAlignment = Alignment.CenterHorizontally
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
            ModalBottomSheet(
                onDismissRequest = {
                    isBottomSheetVisible = false
                },
                modifier = Modifier
                    .fillMaxWidth(),
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = Color.Black
            ) {
                BottomSheetContent(viewModel)
            }
        }

        Button(
            onClick = {
                isBottomSheetVisible = true
            },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.tertiary)
        ) {
            Row(horizontalArrangement = Arrangement.SpaceBetween) {
                Text(
                    text = "Mostrar Filtros",
                    fontSize = 21.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Icon(Icons.Default.Search, contentDescription = "navigate", tint = Color.White)
            }
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
            .padding(16.dp)
    ) {
        Text(
            text = "Filtros",
            fontWeight = FontWeight.ExtraBold,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Spacer(modifier = Modifier.height(10.dp))

        MyTextField(viewModel.date, { viewModel.date = it }, "Fecha", {
            Icon(
                imageVector = Icons.Default.CalendarMonth,
                contentDescription = "Abrir selector de fecha",
                Modifier.clickable { datePickerDialog.show() }
            )
        }, Modifier.align(Alignment.CenterHorizontally))

        MyTextField(viewModel.zone, { viewModel.zone = it }, "Zona", {
            Icon(
                imageVector = Icons.Default.KeyboardArrowDown,
                contentDescription = "Seleccionar zona"
            )
        }, Modifier.align(Alignment.CenterHorizontally))

        MyTextField(viewModel.sector, { viewModel.sector = it }, "Sector", {
            Icon(
                imageVector = Icons.Default.KeyboardArrowDown,
                contentDescription = "Seleccionar sector"
            )
        }, Modifier.align(Alignment.CenterHorizontally))

        MyTextField(
            viewModel.accidentType,
            { viewModel.accidentType = it },
            "Tipo de Incidente",
            {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = "Seleccionar tipo de incidente"
                )
            },
            Modifier.align(Alignment.CenterHorizontally)
        )

        Button(
            onClick = {},
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.tertiary),
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Row() {
                Text(text = "Buscar", fontSize = 21.sp, color = Color.White)
                Icon(Icons.Default.Search, contentDescription = "navigate", tint = Color.White)
            }
        }
    }
    Spacer(modifier = Modifier.width(56.dp))
}

@Composable
fun HeaderSection() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(140.dp)
    ) {
        IconInfo()
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
fun IconInfo() {
    Icon(
        imageVector = Icons.Default.Info,
        contentDescription = "Información sobre incidentes",
        modifier = Modifier
            .padding(18.dp)
            .clickable { },
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
    )
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
            .padding(bottom = 20.dp)
            .width(290.dp)
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