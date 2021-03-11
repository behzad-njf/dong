package ir.mrhib.dong.view.fragments

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.*
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import ir.mrhib.dong.R
import ir.mrhib.dong.databinding.FragmentReportBinding
import ir.mrhib.dong.model.Account
import ir.mrhib.dong.model.Person
import ir.mrhib.dong.model.PersonWithTotalExpense
import ir.mrhib.dong.model.Transaction
import ir.mrhib.dong.repository.AccountingDatabase
import ir.mrhib.dong.view.MainActivity
import ir.mrhib.dong.view.adapter.TransactionsListAdapter
import ir.mrhib.dong.viewmodel.PersonsViewModel
import ir.mrhib.dong.viewmodel.PersonsViewModelFactory
import kotlinx.coroutines.*
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.text.DecimalFormat
import kotlin.math.abs
import kotlin.math.min


class ReportFragment : Fragment() {

    private var finalOutput: String = ""
    private lateinit var acc: Account
    private lateinit var binding: FragmentReportBinding
    private var total: Long = 0
    private var dong: Long = 0
    private var mainPersonIndex = 0

    lateinit var list: List<PersonWithTotalExpense>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentReportBinding.inflate(inflater, container, false)
        val view = binding.root
        acc = (activity as MainActivity).getSelectedAccount()
        setHasOptionsMenu(true)

