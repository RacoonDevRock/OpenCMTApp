package com.cmt.openapp.detail.ui

import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilePresent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
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
import com.cmt.openapp.core.ui.shared.dialog.TopDialogSheet
import com.cmt.openapp.core.ui.shared.loading.LoadingScreen
import com.cmt.openapp.detail.data.network.response.IncidentDTODetail
import com.cmt.openapp.detail.ui.viewmodel.DetailViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun DetailIncidentScreen(
    modifier: Modifier,
    navigationController: NavHostController,
    incidentId: String,
    viewModel: DetailViewModel = hiltViewModel(),
    onThemeChange: (Int) -> Unit,
    onTypographyChange: (Typography) -> Unit,
) {
    val isLoading by viewModel.isLoading.collectAsState()
    var isTopDialogVisible by rememberSaveable { mutableStateOf(false) }
    val incidentDetail by viewModel.incidentDetail.collectAsState()
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        withContext(Dispatchers.IO) {
            viewModel.loadIncidentDetail(incidentId.toLong())
        }
    }

    ConstraintLayout(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        val (header, detail, fold) = createRefs()

        HeaderSection(
            modifier = Modifier.constrainAs(header) { top.linkTo(parent.top) },
            isInfo = false,
            onInfoClick = {},
            onBackClick = {
                scope.launch {
                    navigationController.popBackStack()
                }
            })

        Column(
            modifier = Modifier
                .fillMaxSize()
                .constrainAs(detail) {
                    top.linkTo(header.bottom)
                    bottom.linkTo(fold.top)
                    height = Dimension.fillToConstraints
                },
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            if (isLoading) {
                LoadingScreen()
            } else {
                incidentDetail?.let {
                    IncidentDetailsContainer(
                        it, Modifier.fillMaxWidth()
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

        }

        FAB(
            isDarkTheme = AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES,
            onThemeChange = onThemeChange, { },
            currentTypography = MaterialTheme.typography,
            onTypographyChange = onTypographyChange
        )

        RequestedBox(
            incidentId,
            { id ->
                navigationController.navigate(Routes.ReportScreen.createRoute(id.toLong()))
            },
            Modifier
                .padding(top = 20.dp)
                .constrainAs(fold) {
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },
        )

        if (isTopDialogVisible) {
            TopDialogSheet(onDismissRequest = { isTopDialogVisible = false }) {
                InfoContent()
            }
        }
    }
}

@Composable
fun IncidentDetailsContainer(incidentDetail: IncidentDTODetail, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 25.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surfaceContainer)
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp)
        ) {
            IncidentHeader(
                incidentDetail.nroIncidente,
                incidentDetail.fecha,
                incidentDetail.horallamada
            )
            Spacer(modifier = Modifier.height(10.dp))
            IncidentDetails(
                incidentDetail.tipoIncidente,
                incidentDetail.zona,
                incidentDetail.sector,
                incidentDetail.tipoIntervencion,
                incidentDetail.resultado
            )
        }
    }
}

@Composable
fun RequestedBox(
    incidentId: String,
    navigateToReport: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(210.dp)
            .clip(RoundedCornerShape(topStart = 110.dp, topEnd = 110.dp))
            .background(MaterialTheme.colorScheme.primaryContainer),
    ) {
        Column(
            Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(id = R.string.previous_info_report),
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier
                    .padding(horizontal = 50.dp)
                    .align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.height(25.dp))
            MyButton(
                { navigateToReport(incidentId) },
                stringResource(id = R.string.previous_button_report),
                Icons.Default.FilePresent
            )
        }
    }
}

@Composable
fun IncidentDetails(
    incidentType: String,
    zone: String,
    sector: String,
    interventionType: String,
    interventionResult: String,
) {
    MySection(stringResource(id = R.string.incident_type_field_filter))
    MySectionData(incidentType)
    Spacer(modifier = Modifier.height(3.dp))
    MySection(stringResource(id = R.string.zone_field_filter))
    MySectionData(zone)
    Spacer(modifier = Modifier.height(3.dp))
    MySection(stringResource(id = R.string.sector_field_filter))
    MySectionData(sector)
    Spacer(modifier = Modifier.height(3.dp))
    MySection(stringResource(id = R.string.subtitle_intervention_type_report))
    MySectionData(interventionType)
    Spacer(modifier = Modifier.height(3.dp))
    MySection(stringResource(id = R.string.subtitle_intervention_result_report))
    MySectionData(interventionResult)
}

@Composable
fun IncidentHeader(incidentNumber: String, fecha: String, hora: String) {
    Row(
        Modifier.fillMaxWidth()
    ) {
        Text(
            text = "Incidente N° $incidentNumber",
            style = MaterialTheme.typography.displaySmall,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = "$fecha $hora",
            style = MaterialTheme.typography.displaySmall,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
fun MySectionData(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.displaySmall,
        textAlign = TextAlign.Justify,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 1.dp)
            .padding(bottom = 5.dp)
    )
}

@Composable
fun MySection(text: String) {
    Text(
        text = text,
        color = MaterialTheme.colorScheme.primary,
        style = MaterialTheme.typography.headlineSmall,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.fillMaxWidth()
    )
}

