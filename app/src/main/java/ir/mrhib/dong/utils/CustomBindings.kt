package ir.mrhib.dong.view.adapter

import android.widget.Spinner
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingListener
import androidx.recyclerview.widget.RecyclerView
import ir.mrhib.dong.R
import ir.mrhib.dong.model.Person


@BindingAdapter(value = ["setAdapter"])
fun RecyclerView.bindRecyclerViewAdapter(adapter: RecyclerView.Adapter<*>) {
    this.run {
        this.setHasFixedSize(false)
        this.adapter = adapter
    }
}
/*

@BindingAdapter(
    value = ["persons", "selectedPerson", "selectedPersonAttrChanged"],
    requireAll = false
)
fun setUsers(
    spinner: Spinner,
    users: List<Person>?,
    selectedUser: Person?,
    listener: InverseBindingListener
) {
    if (users == null) return
    spinner.adapter = NameAdapter(spinner.context, R.layout.simple_spinner_dropdown_item, users)
    setCurrentSelection(spinner, selectedUser)
    setSpinnerListener(spinner, listener)
}*/
