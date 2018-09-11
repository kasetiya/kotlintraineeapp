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
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import com.softices.kotlintraineeapp.R
import com.softices.kotlintraineeapp.database.DatabaseManager
import com.softices.kotlintraineeapp.extra.L
import com.softices.kotlintraineeapp.models.UserModel
import com.softices.kotlintraineeapp.sharedPreference.PreferenceHelper
import java.io.File
import java.io.IOException

class ProfileActivity : AppCompatActivity(), View.OnClickListener {

    private val PICK_FROM_CAMERA = 1
    private val PICK_FROM_FILE = 3
    private lateinit var edtFirstName: EditText
    private lateinit var edtLastName: EditText
    private lateinit var edtEmail: EditText
    private lateinit var edtPhone: EditText
    private lateinit var ivPhoto: ImageView
    private lateinit var btnUpdate: Button
    private var uri: Uri? = null
    private var isFromCamera = false
    private var imageFilePath: String = ""
    private val TAG = "ProfileActivity"
    private lateinit var userModel: UserModel
    /**
     * set view
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        init()
    }

    /***
     * initialize activity.
     */
    private fun init() {
        supportActionBar!!.setHomeButtonEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        edtFirstName = findViewById(R.id.edt_first_name)
        edtLastName = findViewById(R.id.edt_last_name)
        edtEmail = findViewById(R.id.edt_email)
        edtEmail.setOnKeyListener(null)
        edtPhone = findViewById(R.id.edt_mobile)
        ivPhoto = findViewById(R.id.iv_photo)
        btnUpdate = findViewById(R.id.btn_update)

        btnUpdate.setOnClickListener(this)
        ivPhoto.setOnClickListener(this)

        setDefaultData()
    }

    /***
     * fill up data of user which is save on signup.
     */
    private fun setDefaultData() {
        userModel = DatabaseManager(applicationContext).getUserDataByEmail(PreferenceHelper.getUserEmail(this))
        edtFirstName.setText(userModel.firstName)
        edtLastName.setText(userModel.lastName)
        edtEmail.setText(userModel.email)
        edtPhone.setText(userModel.mobile)
        ivPhoto.setImageBitmap(userModel.photo)
    }

    /**
     * view click event
     *  @param  p0
     */
    override fun onClick(p0: View?) {
        when (p0!!.id) {
            R.id.iv_photo -> {
                checkPermission()
            }
            R.id.btn_update -> {
                updateProfile()
            }
        }
    }

    /**
     * check validations and update new proile data to database
     */
    private fun updateProfile() {
        if (!L.isNotNull(edtFirstName.text.toString())) {
            L.t(this, "enter first name")
        } else if (!L.isNotNull(edtLastName.text.toString())) {
            L.t(this, "enter last name")
        } else if (!L.isValidEmail(edtEmail.text.toString())) {
            L.t(this, "enter email")
        } else if (!L.isValidMobile(edtPhone.text.toString())) {
            L.t(this, "enter mobile")
        } else {
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
            Log.e("mye", "bitmap " + bitmap);
            //database entry
            L.showProgressDialog(this)
            val i = DatabaseManager(applicationContext).updateIntoTableUser(userModel.email!!,
                    edtFirstName.text.toString(), edtLastName.text.toString(),
                    edtPhone.text.toString(), bitmap)
            if (i > 0) {
                L.hideProgressDialog()
                L.t(this, "Profile update successfully.")
            } else {
                L.t(this, "Something went wrong")
                L.hideProgressDialog()
            }
        }
    }

    /**
     * check pemission given for read write external storage
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

    /**
     * @param permissions
     * @param requestCode
     * @param grantResults
     *
     * when runs when permission dialog close
     */
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
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != RESULT_OK) return
        when (requestCode) {
            PICK_FROM_CAMERA -> try {
                isFromCamera = true
                uri = Uri.parse(imageFilePath)
                ivPhoto.setImageURI(uri);
            } catch (e: IOException) {
                e.printStackTrace()
                Log.e(TAG, "PICK_FROM_CAMERA" + e)
            }

            PICK_FROM_FILE -> try {
                isFromCamera = false;
                uri = data!!.data
                if (uri != null) {
                    var bitmap: Bitmap? = null
                    bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, uri)
                    ivPhoto.setImageBitmap(bitmap)
                } else {
                    L.t(this, "unable to select image")
                }
            } catch (e: IOException) {
                e.printStackTrace()
                Log.e(TAG, "PICK_FROM_FILE $e")
            }

        }
    }

    private fun createImageFile(): File {
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val image = File.createTempFile("img_1", ".jpg", storageDir)
        imageFilePath = image.absolutePath;
        return image
    }

    /**
     * back arrow pressed
     */
    override fun onOptionsItemSelected(menuItem: MenuItem): Boolean {
        when (menuItem.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(menuItem)
    }
}