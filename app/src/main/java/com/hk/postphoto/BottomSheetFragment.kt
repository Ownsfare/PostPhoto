package com.hk.postphoto

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
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
        if(temp == 'a'){
            bottomSheetAdapter = FolderAdapter(requireContext(),folders) { a , b ->
                val intent = Intent(requireContext(), MainActivity::class.java)
                intent.putExtra("FolderID",a)
                intent.putExtra("Temp",temp)
                intent.putExtra("FolderName",b)
                startActivity(intent)
            }
        }else if(temp == 'b'){
            bottomSheetAdapter = FolderAdapter(requireContext(),folders) { a , b->
                val intent = Intent(requireContext(), VideoActivity::class.java)
                intent.putExtra("FolderID1",a)
                intent.putExtra("Temp1",temp)
                intent.putExtra("FolderName1",b)
                startActivity(intent)
            }
        }

//        bottomSheetAdapter = FolderAdapter(requireContext(),folders)
        recyclerView.adapter = bottomSheetAdapter
    }

//    pass an intent to main activity along with folder id(get it as lambda function) and call the same function to get the images and set
//    it to adapter there similarly with the video part

}