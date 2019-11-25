package com.br.homes.adapter


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.br.homes.R
import com.br.homes.model.Property
import com.squareup.picasso.Picasso

class ListMyProperty(context: Context) : ArrayAdapter<Property>(context, 0) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val v: View
        if (convertView != null) {
            v = convertView
        } else {
            v = LayoutInflater.from(context)
                .inflate(R.layout.list_adapter_myproperty, parent, false)
        }
        val item = getItem(position)
        val image = v.findViewById(R.id.image_property) as ImageView
        val address = v.findViewById(R.id.address_view) as TextView
        val type = v.findViewById(R.id.type_view) as TextView
        val description = v.findViewById(R.id.description_view) as TextView
        val option = v.findViewById(R.id.option_view) as TextView
        Picasso.get().load(item!!.url).resize(100,100).centerCrop().into(image)
        val string = "${item.address}, ${item.complement}, ${item.burgh}, Amazonas, AM, ${item.cep}"
        address.text =  string
        description.text = item.description
        option.text = item.option
        type.text = item.type

        return v
    }
}