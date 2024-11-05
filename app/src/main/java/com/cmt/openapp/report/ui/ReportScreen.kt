package com.cmt.openapp.report.ui

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.cmt.openapp.R
import com.cmt.openapp.core.navigation.Routes
import com.cmt.openapp.core.ui.shared.buttonNavigate.MyButton
import com.cmt.openapp.core.ui.shared.dialog.InfoContent
import com.cmt.openapp.core.ui.shared.dialog.TopDialogSheet
import com.cmt.openapp.core.ui.shared.loading.LoadingScreen
import com.cmt.openapp.detail.ui.HeaderDetailAndReport
import com.cmt.openapp.report.data.network.response.SolicitudRequest
import com.cmt.openapp.report.ui.viewmodel.ReportViewModel

@Composable
fun ReportScreen(
    modifier: Modifier,
    navigationController: NavHostController,
    viewModel: ReportViewModel = hiltViewModel(),
    incidentId: Long,
) {
    var isTopDialogVisible by rememberSaveable { mutableStateOf(false) }
    val isLoading by viewModel.isLoading.collectAsState(false)
    val submissionMessage by viewModel.submissionMessage.collectAsState()

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        if (isLoading) {
            LoadingScreen()
        } else {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                HeaderDetailAndReport(navigationController)

                BoxRequest(
                    Modifier.fillMaxSize(),
                    { navigationController.navigate(Routes.HomeScreen.route) },
                    viewModel = viewModel,
                    incidentId = incidentId
                )
            }

            if (isTopDialogVisible) {
                TopDialogSheet(onDismissRequest = { isTopDialogVisible = false }) {
                    InfoContent()
                }
            }

            submissionMessage?.let {
                // Muestra el mensaje de éxito/error
                Toast.makeText(LocalContext.current, it, Toast.LENGTH_SHORT).show()
                viewModel.resetNavigation()
            }
        }
    }
}

@Composable
fun BoxRequest(
    modifier: Modifier,
    navigate: () -> Unit,
    viewModel: ReportViewModel,
    incidentId: Long,
) {
    val name: String by viewModel.name.observeAsState("")
    val idt: String by viewModel.idt.observeAsState("")
    val address: String by viewModel.address.observeAsState("")
    val city: String by viewModel.city.observeAsState("")
    val email: String by viewModel.email.observeAsState("")
    val phone: String by viewModel.phone.observeAsState("")
    val motive: String by viewModel.motive.observeAsState("")

    val onSubmit = { viewModel.solicitarAccesoIncidente(incidentId) }

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
            RequestHeader(Modifier.align(Alignment.CenterHorizontally), incidentId)
            RequestForm(
                name = name,
                onNameChange = { viewModel.updateName(it) },
                idt = idt,
                onIdtChange = { viewModel.updateIdt(it) },
                address = address,
                onAddressChange = { viewModel.updateAddress(it) },
                city = city,
                onCityChange = { viewModel.updateCity(it) },
                email = email,
                onEmailChange = { viewModel.updateEmail(it) },
                phone = phone,
                onPhoneChange = { viewModel.updatePhone(it) },
                motive = motive,
                onMotiveChange = { viewModel.updateMotive(it) },
                onSubmit = onSubmit
            )
        }
    }
}

@Composable
fun RequestForm(
    name: String,
    onNameChange: (String) -> Unit,
    idt: String,
    onIdtChange: (String) -> Unit,
    address: String,
    onAddressChange: (String) -> Unit,
    city: String,
    onCityChange: (String) -> Unit,
    email: String,
    onEmailChange: (String) -> Unit,
    phone: String,
    onPhoneChange: (String) -> Unit,
    motive: String,
    onMotiveChange: (String) -> Unit,
    onSubmit: () -> Unit,
) {

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.verticalScroll(rememberScrollState())
    ) {
        TextFieldRequest(
            stringResource(id = R.string.name_field_report), name, onNameChange, KeyboardType.Text
        )
        TextFieldRequest(
            stringResource(id = R.string.id_field_report), idt, onIdtChange, KeyboardType.Number
        )
        TextFieldRequest(
            stringResource(id = R.string.address_field_report),
            address,
            onAddressChange,
            KeyboardType.Text
        )
        TextFieldRequest(
            stringResource(id = R.string.city_field_report),
            city,
            onCityChange,
            KeyboardType.Text
        )
        TextFieldRequest(
            stringResource(id = R.string.email_field_report),
            email,
            onEmailChange,
            KeyboardType.Email
        )
        TextFieldRequest(
            stringResource(id = R.string.phone_field_report),
            phone,
            onPhoneChange,
            KeyboardType.Number
        )
        TextFieldRequest(
            stringResource(id = R.string.motive_field_report),
            motive,
            onMotiveChange,
            KeyboardType.Text
        )
        MyButton(onSubmit, stringResource(id = R.string.report_button), Icons.Default.FilePresent)
    }
}

@Composable
fun RequestHeader(modifier: Modifier, incidentId: Long) {
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
        text = "Incidente N°$incidentId",
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
    keyboardType: KeyboardType
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = {
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
        modifier = Modifier
            .padding(bottom = 10.dp)
            .width(300.dp)
            .height(50.dp),
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
        shape = RoundedCornerShape(24.dp),
        maxLines = 1,
        singleLine = true
    )
}
