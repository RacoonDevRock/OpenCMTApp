package com.cmt.openapp.report.ui

import androidx.activity.compose.BackHandler
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilePresent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.cmt.openapp.R
import com.cmt.openapp.core.navigation.Routes
import com.cmt.openapp.core.ui.FAB
import com.cmt.openapp.core.ui.HeaderSection
import com.cmt.openapp.core.ui.shared.buttonNavigate.MyButton
import com.cmt.openapp.core.ui.shared.dialog.InfoContent
import com.cmt.openapp.core.ui.shared.dialog.MyConfirmationReport
import com.cmt.openapp.core.ui.shared.dialog.TopDialogSheet
import com.cmt.openapp.core.ui.shared.loading.LoadingScreen
import com.cmt.openapp.report.data.network.response.FormData
import com.cmt.openapp.report.ui.viewmodel.ReportViewModel
import kotlinx.coroutines.launch

@Composable
fun ReportScreen(
    modifier: Modifier,
    navigationController: NavHostController,
    viewModel: ReportViewModel = hiltViewModel(),
    incidentId: Int,
    onThemeChange: (Int) -> Unit,
    onTypographyChange: (Typography) -> Unit,
) {
    var isTopDialogVisible by rememberSaveable { mutableStateOf(false) }
    val isLoading by viewModel.isLoading.collectAsState(false)
    val submissionMessage by viewModel.submissionMessage.collectAsState()
    val focusManager = LocalFocusManager.current
    var showExitDialog by rememberSaveable { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    BackHandler(enabled = true) {
        showExitDialog = true
    }

    LaunchedEffect(submissionMessage) {
        submissionMessage?.let {
            snackbarHostState.showSnackbar(it)
        }
    }

    if (showExitDialog) {
        TopDialogSheet(onDismissRequest = { showExitDialog = false }) {
            MyConfirmationReport(
                onConfirm = {
                    showExitDialog = false
                    scope.launch {
                        navigationController.popBackStack()
                    }
                },
                onCancel = { showExitDialog = false }
            )
        }
    }

    ConstraintLayout(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        val (header, form) = createRefs()

        HeaderSection(
            modifier = Modifier.constrainAs(header) { top.linkTo(parent.top) },
            isInfo = false,
            onInfoClick = {},
            onBackClick = { showExitDialog = true })

        Column(
            modifier = Modifier
                .fillMaxSize()
                .constrainAs(form) {
                    top.linkTo(header.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                    height = Dimension.fillToConstraints
                },
        ) {
            if (isLoading) {
                LoadingScreen()
            } else {

                BoxRequest(
                    Modifier.fillMaxSize(),
                    { navigationController.navigate(Routes.ResearchScreen.route) },
                    viewModel = viewModel,
                    incidentId = incidentId,
                    focusManager
                )

                if (isTopDialogVisible) {
                    TopDialogSheet(onDismissRequest = { isTopDialogVisible = false }) {
                        InfoContent()
                    }
                }
            }
        }

        SnackbarHost(hostState = snackbarHostState)

        FAB(
            isDarkTheme = AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES,
            onThemeChange = onThemeChange,
            {},
            currentTypography = MaterialTheme.typography,
            onTypographyChange = onTypographyChange
        )
    }
}

@Composable
fun BoxRequest(
    modifier: Modifier,
    navigate: () -> Unit,
    viewModel: ReportViewModel,
    incidentId: Int,
    focusManager: FocusManager,
) {
    val formData by viewModel.formData.collectAsState(FormData())

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(topStart = 110.dp, topEnd = 110.dp))
            .background(MaterialTheme.colorScheme.primaryContainer)
    ) {
        Column(
            Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            RequestHeader(
                Modifier
                    .align(Alignment.CenterHorizontally)
                    .width(230.dp), incidentId
            )
            RequestForm(
                focusManager = focusManager,
                formData = formData,
                onFormDataChange = { updatedData ->
                    viewModel.updateFormData {
                        it.copy(
                            updatedData.name,
                            updatedData.idt,
                            updatedData.address,
                            updatedData.city,
                            updatedData.email,
                            updatedData.phone,
                            updatedData.motive
                        )
                    }
                },
                onSubmit = { viewModel.solicitarAccesoIncidente(incidentId) { navigate() } },
                isSubmitEnabled = formData.name.isNotBlank() &&
                        formData.idt.isNotBlank() &&
                        formData.address.isNotBlank() &&
                        formData.city.isNotBlank() &&
                        formData.email.isNotBlank() &&
                        formData.phone.isNotBlank() &&
                        formData.motive.isNotBlank()
            )
        }
    }
}

