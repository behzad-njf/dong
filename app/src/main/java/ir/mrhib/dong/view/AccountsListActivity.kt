package ir.mrhib.dong.view

import android.annotation.TargetApi
import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.lifecycle.ViewModelProvider
import ir.mrhib.dong.R
import ir.mrhib.dong.databinding.ActivityAccountsBinding

import ir.mrhib.dong.model.Account
import ir.mrhib.dong.view.adapter.AccountsListAdapter
import ir.mrhib.dong.view.dialog.DialogAddAccount
import ir.mrhib.dong.viewmodel.AccountsViewModel
import java.util.*


class AccountsListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAccountsBinding

    private lateinit var accountsListAdapter: AccountsListAdapter
    private lateinit var accountViewModel: AccountsViewModel
    private lateinit var accountsList: List<Account>
    private lateinit var sharedPrefs: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAccountsBinding.inflate(layoutInflater)
        val view = binding.root
        sharedPrefs = applicationContext.getSharedPreferences("DongSP", Context.MODE_PRIVATE)
        checkLocale()
        setContentView(view)
        setSupportActionBar(binding.toolbar)


        binding.fab.setOnClickListener { _ ->
            DialogAddAccount(true, null).show(supportFragmentManager, "DialogAddAccount")
        }
        accountsListAdapter = AccountsListAdapter(supportFragmentManager, this)
        binding.adapter = accountsListAdapter
        initData()
    }

    private fun initData() {
        accountViewModel = ViewModelProvider(this).get(AccountsViewModel::class.java)
        accountViewModel.accountsList.observe(this,
            { accounts: List<Account> ->
                accountsList = accounts
                accountsListAdapter.setAccounts(accounts)
            }
        )
    }

    private fun checkLocale() {
        val locale: Locale
        if (sharedPrefs.getInt("LANG", 1) == 1) {
            locale = Locale("fa")
        } else {
            locale = Locale("en")
        }
        Locale.setDefault(locale)
        val config = Configuration()
        config.locale = locale
        baseContext.resources.updateConfiguration(
            config,
            baseContext.resources.displayMetrics
        )
    }

    private fun changeLang() {
        val locale: Locale
        if (sharedPrefs.getInt("LANG", 1) == 1) {
            locale = Locale("en")
            sharedPrefs.edit { putInt("LANG", 2) }
        } else {
            locale = Locale("fa")
            sharedPrefs.edit { putInt("LANG", 1) }
        }
        Locale.setDefault(locale)
        val config = Configuration()
        config.locale = locale
        baseContext.resources.updateConfiguration(
            config,
            baseContext.resources.displayMetrics
        )
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        //menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {
            R.id.action_settings -> {
                changeLang()
                recreate()
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}