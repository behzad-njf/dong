package ir.mrhib.dong.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class Communicator {
    class Communicator : ViewModel() {

        val account = MutableLiveData<Any>()

        fun setAccountCommunicator(acc: String) {
            account.value = acc
        }
    }
}