@Composable
fun RequestForm(
    focusManager: FocusManager,
    formData: FormData,
    onFormDataChange: (FormData) -> Unit,
    onSubmit: () -> Unit,
    isSubmitEnabled: Boolean,
) {

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .width(300.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        MyCustomField(
            placeholder = stringResource(id = R.string.name_field_report),
            value = formData.name,
            onValueChange = { onFormDataChange(formData.copy(name = it)) },
            onNext = {
                focusManager.moveFocus(
                    FocusDirection.Next
                )
            })

        IdtField(
            placeholder = stringResource(id = R.string.id_field_report),
            value = formData.idt,
            onIdtChange = { onFormDataChange(formData.copy(idt = it)) },
            onNext = {
                focusManager.moveFocus(
                    FocusDirection.Next
                )
            })

        MyCustomField(
            placeholder = stringResource(id = R.string.address_field_report),
            value = formData.address,
            onValueChange = { onFormDataChange(formData.copy(address = it)) },
            onNext = {
                focusManager.moveFocus(
                    FocusDirection.Next
                )
            })

        MyCustomField(
            placeholder = stringResource(id = R.string.city_field_report),
            value = formData.city,
            onValueChange = { onFormDataChange(formData.copy(city = it)) },
            onNext = {
                focusManager.moveFocus(
                    FocusDirection.Next
                )
            })

        EmailField(
            placeholder = stringResource(id = R.string.email_field_report),
            value = formData.email,
            onEmailChange = { onFormDataChange(formData.copy(email = it)) },
            onNext = {
                focusManager.moveFocus(
                    FocusDirection.Next
                )
            })

        PhoneField(
            placeholder = stringResource(id = R.string.phone_field_report),
            value = formData.phone,
            onPhoneChange = { onFormDataChange(formData.copy(phone = it)) },
            onNext = {
                focusManager.moveFocus(
                    FocusDirection.Next
                )
            })

        MotiveField(
            placeholder = stringResource(id = R.string.motive_field_report),
            value = formData.motive,
            onValueChange = { onFormDataChange(formData.copy(motive = it)) },
        ) { focusManager.clearFocus() }

        MyButton(
            onSubmit,
            stringResource(id = R.string.report_button),
            Icons.Default.FilePresent,
            onEnable = isSubmitEnabled
        )
    }
}

@Composable
fun MotiveField(
    placeholder: String,
    value: String,
    onValueChange: (String) -> Unit,
    onDone: () -> Unit,
) {
    val focusManager = LocalFocusManager.current

    TextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier.fillMaxWidth(),
        placeholder = {
            Text(
                text = placeholder,
                style = MaterialTheme.typography.displaySmall,
                color = MaterialTheme.colorScheme.onTertiary
            )
        },
        textStyle = MaterialTheme.typography.displaySmall,
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
        keyboardOptions = KeyboardOptions(
            autoCorrectEnabled = true,
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(
            onDone = {
                focusManager.clearFocus()
                onDone()
            }
        ),
        shape = RoundedCornerShape(25.dp),
    )
}

