package com.akiramenaide.capstoneproject.ui.profile

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.akiramenaide.capstoneproject.R
import com.akiramenaide.capstoneproject.databinding.FragmentProfileBinding
import com.akiramenaide.capstoneproject.ui.auth.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream

@Suppress("DEPRECATION")
class ProfileFragment : Fragment() {
    private lateinit var profileBinding: FragmentProfileBinding
    private lateinit var imageUri: Uri
    private lateinit var auth: FirebaseAuth
    private var myBitmap: Bitmap? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        profileBinding = FragmentProfileBinding.inflate(inflater, container, false)
        return profileBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = FirebaseAuth.getInstance()
        val userData = auth.currentUser

        profileBinding.apply {
            if (userData != null) {
                userData.reload()
                if (userData.photoUrl != null) {
                    Picasso.get().load(userData.photoUrl).into(ivProfile)
                } else {
                    Picasso.get().load("https://picsum.photos/200/300").into(ivProfile)
                }
                etName.setText(userData.displayName)
                etEmail.setText(userData.email)

                if (userData.isEmailVerified) {
                    icVerified.visibility = View.VISIBLE
                    icUnverified.visibility = View.GONE
                } else {
                    icUnverified.visibility = View.VISIBLE
                    icVerified.visibility = View.GONE
                }
            }
            ivProfile.setOnClickListener {
                getImage()
            }
            btnUnregis.setOnClickListener {
                val builder = AlertDialog.Builder(it.context)
                with(builder)
                {
                    setTitle("Delete Your Account")
                    setMessage("Are you sure?")
                    setPositiveButton("OK") { _: DialogInterface, _: Int ->
                        userData?.delete()?.addOnCompleteListener { del ->
                            if (del.isSuccessful) {
                                Toast.makeText(activity, "Unregistered", Toast.LENGTH_SHORT).show()
                                auth.signOut()
                                Intent(activity, LoginActivity::class.java).also { destroy ->
                                    destroy.flags =
                                        Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                    startActivity(destroy)
                                }
                            } else {
                                Toast.makeText(
                                    activity,
                                    "${del.exception?.message}",
                                    Toast.LENGTH_SHORT
                                )
                                    .show()
                            }
                        }
                    }
                    setNegativeButton(android.R.string.no) { _: DialogInterface, _: Int -> return@setNegativeButton }
                    show()
                }
            }
            btnUpdate.setOnClickListener {
                val image = when {
                    //kondisi upload foto baru
                    ::imageUri.isInitialized -> imageUri
                    //kondisi jika kita tdk upload foto = default foto
                    userData?.photoUrl == null -> Uri.parse("https://picsum.photos/id/316/200")
                    //kondidi jika sudah ada foto sebelumnya
                    else -> userData.photoUrl
                }
                val builder = AlertDialog.Builder(it.context)
                val name = etName.text.toString().trim()
                if (name.isEmpty()) {
                    etName.error = getString(R.string.required_name)
                    etName.requestFocus()
                    return@setOnClickListener
                }
                with(builder)
                {
                    setTitle("Update Your Account")
                    setMessage("Are you sure?")
                    setPositiveButton("OK") { _: DialogInterface, _: Int ->
                        UserProfileChangeRequest.Builder().setDisplayName(name)
                            .setPhotoUri(image).build().also { changeReq ->
                                userData?.updateProfile(changeReq)
                                    ?.addOnCompleteListener { update ->
                                        if (update.isSuccessful) {
                                            Toast.makeText(
                                                activity,
                                                "Profile Updated",
                                                Toast.LENGTH_SHORT
                                            )
                                                .show()
                                        } else {
                                            Toast.makeText(
                                                activity,
                                                update.exception?.message,
                                                Toast.LENGTH_SHORT
                                            )
                                                .show()
                                        }
                                    }
                            }
                    }
                    setNegativeButton(android.R.string.no) { _: DialogInterface, _: Int -> return@setNegativeButton }
                    show()
                }
            }
            icVerified.setOnClickListener {
                Toast.makeText(activity, "Email is Verified!", Toast.LENGTH_SHORT).show()
            }
            icUnverified.setOnClickListener {
                CoroutineScope(Dispatchers.Main).launch {
                    when (auth.currentUser) {
                        auth.currentUser?.reload() -> {
                            Toast.makeText(activity, "Checking your email..", Toast.LENGTH_LONG)
                                .show()
                            RELOAD = 1
                        }
                    }
                }
                if (RELOAD == 1) {
                    profileBinding.apply {
                        icVerified.visibility = View.VISIBLE
                        icUnverified.visibility = View.GONE
                    }
                } else {
                    if (STATUS == 0) {
                        userData?.sendEmailVerification()?.addOnCompleteListener {
                            if (it.isSuccessful) {
                                Toast.makeText(
                                    activity,
                                    "Email verification has been sent",
                                    Toast.LENGTH_SHORT
                                )
                                    .show()
                                STATUS = 1
                            } else {
                                Toast.makeText(activity, it.exception?.message, Toast.LENGTH_SHORT)
                                    .show()
                            }
                        }
                    } else {
                        Toast.makeText(
                            activity,
                            "Check your email verification!",
                            Toast.LENGTH_SHORT
                        ).show()
                        Navigation.findNavController(view).navigate(R.id.navigation_profile)
                    }
                }
            }
            tvChangePassword.setOnClickListener {
                val actionChangePassword = ProfileFragmentDirections.actionChangePassword()
                Navigation.findNavController(it).navigate(actionChangePassword)
            }
            etEmail.setOnClickListener {
                val actionUpdateEmail = ProfileFragmentDirections.actionUpdateEmail()
                Navigation.findNavController(it).navigate(actionUpdateEmail)
            }
        }
    }

    private fun getImage() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_PICK_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IMAGE_PICK_CODE && resultCode == Activity.RESULT_OK) {
            val img = data?.data
            myBitmap = MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, img)
            uploadImage(myBitmap as Bitmap)
        }
    }

    private fun uploadImage(imgBitmap: Bitmap) {
        val baos = ByteArrayOutputStream()
        val ref = FirebaseStorage.getInstance().reference.child("img/${FirebaseAuth.getInstance().currentUser?.uid}")
        imgBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val image = baos.toByteArray()
        ref.putBytes(image).addOnCompleteListener {
            if (it.isSuccessful) {
                ref.downloadUrl.addOnCompleteListener { task ->
                    task.result?.let { uri ->
                        imageUri = uri
                        profileBinding.ivProfile.setImageBitmap(imgBitmap)
                    }
                }
            }
        }

    }

    companion object {
        var RELOAD = 0
        var STATUS = 0
        const val IMAGE_PICK_CODE = 1000
    }
}