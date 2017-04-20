package fr.nabonne.tigris.myapplication.data;

import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

/**
 * Created by tigris on 4/20/17.
 */

public interface IObservableData {
    public interface IObserver {
        void notifyItemInserted(int position);
        void notifyItemRemoved(int position);
        void onRefreshed();
    }
    void registerObserver(IObserver observer);
    void refresh();
    ArrayList<ImageURLs> getData();
    void addExcludedImage(int position);
}
