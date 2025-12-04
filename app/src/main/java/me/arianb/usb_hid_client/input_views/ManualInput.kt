package me.arianb.usb_hid_client.input_views

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.materialIcon
import androidx.compose.material.icons.materialPath
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import me.arianb.usb_hid_client.MainViewModel
import me.arianb.usb_hid_client.R
import me.arianb.usb_hid_client.hid_utils.KeyCodeTranslation
import me.arianb.usb_hid_client.settings.SettingsViewModel
import me.arianb.usb_hid_client.ui.theme.PaddingExtraSmall
import me.arianb.usb_hid_client.ui.theme.PaddingLarge
import me.arianb.usb_hid_client.ui.theme.PaddingSmall
import timber.log.Timber

@Composable
fun ManualInput(
    mainViewModel: MainViewModel = viewModel(),
    settingsViewModel: SettingsViewModel = viewModel()
) {
    var isHidden by remember { mutableStateOf(false) }
    val preferencesState by settingsViewModel.userPreferencesFlow.collectAsState()
    val shouldClearManualInputOnSend = preferencesState.clearManualInput
    val context = LocalContext.current

    val createFileLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.CreateDocument("application/octet-stream")
        ) { uri ->
            if (uri != null) {
                // Save file through ViewModel
                context.contentResolver.openOutputStream(uri)?.use { output ->
                    output.write(mainViewModel.getManualInputText().toByteArray())
                }
            }
        }

    val VisibleIcon = materialIcon(name = "Filled.Visibility") {
        materialPath {
            moveTo(12.0f, 4.5f)
            curveToRelative(-7.0f, 0.0f, -11.0f, 7.5f, -11.0f, 7.5f)
            reflectiveCurveToRelative(4.0f, 7.5f, 11.0f, 7.5f)
            reflectiveCurveToRelative(11.0f, -7.5f, 11.0f, -7.5f)
            reflectiveCurveToRelative(-4.0f, -7.5f, -11.0f, -7.5f)
            close()
            moveTo(12.0f, 17.0f)
            curveToRelative(-2.76f, 0.0f, -5.0f, -2.24f, -5.0f, -5.0f)
            reflectiveCurveToRelative(2.24f, -5.0f, 5.0f, -5.0f)
            reflectiveCurveToRelative(5.0f, 2.24f, 5.0f, 5.0f)
            reflectiveCurveToRelative(-2.24f, 5.0f, -5.0f, 5.0f)
            close()
            moveTo(12.0f, 9.5f)
            curveToRelative(-1.38f, 0.0f, -2.5f, 1.12f, -2.5f, 2.5f)
            reflectiveCurveToRelative(1.12f, 2.5f, 2.5f, 2.5f)
            reflectiveCurveToRelative(2.5f, -1.12f, 2.5f, -2.5f)
            reflectiveCurveToRelative(-1.12f, -2.5f, -2.5f, -2.5f)
            close()
        }
    }

    val NotVisibleIcon = materialIcon(name = "Filled.VisibilityOff") {
        materialPath {
            moveTo(12.0f, 4.5f)
            curveToRelative(-7.0f, 0.0f, -11.0f, 7.5f, -11.0f, 7.5f)
            reflectiveCurveToRelative(4.0f, 7.5f, 11.0f, 7.5f)
            reflectiveCurveToRelative(11.0f, -7.5f, 11.0f, -7.5f)
            reflectiveCurveToRelative(-4.0f, -7.5f, -11.0f, -7.5f)
            close()
            moveTo(12.0f, 17.0f)
            curveToRelative(-2.76f, 0.0f, -5.0f, -2.24f, -5.0f, -5.0f)
            reflectiveCurveToRelative(2.24f, -5.0f, 5.0f, -5.0f)
            reflectiveCurveToRelative(5.0f, 2.24f, 5.0f, 5.0f)
            reflectiveCurveToRelative(-2.24f, 5.0f, -5.0f, 5.0f)
            close()
            moveTo(12.0f, 9.5f)
            curveToRelative(-1.38f, 0.0f, -2.5f, 1.12f, -2.5f, 2.5f)
            reflectiveCurveToRelative(1.12f, 2.5f, 2.5f, 2.5f)
            reflectiveCurveToRelative(2.5f, -1.12f, 2.5f, -2.5f)
            reflectiveCurveToRelative(-1.12f, -2.5f, -2.5f, -2.5f)
            close()

            moveTo(2.81f, 2.81f)
            lineToRelative(-1.42f, 1.42f)
            lineToRelative(18.38f, 18.38f)
            lineToRelative(1.42f, -1.42f)
            lineToRelative(-3.2f, -3.2f)
            lineToRelative(-0.0f, 0.0f)
            lineToRelative(-0.0f, 0.0f)
            close()
        }
    }


    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(0.dp, PaddingLarge,0.dp,0.dp),
        verticalArrangement = Arrangement.spacedBy(PaddingSmall),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            modifier = Modifier
                .fillMaxWidth(),
            value = mainViewModel.getManualInputText(),
            label = { Text(stringResource(R.string.manual_input)) },
            onValueChange = { mainViewModel.setManualInputText(it) },
            visualTransformation = if (isHidden) PasswordVisualTransformation()
            else VisualTransformation.None,

            trailingIcon = {
                val icon = if (isHidden) NotVisibleIcon else VisibleIcon
                val description = if (isHidden) "Show text" else "Hide text"

                IconButton(onClick = { isHidden = !isHidden }) {
                    Icon(imageVector = icon, contentDescription = description)
                }
            }
        )

        Row (
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                modifier = Modifier
                    .wrapContentSize()
                    .padding(0.dp,0.dp,PaddingExtraSmall,0.dp),
                shape = RoundedCornerShape(
                    topStart = 20.dp,
                    topEnd = 0.dp,
                    bottomStart = 20.dp,
                    bottomEnd = 0.dp
                ),
                onClick = onClick@{
                    if (mainViewModel.getManualInputText().isEmpty()) {
                        Toast.makeText(context, "Nothing to save", Toast.LENGTH_SHORT).show()
                        return@onClick
                    }

                    createFileLauncher.launch("script.ahc") // .ahc (android hid client)

                }
            ) {
                Text(stringResource(R.string.save))
            }

            Button( // pass to sendString()
                modifier = Modifier
                    .wrapContentSize()
                    .padding(0.dp,0.dp, PaddingExtraSmall,0.dp),
                shape = RoundedCornerShape(
                    topStart = 0.dp,
                    topEnd = 0.dp,
                    bottomStart = 0.dp,
                    bottomEnd = 0.dp
                ),
                onClick = onClick@{
                    if (mainViewModel.getManualInputText().isEmpty()) {
                        Toast.makeText(context, "Nothing to send", Toast.LENGTH_SHORT).show()
                        return@onClick
                    }
                    val stringToSend = mainViewModel.getManualInputText()
                    Timber.d("manual input sending string: %s", stringToSend)

                    // Clear ManualInput if the user's preference is to clear it
                    if (shouldClearManualInputOnSend) {
                        mainViewModel.setManualInputText("")
                    }

                    sendInput(stringToSend, mainViewModel)
                }
            ) {
                Text(stringResource(R.string.send))
            }

            Button( // pass to scriptExecutor()
                modifier = Modifier.wrapContentSize(),
                shape = RoundedCornerShape(
                    topStart = 0.dp,
                    topEnd = 20.dp,
                    bottomStart = 0.dp,
                    bottomEnd = 20.dp
                ),
                onClick = onClick@{
                    if (mainViewModel.getManualInputText().isEmpty()) {
                        Toast.makeText(context, "Nothing to execute", Toast.LENGTH_SHORT).show()
                        return@onClick
                    }

                    // Save string
                    val stringToSend = mainViewModel.getManualInputText()
                    Timber.d("manual input sending string: %s", stringToSend)

                    // Clear EditText if the user's preference is to clear it
                    if (shouldClearManualInputOnSend) {
                        mainViewModel.setManualInputText("")
                    }

                    scriptExecutor(stringToSend, mainViewModel)
                }
            ) {
                Text(stringResource(R.string.execute_manual_input))
            }
        }


    }
}

