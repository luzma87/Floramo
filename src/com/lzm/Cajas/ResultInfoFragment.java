package com.lzm.Cajas;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.lzm.Cajas.MapActivity;
import com.lzm.Cajas.utils.SearchResult;
/**
 * Created by Svt on 10/7/2014.
 */
public class ResultInfoFragment  extends Fragment implements Button.OnClickListener, View.OnTouchListener  {
    MapActivity context;
    SearchResult current;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        context = (MapActivity) getActivity();
        current = context.result.get(context.posSearch);
        View view = inflater.inflate(R.layout.search_result_info, container, false);
        ((TextView)view.findViewById(R.id.especie_info_nombre_cientifico)).setText(current.scientificName);
        ((TextView)view.findViewById(R.id.especie_info_familia)).setText(current.family);
        ((TextView)view.findViewById(R.id.txt_autor)).setText(current.author);
        ((TextView)view.findViewById(R.id.txt_rank)).setText(current.rankAbbreviation);
        ((TextView)view.findViewById(R.id.txt_display_ref)).setText(current.displayReference);
        ((TextView)view.findViewById(R.id.txt_display_date)).setText(current.displayDate);
        return view;
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        return false;
    }
}
