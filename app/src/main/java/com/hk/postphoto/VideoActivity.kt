package com.hk.postphoto

import android.annotation.SuppressLint
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.ImageView
import android.widget.MediaController
import android.widget.TextView
import android.widget.VideoView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hk.postphoto.VideoGallery.listOfVideos
import java.lang.Exception
import java.util.ArrayList

class VideoActivity : AppCompatActivity() {
    var recyclerView: RecyclerView? = null
    private lateinit var mediaController: MediaController
    private lateinit var galleryVideo: VideoView
    private var galleryVideoAdapter: VideoAdapter? = null
    var videos: List<String>? = null
    companion object{
        lateinit var folderListVideo: ArrayList<Folder>
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video)
        folderListVideo = arrayListOf()
        getAllVideoFolders()

        val button: ImageView = findViewById(R.id.bottomBtn)
        val bottomSheetFragment = BottomSheetFragment(folderListVideo,'b')
        button.setOnClickListener{
            bottomSheetFragment.show(supportFragmentManager,"BottomSheetDialog")
        }
        mediaController = MediaController(this)
        recyclerView = findViewById(R.id.videoRv)
        galleryVideo = findViewById(R.id.galleryImage)
        val videoTv =  findViewById<TextView>(R.id.videoTv)
        mediaController.setAnchorView(galleryVideo)
        mediaController.setMediaPlayer(galleryVideo)

        videos = listOfVideos(this)
        loadVideos()

        val tempChar2 = intent.getCharExtra("Temp1",'0')
        val folderId2 = intent.getStringExtra("FolderID1")
        val folderName2 = intent.getStringExtra("FolderName1")

       if(intent != null) {
           if (tempChar2 == 'b') {
               videoTv.text = folderName2.toString()
               videos = folderId2?.let { getAllVideoOfFolder(it) }
               loadVideos()
           }
       }else{
           videos = listOfVideos(this)
           loadVideos()
       }


    }

    private fun loadVideos() {
        recyclerView!!.setHasFixedSize(true)
        recyclerView!!.layoutManager = GridLayoutManager(this, 4)
//        videos = listOfVideos(this)
        galleryVideoAdapter = VideoAdapter(this,videos as ArrayList<String>){
            val uri: Uri = Uri.parse(it)
            galleryVideo.setMediaController(mediaController)
            galleryVideo.setVideoURI(uri)
            galleryVideo.start()
        }
        recyclerView!!.adapter =galleryVideoAdapter
    }

    @SuppressLint("Range")
    private fun getAllVideoFolders(){
        val tempFolderList = ArrayList<String>()
        val projection = arrayOf(MediaStore.Video.Media.BUCKET_DISPLAY_NAME,MediaStore.Video.Media.BUCKET_ID)
        val cursor = this.contentResolver.query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,projection,null,null,
        MediaStore.Video.Media.DATE_ADDED + " DESC")
        if(cursor != null)
            if(cursor.moveToNext())
                do {
                    val folderC = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.BUCKET_DISPLAY_NAME))
                    val folderIdC = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.BUCKET_ID))

                    try {
                        if(!tempFolderList.contains(folderC)){
                            tempFolderList.add(folderC)
                            folderListVideo.add(Folder(folderIdC,folderC))
                        }
                    }catch (e: Exception){}
                }while (cursor.moveToNext())
                cursor?.close()
    }

    @SuppressLint("Range")
    private fun getAllVideoOfFolder(folderId: String): ArrayList<String>{
        val tempList = ArrayList<String>()
        val selection = MediaStore.Video.Media.BUCKET_ID + " like? "
        val projection = arrayOf(MediaStore.Video.Media.BUCKET_DISPLAY_NAME,MediaStore.Video.Media.BUCKET_ID,MediaStore.Video.Media.DATA)
        val cursor = this.contentResolver.query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,projection,selection,
            arrayOf(folderId),
            MediaStore.Video.Media.DATE_ADDED + " DESC")
        if(cursor != null)
            if(cursor.moveToNext())
                do {
                    val pathC = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA))
                    try {
                        tempList.add(pathC)
                    }catch (e: Exception){}
                }while (cursor.moveToNext())
        cursor?.close()
        return tempList
    }
}