package app.eliteinnovation.biz.pesafind;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Chebby on 2/16/2016.
 */
public class BankAdapter extends BaseAdapter {
    TextView BankName, Branches;
    ImageView bImageView;
    private Activity activ;
    public Context c;
    private ArrayList<Bank> dat;
    private static LayoutInflater inflater=null;
    //public static String TAG_NAME, TAG_BRANCH, TAG_URL, TAG_ABOUT, TAG_IMGURL;

    public BankAdapter(Activity a,ArrayList<Bank> d){
        activ = a;
        dat=d;

        inflater = (LayoutInflater)activ.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {

        return dat.size();
    }

    @Override
    public Object getItem(int position) {

        return position;
    }

    @Override
    public long getItemId(int position) {

        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View vi=convertView;
        final BankDetail holder;
        if(convertView==null){
            holder= new BankDetail();
            vi = inflater.inflate(R.layout.list_item, null);
            BankName= (TextView) vi.findViewById(R.id.txt_bankname);
            Branches=(TextView) vi.findViewById(R.id.txt_branches);
            bImageView=(ImageView) vi.findViewById(R.id.img_bankicon);

            vi.setTag(holder);
        }
        else{
            holder= (BankDetail) convertView.getTag();
        }
        /*TAG_NAME=dat.get(position).getName();
        TAG_BRANCH=dat.get(position).getBranches();
        TAG_URL=dat.get(position).getImgurl();
        TAG_ABOUT=dat.get(position).getAbout();
        TAG_IMGURL=dat.get(position).getImgurl();*/
        BankName.setText(dat.get(position).getName());
        Branches.setText(dat.get(position).getBranches()+" branches");
        ImageUtil.displayRoundImage(bImageView,dat.get(position).getImgurl(),null);
        vi.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent bintent=new Intent(activ,BankDetail.class);
                bintent.putExtra("BankName", dat.get(position).getName());
                bintent.putExtra("Brances",dat.get(position).getBranches());
                bintent.putExtra("url",dat.get(position).getImgurl());
                bintent.putExtra("about",dat.get(position).getAbout());
                bintent.putExtra("imageurl",dat.get(position).getImageurl());
                activ.startActivity(bintent);

            }
        });
        return vi;
    }


}
