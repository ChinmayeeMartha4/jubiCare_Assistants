package com.example.studentomr;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studentomr.Utils.OnImageClickListener;

import java.net.CookieHandler;
import java.util.ArrayList;

public class RadioCustomAdapter extends RecyclerView.Adapter< RadioCustomAdapter.ViewHolder>{
    private Context context;
    private ArrayList<questionPojo> arrayList;
    private OnImageClickListener onImageClickListener;
    private ImageView iv_option;
//    private CookieHandler fonts;


    public RadioCustomAdapter(Context context, ArrayList<questionPojo> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
        this.onImageClickListener=onImageClickListener;
    }
    ArrayList<Integer> values=new ArrayList<>();

//    public RadioCustomAdapter(QuestionList questionList, ArrayList<questionPojo> pojoArrayList) {
//    }

//    public ArrayList<Integer> getRadioValues() {
//        return values;
//    }


    @NonNull

    @Override
    public  RadioCustomAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.custom_question, parent, false);
        RadioCustomAdapter.ViewHolder viewholder = new  RadioCustomAdapter.ViewHolder(view);
        return viewholder;
    }

    @Override
    public void onBindViewHolder(@NonNull RadioCustomAdapter.ViewHolder holder, final int position) {

        holder.tv_option1.setText(arrayList.get(position).getOption1());
        holder.tv_option2.setText(arrayList.get(position).getOption2());
        holder.tv_option3.setText(arrayList.get(position).getOption3());
        holder.tv_option4.setText(arrayList.get(position).getOption4());
        holder.tv_question.setText(arrayList.get(position).getQuestions());

//        holder.checkBox.setChecked(fonts.get(position).isSelected());
//        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if(isChecked) {
////                    for (int i = 0; i < fonts.size(); i++) {
////                        fonts.get(i).setSelected(false);
////                    }
////                    fonts.get(position).setSelected(isChecked);
//                }
//            }
//        });
//        if (arrayList.get(position).getShown().equals("1")){
//            iv_option.setImageResource(R.drawable.radiochecked);
//
//        }else {
//           iv_option.setImageResource(R.drawable.radio);
//
//        }
//        iv_option.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                onImageClickListener.onImageClick(Integer.parseInt(arrayList.get(position).getId()));
//                for(int i=0; i<arrayList.size();i++) {
//                    if (i == position) {
//                        iv_option.setImageResource(R.drawable.radiochecked);
//                        arrayList.get(i).setShown("1");
//                    } else {
//                        iv_option.setImageResource(R.drawable.radio);
//                        arrayList.get(i).setShown("0");
//
//                    }
//                    notifyDataSetChanged();
//
//                }
//            }
//        });
       /* viewHolder.rg_custom.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i)
            {
                if (viewHolder.rb_custom.isPressed()){
                    values.add(pos);
                }


            }
        });*/






    }


    @Override
    public int getItemCount() {
        return arrayList.size();
    }



    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_option1,tv_option2,tv_option3,tv_option4,tv_question;
        public CompoundButton checkBox;
        ImageView iv_option;
        RadioGroup rg_custom;
        RadioButton rb_custom;


        public ViewHolder(View itemView) {
            super(itemView);
            this.rg_custom = itemView.findViewById(R.id.rg_custom);
            this.iv_option = itemView.findViewById(R.id.iv_option);
            this.tv_option1 = itemView.findViewById(R.id.tv_option1);
            this.tv_option2 = itemView.findViewById(R.id.tv_option2);
            this.tv_option3 = itemView.findViewById(R.id.tv_option3);
            this.tv_option4 = itemView.findViewById(R.id.tv_option4);
            this.tv_question = itemView.findViewById(R.id.tv_question);
            this.rb_custom = itemView.findViewById(R.id.rb_custom);


        }

}
}
