package com.kline.adapter;

import android.content.Context;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.kline.R;
import com.kline.bean.Address;

import java.util.List;

/**
 * Created by mei on 2016/3/29.
 */
public class AddressAdapter extends SimpleRecyclerBaseAdapter<Address> {

    private  AddressLisneter lisneter;

    public AddressAdapter(Context context, List<Address> datas,AddressLisneter lisneter) {
        super(context, datas, R.layout.template_address);

        this.lisneter = lisneter;


    }

    @Override
    public void bindDatas(RecyclerViewBaseViewHolder holder, final Address address) {
        holder.getTextView(R.id.txt_name).setText(address.getConsignee());
        holder.getTextView(R.id.txt_phone).setText(replacePhoneNum(address.getPhone()));
        holder.getTextView(R.id.txt_address).setText(address.getAddr());
        final CheckBox checkBox = holder.getCheckBox(R.id.cb_is_defualt);
        final boolean isDefault = address.getIsDefault();
        checkBox.setChecked(isDefault);
        if(isDefault){
            checkBox.setText("默认地址");
        }
        else{
            checkBox.setClickable(true);
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked && lisneter !=null){
                        address.setIsDefault(true);
                        lisneter.setDefault(address);
                    }
                }
            });
        }
    }

    public String replacePhoneNum(String phone){
        return phone.substring(0,phone.length()-(phone.substring(3)).length())+"****"+phone.substring(7);
    }

    public interface AddressLisneter{
        public void setDefault(Address address);
    }
}
