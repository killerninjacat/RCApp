import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.deanrc.rcapp.KeyValue
import com.deanrc.rcapp.R
import com.deanrc.rcapp.StatusData


data class ItemsViewModel(val title: String, val description: String) {
}
class CustomAdapter(private val mList: StatusData) : RecyclerView.Adapter<CustomAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.each_row_status, parent, false)
        return ViewHolder(view)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val keyValue = mList.fileStatus[position]
        holder.title.text = keyValue.key
        holder.description.text = keyValue.value
        //Log.d("bind position", position.toString()+" "+mList.imageId[position])
        if(mList.imageId[position]==0) {
            holder.img.setImageResource(R.drawable.icon)
            Log.d("keys",keyValue.key)
        }
        else if(mList.imageId[position]==2) holder.img.setImageResource(R.drawable.icon_3)
        else holder.img.setImageResource(R.drawable.icon_2)
    }

    override fun getItemCount(): Int {
        return mList.fileStatus.size
    }
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val description: TextView = this.itemView.findViewById(R.id.description)
        val title: TextView = this.itemView.findViewById(R.id.title)
        val img : ImageView = this.itemView.findViewById(R.id.imageView4)
    }
}
