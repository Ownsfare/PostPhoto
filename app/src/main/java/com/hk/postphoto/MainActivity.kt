package com.hk.postphoto

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import com.hk.postphoto.ImagesGallery.listOfImages
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import android.os.Bundle
import androidx.core.content.ContextCompat
import android.content.pm.PackageManager
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.GridLayoutManager
import android.widget.Toast
import com.bumptech.glide.Glide
import java.util.ArrayList
import android.provider.MediaStore
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.TextView
import java.lang.Exception


class MainActivity() : AppCompatActivity() {
    var recyclerView: RecyclerView? = null
    var galleryAdapter: Adapter? = null
    var images: List<String>? = null
    public lateinit var galleryImage: ImageView
    private lateinit var folderListImage: ArrayList<Folder>
    companion object{
        private const val MY_READ_PERMISSION_CODE = 101
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        folderListImage = arrayListOf()
        getAllImageFolders()

        val button: ImageView = findViewById(R.id.bottomBtn)
        val bottomSheetFragment = BottomSheetFragment(folderListImage,'a')
        button.setOnClickListener{
            bottomSheetFragment.show(supportFragmentManager,"BottomSheetDialog")
        }

        recyclerView = findViewById(R.id.videoRv)
        galleryImage = findViewById(R.id.galleryImage)
        val imageTv =  findViewById<TextView>(R.id.imageTv)
        if (ContextCompat.checkSelfPermission(
                this@MainActivity,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this@MainActivity,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                MY_READ_PERMISSION_CODE
            )
        } else {

            images = listOfImages(this)
            loadImages()

            val tempChar1 = intent.getCharExtra("Temp",'0')
            val folderId1 = intent.getStringExtra("FolderID")
            val folderName1 = intent.getStringExtra("FolderName")

            if(intent != null) {
                if (tempChar1 == 'a') {
                      imageTv.text = folderName1.toString()
                      images = folderId1?.let { getAllImageOfFolder(it) }
                      loadImages()
                }
            }else{
                    images = listOfImages(this)
                     loadImages()
            }
        }
    }



    private fun loadImages() {
        recyclerView!!.setHasFixedSize(true)
        recyclerView!!.layoutManager = GridLayoutManager(this, 4)
//        images = listOfImages(this)
        Glide.with(this).load((images as ArrayList<String>)[0]).into(galleryImage)
        galleryAdapter = Adapter(this, images as ArrayList<String>){
            Glide.with(this).load(it).into(galleryImage)
        }
        recyclerView!!.adapter = galleryAdapter
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu,menu);
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.video ->{
                startActivity(Intent(this,VideoActivity::class.java))
            }
            R.id.post ->{
                Toast.makeText(this,"Post",Toast.LENGTH_SHORT).show()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == MY_READ_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Read external storage permission granted", Toast.LENGTH_SHORT)
                    .show()
                loadImages()
            } else {
                Toast.makeText(this, "Read external storage permission denied", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }


    @SuppressLint("Range")
    private fun getAllImageFolders(){
        val tempFolderList = ArrayList<String>()
        val projection = arrayOf(MediaStore.Images.Media.BUCKET_DISPLAY_NAME,MediaStore.Images.Media.BUCKET_ID)
        val cursor = this.contentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,projection,null,null,
            MediaStore.Images.Media.DATE_ADDED + " DESC")
        if(cursor != null)
            if(cursor.moveToNext())
                do {
                    val folderC = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.BUCKET_DISPLAY_NAME))
                    val folderIdC = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.BUCKET_ID))

                    try {
                        if(!tempFolderList.contains(folderC)){
                            tempFolderList.add(folderC)
                            folderListImage.add(Folder(folderIdC,folderC))
                        }
                    }catch (e: Exception){}
                }while (cursor.moveToNext())
        cursor?.close()
    }

    @SuppressLint("Range")
    private fun getAllImageOfFolder(folderId: String): ArrayList<String>{
        var tempList = ArrayList<String>()
        val selection = MediaStore.Images.Media.BUCKET_ID + " like? "
        val projection = arrayOf(MediaStore.Images.Media.BUCKET_DISPLAY_NAME,MediaStore.Images.Media.BUCKET_ID,MediaStore.Images.Media.DATA)
        val cursor = this.contentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,projection,selection,
            arrayOf(folderId),
            MediaStore.Images.Media.DATE_ADDED + " DESC")
        if(cursor != null)
            if(cursor.moveToNext())
                do {
                    var pathC = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA))
                    try {
                        tempList.add(pathC)
                    }catch (e: Exception){}
                }while (cursor.moveToNext())
        cursor?.close()
        return tempList
    }
}