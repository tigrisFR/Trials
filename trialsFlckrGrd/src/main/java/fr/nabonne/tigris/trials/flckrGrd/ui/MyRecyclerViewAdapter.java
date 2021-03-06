package fr.nabonne.tigris.trials.flckrGrd.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import fr.nabonne.tigris.trials.R;
import fr.nabonne.tigris.trials.flckrGrd.data.IObservableData;

import static android.support.v7.widget.RecyclerView.NO_POSITION;

/**
 * Created by tigris on 4/20/17.
 */

public class MyRecyclerViewAdapter extends RecyclerView.Adapter {
    Context mAppContext;
    IObservableData data;

//    private static String[] testURLs = {
//            "https://farm3.staticflickr.com/2849/33969447032_727bc1954a_t.jpg",
//            "https://farm3.staticflickr.com/2867/33969443852_81af9555d9_b.jpg",
//            "https://farm4.staticflickr.com/3931/33742057300_0398372013_t.jpg"
//    };

    public MyRecyclerViewAdapter(Context applicationContext, IObservableData data) {
        mAppContext = applicationContext;
        this.data = data;// observable will call observer methods to notify of updates
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.recyclerview_element)
        ImageView mImageView;

        public ViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        // create a new view
        ImageView v = (ImageView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_element, parent, false);

        // set the view's size, margins, paddings and layout parameters
        //...

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int positionAtBindTime) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        ViewHolder holder2 = (ViewHolder) holder ;

        Picasso.with(mAppContext)
                .load(data.getData().get(positionAtBindTime).thumbnail)
//                .resize(120, 120)
//                .centerCrop()
                .fit()
                .centerCrop()
                .into(holder2.mImageView);

        // construct long click listener for switch to fullRez activity
        holder2.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                // This ViewHolder's position might have changed since bind time
                // (because of removal of an item at a lower index) get an
                // updated one.
                final int position = holder.getAdapterPosition();
                if (position == NO_POSITION)// adapter position unavailable (e.g. layout recalculation on data set changes)
                    return false;
                return MyRecyclerViewAdapter.this.onLongClick(v, position);
            }
        });
        // construct click listener for item removal
        holder2.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // This ViewHolder's position might have changed since bind time
                // (because of removal of an item at a lower index) get an
                // updated one.
                final int position = holder.getAdapterPosition();
                if (position == NO_POSITION)// adapter position unavailable (e.g. layout recalculation on data set changes)
                    return;
                MyRecyclerViewAdapter.this.onClick(v, position);
            }
        });

        // Request more data when we reach the last item
        if (positionAtBindTime >= data.getData().size()-1)
            data.getMoreData();
    }



    @Override
    public int getItemCount() {
        return data.getData()==null? 0:data.getData().size();
    }

    public void onClick(View v, int position) {
        // construct long click listener for switch to fullRez activity
        final String urlL = data.getData().get(position).fullRez;
        final String title = data.getData().get(position).title;
        Intent fullRezImgIntent = new Intent(v.getContext(), FullRezImgActivity.class);
        fullRezImgIntent.putExtra("urlL", urlL);
        fullRezImgIntent.putExtra("title", title);
        v.getContext().startActivity(fullRezImgIntent);
    }

    public boolean onLongClick(View v, final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());

        builder.setMessage(R.string.dialog_message_remove_img);
        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                data.addExcludedImage(position);
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
        return true;
    }
}
