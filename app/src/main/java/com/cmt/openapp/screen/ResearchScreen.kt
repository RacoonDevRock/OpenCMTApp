package com.cmt.openapp.screen

import android.app.DatePickerDialog
import android.content.Context
import android.widget.DatePicker
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.cmt.openapp.R
import com.cmt.openapp.model.Routes
import com.cmt.openapp.model.SearchViewModel
import java.util.*

//@Preview(showSystemUi = true)
@Composable
fun ResearchScreen(modifier: Modifier, viewModel: SearchViewModel = SearchViewModel(), navigationController: NavHostController) {
    var isExpanded by remember { mutableStateOf(false) }

    val maxHeight = 500.dp // Altura máxima
    val minHeight = 100.dp // Altura mínima

    // Animación de la altura del Box
    val animatedBoxHeight by animateDpAsState(
        targetValue = if (isExpanded) maxHeight else minHeight,
        animationSpec = tween(durationMillis = 800)
    )

    // Animación para rotar el ícono
    val rotationAngle by animateFloatAsState(
        targetValue = if (isExpanded) 180f else 0f,
        animationSpec = tween(durationMillis = 800)
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            HeaderSection()

            Spacer(modifier = Modifier.height(20.dp))

            LazyColumn(
                Modifier
                    .fillMaxSize()
                    .weight(1f), horizontalAlignment = Alignment.CenterHorizontally
            ) {
                items(1) {
                    IncidentBox {
                         navigationController.navigate(Routes.DetailIncidentScreen.route)
                    }
                }
            }

//            Spacer(modifier = Modifier.height(70.dp))
        }

        // Box anclado al fondo, que se expande/contrae con click
        ExpandableBox(isExpanded, animatedBoxHeight, rotationAngle, Modifier.align(Alignment.BottomCenter)) {
            isExpanded = !isExpanded
        }
    }
}

@Composable
fun HeaderSection() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(140.dp)
    ) {
        Icon(
            imageVector = Icons.Default.Info,
            contentDescription = "Información sobre incidentes",
            Modifier.padding(18.dp), tint = Color(0xFF848688)
        )
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
fun IncidentBox(navigate: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(90.dp)
            .padding(start = 26.dp, end = 26.dp, bottom = 16.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.secondaryContainer)
            .clickable { navigate() }
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .padding(14.dp)
        ) {
            Row(
                Modifier
                    .fillMaxWidth()
            ) {
                Text(
                    text = "Incidente N° 1999",
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "03/08/2024 10:29",
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = "Consumo de licor en la vía pública",
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                modifier = Modifier
                    .fillMaxWidth()
            )
        }
    }
}

@Composable
fun ExpandableBox(
    isExpanded: Boolean,
    animatedBoxHeight: Dp,
    rotationAngle: Float,
    modifier: Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(animatedBoxHeight)
            .clip(RoundedCornerShape(topStart = 130.dp, topEnd = 130.dp))
            .background(MaterialTheme.colorScheme.primaryContainer)
            .clickable { onClick() }
    ) {
        Column(
            Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Filled.KeyboardArrowUp,
                contentDescription = "Expandir filtros",
                modifier = Modifier
                    .padding(20.dp)
                    .rotate(rotationAngle),
                tint = Color(0XFF848688)
            )
            Text(
                text = "Filtros",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 20.dp)
            )
            FilterSection()
        }
    }
}

@Composable
fun FilterSection(viewModel: SearchViewModel = SearchViewModel()) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()
    val datePickerDialog = rememberDatePicker(context, calendar, viewModel)

    MyTextField(viewModel.date, { viewModel.date = it }, "Fecha") {
        Icon(
            imageVector = Icons.Default.CalendarMonth,
            contentDescription = "Abrir selector de fecha",
            Modifier.clickable { datePickerDialog.show() }
        )
    }
    MyTextField(viewModel.zone, { viewModel.zone = it }, "Zona") {
        Icon(
            imageVector = Icons.Default.KeyboardArrowDown,
            contentDescription = "Seleccionar zona"
        )
    }
    MyTextField(viewModel.sector, { viewModel.sector = it }, "Sector") {
        Icon(
            imageVector = Icons.Default.KeyboardArrowDown,
            contentDescription = "Seleccionar sector"
        )
    }
    MyTextField(viewModel.accidentType, { viewModel.accidentType = it }, "Tipo de Incidente") {
        Icon(
            imageVector = Icons.Default.KeyboardArrowDown,
            contentDescription = "Seleccionar tipo de incidente"
        )
    }
    MyButtonNavigate({  }, "Buscar", Icons.Default.Search)
}

@Composable
fun rememberDatePicker(
    context: Context,
    calendar: Calendar,
    viewModel: SearchViewModel
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
    trailingIcon: @Composable () -> Unit
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = {
            Text(
                text = placeholder,
                fontWeight = FontWeight.ExtraBold,
                modifier = Modifier.padding(start = 8.dp),
                color = MaterialTheme.colorScheme.onSurface
            )
        },
        readOnly = true,
        modifier = Modifier
            .padding(bottom = 25.dp)
            .width(310.dp),
        trailingIcon = trailingIcon,
        colors = TextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
            unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        ),
        shape = RoundedCornerShape(25.dp)
    )
}