package com.akiramenaide.capstoneproject.ui.auth.fragment

import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.akiramenaide.capstoneproject.databinding.FragmentUpdateEmailBinding
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException

class UpdateEmailFragment : Fragment() {
    private lateinit var auth: FirebaseAuth
    private lateinit var updateEmailBinding: FragmentUpdateEmailBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        updateEmailBinding = FragmentUpdateEmailBinding.inflate(inflater, container, false)
        return updateEmailBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = FirebaseAuth.getInstance()
        val userDara = auth.currentUser
        updateEmailBinding.apply {
            layoutPassword.visibility = View.VISIBLE
            layoutEmail.visibility = View.GONE

            btnAuth.setOnClickListener {
                val passUser = etPassword.text.toString().trim()

                if (passUser.isEmpty()) {
                    etPassword.error = "Password must be filled"
                    etPassword.requestFocus()
                    return@setOnClickListener
                }
                userDara.let { firebaseUsr ->
                    val userCredential =
                        EmailAuthProvider.getCredential(firebaseUsr?.email!!, passUser)
                    firebaseUsr.reauthenticate(userCredential).addOnCompleteListener {
                        when {
                            it.isSuccessful -> {
                                layoutPassword.visibility = View.GONE
                                layoutEmail.visibility = View.VISIBLE
                            }
                            it.exception is FirebaseAuthInvalidCredentialsException -> {
                                etPassword.error = "Wrong Password!"
                                etPassword.requestFocus()
                            }
                            else -> {
                                Toast.makeText(
                                    activity,
                                    "${it.exception?.message}",
                                    Toast.LENGTH_SHORT
                                )
                                    .show()
                            }
                        }
                    }
                }
                btnUpdate.setOnClickListener { view ->
                    val emailUser = etEmail.text.toString().trim()

                    if (emailUser.isEmpty()) {
                        etEmail.error = "Please fill the email!"
                        etEmail.requestFocus()
                        return@setOnClickListener
                    }
                    if (!Patterns.EMAIL_ADDRESS.matcher(emailUser).matches()) {
                        etEmail.error = "Email is not valid!"
                        etEmail.requestFocus()
                        return@setOnClickListener
                    }
                    userDara.let {
                        userDara?.updateEmail(emailUser)?.addOnCompleteListener {
                            if (it.isSuccessful) {
                                val actionEmailUpdated =
                                    UpdateEmailFragmentDirections.actionEmailUpdated()
                                Navigation.findNavController(view).navigate(actionEmailUpdated)
                            }else{
                                Toast.makeText(
                                    activity,
                                    "${it.exception?.message}",
                                    Toast.LENGTH_SHORT
                                )
                                    .show()
                            }
                        }
                    }
                }
            }
        }
    }

}