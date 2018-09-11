package taha.com.whatsapp.Adapters;

import android.content.Context;
import android.os.Debug;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.module.AppGlideModule;

import java.util.ArrayList;

import taha.com.whatsapp.GlideApp;
import taha.com.whatsapp.Models.Contact;
import taha.com.whatsapp.R;

/**
 * Created by Taha on 11/09/2018.
 */


public class CustomAdapter extends ArrayAdapter<Contact> {

    private ArrayList<Contact> dataSet = new ArrayList<>();
    Context mContext;

    // View lookup cache
    private static class ViewHolder {
        TextView txtName;
        TextView txtNumber;
        ImageView image;
    }

    public CustomAdapter(ArrayList<Contact> data, Context context) {
        super(context, R.layout.one_item_contact, data);
        this.dataSet = data;
        this.mContext=context;

    }

    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Contact dataModel = dataSet.get(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(R.layout.one_item_contact, parent, false);
            viewHolder.txtName = (TextView) convertView.findViewById(R.id.txtName);
            viewHolder.txtNumber = (TextView) convertView.findViewById(R.id.txtNumber);
            viewHolder.image = (ImageView) convertView.findViewById(R.id.image_contact);

            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }

        Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        result.startAnimation(animation);
        lastPosition = position;

        viewHolder.txtName.setText(dataModel.getNom());
        viewHolder.txtNumber.setText(dataModel.getNumber());
        Log.d("contact",dataModel.toString());
        GlideApp.with(mContext)
                .load(dataModel.getImageURL())
                .fitCenter()
                .placeholder(R.drawable.ic_launcher_background)
                .into(viewHolder.image);

        // Return the completed view to render on screen
        return convertView;
    }
}