        GlobalScope.launch(Dispatchers.Main) {
            withContext(Dispatchers.Default) {
                initData()
            }
            if (list.isEmpty()) {
                binding.txtEnterPersonsExpenses.visibility = View.VISIBLE
                binding.scrollView.visibility = View.GONE
            } else {
                binding.txtEnterPersonsExpenses.visibility = View.GONE
                binding.scrollView.visibility = View.VISIBLE
                calcTotalAndDong()
                summaryData()
                suggestedSolution()
            }
        }
        return view
    }


    private suspend fun initData() {
        val personsViewModel: PersonsViewModel by viewModels {
            PersonsViewModelFactory(requireContext(), acc)
        }
        list = personsViewModel.getAllPersonsWithTotalExpenses()
    }

    private fun calcTotalAndDong() {
        list.forEachIndexed { index, person ->
            total += person.totalExpense
        }
        binding.txtSumPrices.text = String.format(
            "%s  %s", DecimalFormat("###,###,###").format(total).toString(),
            getString(R.string.price_unit)
        )
        dong = total / list.size
        binding.txtDong.text = String.format(
            "%s  %s", DecimalFormat("###,###,###").format(dong).toString(),
            getString(R.string.price_unit)
        )
    }

    private fun summaryData() {
        val sumMainPerson = 0
        list.forEachIndexed { index, person ->
            val sahm: Long = person.totalExpense - dong
            if (sahm > 0 && person.totalExpense > sumMainPerson) mainPersonIndex = index

            val layoutSummaries = createLinearLayout()
            val layoutDebtorCreditor = createLinearLayout()

            val txtPersonName = createTextView(person.person_name, 0.5f, Gravity.START)
            txtPersonName.setCompoundDrawablesRelativeWithIntrinsicBounds(
                R.drawable.ic_person,
                0,
                0,
                0
            )
            layoutSummaries.addView(txtPersonName)
            val txtPersonName2 = createTextView(person.person_name, 0.5f, Gravity.START)
            txtPersonName2.setCompoundDrawablesRelativeWithIntrinsicBounds(
                R.drawable.ic_person,
                0,
                0,
                0
            )
            layoutDebtorCreditor.addView(txtPersonName2)

            layoutDebtorCreditor.addView(
                createTextView(
                    if (sahm < 0) getString(R.string.Deptor) else if (sahm > 0) getString(R.string.Creditor) else "",
                    0.35f, Gravity.START
                )
            )
            layoutDebtorCreditor.addView(
                createTextView(
                    String.format(
                        "%s  %s", DecimalFormat("###,###,###").format(abs(sahm)).toString(),
                        getString(R.string.price_unit)
                    ), 0.35f, Gravity.START
                )
            )
            layoutSummaries.addView(
                createTextView(
                    String.format(
                        "%s  %s",
                        DecimalFormat("###,###,###").format(person.totalExpense).toString(),
                        getString(R.string.price_unit)
                    ), 0.5f, Gravity.END
                )
            )
            binding.layoutSummaries.addView(layoutSummaries)
            binding.layoutCreditorsAndDebtors.addView(layoutDebtorCreditor)
            if (index != list.size - 1) {
                drawLine(binding.layoutSummaries)
                drawLine(binding.layoutCreditorsAndDebtors)
            }
        }

    }

    private fun suggestedSolution() {
        val input = Array(list.size) {
            IntArray(
                list.size
            )
        }
        list.forEachIndexed { index, person ->
            if (person.totalExpense - dong < 0) {
                input[index][mainPersonIndex] = abs(person.totalExpense - dong).toInt()
            } else if (person.totalExpense - dong > 0 && index != mainPersonIndex) {
                input[mainPersonIndex][index] = abs(person.totalExpense - dong).toInt()
            }
        }
        minCashFlow(input)
        if (finalOutput.length > 1) {
            finalOutput = finalOutput.substring(0, finalOutput.length - 1)
            val transactions: MutableList<Transaction> = ArrayList()
            for (str in finalOutput.split("@").toTypedArray()) {
                transactions.add(
                    Transaction(
                        list[str.split("-").toTypedArray()[1].toInt() - 1].person_name,
                        list[(str.split("-").toTypedArray()[2].toInt() - 1)].person_name,
                        str.split("-").toTypedArray()[0].toLong()
                    )
                )
            }
            val adapter = TransactionsListAdapter(transactions)
            binding.listTransactions.adapter = adapter
        }
    }

    private fun drawLine(parentLayout: LinearLayout) {
        val line = View(context)
        line.layoutParams =
            LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1)
        line.setBackgroundColor(Color.BLUE)
        parentLayout.addView(line)
    }

    private fun createLinearLayout(): LinearLayout {
        val linear = LinearLayout(context)
        linear.orientation = LinearLayout.HORIZONTAL
        linear.layoutParams =
            LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )

        return linear
    }

    private fun createTextView(
        text: String,
        weight: Float,
        gravity: Int
    ): TextView {
        val txt = TextView(context)
        txt.text = text
        txt.textSize = 16f
        txt.setPadding(20, 5, 20, 5)
        txt.layoutParams = LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, weight)
        txt.gravity = gravity
        when (text) {
            getString(R.string.Deptor) -> {
                txt.setTextColor(Color.RED)
            }
            getString(R.string.Creditor) -> {
                txt.setTextColor(Color.rgb(0, 160, 10))
            }
            else -> {
                txt.setTextColor(Color.BLACK)
            }
        }
        return txt
    }


    private fun getMin(arr: IntArray): Int {
        var minInd = 0
        for (i in 1 until list.size) if (arr[i] < arr[minInd]) minInd = i
        return minInd
    }

    private fun getMax(arr: IntArray): Int {
        var maxInd = 0
        for (i in 1 until list.size) if (arr[i] > arr[maxInd]) maxInd = i
        return maxInd
    }

    private fun minCashFlowRec(amount: IntArray) {
        val mxCredit = getMax(amount)
        val mxDebit = getMin(amount)
        if (amount[mxCredit] == 0 && amount[mxDebit] == 0) return
        val pay = min(-amount[mxDebit], amount[mxCredit])
        amount[mxCredit] -= pay
        amount[mxDebit] += pay
        finalOutput += pay.toString() + "-" + (mxDebit + 1) + "-" + (mxCredit + 1) + "@"
        minCashFlowRec(amount)
    }

    private fun minCashFlow(graph: Array<IntArray>) {
        val amount = IntArray(list.size)
        for (p in amount.indices) for (i in amount.indices) amount[p] += graph[i][p] - graph[p][i]
        minCashFlowRec(amount)
    }

    private fun takeScreenshotOfView(view: View): Bitmap {
        val bitmap =
            Bitmap.createBitmap(view.measuredWidth, view.measuredHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        val bgDrawable = view.background
        if (bgDrawable != null) {
            bgDrawable.draw(canvas)
        } else {
            canvas.drawColor(Color.WHITE)
        }
        view.draw(canvas)
        return bitmap
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_settings -> {
                showShareIntent()
                return true
            }
        }
        return false
    }


    private fun showShareIntent() {
        val intent = Intent(Intent.ACTION_SEND).setType("image/*")

        val bitmap = takeScreenshotOfView(binding.scrollView)

        val bytes = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = MediaStore.Images.Media.insertImage(
            requireContext().contentResolver,
            bitmap,
            acc.account_name + " Summary",
            "Summary of Expenses for Account " + acc.account_name
        )
        val uri = Uri.parse(path)
        intent.putExtra(Intent.EXTRA_STREAM, uri)
        startActivity(intent)
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

}