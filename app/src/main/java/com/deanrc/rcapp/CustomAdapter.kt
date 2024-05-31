import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.deanrc.rcapp.KeyValue
import com.deanrc.rcapp.R


data class ItemsViewModel(val title: String, val description: String) {
}
class CustomAdapter(private val mList: List<KeyValue>) : RecyclerView.Adapter<CustomAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.each_row, parent, false)
        return ViewHolder(view)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val keyValue = mList[position]
        holder.title.text = keyValue.key
        holder.description.text = keyValue.value
    }

    override fun getItemCount(): Int {
        return mList.size
    }
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val description: TextView = this.itemView.findViewById(R.id.description)
        val title: TextView = this.itemView.findViewById(R.id.title)
    }
}