@Composable
fun PhoneField(
    placeholder: String,
    value: String,
    onPhoneChange: (String) -> Unit,
    onNext: () -> Unit,
) {
    val focusManager = LocalFocusManager.current

    TextField(
        value = value,
        onValueChange = onPhoneChange,
        modifier = Modifier.fillMaxWidth(),
        placeholder = {
            Text(
                text = placeholder,
                style = MaterialTheme.typography.displaySmall,
                color = MaterialTheme.colorScheme.onTertiary
            )
        },
        maxLines = 1,
        singleLine = true,
        textStyle = MaterialTheme.typography.displaySmall,
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
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Next
        ),
        keyboardActions = KeyboardActions(
            onDone = {
                focusManager.clearFocus()
                onNext()
            }
        ),
        shape = RoundedCornerShape(25.dp),
    )
}

@Composable
fun EmailField(
    placeholder: String,
    value: String,
    onEmailChange: (String) -> Unit,
    onNext: () -> Unit,
) {
    val focusManager = LocalFocusManager.current

    TextField(
        value = value,
        onValueChange = onEmailChange,
        modifier = Modifier.fillMaxWidth(),
        placeholder = {
            Text(
                text = placeholder,
                style = MaterialTheme.typography.displaySmall,
                color = MaterialTheme.colorScheme.onTertiary
            )
        },
        maxLines = 1,
        singleLine = true,
        textStyle = MaterialTheme.typography.displaySmall,
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
        shape = RoundedCornerShape(25.dp),
        keyboardOptions = KeyboardOptions(
            autoCorrectEnabled = true,
            keyboardType = KeyboardType.Email,
            imeAction = ImeAction.Next
        ),
        keyboardActions = KeyboardActions(
            onDone = {
                focusManager.clearFocus()
                onNext()
            }
        ),
    )
}

@Composable
fun MyCustomField(
    placeholder: String,
    value: String,
    onValueChange: (String) -> Unit,
    onNext: () -> Unit,
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier.fillMaxWidth(),
        placeholder = {
            Text(
                text = placeholder,
                style = MaterialTheme.typography.displaySmall,
                color = MaterialTheme.colorScheme.onTertiary
            )
        },
        maxLines = 1,
        singleLine = true,
        textStyle = MaterialTheme.typography.displaySmall,
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
        keyboardOptions = KeyboardOptions(
            capitalization = KeyboardCapitalization.Words,
            autoCorrectEnabled = true,
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Next
        ),
        keyboardActions = KeyboardActions(
            onNext = { onNext() }
        ),
        shape = RoundedCornerShape(25.dp),
    )
}

@Composable
fun IdtField(
    placeholder: String,
    value: String,
    onIdtChange: (String) -> Unit,
    onNext: () -> Unit,
) {
    val focusManager = LocalFocusManager.current

    TextField(
        value = value,
        onValueChange = onIdtChange,
        modifier = Modifier.fillMaxWidth(),
        placeholder = {
            Text(
                text = placeholder,
                style = MaterialTheme.typography.displaySmall,
                color = MaterialTheme.colorScheme.onTertiary
            )
        },
        maxLines = 1,
        singleLine = true,
        textStyle = MaterialTheme.typography.displaySmall,
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
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Next
        ),
        keyboardActions = KeyboardActions(
            onDone = {
                focusManager.clearFocus()
                onNext()
            }
        ),
        shape = RoundedCornerShape(25.dp),
    )
}

@Composable
fun RequestHeader(modifier: Modifier, incidentId: Int) {
    Text(
        text = stringResource(id = R.string.title_report),
        color = MaterialTheme.colorScheme.primary,
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.labelLarge,
        modifier = modifier.padding(bottom = 10.dp)
    )

    Text(
        text = "Incidente N°$incidentId",
        color = MaterialTheme.colorScheme.primary,
        style = MaterialTheme.typography.titleSmall,
        modifier = Modifier
            .padding(bottom = 10.dp)
    )
}
