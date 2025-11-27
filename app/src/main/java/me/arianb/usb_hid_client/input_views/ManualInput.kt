package me.arianb.usb_hid_client.input_views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
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
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import me.arianb.usb_hid_client.MainViewModel
import me.arianb.usb_hid_client.R
import me.arianb.usb_hid_client.hid_utils.KeyCodeTranslation
import me.arianb.usb_hid_client.settings.SettingsViewModel
import me.arianb.usb_hid_client.ui.theme.PaddingLarge
import timber.log.Timber
import java.util.Locale.getDefault

@Composable
fun ManualInput(
    mainViewModel: MainViewModel = viewModel(),
    settingsViewModel: SettingsViewModel = viewModel()
) {
    var manualInputString by remember { mutableStateOf("") }
    val preferencesState by settingsViewModel.userPreferencesFlow.collectAsState()
    val shouldClearManualInputOnSend = preferencesState.clearManualInput

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(PaddingLarge),
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f), // For some reason, this makes the button not be squished at the end of the Row
            value = manualInputString,
            label = { Text(stringResource(R.string.manual_input)) },
            onValueChange = { manualInputString = it }
        )

        Column (
            modifier = Modifier.wrapContentSize(),
            verticalArrangement = Arrangement.spacedBy(PaddingLarge),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button( // pass to sendString()
                modifier = Modifier.wrapContentSize(),
                onClick = onClick@{
                    // If empty, don't do anything
                    if (manualInputString.isEmpty()) {
                        return@onClick
                    }

                    // Save string
                    val stringToSend = manualInputString
                    Timber.d("manual input sending string: %s", stringToSend)

                    // Clear EditText if the user's preference is to clear it
                    if (shouldClearManualInputOnSend) {
                        manualInputString = ""
                    }

                    sendInput(stringToSend, mainViewModel)
                }
            ) {
                Text(stringResource(R.string.send))
            }

            Button( // pass to scriptExecutor()
                modifier = Modifier.wrapContentSize(),
                onClick = onClick@{
                    // If empty, don't do anything
                    if (manualInputString.isEmpty()) {
                        return@onClick
                    }

                    // Save string
                    val stringToSend = manualInputString
                    Timber.d("manual input sending string: %s", stringToSend)

                    // Clear EditText if the user's preference is to clear it
                    if (shouldClearManualInputOnSend) {
                        manualInputString = ""
                    }

                    scriptExecutor(stringToSend, mainViewModel)
                }
            ) {
                Text(stringResource(R.string.exec))
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

fun scriptExecutor(script: String, mainViewModel: MainViewModel) {
    //---------- Contributed by saaiqSAS ----------
    // This method allows the use of scripting. The entire script should be passed to this method and it will be executed line by line

    val lines = (script).split("\n")


    Timber.d("num of lines: " + lines.size) //test

    for (line in lines) {
        val command: String
        var key = ""
        var para = ""
        val firstSpace = line.indexOf(" ")

        Timber.d("line: " + line) //test

        if (firstSpace == -1) {
            command = line
        } else {
            command = line.take(firstSpace)
            para = line.substring(firstSpace + 1)
        }

        Timber.d("command: $command") //test
        Timber.d("para: $para") //test

        when (command.uppercase(getDefault())) {
            //COMMANDS - only commands takes a parameter
            "//","REM","NAME","DESC","AUTHOR"   -> {} //do nothing
            "SEND", "STRING"                    -> sendInput(para, mainViewModel)
            "SENDLN", "STRINGLN"                -> sendInput(para+"\n", mainViewModel)
            "SLEEP","DELAY"                     -> Thread.sleep(para.toLong())

            //MODIFIER KEYS
            "L_CTRL", "L_CONTROL", "CTRL"       -> key = "left-ctrl"
            "L_ALT", "ALT"                      -> key = "left-alt"
            "L_SHIFT", "SHIFT"                  -> key = "left-shift"
            "L_META", "L_WIN", "WIN"            -> key = "left-meta"
            "R_CTRL", "R_CONTROL"               -> key = "right-ctrl"
            "R_ALT"                             -> key = "right-alt"
            "R_SHIFT"                           -> key = "right-shift"
            "R_META", "R_WIN"                   -> key = "right-meta"

            //SPECIAL KEYS
            "UP"                                -> key = "up"
            "DOWN"                              -> key = "down"
            "LEFT"                              -> key = "left"
            "RIGHT"                             -> key = "right"
            "ESCAPE", "ESC"                     -> key = "escape"
            "TAB"                               -> key = "tab"
            "BACKSPACE","BACK"                  -> key = "backspace"
            "DELETE","DEL"                      -> key = "delete"
            "PRINT"                             -> key = "print"
            "SPACE"                             -> key = " "
            "ENTER"                             -> key = "\n"
            "SCROLL_LOCK"                       -> key = "scroll-lock"
            "NUM_LOCK"                          -> key = "num-lock"
            "PAUSE"                             -> key = "pause"
            "INSERT"                            -> key = "insert"
            "HOME"                              -> key = "home"
            "END"                               -> key = "end"
            "PAGE_UP", "PG_UP"                  -> key = "page-up"
            "PAGE_DOWN", "PG_DOWN"              -> key = "page-down"
            "NEXT"                              -> key = "next"
            "PREVIOUS", "PREV"                  -> key = "previous"
            "PLAY_PAUSE", "PLAY","PP"           -> key = "play-pause"
            "VOLUME_UP", "VOL_UP"               -> key = "volume-up"
            "VOLUME_DOWN", "VOL_DOWN"           -> key = "volume-down"
            "F1"                                -> key = "f1"
            "F2"                                -> key = "f2"
            "F3"                                -> key = "f3"
            "F4"                                -> key = "f4"
            "F5"                                -> key = "f5"
            "F6"                                -> key = "f6"
            "F7"                                -> key = "f7"
            "F8"                                -> key = "f8"
            "F9"                                -> key = "f9"
            "F10"                               -> key = "f10"
            "F11"                               -> key = "f11"
            "F12"                               -> key = "f12"


        }
        if (!key.isEmpty()) {
            val scanCodes = KeyCodeTranslation.keyCharToScanCodes(key)

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

}
