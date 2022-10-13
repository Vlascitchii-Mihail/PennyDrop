package com.bignerdranch.android.pennydrop

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.NavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.onNavDestinationSelected
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.preference.PreferenceManager
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        //getDefaultSharedPreferences() - Gets a SharedPreferences instance that points to
        // the default file that is used by the preference framework in the given context.
        val prefs = PreferenceManager.getDefaultSharedPreferences(this)

        //receiving the theme ID
        val themeId = when(prefs.getString("theme", "AppTheme")) {
            "Kotlin" -> R.style.Kotlin
            "Crew" -> R.style.Crew
            "FTD" -> R.style.FTD
            "GPG" -> R.style.GPG
            "Hazel" -> R.style.Hazel
            else -> R.style.Theme_PennyDrop
        }

        //set new theme at app start
        //using before  setContentView(R.layout.activity_main)
        setTheme(themeId)



        //change tne mode on App Startup using settings
        val nightMode = when(prefs.getString("themeMode", "")) {
            "Light" -> AppCompatDelegate.MODE_NIGHT_NO
            "Dark" -> AppCompatDelegate.MODE_NIGHT_YES
            else -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
        }

        //Sets the default night mode.
        AppCompatDelegate.setDefaultNightMode(nightMode)



        //connects activity_main.xml with MainActivity.kt
        setContentView(R.layout.activity_main)

        //finding containerFragment
        //NavHostFragment предоставляет область в вашем макете для автономной навигации.
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.containerFragment) as NavHostFragment

        //Каждый NavHostFragment имеет NavController, который определяет допустимую навигацию внутри узла навигации.
        // Это включает в себя граф навигации, а также состояние навигации, такое как текущее местоположение и задний
        // стек, которые будут сохранены и восстановлены вместе с самим NavHostFragment.
        this.navController = navHostFragment.navController

        //setupWithNavController() - Настраивает NavigationBarView для использования с NavController. Это вызовет
        // android.view.MenuItem.onNavDestinationSelected при выборе пункта меню.
        //Выбранный элемент в NavigationView будет автоматически обновляться при изменении пункта назначения.
        findViewById<BottomNavigationView>(R.id.bottom_nav).setupWithNavController(this.navController)



        //AppBarConfiguration() - create a new AppBar
        //send all the classes (from bottom_nav menu) to constructor, which shouldn't display
                // their label from nav_graph.xml on the appBar with a back arrow
        //also we can write tne names of thr classes instead of sending bottom_nav menu
        val appBarConfiguration =
            AppBarConfiguration(findViewById<BottomNavigationView>(R.id.bottom_nav).menu)

        //setup the AppBar
        setupActionBarWithNavController(this.navController, appBarConfiguration)
    }

    /**
     * create optionMenu in the app bar
     */
    override fun onCreateOptionsMenu(menu: Menu?) : Boolean {
        super.onCreateOptionsMenu(menu)

        //inflate ihe option menu
        menuInflater.inflate(R.menu.options, menu)
        return true
    }

    /**
     * menu item listener
     */
    override fun onOptionsItemSelected(item: MenuItem) : Boolean =
        if (this::navController.isInitialized) {

            //navigate to the item
            item.onNavDestinationSelected(this.navController) ||
                    super.onOptionsItemSelected(item)
        } else false



    //navigate Up(back) using the arrow left on the action bar using navController
    override fun onSupportNavigateUp(): Boolean =
        (this::navController.isInitialized &&
                this.navController.navigateUp() || super.onSupportNavigateUp())
}