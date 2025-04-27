package com.devspace.taskbeats

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import androidx.core.view.isVisible
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText

class CreateOrUpdateTaskBottomSheet(
    private val categoryList: List<CategoryEntity>,
    private val task: TaskUiData? = null,
    private val onCreateClicked: (TaskUiData) -> Unit,
    private val onUpdateClicked: (TaskUiData) -> Unit,
    private val onDeleteClicked: (TaskUiData) -> Unit
) :
    BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.create_or_update_task_bottom_sheet, container, false)

        val tvTitle = view.findViewById<TextView>(R.id.tv_title)
        val btnCreateOrUpdate = view.findViewById<Button>(R.id.btn_task_create_or_update)
        val btnDelete = view.findViewById<Button>(R.id.btn_task_delete)
        val tieTask = view.findViewById<TextInputEditText>(R.id.tie_task_name)
        val spinner: Spinner = view.findViewById(R.id.spinner_category_list)

        var taskCategory: String? = null
        val categoryListTemp = mutableListOf("Select")
        categoryListTemp.addAll(
            categoryList.map { it.name }
        )
        val categoryStr: List<String> = categoryListTemp

        ArrayAdapter(
            requireActivity().baseContext,
            android.R.layout.simple_spinner_item,
            categoryStr
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                taskCategory = categoryStr[p2]
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }
        }

        if (task == null) {
            btnDelete.isVisible = false
            tvTitle.setText(R.string.create_task_title)
            btnCreateOrUpdate.setText(R.string.create)
        } else {
            tvTitle.setText(R.string.update_task_title)
            btnCreateOrUpdate.setText(R.string.update)
            tieTask.setText(task.name)
            btnDelete.isVisible = true

            val currentCategory = categoryList.first { it.name == task.category }
            val index = categoryList.indexOf(currentCategory)

            spinner.setSelection(index)
        }

        btnCreateOrUpdate.setOnClickListener {
            val name = tieTask.text.toString().trim()

            if (name.isEmpty()) {
                Snackbar.make(btnCreateOrUpdate,
                    "Please write a task.",
                    Snackbar.LENGTH_LONG).show()
            } else {
                if (taskCategory != "Select") {
                    if (task == null) {
                        onCreateClicked.invoke(
                            TaskUiData(
                                id = 0,
                                name = name,
                                category = requireNotNull(taskCategory)
                            )
                        )
                    } else {
                        onUpdateClicked.invoke(
                            TaskUiData(
                                id = task.id,
                                name = name,
                                category = requireNotNull(taskCategory)
                            )
                        )
                    }
                    dismiss()
                } else {
                    Snackbar.make(
                        btnCreateOrUpdate,
                        "Please select a category",
                        Snackbar.LENGTH_LONG
                    )
                        .show()
                }
            }
        }

        btnDelete.setOnClickListener {
            if (task != null) {
                onDeleteClicked.invoke(task)
                dismiss()
            } else {
                Log.d("CreateOrUpdateTaskBottomSheet", "Task not found")
            }

        }

        return view
    }
}