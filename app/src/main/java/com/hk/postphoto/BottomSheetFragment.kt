package com.hk.postphoto

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomSheetFragment(
    private val folderList: ArrayList<Folder>,
    private val temp: Char
): BottomSheetDialogFragment(){
    private lateinit var bottomSheetAdapter: FolderAdapter
    private lateinit var folders: ArrayList<Folder>
    private lateinit var recyclerView: RecyclerView
    private  var adapter: Adapter? = null

//   private lateinit var listOfImagesOfFolder: ArrayList<String>
//   private lateinit var listOfVideosOfFolder: ArrayList<String>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.bottomsheet_fragment,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        folders = folderList
        recyclerView = view.findViewById(R.id.bottomSheetRv)
//        if(temp == 'a'){                                                                //start
//            bottomSheetAdapter = FolderAdapter(requireContext(),folders){
//                listOfImagesOfFolder = ImagesGallery.listOfImages(requireContext(),it)
//                adapter = Adapter(requireContext(),listOfImagesOfFolder){ it->
//                    Glide.with(requireContext()).load(it).into()
//                }
//            }
//        }                                                                                 //end
        bottomSheetAdapter = FolderAdapter(requireContext(),folders)
        recyclerView.adapter = bottomSheetAdapter
    }

//    pass an intent to main activity along with folder id(get it as lambda function) and call the same function to get the images and set
//    it to adapter there similarly with the video part

//    @SuppressLint("Range")
//    private fun getAllVideoOfFolder(folderId: String){
//        val tempList = ArrayList<String>()
//        val selection = MediaStore.Video.Media.BUCKET_ID + " like? "
//        val projection = arrayOf(MediaStore.Video.Media.BUCKET_DISPLAY_NAME,MediaStore.Video.Media.BUCKET_ID)
//        val cursor = requireContext().contentResolver.query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,projection,selection,
//            arrayOf(folderId),
//            MediaStore.Video.Media.DATE_ADDED + " DESC")
//        if(cursor != null)
//            if(cursor.moveToNext())
//                do {
//                    val folderC = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.BUCKET_DISPLAY_NAME))
//                    val folderIdC = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.BUCKET_ID))
//
//                    try {
//                        if(!tempFolderList.contains(folderC)){
//                            tempFolderList.add(folderC)
//                            VideoActivity.folderListVideo.add(Folder(folderIdC,folderC))
//                        }
//                    }catch (e: Exception){}
//                }while (cursor.moveToNext())
//        cursor?.close()
//    }

}