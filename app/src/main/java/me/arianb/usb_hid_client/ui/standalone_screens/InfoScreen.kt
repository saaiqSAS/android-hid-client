package me.arianb.usb_hid_client.ui.standalone_screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.Navigator
import me.arianb.usb_hid_client.BuildConfig
import me.arianb.usb_hid_client.R
import me.arianb.usb_hid_client.ui.theme.PaddingLarge
import me.arianb.usb_hid_client.ui.theme.PaddingNormal
import me.arianb.usb_hid_client.ui.utils.BasicPage
import me.arianb.usb_hid_client.ui.utils.DarkLightModePreviews
import me.arianb.usb_hid_client.ui.utils.SimpleNavTopBar

class InfoScreen : Screen {
    @Composable
    override fun Content() {
        InfoPage()
    }
}

@Composable
fun InfoPage() {
    val scrollState = rememberScrollState()
    BasicPage(
        topBar = { InfoTopBar() },
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .verticalScroll(scrollState),  // Apply vertical scrolling
             horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_launcher_foreground),
                contentDescription = stringResource(R.string.info_icon_description)
            )
            AppDescriptionText()
            AppDeveloperText()
            AppContributorsText()
            AppSourceCodeButton()
            AppLicenceText()
            AppVersionText()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun InfoTopBar() {
    SimpleNavTopBar(
        title = stringResource(id = R.string.info)
    )
}

@Composable
fun AppDescriptionText() {
    Text(
        text = stringResource(R.string.info_description),
        textAlign = TextAlign.Center
    )
}

@Composable
fun AppLicenceText() {
    Text(
        text = stringResource(R.string.license),
        textAlign = TextAlign.Center
    )
}

@Composable
fun AppDeveloperText() {
    Text(
        text = stringResource(R.string.original_developer),
        textAlign = TextAlign.Center
    )
}

@Composable
fun AppContributorsText() {
    Text(
        text = stringResource(R.string.contributors),
        textAlign = TextAlign.Center
    )
}
@Composable
fun AppVersionText() {
    Text(
        text = BuildConfig.VERSION_NAME,
        textAlign = TextAlign.Center
    )
}

@Composable
fun AppSourceCodeButton() {
    val uriHandler = LocalUriHandler.current
    val sourceCodeURL = stringResource(R.string.info_source_code_url)

    Button(
        onClick = { uriHandler.openUri(sourceCodeURL) },
        modifier = Modifier
            .padding(0.dp, PaddingLarge,0.dp,PaddingLarge),
    ) {
        Image(
            painter = painterResource(R.drawable.ic_github),
            contentDescription = "Github icon",
            modifier = Modifier.padding(end = PaddingNormal),
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primaryContainer)
        )
        Text(text = "Source Code")
    }
}

@DarkLightModePreviews
@Composable
private fun InfoScreenPreview() {
    Navigator(InfoScreen())
}
