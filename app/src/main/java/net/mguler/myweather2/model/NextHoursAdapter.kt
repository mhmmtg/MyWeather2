package net.mguler.myweather2.model

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import net.mguler.myweather2.databinding.HomeRecylerRowBinding

class NextHoursAdapter(var myContext: Context, var nextHoursList: ArrayList<NextHours>) : RecyclerView.Adapter<NextHoursAdapter.NextHoursHolder>() {

    class NextHoursHolder(var binding: HomeRecylerRowBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NextHoursHolder {
        val binding = HomeRecylerRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NextHoursHolder(binding)
    }

    override fun onBindViewHolder(holder: NextHoursHolder, position: Int) {
        holder.binding.textSmallDegree.text = String.format("%.0f", nextHoursList[position].temperature)
        holder.binding.textTime.text = nextHoursList[position].nextHours?.substring(11, 16)

        val nextHoursIcon = nextHoursList[position].icon
        val imgRes = myContext.resources.getIdentifier("img" + nextHoursIcon, "drawable", myContext.packageName)
        holder.binding.imageSmallForecast.setImageResource(imgRes)
    }

    override fun getItemCount(): Int {
        return nextHoursList.size
    }
}