package dodeunifront.dodeuni.community.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import dodeunifront.dodeuni.Hue.API_Hyu;
import dodeunifront.dodeuni.R;
import dodeunifront.dodeuni.community.API_Postcommunity;
import dodeunifront.dodeuni.community.DTO.CommentResponseDTO;
import dodeunifront.dodeuni.community.DTO.CommunityListResponseDto;
import dodeunifront.dodeuni.community.DetailCommunityActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CommentAdapter  extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {
    public ArrayList<CommentResponseDTO> mdataList = null;
    Context mcontext;
    Dialog dilaog01;
    Gson gson = new GsonBuilder()
            .setLenient()
            .create();

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(API_Hyu.URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build();

    public CommentAdapter(ArrayList<CommentResponseDTO> dataList,Context mcontext){

        mdataList = dataList;
        this.mcontext = mcontext;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.itemlist_comment, parent, false);
        return new CommentAdapter.MyViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        ((CommentAdapter.MyViewHolder) viewHolder).content.setText(mdataList.get(position).getContent());
        ((CommentAdapter.MyViewHolder) viewHolder).nickname.setText(mdataList.get(position).getNickname());
        ((CommentAdapter.MyViewHolder) viewHolder).createdate.setText(mdataList.get(position).getCreatedDateTime());

        ((MyViewHolder) viewHolder).btn_commentmenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(mcontext.getApplicationContext(),view);
                popupMenu.getMenuInflater().inflate(R.menu.popup_menu_comment,popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        int nowposi = viewHolder.getAbsoluteAdapterPosition();
                        if (menuItem.getItemId() == R.id.popup_deleteco){
//                            dilaog01 = new Dialog(mcontext);       // Dialog 초기화
//                            dilaog01.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//                            dilaog01.requestWindowFeature(Window.FEATURE_NO_TITLE);
//                            dilaog01.setContentView(R.layout.dialog_deletecommunity);
                            showDialog_delete(mdataList.get(nowposi).getId(),mdataList.get(nowposi).getCid(),nowposi,view);
//                            Toast.makeText(view.getContext(), "삭제하기", Toast.LENGTH_SHORT).show();
                        }
                        return false;
                    }
                });
                popupMenu.show();
            }
        });
    }
    @Override
    public int getItemCount()
    {
        return mdataList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView content;
        TextView nickname;
        TextView createdate;
        ImageView btn_commentmenu;

        MyViewHolder(View itemView)
        {
            super(itemView);
            content = itemView.findViewById(R.id.tv_comment_content);
            nickname = itemView.findViewById(R.id.tv_community_comment_writer);
            createdate = itemView.findViewById(R.id.tv_comment_date);
            btn_commentmenu = itemView.findViewById(R.id.btn_comment_menu);

        }
    }
//    public void showDialog_delete(Long id, Long cid,int positions){
//        //dilaog01.show(); // 다이얼로그 띄우기
//        Button noBtn = dilaog01.findViewById(R.id.btn_delete_cancel);
//        TextView tv_cocon = dilaog01.findViewById(R.id.tv_question_co);
//        TextView tv_cocon1= dilaog01.findViewById(R.id.tv_question_coco);
//        tv_cocon.setText("댓글을 삭제하시겠습니까?");
//        tv_cocon1.setText("완료 버튼을 누르면 댓글이 삭제됩니다");
//        noBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                dilaog01.dismiss();
//            }
//        });
//        dilaog01.findViewById(R.id.btn_delete_ok).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                // 원하는 기능 구현
//                API_Postcommunity api_postcommunity_comment = retrofit.create(API_Postcommunity.class);
//                api_postcommunity_comment.deletecomment(id,cid).enqueue(new Callback<List<CommentResponseDTO>>() {
//                    @Override
//                    public void onResponse(Call<List<CommentResponseDTO>> call, Response<List<CommentResponseDTO>> response) {
//                        Log.e("삭제성공",response.body()+"");
//                        mdataList.remove(positions);
//                        notifyItemRemoved(positions);
//                        notifyDataSetChanged();
//                    }
//                    @Override
//                    public void onFailure(Call<List<CommentResponseDTO>> call, Throwable t) {
//                        Log.e("onfailure",t.toString());
//                    }
//                });
//                //dilaog01.dismiss();
//            }
//        });
//    }
    public void showDialog_delete(Long id, Long cid,int positions,View view){
        API_Postcommunity api_postcommunity_comment = retrofit.create(API_Postcommunity.class);
        api_postcommunity_comment.deletecomment(id,cid).enqueue(new Callback<List<CommentResponseDTO>>() {
            @Override
            public void onResponse(Call<List<CommentResponseDTO>> call, Response<List<CommentResponseDTO>> response) {
                Log.e("삭제성공",response.body()+"");
                mdataList.remove(positions);
                notifyItemRemoved(positions);
                notifyDataSetChanged();
                Toast.makeText(view.getContext(), "댓글이 삭제되었습니다", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onFailure(Call<List<CommentResponseDTO>> call, Throwable t) {
                Log.e("onfailure",t.toString());
            }
        });
    }

}
