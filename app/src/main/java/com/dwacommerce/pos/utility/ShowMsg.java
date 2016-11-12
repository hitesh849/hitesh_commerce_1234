package com.dwacommerce.pos.utility;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;

import com.dwacommerce.pos.R;
import com.epson.epos2.Epos2CallbackCode;
import com.epson.epos2.Epos2Exception;

/**
 * Created by Admin on 10/18/2016.
 */
public class ShowMsg {
    public static void showException(Exception e, String method, Context context) {
        String msg = "";
        if (e instanceof Epos2Exception) {
            msg = String.format("%s\n\t%s\n%s\n\t%s", context.getString(R.string.title_err_code), getEposExceptionText(((Epos2Exception) e).getErrorStatus()), context.getString(R.string.title_err_method), method);
        } else {
            msg = e.toString();
        }
        show(msg, context);
    }

    public static void showResult(int code, String errMsg, Context context) {
        String msg = "";
        if (errMsg.isEmpty()) {
            msg = String.format("\t%s\n\t%s\n", context.getString(R.string.title_msg_result), getCodeText(code));
        } else {
            msg = String.format("\t%s\n\t%s\n\n\t%s\n\t%s\n", context.getString(R.string.title_msg_result), getCodeText(code), context.getString(R.string.title_msg_description), errMsg);
        }
        show(msg, context);
    }

    public static void showMsg(String msg, Context context) {
        show(msg, context);
    }

    private static void show(String msg, Context context) {
        if (!TextUtils.isEmpty(msg)) {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
            alertDialog.setMessage(msg);
            alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    return;
                }
            });
            alertDialog.create();
            alertDialog.show();
        }

    }

    private static String getEposExceptionText(int state) {
        String return_text = "Something went wrong!";
        switch (state) {
            case Epos2Exception.ERR_PARAM:
                return_text = "";
                break;
            case Epos2Exception.ERR_CONNECT:
                return_text = "Epson Printer: Successfully connected";
                break;
            case Epos2Exception.ERR_TIMEOUT:
                return_text = "Epson Printer: Connection timeout";
                break;
            case Epos2Exception.ERR_MEMORY:
                return_text = "Epson Printer: Memory full";
                break;
            case Epos2Exception.ERR_ILLEGAL:
                return_text = "Epson Printer: Illegal";
                break;
            case Epos2Exception.ERR_PROCESSING:
                return_text = "Epson Printer: Processing";
                break;
            case Epos2Exception.ERR_NOT_FOUND:
                return_text = "Epson Printer: Not Found";
                break;
            case Epos2Exception.ERR_IN_USE:
                return_text = "Epson Printer: In Use";
                break;
            case Epos2Exception.ERR_TYPE_INVALID:
                return_text = "Epson Printer: Invalid";
                break;
            case Epos2Exception.ERR_DISCONNECT:
                return_text = "Epson Printer: Disconnected";
                break;
            case Epos2Exception.ERR_ALREADY_OPENED:
                return_text = "Epson Printer: Already open";
                break;
            case Epos2Exception.ERR_ALREADY_USED:
                return_text = "Epson Printer: Already in use";
                break;
            case Epos2Exception.ERR_BOX_COUNT_OVER:
                return_text = "Epson Printer: Error box count over";
                break;
            case Epos2Exception.ERR_BOX_CLIENT_OVER:
                return_text = "Epson Printer: Error box client over";
                break;
            case Epos2Exception.ERR_UNSUPPORTED:
                return_text = "Epson Printer: Unsupported";
                break;
            case Epos2Exception.ERR_FAILURE:
                return_text = "Epson Printer: Failure";
                break;
            default:
                return_text = String.format("%d", state);
                break;
        }
        return return_text;
    }

    private static String getCodeText(int state) {
        String return_text = "";
        switch (state) {
            case Epos2CallbackCode.CODE_SUCCESS:
                return_text = "Epson Printer: Success";
                break;
            case Epos2CallbackCode.CODE_PRINTING:
                return_text = "Epson Printer: Priniting";
                break;
            case Epos2CallbackCode.CODE_ERR_AUTORECOVER:
                return_text = "Epson Printer: Auto recover";
                break;
            case Epos2CallbackCode.CODE_ERR_COVER_OPEN:
                return_text = "Epson Printer: Cover open";
                break;
            case Epos2CallbackCode.CODE_ERR_CUTTER:
                return_text = "Epson Printer: Error cutter";
                break;
            case Epos2CallbackCode.CODE_ERR_MECHANICAL:
                return_text = "Epson Printer: Mechanical error";
                break;
            case Epos2CallbackCode.CODE_ERR_EMPTY:
                return_text = "Epson Printer: Empty";
                break;
            case Epos2CallbackCode.CODE_ERR_UNRECOVERABLE:
                return_text = "Epson Printer: Unrecoverable";
                break;
            case Epos2CallbackCode.CODE_ERR_FAILURE:
                return_text = "Epson Printer: Failure";
                break;
            case Epos2CallbackCode.CODE_ERR_NOT_FOUND:
                return_text = "Epson Printer: Error not found";
                break;
            case Epos2CallbackCode.CODE_ERR_SYSTEM:
                return_text = "Epson Printer: System error";
                break;
            case Epos2CallbackCode.CODE_ERR_PORT:
                return_text = "Epson Printer: Port error";
                break;
            case Epos2CallbackCode.CODE_ERR_TIMEOUT:
                return_text = "Epson Printer: Timeout error";
                break;
            case Epos2CallbackCode.CODE_ERR_JOB_NOT_FOUND:
                return_text = "Epson Printer: Job not found error";
                break;
            case Epos2CallbackCode.CODE_ERR_SPOOLER:
                return_text = "Epson Printer: Spooler error";
                break;
            case Epos2CallbackCode.CODE_ERR_BATTERY_LOW:
                return_text = "Epson Printer: Low battery error";
                break;
            default:
                return_text = String.format("%d", state);
                break;
        }
        return return_text;
    }
}
