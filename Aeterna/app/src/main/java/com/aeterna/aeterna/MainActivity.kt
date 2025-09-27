package com.aeterna.aeterna

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.aeterna.aeterna.ui.MainScreen
import com.aeterna.aeterna.ui.theme.AeternaTheme
import com.aeterna.core.data.datastore.AuthDataStore
import com.aeterna.core.youtube.AuthManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import net.openid.appauth.AuthorizationResponse
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var authManager: AuthManager

    @Inject
    lateinit var authDataStore: AuthDataStore

    private val viewModel: MainActivityViewModel by viewModels()

    private lateinit var authActivityResultLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        authActivityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            val data: Intent? = result.data
            if (data != null) {
                val authorizationResponse = AuthorizationResponse.fromIntent(data)

                if (authorizationResponse != null) {
                    authManager.exchangeToken(authorizationResponse) { authState ->
                        if (authState != null) {
                            CoroutineScope(Dispatchers.IO).launch {
                                authDataStore.saveAuthState(authState)
                                // TODO: Notify UI about successful login (e.g., navigate to home screen)
                            }
                        } else {
                            // TODO: Handle authentication failure (e.g., show error message)
                        }
                    }
                }
            }
        }

        setContent {
            val isAuthenticated by viewModel.isAuthenticated.collectAsState()

            AeternaTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen(
                        isAuthenticated = isAuthenticated,
                        onSignInClick = {
                            val authIntent = authManager.getAuthRequestIntent()
                            authActivityResultLauncher.launch(authIntent)
                        }
                    )
                }
            }
        }
    }
}