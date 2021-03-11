package ir.mrhib.dong.view

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import ir.mrhib.dong.R
import ir.mrhib.dong.databinding.ActivityMainBinding
import ir.mrhib.dong.model.Account
import ir.mrhib.dong.view.dialog.DialogAddExpense
import ir.mrhib.dong.view.dialog.DialogAddPerson
import ir.mrhib.dong.viewmodel.PersonsViewModel
import ir.mrhib.dong.viewmodel.PersonsViewModelFactory

class MainActivity : AppCompatActivity() {
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var account: Account
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        account = intent.extras?.get("account") as Account
        //fetchPersons()
        setupNavigation()
        navController.setGraph(R.navigation.mobile_navigation)
    }

    private fun setupNavigation() {
        val navView: NavigationView = findViewById(R.id.nav_view)
        navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_persons, R.id.nav_expenses, R.id.nav_report
            ), binding.drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)


        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.nav_report -> {
                    binding.include.fab.visibility = View.GONE
                }
                R.id.nav_persons -> {
                    binding.include.fab.visibility = View.VISIBLE
                    binding.include.fab.setOnClickListener {
                        DialogAddPerson(true, null, account.account_id).show(
                            supportFragmentManager,
                            "DialogAddAccount"
                        )
                    }
                }
                else -> {
                    binding.include.fab.visibility = View.VISIBLE
                    binding.include.fab.setOnClickListener {
                        if (account.persons.isEmpty()) {
                            Toast.makeText(
                                applicationContext,
                                getString(R.string.toast_msg_add_person),
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            DialogAddExpense(account).show(
                                supportFragmentManager,
                                "DialogAddAccount"
                            )
                        }
                    }
                }
            }
        }
    }
/*

    private fun fetchPersons(){
        val personsViewModel: PersonsViewModel by viewModels {
            PersonsViewModelFactory(applicationContext, account)
        }
        personsViewModel.personsList.apply {
            account.persons = value!!
        }
    }
*/


    override fun onBackPressed() {
        when (navController.currentDestination?.id) {
            R.id.nav_expenses -> {
                finish()
            }
            else -> {
                navController.navigate(R.id.nav_expenses)
            }
        }
    }

    fun getSelectedAccount(): Account {
        return account
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}