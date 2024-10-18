package com.cmt.openapp.report.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilePresent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.cmt.openapp.R
import com.cmt.openapp.core.navigation.Routes
import com.cmt.openapp.core.ui.shared.buttonNavigate.MyButton
import com.cmt.openapp.core.ui.shared.dialog.InfoContent
import com.cmt.openapp.core.ui.shared.dialog.TopDialogSheet
import com.cmt.openapp.research.ui.HeaderSection

@Composable
fun ReportScreen(
    modifier: Modifier,
    navigationController: NavHostController
) {
    var isTopDialogVisible by rememberSaveable { mutableStateOf(false) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            HeaderSection { isTopDialogVisible = true }

            BoxRequest(Modifier.weight(1f)) { navigationController.navigate(Routes.HomeScreen.route) }
        }

        if (isTopDialogVisible) {
            TopDialogSheet(onDismissRequest = { isTopDialogVisible = false }) {
                InfoContent()
            }
        }
    }
}

@Composable
fun BoxRequest(modifier: Modifier, navigate: () -> Unit) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(topStart = 110.dp, topEnd = 110.dp))
            .background(MaterialTheme.colorScheme.primaryContainer)
    ) {
        Column(
            Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            RequestHeader(Modifier.align(Alignment.CenterHorizontally))
            RequestForm()
            MyButton(
                { navigate() },
                stringResource(id = R.string.report_button),
                Icons.Default.FilePresent
            )
        }
    }
}

@Composable
fun RequestForm() {
    // Estados para cada campo de texto
    var name by rememberSaveable { mutableStateOf("") }
    var idt by rememberSaveable { mutableStateOf("") }
    var address by rememberSaveable { mutableStateOf("") }
    var city by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var phone by rememberSaveable { mutableStateOf("") }
    var motive by rememberSaveable { mutableStateOf("") }
    val scrollState = rememberScrollState()

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.verticalScroll(scrollState)
    ) {
        TextFieldRequest(
            stringResource(id = R.string.name_field_report),
            name,
            { name = it },
            KeyboardType.Text
        )
        TextFieldRequest(
            stringResource(id = R.string.id_field_report),
            idt,
            { idt = it },
            KeyboardType.Number
        )
        TextFieldRequest(
            stringResource(id = R.string.address_field_report),
            address,
            { address = it },
            KeyboardType.Text
        )
        TextFieldRequest(
            stringResource(id = R.string.city_field_report),
            city,
            { city = it },
            KeyboardType.Text
        )
        TextFieldRequest(
            stringResource(id = R.string.email_field_report),
            email,
            { email = it },
            KeyboardType.Email
        )
        TextFieldRequest(stringResource(id = R.string.phone_field_report), phone, {
            if (it.length <= 9) phone =
                it // valir que cumple con phone.matches(Regex("^9\\d{8}$")) para habilitar el boton
        }, KeyboardType.Number)
        TextFieldRequest(
            stringResource(id = R.string.motive_field_report),
            motive,
            { motive = it },
            KeyboardType.Text
        )
    }
}

@Composable
fun RequestHeader(modifier: Modifier) {
    Text(
        text = stringResource(id = R.string.title_report),
        color = Color.Black,
        textAlign = TextAlign.Center,
        fontSize = 17.sp,
        fontWeight = FontWeight.Bold,
        modifier = modifier
            .padding(top = 20.dp, bottom = 10.dp, start = 70.dp, end = 70.dp)
    )

    Text(
        text = "Incidente N°1999",
        color = Color.Black,
        fontSize = 14.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier
            .padding(bottom = 10.dp)
    )
}

@Composable
fun TextFieldRequest(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    keyboardType: KeyboardType,
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = {
            Text(
                text = label,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.tertiary
            )
        },
        modifier = Modifier
            .padding(bottom = 13.dp)
            .width(300.dp)
            .height(55.dp),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White,
            unfocusedTextColor = MaterialTheme.colorScheme.tertiary,
            unfocusedTrailingIconColor = MaterialTheme.colorScheme.tertiary,
            focusedTextColor = MaterialTheme.colorScheme.primary,
            focusedTrailingIconColor = MaterialTheme.colorScheme.primary,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
        ),
        keyboardOptions = KeyboardOptions(
            capitalization = KeyboardCapitalization.Words,
            autoCorrectEnabled = true,
            keyboardType = keyboardType
        ),
        shape = RoundedCornerShape(25.dp),
        maxLines = 1,
        singleLine = true
    )
}
