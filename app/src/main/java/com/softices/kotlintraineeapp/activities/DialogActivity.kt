package com.softices.kotlintraineeapp.activities

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.softices.kotlintraineeapp.R
import kotlinx.android.synthetic.main.activity_dialog.*

class DialogActivity : AppCompatActivity() {

    /**
     * set view
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dialog)
        init()
    }

    /***
     * initialize activity.
     */
    private fun init() {
        supportActionBar!!.setHomeButtonEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        btn_simple_alert.setOnClickListener {
            showSimpleAlertDialog()
        }
        btn_alert_2.setOnClickListener {
            showAlertDialogWithButton()
        }
        btn_alert_3.setOnClickListener {
            showCustomDialog()
        }
    }

    /***
     * show dialog with custom views.
     */
    private fun showCustomDialog() {
        val factory = LayoutInflater.from(this)
        val dialogView = factory.inflate(R.layout.layout_cutom_dialog, null)
        val myDialog = android.support.v7.app.AlertDialog.Builder(this).create()
        myDialog.setCancelable(false)

        val edtFeedback = dialogView.findViewById<EditText>(R.id.edt_feedback)
        val btnSubmit = dialogView.findViewById<Button>(R.id.btn_submit)
        val btnCancel = dialogView.findViewById<Button>(R.id.btn_cancel)

        btnSubmit.setOnClickListener {
            edtFeedback.setText("")
            Toast.makeText(this, "Submit successfully!", Toast.LENGTH_SHORT).show()
            myDialog.cancel()
        }
        btnCancel.setOnClickListener { myDialog.cancel() }
        myDialog.setView(dialogView)
        myDialog.show()
    }

    /***
     * show dialog with custom views.
     */
    private fun showSimpleAlertDialog() {
        AlertDialog.Builder(this)
                .setTitle("Simple Dialog")
                .setMessage("Only information read")
                .setCancelable(true).show()
    }

    private fun showAlertDialogWithButton() {
        AlertDialog.Builder(this)
                .setTitle("Softices")
                .setMessage("You like softices.?")
                .setCancelable(false)
                .setPositiveButton("yes",
                        DialogInterface.OnClickListener { dialog, which ->
                            Toast.makeText(this, "yes", Toast.LENGTH_SHORT).show()
                            dialog.dismiss()
                        })
                .setNegativeButton("no", DialogInterface.OnClickListener { dialogInterface, i ->
                    Toast.makeText(this, "no", Toast.LENGTH_SHORT).show()
                    dialogInterface.dismiss()
                }).show()
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
