package app.eliteinnovation.biz.pesafind.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import app.eliteinnovation.biz.pesafind.R;

/**
 * Created by Chebby on 2/14/2016.
 */
public class DropdownAdapter extends BaseAdapter implements Filterable {
    Context ctx;

    ArrayList<HashMap<String, String>> originalEntries;
    ArrayList<HashMap<String, String>> filteredEntries;

    private Filter filter;

    public DropdownAdapter(Context context, ArrayList<HashMap<String, String>> entries) {
        this.ctx = context;
        this.originalEntries = entries;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) ctx
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.dropdown_item, parent,
                    false);
        }

        ((TextView) convertView.findViewById(R.id.dropdown_item)).setText(getItem(position));

        return convertView;

    }

    @Override
    public Filter getFilter() {
        if (filter == null) {
            filter = new SampleFilter();
        }
        return filter;
    }

    @Override
    public int getCount() {
        return filteredEntries.size();
    }

    @Override
    public String getItem(int position) {
        return filteredEntries.get(position).get("bankid").toString() + ". "+filteredEntries.get(position).get("bankname").toString();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class SampleFilter extends Filter {

        @SuppressLint("DefaultLocale")
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();

            if (constraint != null) {
                // load more data here.. eg from webservice
                final String prefixString = constraint.toString().toLowerCase();
                ArrayList<HashMap<String, String>> values = originalEntries;
                int count = values.size();

                ArrayList<HashMap<String, String>> newValues = new ArrayList<HashMap<String, String>>(count);

                for (int i = 0; i < count; i++) {
                    String item = values.get(i).get("bankname").toString();
                    if (item.toLowerCase().startsWith(prefixString)) {
                        newValues.add(values.get(i));
                    }
                }

                results.values = newValues;
                results.count = newValues.size();
            }

            return results;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint,
                                      FilterResults results) {

            if (results.values != null) {
                filteredEntries = (ArrayList<HashMap<String, String>>) results.values;
            } else {
                filteredEntries = new ArrayList<HashMap<String, String>>();
            }
            if (results.count > 0) {
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }
        }

    }

}
