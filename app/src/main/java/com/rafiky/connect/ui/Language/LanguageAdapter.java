package com.rafiky.connect.ui.Language;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rafiky.connect.R;
import com.rafiky.connect.model.responsemodel.EventResponseModelClass;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LanguageAdapter extends RecyclerView.Adapter<LanguageAdapter.ViewHolder> {

    private Context mContext ;
    private List<EventResponseModelClass.DataBean.LanguagesBean> langageList ;
    private static final int VIEW_TYPE_DATA = 1;
    private static final int VIEW_TYPE_EMPTY = 0;
    private OnSelectLanguage onSelectLanguage ;


    public LanguageAdapter(Context mContext, List<EventResponseModelClass.DataBean.LanguagesBean> roomsBeanList) {
        this.mContext = mContext ;
        this.langageList = roomsBeanList ;
    }

    @NonNull
    @Override
    public LanguageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view ;
        if(viewType == VIEW_TYPE_EMPTY) {
            view = LayoutInflater.from(mContext).inflate(R.layout.no_room_avilable_layout, parent, false);
        }else {
            view = LayoutInflater.from(mContext).inflate(R.layout.language_list_item, parent, false);
        }
        return new ViewHolder(view,viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull LanguageAdapter.ViewHolder holder, int position) {
        int viewType = getItemViewType(position);
        if(viewType == VIEW_TYPE_DATA){
            holder.languageTitle.setText(langageList.get(position).getName());
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSelectLanguage.onSelectedLanguage(langageList.get(position));
            }
        });

    }

    @Override
    public int getItemCount() {
        if(langageList.size() == 0){
            return 1;
        }else {
            return langageList.size();
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(langageList.size() == 0){
            return VIEW_TYPE_EMPTY;
        }else {
            return VIEW_TYPE_DATA;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_sessontitle)
        TextView languageTitle;


        public ViewHolder(@NonNull View itemView, int viewType) {
            super(itemView);
            if(viewType == VIEW_TYPE_DATA){
                ButterKnife.bind(this,itemView);

            }
        }
    }


    public void setListner(OnSelectLanguage onSelectLanguageadpter){
        this.onSelectLanguage = onSelectLanguageadpter;
    }


    interface OnSelectLanguage{
        void onSelectedLanguage(EventResponseModelClass.DataBean.LanguagesBean lanfuageItem);
    }


}