//-----ORIGINAL for v3.0.1 -----
//fun sendInput(stringToSend: String, mainViewModel: MainViewModel) {
//    // Sends all keys
//    for (char in stringToSend) {
//        val scanCodes = KeyCodeTranslation.keyCharToScanCodes(char)
//        if (scanCodes == null) {
//            val error = "key: '$char' is not supported."
//            Timber.e(error)
//            // FIXME: snackbar
//            // Snackbar.make(parentLayout, error, Snackbar.LENGTH_SHORT).show()
//            return
//        }
//
//        mainViewModel.addStandardKey(scanCodes.first, scanCodes.second)
//    }
//}


fun sendInput(stringToSend: String, mainViewModel: MainViewModel) {
    //---------- Contributed by saaiqSAS ----------
    // Updated to allow the use of {[X]} tag format in the manual input field or in any String passed to this method
    // Hence setting a mockup foundation for scripting and automation

    // MODIFIER KEYS
    // {[C]} for Ctrl
    // {[A]} for Alt
    // {[S]} for Shift
    // {[M]} for Meta/Windows key

    //SPECIAL KEYS
    // {[E]} for Escape
    // {[T]} for Tab
    // {[B]} for Backspace
    // {[N]} for Enter
    // {[U]} for Up
    // {[D]} for Down
    // {[R]} for Right
    // {[L]} for Left
    // {[1]} for F1
    // {[2]} for F2
    // {[3]} for F3
    // {[4]} for F4
    // {[5]} for F5
    // {[6]} for F6
    // {[7]} for F7
    // {[8]} for F8
    // {[9]} for F9
    // {[!]} for F10
    // {[@]} for F11
    // {[#]} for F12

    //COMMANDS
    // {[ ]} to sleep for 500ms

    val arrayLength = stringToSend.length
    var i = 0
    while (i < arrayLength) {
        var key: String = stringToSend[i].toString()
        var scanCodes: Pair<Byte, Byte>?

        if (i+4 < arrayLength) {
            if (stringToSend[i] == '{' && stringToSend[i + 1] == '[' && stringToSend[i + 3] == ']' && stringToSend[i + 4] == '}') {
                when (stringToSend[i + 2]) {
                    'C' -> key = "left-ctrl"
                    'A' -> key = "left-alt"
                    'S' -> key = "left-shift"
                    'M' -> key = "left-meta"
                    'E' -> key = "escape"
                    'T' -> key = "tab"
                    'B' -> key = "backspace"
                    'N' -> key = "\n"
                    'U' -> key = "up"
                    'D' -> key = "down"
                    'R' -> key = "right"
                    'L' -> key = "left"
                    '1' -> key = "f1"
                    '2' -> key = "f2"
                    '3' -> key = "f3"
                    '4' -> key = "f4"
                    '5' -> key = "f5"
                    '6' -> key = "f6"
                    '7' -> key = "f7"
                    '8' -> key = "f8"
                    '9' -> key = "f9"
                    '!' -> key = "f10"
                    '@' -> key = "f11"
                    '#' -> key = "f12"

                    ' ' -> { // sleep for 500ms
                        key = ""
                        Thread.sleep(500)
                    }

                }
                 scanCodes = KeyCodeTranslation.keyCharToScanCodes(key)
                i+=4
            } else {
                scanCodes = KeyCodeTranslation.keyCharToScanCodes(stringToSend[i].toString())
            }
        } else {
            scanCodes = KeyCodeTranslation.keyCharToScanCodes(stringToSend[i].toString())
        }
        i++;

       
        if (scanCodes == null) {
            val error = "key: '$key' is not supported."
            Timber.e(error)
            return
        }

        if (scanCodes.second != 0x0.toByte()) {
            mainViewModel.addStandardKey(scanCodes.first, scanCodes.second)
        }

    }

}


