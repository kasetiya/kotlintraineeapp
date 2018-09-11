package com.softices.kotlintraineeapp.activities

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v4.content.FileProvider
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.softices.kotlintraineeapp.R
import com.softices.kotlintraineeapp.database.DatabaseManager
import com.softices.kotlintraineeapp.extra.L
import com.softices.kotlintraineeapp.models.UserModel
import com.softices.kotlintraineeapp.sharedPreference.PreferenceHelper
import kotlinx.android.synthetic.main.activity_signup.*
import java.io.File
import java.io.IOException


class SignupActivity : AppCompatActivity() {
    private val PICK_FROM_CAMERA = 1
    private val PICK_FROM_FILE = 3
    private lateinit var edtFirstName: EditText
    private lateinit var edtLastName: EditText
    private lateinit var edtEmail: EditText
    private lateinit var edtPhone: EditText
    private lateinit var edtPassword: EditText
    private lateinit var edtConPassword: EditText
    private lateinit var ivPhoto: ImageView
    private var imageFilePath: String = ""
    private var uri: Uri? = null
    private var isFromCamera = false
    private val TAG = "SignupActivity"
    /**
     * set view
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        init()
    }

    /***
     * initialize activity.
     */
    private fun init() {
        edtFirstName = findViewById(R.id.edt_first_name)
        edtLastName = findViewById(R.id.edt_last_name)
        edtEmail = findViewById(R.id.edt_email)
        edtPhone = findViewById(R.id.edt_mobile)
        edtPassword = findViewById(R.id.edt_password)
        edtConPassword = findViewById(R.id.edt_conf_password)
        ivPhoto = findViewById(R.id.iv_photo)

        /**
         * Sign up button click event and
         * check validations and store data into database
         */
        btn_signup.setOnClickListener {
            if (!L.isNotNull(edtFirstName.text.toString())) {
                L.t(this, "enter first name")
            } else if (!L.isNotNull(edtLastName.text.toString())) {
                L.t(this, "enter last name")
            } else if (!L.isValidEmail(edtEmail.text.toString())) {
                L.t(this, "enter email")
            } else if (!L.isValidMobile(edtPhone.text.toString())) {
                L.t(this, "enter mobile")
            } else if (!L.isValidPassword(edtPassword.text.toString())) {
                L.t(this, "enter password min 5 character")
            } else if (!L.isValidPassword(edtConPassword.text.toString())) {
                L.t(this, "enter confirm password min 5 character")
            } else if (!L.isPasswordMatch(edtPassword.text.toString(), edtConPassword.text.toString())) {
                L.t(this, "password not match")
            } else {
                L.showProgressDialog(this)
                val bitmap: Bitmap
                if (uri != null) {
                    if (isFromCamera) {
                        val imgFile = File(imageFilePath)
                        if (imgFile.exists()) {
                            bitmap = BitmapFactory.decodeFile(imgFile.absolutePath)
                        } else
                            bitmap = BitmapFactory.decodeResource(resources,
                                    R.drawable.ic_user)
                    } else {
                        bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, uri)
                    }
                } else {
                    bitmap = BitmapFactory.decodeResource(resources,
                            R.drawable.ic_user)
                }
                //database entry
                if (bitmap != null) {
                    val isTrue = DatabaseManager(applicationContext).insertIntoTableUser(
                            UserModel(edtFirstName.text.toString()
                                    , edtLastName.text.toString()
                                    , edtEmail.text.toString()
                                    , edtPhone.text.toString()
                                    , edtPassword.text.toString()
                                    , bitmap))
                    if (isTrue) {
                        L.hideProgressDialog()
                        PreferenceHelper.setIsLogin(this, true)
                        PreferenceHelper.setUserEmail(this, edtEmail.text.toString())
                        startActivity(Intent(this, DashboardActivity::class.java))
                    } else {
                        L.t(this, "Please try again.")
                        L.hideProgressDialog()
                    }
                } else {
                    L.hideProgressDialog()
                    Toast.makeText(this, "image null", Toast.LENGTH_SHORT).show()
                }
            }
        }

        ivPhoto.setOnClickListener {
            checkPermission()
        }
    }

    /**
     * check pemission given for read write external storage.
     * and version above 23.
     */
    private fun checkPermission() {
        if (Build.VERSION.SDK_INT < 23) {
            imageSelectDialog()
        } else {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                imageSelectDialog()
            } else
                ActivityCompat.requestPermissions(this, arrayOf(Manifest
                        .permission.WRITE_EXTERNAL_STORAGE, Manifest
                        .permission.CAMERA), 152)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults.size > 0) {
            when (requestCode) {
                152 -> goWithCameraPermission(grantResults)
            }
        }
    }

    private fun goWithCameraPermission(grantResults: IntArray) {
        if (grantResults.get(0) == PackageManager.PERMISSION_GRANTED &&
                grantResults.get(1) == PackageManager.PERMISSION_GRANTED) {
            imageSelectDialog()
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest
                    .permission.WRITE_EXTERNAL_STORAGE, Manifest
                    .permission.CAMERA), 152)
        }
    }

    /**
     * Open dialog with options for camera and gallery
     */
    private fun imageSelectDialog() {
        val items = arrayOf(getString(R.string.txt_take_from_camera)
                , getString(R.string.txt_select_image_gallery))
        val adapter = ArrayAdapter<String>(this, android.R.layout.select_dialog_item, items)
        val builder = android.app.AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.txt_select_image))
        builder.setAdapter(adapter) { dialog, item ->
            //pick from camera
            if (item == 0) {
                openCameraIntent()
            } else { //pick from file
                val intent = Intent(Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                intent.type = "image/*"
                intent.action = Intent.ACTION_GET_CONTENT
                startActivityForResult(Intent.createChooser(intent,
                        getString(R.string.txt_complete_action_using)),
                        PICK_FROM_FILE)
            }
        }
        val dialog = builder.create()
        dialog.show()
    }

    //open camera for click picture.
    private fun openCameraIntent() {
        val pictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (pictureIntent.resolveActivity(packageManager) != null) {

            var photoFile: File? = null
            try {
                photoFile = createImageFile()
            } catch (e: IOException) {
                e.printStackTrace()
                return
            }

            val photoUri = FileProvider.getUriForFile(this, applicationContext.packageName +
                    ".provider", photoFile)
            if (photoUri != null) {
                pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
                startActivityForResult(pictureIntent, PICK_FROM_CAMERA)
            }
        }
    }

    /** @param requestCode
     *  @param resultCode
     *  @param data
     *  result of either pick prom camera or gallery
     */
    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode != RESULT_OK) return
        when (requestCode) {
            PICK_FROM_CAMERA -> try {
                isFromCamera = true
                uri = Uri.parse(imageFilePath)
                Log.e("mye", "camera uri " + uri)
                ivPhoto.setImageURI(uri);
            } catch (e: IOException) {
                e.printStackTrace()
                Log.e(TAG, "PICK_FROM_CAMERA" + e)
            }

            PICK_FROM_FILE -> try {
                isFromCamera = false
                uri = data!!.data
                Log.e("mye", "image uri " + uri)
                if (uri != null) {
                    var bitmap: Bitmap? = null
                    bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, uri)
                    ivPhoto.setImageBitmap(bitmap)
                } else {
                    L.t(this, "unable to select image")
                }
            } catch (e: IOException) {
                e.printStackTrace()
                Log.e(TAG, "PICK_FROM_FILE" + e)
            }

        }
    }

    private fun createImageFile(): File {
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val image = File.createTempFile("img_1", ".jpg", storageDir)
        imageFilePath = image.absolutePath;
        return image
    }
}