package com.devspace.taskbeats

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class InfoBottomSheet(
    private val title: String,
    private val description: String,
    private val btnText: String,
    private val onClicked: () -> Unit
) :
    BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.info_bottom_sheet, container, false)

        val tvTitle = view.findViewById<TextView>(R.id.tv_info_title)
        val btnAction = view.findViewById<Button>(R.id.btn_info_delete)
        val tvDescription = view.findViewById<TextView>(R.id.tv_info_description)
        val btnCancel = view.findViewById<Button>(R.id.btn_info_cancel)

        tvTitle.text = title
        btnAction.text = btnText
        tvDescription.text = description

        btnAction.setOnClickListener {
            onClicked.invoke()
            dismiss()
        }

        btnCancel.setOnClickListener {
            dismiss()
        }


        return view
    }
}