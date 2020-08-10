package com.bookeey.wallet.live.mainmenu;

import org.json.JSONObject;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bookeey.wallet.live.R;

public class EfficientAdapter extends BaseAdapter {
    public static enum ListId {
        ID_ECOM,
        ID_MERCHNAT_LOGO
    }

    private LayoutInflater mInflater;
    private Bitmap[] iconArray = null;
    private String[] data = null;
    private String[] dataNextLine = null;
    private Integer[] type = null;
    private Context _context;
    private Processor processor = null;
    ListId selectedId = null;
    static final String tag = EfficientAdapter.class.getCanonicalName();

    public EfficientAdapter(Context context, ListId id, Processor proc) {
        _context = context;
        processor = proc;
        mInflater = LayoutInflater.from(context);
        selectedId = id;
        initOrRefreshProcessorResources();
        initializeIcons(selectedId);
        initImageLoader(_context);
    }

    public static void initImageLoader(Context context) {
    }

    public void initOrRefreshProcessorResources() {
        data = processor.getDisplayable();
        if (processor.isDoubleLine()) {
            dataNextLine = processor.getNextLineDisplayable();
        }
        type = processor.getTypes();
    }

    private void initializeIcons(ListId id) {
        switch (id) {
        }
    }

    private Bitmap getIcon(ListId id, int position) {
        Bitmap bMap = null;
        return bMap;
    }

    public int getCount() {
        if (data.length > 0)
            return data.length;
        else
            return 0;
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (selectedId == ListId.ID_ECOM) {
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.generic_list_view, null);
                holder = new ViewHolder();
                holder.text = (TextView) convertView.findViewById(R.id.generic_list_view_text);
                holder.icon = (ImageView) convertView.findViewById(R.id.generic_list_view_icon);
                holder.text.setTextSize(18);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            if (data[position].toString().equals("All")) {
                holder.icon.setImageResource(R.drawable.all);
//                holder.text.setText(data[position]);
                holder.text.setText(""+_context.getString(R.string.where_to_pay_all));
            } else if (data[position].toString().equals("Food & Beverage")) {
                holder.icon.setImageResource(R.drawable.foodandbeverage);

                holder.text.setText(""+_context.getString(R.string.where_to_pay_food));

            } else if (data[position].toString().equals("Fashion")) {
                holder.icon.setImageResource(R.drawable.fashion);

                holder.text.setText(""+_context.getString(R.string.where_to_pay_fashion));

            } else if (data[position].toString().equals("Retail")) {
                holder.icon.setImageResource(R.drawable.retail);
                holder.text.setText(""+_context.getString(R.string.where_to_pay_retail));

            } else if (data[position].toString().equals("Medical Care")) {
                holder.icon.setImageResource(R.drawable.medical_care);
                holder.text.setText(""+_context.getString(R.string.where_to_pay_medical));

            } else if (data[position].toString().equals("Health & Beauty")) {
                holder.icon.setImageResource(R.drawable.healthbeauty);
                holder.text.setText(""+_context.getString(R.string.where_to_pay_health));


            } else if (data[position].toString().equals("Hotels & Accommodations")) {
                holder.icon.setImageResource(R.drawable.hotelsandaccomodation);
                holder.text.setText(""+_context.getString(R.string.where_to_pay_hotels));

            } else if (data[position].toString().equals("Transportation")) {
                holder.icon.setImageResource(R.drawable.transportation);
                holder.text.setText(""+_context.getString(R.string.where_to_pay_trasportation));

            } else if (data[position].toString().equals("Groceries & Hypermarkets")) {
                holder.icon.setImageResource(R.drawable.groceries);
                holder.text.setText(""+_context.getString(R.string.where_to_pay_groceries_hypermarket));

            } else if (data[position].toString().equals("Electronics & Computers")) {
                holder.icon.setImageResource(R.drawable.electronics);
                holder.text.setText(""+_context.getString(R.string.where_to_pay_electronics));

            } else if (data[position].toString().equals("Games & Toys")) {
                holder.icon.setImageResource(R.drawable.toys);
                holder.text.setText(""+_context.getString(R.string.where_to_pay_games));

            } else if (data[position].toString().equals("Services")) {
                holder.icon.setImageResource(R.drawable.services);
                holder.text.setText(""+_context.getString(R.string.where_to_pay_service));

            } else if (data[position].toString().equals("Automotive")) {
                holder.icon.setImageResource(R.drawable.automotive);
                holder.text.setText(""+_context.getString(R.string.where_to_pay_automotive));

            }

        } else if (selectedId == ListId.ID_MERCHNAT_LOGO) {
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.generic_list_view, null);
                holder = new ViewHolder();
                holder.text = (TextView) convertView.findViewById(R.id.generic_list_view_text);
                holder.text.setTextSize(18);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.text.setText(data[position]);
        }
        return convertView;
    }

    class ViewHolder {
        TextView text;
        ImageView icon;
    }

    public static class TripsListViewHolder {
        JSONObject jsondata;

        public JSONObject getJsonData() {
            return jsondata;
        }
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        initOrRefreshProcessorResources();
    }

    private Bitmap getYPCCustomerLogsIcon(int value) {
        Bitmap bMap = null;
        switch (value) {
        }
        return bMap;
    }
}