package com.example.to_dolist.fragments

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.DatePicker
import android.widget.EditText
import android.widget.Spinner
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.example.to_dolist.R
import java.util.*

class AddToDoDialogFragment(private val addItem: (String, String, String) -> Unit) : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireActivity())
        val inflater = requireActivity().layoutInflater
        val view = inflater.inflate(R.layout.dialog_add_to_do, null)
        val editText = view.findViewById<EditText>(R.id.edit_text)
        val prioritySpinner = view.findViewById<Spinner>(R.id.priority_spinner)
        val datePicker = view.findViewById<DatePicker>(R.id.due_date_picker)

        builder.setView(view)
            .setTitle("Add New To-Do")
            .setNegativeButton("cancel") { dialog, _ -> dialog?.dismiss() }
            .setPositiveButton("add") { _, _ ->
                val item = editText.text.toString()
                val priority = prioritySpinner.selectedItem.toString()
                val day = datePicker.dayOfMonth
                val month = datePicker.month + 1
                val year = datePicker.year
                val dueDate = "$day/$month/$year"
                addItem(item, priority, dueDate)
            }

        return builder.create()
    }
}
