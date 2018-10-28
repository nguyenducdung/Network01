package rikkeisoft.nguyenducdung.com.network01.custom;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import rikkeisoft.nguyenducdung.com.network01.R;
import rikkeisoft.nguyenducdung.com.network01.model.User;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {
    private ArrayList<User> users;
    private Context context;
    private ItemClick itemClick;

    public UserAdapter(ArrayList<User> users, Context context, ItemClick itemClick) {
        this.users = users;
        this.context = context;
        this.itemClick = itemClick;
    }

    public UserAdapter(ArrayList<User> users, Context context) {
        this.users = users;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.user_list, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        String id = String.valueOf(users.get(position).getId());
        holder.tvUserId.setText(id);
        holder.tvUserTitle.setText(users.get(position).getTitle());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClick.onClickItem(position);
                notifyItemChanged(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvUserId;
        private TextView tvUserTitle;

        public ViewHolder(View itemView) {
            super(itemView);
            tvUserId = itemView.findViewById(R.id.tv_user_id);
            tvUserTitle = itemView.findViewById(R.id.tv_user_title);
        }
    }

    public interface ItemClick {
        void onClickItem(int i);
    }
}
