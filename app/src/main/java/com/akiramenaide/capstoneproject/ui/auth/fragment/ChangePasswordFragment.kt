package com.akiramenaide.capstoneproject.ui.auth.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.akiramenaide.capstoneproject.databinding.FragmentChangePasswordBinding
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException

class ChangePasswordFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var chanePasswordBinding: FragmentChangePasswordBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        chanePasswordBinding = FragmentChangePasswordBinding.inflate(inflater, container, false)
        return chanePasswordBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = FirebaseAuth.getInstance()
        val userData = auth.currentUser
        chanePasswordBinding.apply {
            layoutPassword.visibility = View.VISIBLE
            layoutNewPassword.visibility = View.GONE
            btnAuth.setOnClickListener {
                val passUser = etPassword.text.toString().trim()
                if (passUser.isEmpty()) {
                    etPassword.error = "Password Must be filled"
                    etPassword.requestFocus()
                    return@setOnClickListener
                }
                userData?.let {
                    val userCredential = EmailAuthProvider.getCredential(it.email!!, passUser)
                    it.reauthenticate(userCredential).addOnCompleteListener { cred->
                        when {
                            cred.isSuccessful -> {
                                layoutPassword.visibility = View.GONE
                                layoutNewPassword.visibility = View.VISIBLE
                            }
                            cred.exception is FirebaseAuthInvalidCredentialsException -> {
                                etPassword.error = "Wrong Password!"
                                etPassword.requestFocus()
                            }
                            else -> {
                                Toast.makeText(activity, "${cred.exception?.message}", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        }
                    }
                }
                btnUpdate.setOnClickListener {
                    val newPassword = etNewPassword.text.toString().trim()
                    val newPasswordConfirm = etNewPasswordConfirm.text.toString().trim()

                    if (newPassword.isEmpty() || newPassword.length < 6) {
                        etNewPassword.error = "Password must more than 6 character"
                        etNewPassword.requestFocus()
                        return@setOnClickListener
                    }
                    if (newPassword != newPasswordConfirm) {
                        etNewPasswordConfirm.error = "Password not same"
                        etNewPasswordConfirm.requestFocus()
                        return@setOnClickListener
                    }

                    userData.let {
                        userData?.updatePassword(newPassword)?.addOnCompleteListener {
                            if (it.isSuccessful) {
                                val actionPasswordChange =
                                    ChangePasswordFragmentDirections.actionPasswordChanged()
                                Navigation.findNavController(view).navigate(actionPasswordChange)
                                Toast.makeText(activity, "Password Updated!", Toast.LENGTH_SHORT)
                                    .show()
                            } else {
                                Toast.makeText(
                                    activity,
                                    "${it.exception?.message}",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }

                }
            }
        }

    }
}