package com.dwacommerce.pos.viewControllers.settingsFragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.dwacommerce.pos.R;
import com.dwacommerce.pos.sharedPreferences.Config;
import com.dwacommerce.pos.utility.Constants;
import com.dwacommerce.pos.utility.ShowMsg;
import com.dwacommerce.pos.utility.SpnModelsItem;
import com.dwacommerce.pos.viewControllers.DiscoverActivity;
import com.dwacommerce.pos.viewControllers.SettingActivity;
import com.epson.epos2.printer.Printer;

import org.byteclues.lib.model.BasicModel;
import org.byteclues.lib.utils.Util;
import org.byteclues.lib.view.AbstractFragment;

import java.util.Observable;

/**
 * Created by Admin on 10/23/2016.
 */
public class PrinterScannerFragment extends AbstractFragment implements View.OnClickListener {
    private View view;
    private TextView txtSavePrinterSetting;
    private TextView txtCancelPrinterSetting;
    private RadioGroup rdbtngrpPrinterWidth;
    private RadioButton rdbtnTwoInchPrinter;
    private RadioButton rdbtnThreeInchPrinter;
    private RadioGroup rdbtngrpPrintConfirmation;
    private TextView txtSearchPrinter;
    private TextView txtTestConnection;
    private TextView txtPrinterName;
    private Spinner mSpnSeries;
    private Spinner spnLang;
    private EditText etxtPrinterIpPort;
    private Printer mPrinter;
    private RadioButton rdbtnWithoutConfirmation;
    private RadioButton rdbtnWithConfirmation;
    private RadioGroup rdgroupSelectPrinter;
    private RadioButton rdbtnAemPrinter;
    private RadioButton rdbtnEpsonPrinter;

