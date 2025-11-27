package me.arianb.usb_hid_client.input_views

import android.content.ContentResolver
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import me.arianb.usb_hid_client.MainViewModel
import me.arianb.usb_hid_client.hid_utils.KeyCodeTranslation
import me.arianb.usb_hid_client.ui.theme.PaddingLarge
import me.arianb.usb_hid_client.ui.theme.PaddingNormal
import me.arianb.usb_hid_client.ui.theme.PaddingSmall
import timber.log.Timber
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.util.Locale.getDefault


//---------- Contribution by saaiqSAS ----------
@Composable
fun ScriptsDisplayView(mainViewModel: MainViewModel = viewModel()) {
    var scriptPathString by remember { mutableStateOf("") }
    var scriptFileUri by remember { mutableStateOf<Uri?>(null) }

    val context = LocalContext.current
    val contentResolver = context.contentResolver
    val scriptFileExtension = ".txt"

    val filePickerLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                val mimeType = context.contentResolver.getType(uri)
                if (mimeType == "text/plain" && uri.path.toString().endsWith(scriptFileExtension)) {
                    scriptPathString = uri.path.toString()
                    scriptFileUri = uri
                } else {
                    Toast.makeText(context,"Only $scriptFileExtension files are accepted", Toast.LENGTH_SHORT).show()
                }
            }

        }

    Column(
        modifier = Modifier
            .fillMaxHeight()
            .padding(0.dp, PaddingLarge,0.dp,0.dp),
        verticalArrangement = Arrangement.spacedBy(PaddingSmall),
        horizontalAlignment = Alignment.CenterHorizontally,

    ) {
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            value = scriptPathString,
            label = { Text("Script File Path ($scriptFileExtension)") },
            onValueChange = { scriptPathString = it },
            readOnly = true
        )

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                modifier = Modifier
                    .wrapContentSize()
                    .padding(0.dp,0.dp,PaddingNormal,0.dp),
                onClick = onClick@{
                    filePickerLauncher.launch("application/plain")
                }
            ) {
                Text("Select")
            }

            Button(
                modifier = Modifier.wrapContentSize(),
                onClick = onClick@{
                    executeScriptFile(contentResolver, scriptFileUri, mainViewModel)
                }
            ) {
                Text("Execute")
            }
        }

    }


}

fun executeScriptFile(contentResolver: ContentResolver, scriptFileUri: Uri?, mainViewModel: MainViewModel) {
    if (scriptFileUri != null) {
        val content = readFileFromUri(contentResolver ,scriptFileUri).toString()
        scriptExecutor(content, mainViewModel)
    }
}

fun readFileFromUri(contentResolver: ContentResolver, fileUri: Uri): String? {
    var fileContent: String? = null
    val inputStream = contentResolver.openInputStream(fileUri)

    inputStream?.let {
        try {
            val reader = BufferedReader(InputStreamReader(it))
            val stringBuilder = StringBuilder()

            reader.forEachLine { line ->
                stringBuilder.append(line).append("\n")
            }

            fileContent = stringBuilder.toString()
            reader.close()
        } catch (e: IOException) {
            Timber.e("Error reading file: ${e.message}")
        } finally {
            it.close()
        }
    }
    return fileContent
}


fun scriptExecutor(script: String, mainViewModel: MainViewModel) {
    //---------- Contribution by saaiqSAS ----------
    // DECODER FOR SCRIPTING FORMAT
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