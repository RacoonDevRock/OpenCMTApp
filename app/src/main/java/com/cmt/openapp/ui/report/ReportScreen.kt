package com.cmt.openapp.ui.report

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.cmt.openapp.R
import com.cmt.openapp.model.Routes
import com.cmt.openapp.ui.research.HeaderSection
import com.cmt.openapp.ui.research.InfoContent
import com.cmt.openapp.ui.home.MyButtonNavigate
import com.cmt.openapp.ui.research.TopDialogSheet

//@Preview(showSystemUi = true)
@Composable
fun ReportScreen(modifier: Modifier, navigationController: NavHostController) {
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

            Spacer(modifier = Modifier.height(20.dp))

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
            MyButtonNavigate(
                navigate,
                stringResource(id = R.string.report_button),
                Icons.Default.FilePresent
            )
        }
    }
}

@Composable
fun RequestForm() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        TextFieldRequest(stringResource(id = R.string.name_field_report), "") {}
        TextFieldRequest(stringResource(id = R.string.id_field_report), "") {}
        TextFieldRequest(stringResource(id = R.string.address_field_report), "") {}
        TextFieldRequest(stringResource(id = R.string.city_field_report), "") {}
        TextFieldRequest(stringResource(id = R.string.email_field_report), "") {}
        TextFieldRequest(stringResource(id = R.string.phone_field_report), "") {}
        TextFieldRequest(stringResource(id = R.string.motive_field_report), "") {}
    }
}

@Composable
fun RequestHeader(modifier: Modifier) {
    Text(
        text = stringResource(id = R.string.title_report),
        color = Color.Black,
        textAlign = TextAlign.Center,
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold,
        modifier = modifier
            .padding(top = 24.dp, bottom = 15.dp, start = 70.dp, end = 70.dp)
    )

    Text(
        text = "Incidente N°1999",
        color = Color.Black,
        fontSize = 15.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier
            .padding(bottom = 15.dp)
    )
}

@Composable
fun TextFieldRequest(label: String, value: String, onValueChange: (String) -> Unit) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = {
            Text(
                text = label,
                fontWeight = FontWeight.ExtraBold,
                modifier = Modifier.padding(start = 4.dp),
                fontSize = 14.sp,
                color = Color(0xFF848688)
            )
        },
        modifier = Modifier
            .padding(start = 30.dp, end = 30.dp, bottom = 13.dp)
            .height(50.dp),
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
