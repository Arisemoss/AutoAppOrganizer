package com.autoapporganizer.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.autoapporganizer.R
import com.autoapporganizer.databinding.ActivityHistoryBinding
import com.autoapporganizer.databinding.ItemHistoryBinding
import com.autoapporganizer.model.OrganizeSession
import com.autoapporganizer.util.HistoryManager
import com.google.android.material.snackbar.Snackbar
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * 历史记录页 —— 展示过往整理会话列表，支持删除单条。
 */
class HistoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHistoryBinding
    private lateinit var historyManager: HistoryManager
    private lateinit var adapter: HistoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        historyManager = HistoryManager(this)
        adapter = HistoryAdapter(emptyList()) { session -> deleteSession(session) }

        binding.recyclerHistory.layoutManager = LinearLayoutManager(this)
        binding.recyclerHistory.adapter = adapter

        binding.btnBack.setOnClickListener { finish() }
        refresh()
    }

    override fun onResume() {
        super.onResume()
        refresh()
    }

    private fun refresh() {
        val items = historyManager.loadAll()
        adapter.update(items)
        binding.layoutEmpty.visibility = if (items.isEmpty()) android.view.View.VISIBLE else android.view.View.GONE
        binding.recyclerHistory.visibility = if (items.isEmpty()) android.view.View.GONE else android.view.View.VISIBLE
    }

    private fun deleteSession(session: OrganizeSession) {
        historyManager.delete(session.timestamp)
        refresh()
        Snackbar.make(binding.root, R.string.history_deleted, Snackbar.LENGTH_SHORT).show()
    }

    // ──────────────────────────────────────────────
    // Adapter
    // ──────────────────────────────────────────────

    private inner class HistoryAdapter(
        private var items: List<OrganizeSession>,
        private val onDelete: (OrganizeSession) -> Unit
    ) : RecyclerView.Adapter<HistoryAdapter.VH>() {

        fun update(newItems: List<OrganizeSession>) {
            items = newItems
            notifyDataSetChanged()
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
            val b = ItemHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return VH(b)
        }

        override fun onBindViewHolder(holder: VH, position: Int) = holder.bind(items[position])

        override fun getItemCount(): Int = items.size

        inner class VH(private val b: ItemHistoryBinding) : RecyclerView.ViewHolder(b.root) {
            private val fmt = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())

            fun bind(session: OrganizeSession) {
                b.tvTime.text = fmt.format(Date(session.timestamp))
                b.tvSummary.text = getString(
                    R.string.history_created, session.folderCount
                ) + " · " + session.appCount + " 个应用"

                val cats = session.sortedCategories.joinToString(" · ") { "${it.key} ${it.value}" }
                b.tvCategories.text = if (cats.isBlank()) "—" else cats
                b.btnDelete.setOnClickListener { onDelete(session) }
            }
        }
    }
}
