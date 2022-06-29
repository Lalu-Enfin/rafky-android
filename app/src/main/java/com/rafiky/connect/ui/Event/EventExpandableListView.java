package com.rafiky.connect.ui.Event;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.rafiky.connect.R;
import com.rafiky.connect.model.responsemodel.EventResponseModelClass;

import java.util.List;

public class EventExpandableListView extends BaseExpandableListAdapter {

    private Context context ;
    private List<EventResponseModelClass.DataBean.RoomsBean> roomsBeanList;

    public EventExpandableListView(Context context, List<EventResponseModelClass.DataBean.RoomsBean> roomsBeanList) {
        this.context = context;
        this.roomsBeanList = roomsBeanList;
    }

    @Override
    public int getGroupCount() {
        return roomsBeanList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return roomsBeanList.get(groupPosition).getSessions().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return roomsBeanList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return roomsBeanList.get(groupPosition).getSessions().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if(convertView == null){
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.event_list_item_room, null);
        }
        TextView title = (TextView) convertView.findViewById(R.id.tv_roomtitle);
        title.setText(roomsBeanList.get(groupPosition).getRoomName());
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if(convertView == null){
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.event_list_item_session, null);
        }
        TextView title = (TextView) convertView.findViewById(R.id.tv_sessontitle);
        title.setText(roomsBeanList.get(groupPosition).getSessions().get(childPosition).getSessionName());

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
