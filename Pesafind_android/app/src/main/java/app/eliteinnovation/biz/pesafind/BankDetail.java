package app.eliteinnovation.biz.pesafind;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class BankDetail extends AppCompatActivity {
    public static final String EXTRA_NAME = "bank_name";
    TextView txtName,txtBranches, txtAbout;
    String name, branches, about, imageurl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_bank_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        final String cheeseName = intent.getStringExtra(EXTRA_NAME);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        name=getIntent().getExtras().getString("BankName");
        branches=getIntent().getExtras().getString("Brances");
        about=getIntent().getExtras().getString("about");
        imageurl=getIntent().getExtras().getString("imageurl");
        CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(name);

        txtName = (TextView) findViewById(R.id.txt_namebranch);
        txtName.setText(name);
        txtBranches= (TextView) findViewById(R.id.txt_branches);
        txtBranches.setText(name+" has "+branches+" number of branches.");
        txtAbout = (TextView) findViewById(R.id.txt_about);
        txtAbout.setText(about);
        loadBackdrop();
    }
    private void loadBackdrop() {
        final ImageView imageView = (ImageView) findViewById(R.id.backdrop);
        Glide.with(this).load(imageurl).centerCrop().into(imageView);
    }
}
