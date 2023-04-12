package com.example.midmorningsqlite

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.icu.text.CaseMap.Title
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Message
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog

class MainActivity : AppCompatActivity() {
    lateinit var edtName: EditText
    lateinit var edtEmail:EditText
    lateinit var edtIdNumber: EditText
    lateinit var btnsave:Button
    lateinit var btnview:Button
    lateinit var btnDelete:Button
    lateinit var db:SQLiteDatabase
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        edtName =findViewById(R.id.mEdtName)
        edtEmail =findViewById(R.id.mEditEmail)
        edtIdNumber = findViewById(R.id.mEditNumber)
        btnsave = findViewById(R.id.mBtnSave)
        btnview = findViewById(R.id.mBtnview)
        btnDelete = findViewById(R.id.mBtnDelete)
        // Create a database called eMobillisDB
        db = openOrCreateDatabase("eMobilisDB", Context.MODE_PRIVATE, null)
        //Create a table called users in the database
        db.execSQL("CREATE TABLE IF NOT EXISTS users (jina VARCHAR, arafa VARCHAR, kitambulisho VARCHAR)")
        //set onclicklisteners to buttons
        btnsave.setOnClickListener {
            //Recieve data from the user
            var name = edtName.text.toString().trim()
            var email = edtEmail.text.toString().trim()
            var idNumber =edtIdNumber.text.toString().trim()
            //Check if the user is submitting empty fields
            if (name.isEmpty()|| email.isEmpty() || idNumber.isEmpty()){
                //Display an error message using the defined message funtions
                message("EMPTY FIELDS!!!","please fill all inputs")
            }else{
                //PROCEED TO SAVE
                db.execSQL("INSERT INTO users VALUES('"+name+"','"+email+"','"+idNumber+"')")
                clear()
                message("SUCCESS!!!","User saved succesfully")
            }
        }
        btnview.setOnClickListener {
            //use cursor to select all  the records
            var cursor = db.rawQuery("SELECT* FROM users",null)
            //check if there is any record found
            if (cursor.count == 0){
                message("No RECORDS!!!","sorry, no users were found!!!")
            }else{
                //use string buffer to append all records to be displayed using a loop
                var buffer = StringBuffer()
                while (cursor.moveToNext()){
                    var retrievedName = cursor.getString(0)
                    var retrievedEmail = cursor.getString(1)
                    var retrievedIdNumber= cursor.getString(2)
                    buffer.append(retrievedName+"\n")
                    buffer.append(retrievedEmail+"\n")
                    buffer.append(retrievedIdNumber+"\n\n")

                }
                message("USERS",buffer.toString())
            }

        }
        btnDelete.setOnClickListener {
            val idNumber = edtIdNumber.text.toString().trim()
            if (idNumber.isEmpty()){
                message("EMPTY FIELD!!!","please fill in ID field")
            }else{
                var cursor =db.rawQuery("SELECT * FROM users WHERE kitambulisho='"+idNumber+"'",null)
                if (cursor.count== 0){
                    message("NO RECORD FOUND!!!","Sorry, there's no user with provided ID")
                }else{
                    db.execSQL("DELETE FROM users WHERE kitambulisho='"+idNumber+"'")
                    clear()
                    message("SUCCESS!!!","user deleted successfully")
                }
            }

        }
    } 
    fun message(title:String, message:String){
        var alertDialog =AlertDialog.Builder(this)
        alertDialog.setTitle(title)
        alertDialog.setMessage(message)
        alertDialog.setPositiveButton("Cancel",null)
        alertDialog.create().show()
    }
    fun clear(){
        edtName.setText("")
        edtEmail.setText("")
        edtIdNumber.setText("")
    }
}