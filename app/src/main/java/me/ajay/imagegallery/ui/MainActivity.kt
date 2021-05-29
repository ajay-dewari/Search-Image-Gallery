package me.ajay.imagegallery.ui

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.NetworkInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import dagger.hilt.android.AndroidEntryPoint
import me.ajay.imagegallery.R


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private lateinit var internetBroadcastReceiver: BroadcastReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_container)
                    as NavHostFragment
        navController = navHostFragment.findNavController()
        setupActionBarWithNavController(navController)

        internetBroadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(contxt: Context, intent: Intent?) {
                val noInternetFragmentID = R.id.noInternetFragment
                if (isOnline()) {
                    val currentId = navController.currentDestination!!.id
                    if (noInternetFragmentID == currentId) {
                        navController.popBackStack()
                    }
                } else {
                    navController.navigate(noInternetFragmentID)
                }
            }
        }
        this.registerReceiver(
            internetBroadcastReceiver, IntentFilter(
                ConnectivityManager
                    .CONNECTIVITY_ACTION
            )
        )

    }

    fun isOnline(): Boolean {
        val connMgr = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo: NetworkInfo? = connMgr.activeNetworkInfo
        return networkInfo?.isConnected == true
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    override fun onDestroy() {
        super.onDestroy()
        this.unregisterReceiver(internetBroadcastReceiver)
    }

}