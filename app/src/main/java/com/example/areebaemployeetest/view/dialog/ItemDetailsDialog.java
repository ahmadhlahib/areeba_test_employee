package com.example.areebaemployeetest.view.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.areebaemployeetest.R;
import com.example.areebaemployeetest.model.data.Name;
import com.example.areebaemployeetest.model.data.Employee;
import com.squareup.picasso.Picasso;

public class ItemDetailsDialog extends Dialog {

    public Activity activity;
    public Employee item;

    public ItemDetailsDialog(Activity activity, Employee item) {
        super(activity);
        // TODO Auto-generated constructor stub
        this.activity = activity;
        this.item=item;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.item_details_dialog, null);
        setContentView(view);
        TextView tv_username = (TextView) findViewById( R.id.tv_username);
        TextView tv_email = (TextView) findViewById( R.id.tv_email);
        TextView tv_phone = (TextView) findViewById( R.id.tv_phone);
        TextView tv_name = (TextView) findViewById( R.id.tv_name);
        TextView tv_age = (TextView) findViewById( R.id.tv_age);
        TextView tv_gender = (TextView) findViewById( R.id.tv_gender);
        TextView tv_location = (TextView) findViewById( R.id.tv_location);
//        TextView tv_pass = (TextView) findViewById( R.id.tv_pass);
        ImageView ivImage = (ImageView) findViewById( R.id.iv_image);
        ImageView ivClose = (ImageView) findViewById( R.id.iv_close);
        ImageView ivMap = (ImageView) findViewById( R.id.iv_map);
        tv_username.setText(item.getLogin().getUsername());
        tv_email.setText(item.getEmail());
        tv_phone.setText(item.getCell());
        tv_gender.setText(item.getGender());
        Name name = item.getName();
        tv_name.setText(name.getTitle()+" "+name.getFirst()+" "+name.getLast());
        tv_age.setText(item.getDob().getAge()+"");
        String location=item.getLocation().getCountry()+" - "+item.getLocation().getCity()+" - "+item.getLocation().getState()+" - "+item.getLocation().getStreet().getName()+"("+item.getLocation().getStreet().getNumber()+")";
        tv_location.setText(location);
//        tv_pass.setText(item.getLogin().getPassword());
        try
        {
            String imageUrl=item.getPicture().getLarge();
            if(imageUrl!=null && !imageUrl.isEmpty())
            {
                Picasso.with(activity).load(imageUrl).fit().centerCrop()
                        .placeholder(R.drawable.placeholder)
                        .error(R.drawable.error)
                        .into(ivImage);
            }
            else {
                if(item.getGender().equalsIgnoreCase("male"))
                    Picasso.with(activity).load(R.drawable.male).fit().centerCrop()
                            .placeholder(R.drawable.placeholder)
                            .error(R.drawable.error)
                            .into(ivImage);
                else
                    Picasso.with(activity).load(R.drawable.female).fit().centerCrop()
                            .placeholder(R.drawable.placeholder)
                            .error(R.drawable.error)
                            .into(ivImage);
            }
        }catch (Exception exception)
        {
            exception.printStackTrace();
        }

        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        ivMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String latitude= item.getLocation().getCoordinates().getLatitude();
                String longitude= item.getLocation().getCoordinates().getLongitude();
                Uri geoUri = Uri.parse("geo:" + latitude + "," + longitude + "?z=15&q=" + latitude + "," + longitude + "(Label)");

                // Create an Intent with the Uri
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, geoUri);

                // Check if there's a map app available to handle the Intent
                if (mapIntent.resolveActivity(getContext().getPackageManager()) != null) {
                    // Start the map activity
                    activity.startActivity(mapIntent);
                }
            }
        });
    }

}