    @Override
    protected View onCreateViewPost(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.printer_scanner_layout, null);
        init();
        return view;
    }

    private void init() {
        rdbtnTwoInchPrinter = ((RadioButton) view.findViewById(R.id.rdbtnTwoInchPrinter));
        rdbtnThreeInchPrinter = ((RadioButton) view.findViewById(R.id.rdbtnThreeInchPrinter));
        rdbtngrpPrintConfirmation = (RadioGroup) view.findViewById(R.id.rdbtngrpPrintConfirmation);
        rdbtnWithConfirmation = (RadioButton) view.findViewById(R.id.rdbtnWithConfirmation);
        rdbtnWithoutConfirmation = (RadioButton) view.findViewById(R.id.rdbtnWithoutConfirmation);
        txtSearchPrinter = ((TextView) view.findViewById(R.id.txtSearchPrinter));
        txtPrinterName = ((TextView) view.findViewById(R.id.txtPrinterName));
        etxtPrinterIpPort = ((EditText) view.findViewById(R.id.etxtPrinterIpPort));
        txtTestConnection = ((TextView) view.findViewById(R.id.txtTestConnection));
        rdgroupSelectPrinter = ((RadioGroup) view.findViewById(R.id.rdgroupSelectPrinter));
        rdbtnAemPrinter = ((RadioButton) view.findViewById(R.id.rdbtnAemPrinter));
        rdbtnEpsonPrinter = ((RadioButton) view.findViewById(R.id.rdbtnEpsonPrinter));
        if (Config.getPrinterWidth() == 2) {
            rdbtnTwoInchPrinter.setChecked(true);
        } else {
            rdbtnThreeInchPrinter.setChecked(true);
        }
        if (Config.getPrintWithoutUserConfirmation()) {
            rdbtnWithConfirmation.setChecked(true);
        } else {
            rdbtnWithoutConfirmation.setChecked(true);
        }

        rdbtngrpPrinterWidth = ((RadioGroup) view.findViewById(R.id.rdbtngrpPrinterWidth));
        txtSavePrinterSetting = ((TextView) view.findViewById(R.id.txtSavePrinterSetting));
        txtCancelPrinterSetting = ((TextView) view.findViewById(R.id.txtCancelPrinterSetting));
        txtSavePrinterSetting.setOnClickListener(this);
        txtCancelPrinterSetting.setOnClickListener(this);
        txtTestConnection.setOnClickListener(this);
        txtSearchPrinter.setOnClickListener(this);
        mSpnSeries = (Spinner) view.findViewById(R.id.spnModel);
        etxtPrinterIpPort.setText(Config.getPrinterIpAddress());
        setModelSpinner();
        spnLang = (Spinner) view.findViewById(R.id.spnLang);
        setLanguageSpinner();
    }

    private void setLanguageSpinner() {
        ArrayAdapter<SpnModelsItem> langAdapter = new ArrayAdapter<SpnModelsItem>(getActivity(), android.R.layout.simple_spinner_item);
        langAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        langAdapter.add(new SpnModelsItem(getString(R.string.lang_ank), Printer.MODEL_ANK));
        langAdapter.add(new SpnModelsItem(getString(R.string.lang_japanese), Printer.MODEL_JAPANESE));
        langAdapter.add(new SpnModelsItem(getString(R.string.lang_chinese), Printer.MODEL_CHINESE));
        langAdapter.add(new SpnModelsItem(getString(R.string.lang_taiwan), Printer.MODEL_TAIWAN));
        langAdapter.add(new SpnModelsItem(getString(R.string.lang_korean), Printer.MODEL_KOREAN));
        langAdapter.add(new SpnModelsItem(getString(R.string.lang_thai), Printer.MODEL_THAI));
        langAdapter.add(new SpnModelsItem(getString(R.string.lang_southasia), Printer.MODEL_SOUTHASIA));
        spnLang.setAdapter(langAdapter);
        spnLang.setSelection(Config.getPrinterLanguageConstant());
    }

    private void setModelSpinner() {
        ArrayAdapter<SpnModelsItem> seriesAdapter = new ArrayAdapter<SpnModelsItem>(getActivity(), android.R.layout.simple_spinner_item);
        seriesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        seriesAdapter.add(new SpnModelsItem(getString(R.string.printerseries_m10), Printer.TM_M10));
        seriesAdapter.add(new SpnModelsItem(getString(R.string.printerseries_m30), Printer.TM_M30));
        seriesAdapter.add(new SpnModelsItem(getString(R.string.printerseries_p20), Printer.TM_P20));
        seriesAdapter.add(new SpnModelsItem(getString(R.string.printerseries_p60), Printer.TM_P60));
        seriesAdapter.add(new SpnModelsItem(getString(R.string.printerseries_p60ii), Printer.TM_P60II));
        seriesAdapter.add(new SpnModelsItem(getString(R.string.printerseries_p80), Printer.TM_P80));
        seriesAdapter.add(new SpnModelsItem(getString(R.string.printerseries_t20), Printer.TM_T20));
        seriesAdapter.add(new SpnModelsItem(getString(R.string.printerseries_t60), Printer.TM_T60));
        seriesAdapter.add(new SpnModelsItem(getString(R.string.printerseries_t70), Printer.TM_T70));
        seriesAdapter.add(new SpnModelsItem(getString(R.string.printerseries_t81), Printer.TM_T81));
        seriesAdapter.add(new SpnModelsItem(getString(R.string.printerseries_t82), Printer.TM_T82));
        seriesAdapter.add(new SpnModelsItem(getString(R.string.printerseries_t83), Printer.TM_T83));
        seriesAdapter.add(new SpnModelsItem(getString(R.string.printerseries_t88), Printer.TM_T88));
        seriesAdapter.add(new SpnModelsItem(getString(R.string.printerseries_t90), Printer.TM_T90));
        seriesAdapter.add(new SpnModelsItem(getString(R.string.printerseries_t90kp), Printer.TM_T90KP));
        seriesAdapter.add(new SpnModelsItem(getString(R.string.printerseries_u220), Printer.TM_U220));
        seriesAdapter.add(new SpnModelsItem(getString(R.string.printerseries_u330), Printer.TM_U330));
        seriesAdapter.add(new SpnModelsItem(getString(R.string.printerseries_l90), Printer.TM_L90));
        seriesAdapter.add(new SpnModelsItem(getString(R.string.printerseries_h6000), Printer.TM_H6000));
        mSpnSeries.setAdapter(seriesAdapter);
        mSpnSeries.setSelection(Config.getPrinterSeriesConstant());
    }

    @Override
    protected BasicModel getModel() {
        return null;
    }

    @Override
    public void update(Observable observable, Object data) {
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null && resultCode == getActivity().RESULT_OK) {
            try {
                String target = data.getStringExtra(getString(R.string.title_target));
                if (target != null) {
                    String printerName = data.getStringExtra("PrinterName");
                    txtPrinterName.setText(printerName);
                    etxtPrinterIpPort.setText(target);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private boolean initializeObject() {
        try {
            mPrinter = new Printer(((SpnModelsItem) mSpnSeries.getSelectedItem()).getModelConstant(), ((SpnModelsItem) spnLang.getSelectedItem()).getModelConstant(), getActivity());
        } catch (Exception e) {
            ShowMsg.showException(e, "Printer", getActivity());
            return false;
        }
        return true;
    }

    private boolean connectPrinter() {
        try {
            mPrinter.connect(etxtPrinterIpPort.getText().toString(), Printer.PARAM_DEFAULT);
        } catch (Exception e) {
            ShowMsg.showException(e, "connect", getActivity());
            return false;
        }
        return true;
    }

    private boolean disconnectPrinter() {
        try {
            mPrinter.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        int vid = v.getId();
        if (vid == R.id.txtSavePrinterSetting) {
            ((SettingActivity) getActivity()).setResult = true;
            int printerWidthSelectedId = rdbtngrpPrinterWidth.getCheckedRadioButtonId();
            Config.setPrinterWidth(printerWidthSelectedId == R.id.rdbtnTwoInchPrinter ? 2 : 3);
            Config.setPrinterIpAddress(etxtPrinterIpPort.getText().toString().trim());
            Config.setPrinterSeriesConstant(((SpnModelsItem) mSpnSeries.getSelectedItem()).getModelConstant());
            Config.setPrinterLanguageConstant(((SpnModelsItem) spnLang.getSelectedItem()).getModelConstant());
            int selctedRdBtnIdPrinting = rdbtngrpPrintConfirmation.getCheckedRadioButtonId();
            int selctedRdBtnSelectBtnId = rdgroupSelectPrinter.getCheckedRadioButtonId();
            Config.setPrintWithoutUserConfirmation(selctedRdBtnIdPrinting == R.id.rdbtnWithConfirmation);
            Config.setPrinterId(selctedRdBtnSelectBtnId);
            Util.showCenteredToast(getActivity(), "Settings saved");
        } else if (vid == R.id.txtCancelPrinterSetting) {
            getActivity().finish();
        } else if (vid == R.id.txtSearchPrinter) {
            Intent intent = new Intent(getActivity(), DiscoverActivity.class);
            startActivityForResult(intent, Constants.REQUEST_CODE_FOR_DISCOVER_PRINTER);
        } else if (vid == R.id.txtTestConnection) {
            if (!initializeObject()) {
                Util.showAlertDialog(null, "Unable to connect to printer");
            }
            if (connectPrinter()) {
                Util.showAlertDialog(null, "Connection Successful");
                disconnectPrinter();
            }
        }
    }
}
