package com.example.to_dolist

import android.os.Bundle
import android.view.*
import android.widget.ListView
import android.widget.SimpleAdapter
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.to_dolist.fragments.AddToDoDialogFragment

class ToDoListFragment : Fragment() {

    private lateinit var listView: ListView
    private val toDoItems = ArrayList<Map<String, String>>()
    private lateinit var adapter: SimpleAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_to_do_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        listView = view.findViewById(R.id.list_view)
        adapter = object : SimpleAdapter(
            requireContext(),
            toDoItems,
            R.layout.item_todo,
            arrayOf("description", "dueDate"),
            intArrayOf(R.id.tv_todo_item, R.id.tv_due_date)
        ) {
            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = super.getView(position, convertView, parent)
                val priority = toDoItems[position]["priority"]
                val priorityIndicator = view.findViewById<View>(R.id.priority_indicator)
                val color = when (priority) {
                    "High" -> ContextCompat.getColor(requireContext(), android.R.color.holo_red_light)
                    "Medium" -> ContextCompat.getColor(requireContext(), android.R.color.holo_orange_light)
                    "Low" -> ContextCompat.getColor(requireContext(), android.R.color.holo_green_light)
                    else -> ContextCompat.getColor(requireContext(), android.R.color.white)
                }
                priorityIndicator.setBackgroundColor(color)
                return view
            }
        }

        listView.adapter = adapter
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_main, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_add_item -> {
                val dialog = AddToDoDialogFragment { description, priority, dueDate ->
                    addItem(description, priority, dueDate)
                }
                dialog.show(parentFragmentManager, "AddToDoDialog")
                true
            }
            R.id.action_sort_priority -> {
                sortItemsByPriority()
                true
            }
            R.id.action_clear_all -> {
                clearAllItems()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun addItem(description: String, priority: String, dueDate: String) {
        val item = mapOf("description" to description, "dueDate" to dueDate, "priority" to priority)
        toDoItems.add(item)
        adapter.notifyDataSetChanged()
    }

    private fun sortItemsByPriority() {
        toDoItems.sortWith(compareBy {
            when (it["priority"]) {
                "High" -> 1
                "Medium" -> 2
                "Low" -> 3
                else -> 4
            }
        })
        adapter.notifyDataSetChanged()
    }

    private fun clearAllItems() {
        toDoItems.clear()
        adapter.notifyDataSetChanged()
    }

    companion object {
        fun newInstance() = ToDoListFragment()
    }
}
