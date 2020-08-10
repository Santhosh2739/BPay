//package newflow;
//
//import android.support.annotation.NonNull;
//import android.support.v7.widget.RecyclerView;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import com.bookeey.wallet.live.R;
//
//import java.util.List;
//
//public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.MyViewHolder> {
//    private List<CategoryModel> categoryList;
//    class MyViewHolder extends RecyclerView.ViewHolder {
//        TextView tv_category_name;
//        ImageView iv_category_image;
//        MyViewHolder(View view) {
//            super(view);
//            tv_category_name = view.findViewById(R.id.tv_category_name);
//            iv_category_image = view.findViewById(R.id.iv_category_image);
//
//        }
//    }
//    public CategoryAdapter(List<CategoryModel> categoryList) {
//        this.categoryList = categoryList;
//    }
//    @NonNull
//    @Override
//    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View itemView = LayoutInflater.from(parent.getContext())
//                .inflate(R.layout.custom_category_view, parent, false);
//        return new MyViewHolder(itemView);
//    }
//    @Override
//    public void onBindViewHolder(MyViewHolder holder, int position) {
//        CategoryModel category = categoryList.get(position);
//        holder.iv_category_image.setImageResource(category.getCategoryIcon());
//        holder.tv_category_name.setText(category.getCategoryName());
//
//    }
//    @Override
//    public int getItemCount() {
//        return categoryList.size();
//    }
//}