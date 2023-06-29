package com.ali.retrofitjava.adapter;

import android.graphics.Color;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ali.retrofitjava.R;
import com.ali.retrofitjava.model.CryptoModel;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.RowHolder> {

    private ArrayList<CryptoModel> cryptoList;
    private String[] colors={"#8E44AD","#F39C12","#E4E71D","#3617D5","#EB235D","#15E6E9","#DD0E0E","#990AF0"};//hex colors codes yazarak renklerin hex kodlarını internetten aldım ve renkli recycler view olayımızda bu string colors dizisiin kullanıcam

    public RecyclerViewAdapter(ArrayList<CryptoModel> cryptoList){
        this.cryptoList=cryptoList;
    }


    @NonNull
    @Override
    public RowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater= LayoutInflater.from(parent.getContext());//Inflater'lar bu tarz farkı xml'leri birbirlerine bağlamamıza yararlar. Zaten method içerisinde bağlama(Inflate) işlemleri yapılıyordu hatırlarsak
        View view=layoutInflater.inflate(R.layout.row_layout,parent,false); // row_layout ile parentı(burayı) bağla
        return new RowHolder(view);//oluşturduğumuz holder sınıfı(burada RowHolder) bizden bir view(görünüm) bekliyor, ve ona burdan veriyoruz o görünümü

    }

    @Override
    public void onBindViewHolder(@NonNull RowHolder holder, int position) {

        holder.bind(cryptoList.get(position),colors,position);

    }

    @Override
    public int getItemCount() {
        return cryptoList.size();
    }

    public class RowHolder extends RecyclerView.ViewHolder{
        TextView textName;
        TextView textPrice;

        public RowHolder(@NonNull View itemView) {
            super(itemView);


        }

        public void bind(CryptoModel cryptoModel, String[] colors, Integer position){
            itemView.setBackgroundColor(Color.parseColor(colors[position %8]));//yazıların arka planını renkli yapmak için bunu yaptık. Önceki yaptığımız tüm RecyclerView'lardan 2 tane farkı var bunun. binding kullanmadık ve renkli rowlar olsun istedik
            textName=itemView.findViewById(R.id.text_name);//row_layout xml'i içerisindeki textView'larımızla buradaki değişkenlerimizi bağlama işlemlerini yapıyoruz
            textPrice=itemView.findViewById(R.id.text_price);
            textName.setText(cryptoModel.currency);
            textPrice.setText(cryptoModel.price);

        }


    }


}
