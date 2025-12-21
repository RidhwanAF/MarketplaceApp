package com.raf.marketplaceapp

import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.raf.core.domain.model.DarkTheme
import com.raf.marketplaceapp.navigation.AppNavGraph
import com.raf.marketplaceapp.navigation.Route
import com.raf.marketplaceapp.ui.theme.MarketPlaceAppTheme
import com.raf.marketplaceapp.viewmodel.AppState
import com.raf.marketplaceapp.viewmodel.AppViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var appViewModel: AppViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val content: View = findViewById(android.R.id.content)
        content.viewTreeObserver.addOnPreDrawListener(
            object : ViewTreeObserver.OnPreDrawListener {
                override fun onPreDraw(): Boolean {
                    return if (appViewModel.appState.value is AppState.Loaded) {
                        content.viewTreeObserver.removeOnPreDrawListener(this)
                        true
                    } else false
                }
            }
        )

        setContent {
            appViewModel = hiltViewModel()
            val appState by appViewModel.appState.collectAsStateWithLifecycle()

            if (appState is AppState.Loaded) {
                val appSettings = (appState as AppState.Loaded).appSettings
                val isLoggedIn = (appState as AppState.Loaded).isLoggedIn

                val darkTheme = when (appSettings.darkTheme) {
                    DarkTheme.FOLLOW_SYSTEM -> isSystemInDarkTheme()
                    DarkTheme.LIGHT -> false
                    DarkTheme.DARK -> true
                }
                val startDestination = if (isLoggedIn) Route.Home else Route.Auth

                MarketPlaceAppTheme(
                    darkTheme = darkTheme,
                    dynamicColor = appSettings.dynamicColor,
                ) {
                    AppNavGraph(startDestination = startDestination)
                }
            }
        }
    }
}
