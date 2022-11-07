package org.tensorflow.lite.examples.detection;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityOptionsCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import org.tensorflow.lite.examples.Dialog.Toast_Custom;
import org.tensorflow.lite.examples.Main.MainActivity;
import org.tensorflow.lite.examples.R;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ItemViewHolder> {
    // adapter에 들어갈 list(CertificationFragment 내의 리스트와 연결)
    public ArrayList<Data> listData = new ArrayList<>();
    private Context context;

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // LayoutInflater를 이용하여 전 단계에서 만들었던 item.xml을 inflate 시킵니다.
        // return 인자는 ViewHolder 입니다.
        context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item, parent, false);
        return new ItemViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        // Item을 하나, 하나 보여주는(bind 되는) 함수입니다.
        holder.onBind(listData.get(position));
    }

    @Override
    public int getItemCount() {
        // RecyclerView의 총 개수 입니다.
        return listData.size();
    }

    void addItem(Data data) {
        // 외부에서 item을 추가시킬 함수입니다.
        listData.add(data);
    }

    // RecyclerView의 핵심인 ViewHolder 입니다.
    // 여기서 subView를 setting 해줍니다.
    static class ItemViewHolder extends RecyclerView.ViewHolder {

        private TextView textView;
        private TextView certView;
        private ImageView imageView;
        private Data data;
        private Context context;

        ItemViewHolder(View itemView, Context context) {
            super(itemView);

            textView = itemView.findViewById(R.id.textView);
            certView = itemView.findViewById(R.id.certView);
            imageView = itemView.findViewById(R.id.imageView);
            itemView.setOnClickListener(view -> {
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra("targetI", getAdapterPosition());

                Pair<View, String> pair_shared = Pair.create(imageView, imageView.getTransitionName());
                ActivityOptions options =
                        ActivityOptions.makeSceneTransitionAnimation((Activity) context, pair_shared);

                context.startActivity(intent, options.toBundle());
            });
            this.context = context;
        }

        void onBind(Data data) {
            this.data = data;

            textView.setText(data.getTitle());
            certView.setText(data.getCertification());
            imageView.setImageResource(data.getResId());
        }
    }
}
