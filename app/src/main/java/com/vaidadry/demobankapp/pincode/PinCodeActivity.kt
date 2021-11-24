package com.vaidadry.demobankapp.pincode

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.hanks.passcodeview.PasscodeView.PasscodeViewListener
import com.hanks.passcodeview.PasscodeView.PasscodeViewType.*
import com.vaidadry.demobankapp.databinding.ActivityPinCodeBinding
import com.vaidadry.demobankapp.transactions.TransactionsActivity
import com.vaidadry.demobankapp.R
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class PinCodeActivity : AppCompatActivity() {

    @Inject lateinit var sharedPreferences: SharedPreferences

    private lateinit var binding: ActivityPinCodeBinding

    companion object {
        private const val PIN_CODE = "pin_code"
        private var currentPin: String = ""
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.AppTheme)
        binding = ActivityPinCodeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        currentPin = sharedPreferences.getString(PIN_CODE, "") ?: ""
        initPinView(currentPin.isNotEmpty())
    }

    private fun initPinView(passcodeIsSet: Boolean) {
        with(binding.passcodeView) {
            passcodeLength = 4
            if (!passcodeIsSet) {
                passcodeType = TYPE_SET_PASSCODE
                firstInputTip = getString(R.string.pin_code_create_label)
            } else {
                passcodeType = TYPE_CHECK_PASSCODE
                firstInputTip = getString(R.string.pin_code_enter_label)
                localPasscode = currentPin
            }
            listener = object : PasscodeViewListener {
                override fun onFail() {
                    Snackbar.make(
                        binding.root,
                        getString(R.string.please_enter_passcode),
                        Snackbar.LENGTH_SHORT
                    ).show()
                }

                override fun onSuccess(number: String) {
                    if (!passcodeIsSet) {
                        currentPin = number
                        sharedPreferences.edit().putString(PIN_CODE, number).apply()
                    }
                    val intent = Intent(context, TransactionsActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        }
    }
}