package com.dwacommerce.pos.viewControllers.settingsFragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.dwacommerce.pos.R;
import com.dwacommerce.pos.sharedPreferences.Config;
import com.dwacommerce.pos.viewControllers.SettingActivity;

import org.byteclues.lib.model.BasicModel;
import org.byteclues.lib.utils.Util;
import org.byteclues.lib.view.AbstractFragment;

import java.util.Observable;

/**
 * Created by Admin on 10/23/2016.
 */
public class DisplaySettingsFragment extends AbstractFragment implements View.OnClickListener {
    private View view;
    private TextView txtSaveDisplaySetting;
    private TextView txtCancelDisplaySetting;
    private RadioGroup rdbtngrpLayoutSetting;
    private RadioButton rdbtnWithCategory;
    private RadioButton rdbtnWithoutCategory;

    @Override
    protected View onCreateViewPost(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.display_settings, null);
        init();
        return view;
    }

    private void init() {
        txtSaveDisplaySetting = (TextView) view.findViewById(R.id.txtSaveDisplaySetting);
        txtCancelDisplaySetting = (TextView) view.findViewById(R.id.txtCancelDisplaySetting);
        rdbtngrpLayoutSetting = (RadioGroup) view.findViewById(R.id.rdbtngrpLayoutSetting);
        rdbtnWithCategory = (RadioButton) view.findViewById(R.id.rdbtnWithCategory);
        rdbtnWithoutCategory = (RadioButton) view.findViewById(R.id.rdbtnWithoutCategory);
        if (Config.getFancyDashboard()) {
            rdbtnWithCategory.setChecked(true);
        } else {
            rdbtnWithoutCategory.setChecked(true);
        }
        txtSaveDisplaySetting.setOnClickListener(this);
        txtCancelDisplaySetting.setOnClickListener(this);
    }

    @Override
    protected BasicModel getModel() {
        return null;
    }

    @Override
    public void update(Observable observable, Object data) {

    }

    @Override
    public void onClick(View v) {
        int vid = v.getId();
        if (vid == R.id.txtSaveDisplaySetting) {
            ((SettingActivity) getActivity()).setResult = true;
            int selectedRdBtnId = rdbtngrpLayoutSetting.getCheckedRadioButtonId();
            Config.setFancyDashboard(selectedRdBtnId == R.id.rdbtnWithCategory);
            Util.showCenteredToast(getActivity(), "Settings saved");
        } else if (vid == R.id.txtCancelDisplaySetting) {
            getActivity().finish();
        }
    }
}
