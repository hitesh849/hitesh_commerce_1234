package com.dwacommerce.pos.model;

import android.text.TextUtils;

import com.dwacommerce.pos.RetroInterface.RestClient;
import com.dwacommerce.pos.RetroInterface.RestInterface;
import com.dwacommerce.pos.dao.AddToCartData;
import com.dwacommerce.pos.dao.CartItemsData;
import com.dwacommerce.pos.dao.CartPaymentData;
import com.dwacommerce.pos.dao.CheckOutData;
import com.dwacommerce.pos.dao.ClearCartData;
import com.dwacommerce.pos.dao.CloseTerminalData;
import com.dwacommerce.pos.dao.CommonResponseData;
import com.dwacommerce.pos.dao.CountryData;
import com.dwacommerce.pos.dao.CustomerData;
import com.dwacommerce.pos.dao.LoyaltyPointsData;
import com.dwacommerce.pos.dao.OpenTerminalData;
import com.dwacommerce.pos.dao.PaidInOutData;
import com.dwacommerce.pos.dao.PaidInOutResponse;
import com.dwacommerce.pos.dao.PromoCodeData;
import com.dwacommerce.pos.dao.ReceiptData;
import com.dwacommerce.pos.dao.SaleData;
import com.dwacommerce.pos.dao.SaveSaleData;
import com.dwacommerce.pos.dao.SignOutData;
import com.dwacommerce.pos.sharedPreferences.Config;
import com.dwacommerce.pos.utility.Constants;
import com.dwacommerce.pos.viewControllers.DashBordActivity;
import com.google.gson.JsonElement;

import org.byteclues.lib.init.Env;
import org.byteclues.lib.model.BasicModel;
import org.byteclues.lib.utils.Util;

import java.util.HashMap;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by admin on 8/11/2016.
 */

public class DashboardModel extends BasicModel {

    RestInterface restInterface = RestClient.getRestInterface();

    public void showCart(String jsessionId, String posTerminalId) {
        restInterface.showCartRequest(new HashMap<String, Object>(), "JSESSIONID=" + Config.getSessionId(), jsessionId, posTerminalId, Config.getCustomerId(), new Callback<CartItemsData>() {
            @Override
            public void success(CartItemsData cartItemsData, Response response) {
                notifyObservers(cartItemsData);
            }

            @Override
            public void failure(RetrofitError error) {
                notifyObservers(error);
            }
        });
    }

    public void getPartyInfo() {
        restInterface.getPartyInfoRequest(new HashMap<String, Object>(), "JSESSIONID=" + Config.getSessionId(), Config.getPosTerminalId(), Config.getCustomerId(), new Callback<CustomerData>() {
            @Override
            public void success(CustomerData customerData, Response response) {
                notifyObservers(customerData);
            }

            @Override
            public void failure(RetrofitError error) {
                notifyObservers(error);
            }
        });
    }

    public void addLoyaltyPoints(String loyaltyPointsAmount) {
        restInterface.addLoyaltyPointsRequest(new HashMap<String, Object>(), "JSESSIONID=" + Config.getSessionId(), loyaltyPointsAmount, Config.getPosTerminalId(), Config.getSessionId(), Config.getCustomerId(), new Callback<LoyaltyPointsData>() {
            @Override
            public void success(LoyaltyPointsData responseData, Response response) {
                notifyObservers(responseData);
            }

            @Override
            public void failure(RetrofitError error) {
                notifyObservers(error);
            }
        });
    }

    public void clearCart() {
        restInterface.clearCartRequest(new HashMap<String, Object>(), "JSESSIONID=" + Config.getSessionId(), Config.getPosTerminalId(), Config.getSessionId(), Config.getCustomerId(), new Callback<ClearCartData>() {
            @Override
            public void success(ClearCartData responseData, Response response) {
                notifyObservers(responseData);
            }

            @Override
            public void failure(RetrofitError error) {
                notifyObservers(error);
            }
        });
    }

    public void doLogout() {
        restInterface.logoutRequest(new HashMap<String, Object>(), "JSESSIONID=" + Config.getSessionId(), Config.getSessionId(), Config.getCustomerId(), new Callback<SignOutData>() {
            @Override
            public void success(SignOutData responseData, Response response) {
                notifyObservers(responseData);
            }

            @Override
            public void failure(RetrofitError error) {
                notifyObservers(error);
            }
        });
    }

    public void deleteCartItem(String cartLineIndex) {
        restInterface.deleteCartItemRequest(new HashMap<String, Object>(), "JSESSIONID=" + Config.getSessionId(), cartLineIndex, Config.getSessionId(), Config.getPosTerminalId(), Config.getCustomerId(), new Callback<CommonResponseData>() {
            @Override
            public void success(CommonResponseData responseData, Response response) {
                notifyObservers(responseData);
            }

            @Override
            public void failure(RetrofitError error) {
                notifyObservers(error);
            }
        });
    }

    public void getCountryList() {
        restInterface.getCountryList(new HashMap<String, Object>(), Config.getCustomerId(), new Callback<CountryData>() {
            @Override
            public void success(CountryData countryData, Response response) {
                try {
                    if (Constants.RESPONSE_SUCCESS_MSG.equals(countryData.response)) {
                        Config.setCountryList(countryData.countryList.toString());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }

    public void cashPayment(String paymentMode, String amount) {
        restInterface.cashPaymentRequest(new HashMap<String, Object>(), "JSESSIONID=" + Config.getSessionId(), paymentMode, amount, Config.getSessionId(), Config.getPosTerminalId(), Config.getCustomerId(), new Callback<CartPaymentData>() {
            @Override
            public void success(CartPaymentData responseData, Response response) {
                notifyObservers(responseData);
            }

            @Override
            public void failure(RetrofitError error) {
                notifyObservers(error);
            }
        });
    }

    public void cartPayment(String paymentMode, String refNo, String amount) {
        restInterface.cartPaymentRequest(new HashMap<String, Object>(), "JSESSIONID=" + Config.getSessionId(), paymentMode, refNo, amount, Config.getSessionId(), Config.getPosTerminalId(), Config.getCustomerId(), new Callback<CartPaymentData>() {
            @Override
            public void success(CartPaymentData responseData, Response response) {
                notifyObservers(responseData);
            }

            @Override
            public void failure(RetrofitError error) {
                notifyObservers(error);
            }
        });
    }

    public void cartPaymentForAllCategories(String cashAmt, String ccAmt, String ccRefNum, String bankName, String checkAmt, String checkRefNum, String billAccId, String billAccAmt) {
        restInterface.cartPaymentRequestForAllMode(new HashMap<String, Object>(), "JSESSIONID=" + Config.getSessionId(), cashAmt, ccAmt, ccRefNum, bankName, checkAmt, checkRefNum, billAccId, billAccAmt, Config.getSessionId(), Config.getPosTerminalId(), Config.getCustomerId(), new Callback<CartPaymentData>() {
            @Override
            public void success(CartPaymentData responseData, Response response) {
                notifyObservers(responseData);
            }

            @Override
            public void failure(RetrofitError error) {
                notifyObservers(error);
            }
        });
    }

    public void checkout() {
        restInterface.checkoutRequest(new HashMap<String, Object>(), "JSESSIONID=" + Config.getSessionId(), Config.getPosTerminalId(), Config.getSessionId(), Config.getCustomerId(), new Callback<CheckOutData>() {
            @Override
            public void success(CheckOutData responseData, Response response) {
                notifyObservers(responseData);
            }

            @Override
            public void failure(RetrofitError error) {
                notifyObservers(error);
            }
        });
    }

    public void applyPromoCode(String promoCode) {
        restInterface.applyPromoCodeRequest(new HashMap<String, Object>(), "JSESSIONID=" + Config.getSessionId(), promoCode, Config.getPosTerminalId(), Config.getSessionId(), Config.getCustomerId(), new Callback<PromoCodeData>() {
            @Override
            public void success(PromoCodeData responseData, Response response) {
                notifyObservers(responseData);
            }

            @Override
            public void failure(RetrofitError error) {
                notifyObservers(error);
            }
        });
    }

    public void getOrderDetails(String orderId) {
        restInterface.getOrderDetailsRequest(new HashMap<String, Object>(), orderId, Config.getCustomerId(), new Callback<ReceiptData>() {
            @Override
            public void success(ReceiptData receiptData, Response response) {
                notifyObservers(receiptData);
            }

            @Override
            public void failure(RetrofitError error) {
                notifyObservers(error);
            }
        });
    }

    public void voidOrder(String orderId) {
        restInterface.voidOrderRequest(new HashMap<String, Object>(), Config.getPosTerminalId(), orderId, Config.getCustomerId(), new Callback<JsonElement>() {
            @Override
            public void success(JsonElement jsonElement, Response response) {
                notifyObservers(jsonElement);
            }

            @Override
            public void failure(RetrofitError error) {
                notifyObservers(error);
            }
        });
    }

    public void addItemToCart(String addProductId, String quantity) {
        restInterface.addCartItemRequest(new HashMap<String, Object>(), "JSESSIONID=" + Config.getSessionId(), Config.getProductStoreId(), addProductId, quantity, Config.getSessionId(), Config.getPosTerminalId(), Config.getCustomerId(), new Callback<CommonResponseData>() {
            @Override
            public void success(CommonResponseData responseData, Response response) {
                if (Constants.RESPONSE_SUCCESS_MSG.equals(responseData.response)) {
                    ((DashBordActivity) Env.currentActivity).fetchCartData();
                } else {
                    Util.showCenteredToast(Env.currentActivity, TextUtils.isEmpty(responseData.responseMessage) ? "Product out of stock or something went wrong!" : responseData.responseMessage);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                notifyObservers(error);
            }
        });
    }

    public void addItemToCartWithBarcode(String barcode, String quantity) {
        restInterface.addCartItemWithBarCodeRequest(new HashMap<String, Object>(), "JSESSIONID=" + Config.getSessionId(), Config.getProductStoreId(), barcode, quantity, Config.getSessionId(), Config.getPosTerminalId(), Config.getCustomerId(), new Callback<AddToCartData>() {
            @Override
            public void success(AddToCartData responseData, Response response) {
                notifyObservers(responseData);
            }

            @Override
            public void failure(RetrofitError error) {
                notifyObservers(error);
            }
        });
    }

    public void getPaidInOutReasons(final String paidType) {
        restInterface.getPaidInOutReasonsRequest(new HashMap<String, Object>(), paidType, Config.getCustomerId(), new Callback<PaidInOutData>() {
            @Override
            public void success(PaidInOutData responseData, Response response) {
                responseData.paidType = paidType;
                notifyObservers(responseData);
            }

            @Override
            public void failure(RetrofitError error) {
                notifyObservers(error);
            }
        });
    }

    public void paidOutAndIn(String amountInOut, String reasonCommentInOut, String reasonEnumId, String paidType) {
        restInterface.paidOutAndInRequest(new HashMap<String, Object>(), "JSESSIONID=" + Config.getSessionId(), Config.getPosTerminalId(), amountInOut, reasonCommentInOut, reasonEnumId, paidType, Config.getCustomerId(), new Callback<PaidInOutResponse>() {
            @Override
            public void success(PaidInOutResponse responseData, Response response) {
                notifyObservers(responseData);
            }

            @Override
            public void failure(RetrofitError error) {
                notifyObservers(error);
            }
        });
    }

    public void saveSale(String shoppingListName, final boolean clearSale) {
        restInterface.saveSaleListRequest(new HashMap<String, Object>(), "JSESSIONID=" + Config.getSessionId(), Config.getPosTerminalId(), shoppingListName, Config.getCustomerId(), new Callback<SaveSaleData>() {
            @Override
            public void success(SaveSaleData saveSaleData, Response response) {
                saveSaleData.clearSale = clearSale;
                notifyObservers(saveSaleData);
            }

            @Override
            public void failure(RetrofitError error) {
                notifyObservers(error);
            }
        });
    }

    public void fetchSavedSaleList() {
        restInterface.fetchSaveSaleListRequest(new HashMap<String, Object>(), "JSESSIONID=" + Config.getSessionId(), Config.getCustomerId(), new Callback<SaleData>() {
            @Override
            public void success(SaleData responseData, Response response) {
                notifyObservers(responseData);
            }

            @Override
            public void failure(RetrofitError error) {
                notifyObservers(error);
            }
        });
    }

    public void openTerminal(String startingDrawerAmount) {
        restInterface.openTerminalRequest(new HashMap<String, Object>(), "JSESSIONID=" + Config.getSessionId(), Config.getPosTerminalId(), startingDrawerAmount, Config.getCustomerId(), new Callback<OpenTerminalData>() {
            @Override
            public void success(OpenTerminalData responseData, Response response) {
                notifyObservers(responseData);
            }

            @Override
            public void failure(RetrofitError error) {
                notifyObservers(error);
            }
        });
    }

    public void closeTerminal(String cashAmount, String chequeAmount, String CCAmount, String GCAmount, String otherAmount) {
        restInterface.closeTerminalRequest(new HashMap<String, Object>(), "JSESSIONID=" + Config.getSessionId(), Config.getPosTerminalId(), cashAmount, chequeAmount, CCAmount, GCAmount, otherAmount, Config.getCustomerId(), new Callback<CloseTerminalData>() {
            @Override
            public void success(CloseTerminalData closeTerminalData, Response response) {
                notifyObservers(closeTerminalData);
            }

            @Override
            public void failure(RetrofitError error) {
                notifyObservers(error);
            }
        });
    }

    public void loadSaleFromList(String shoppingListId, String action) {
        restInterface.loadSaleRequest(new HashMap<String, Object>(), "JSESSIONID=" + Config.getSessionId(), Config.getPosTerminalId(), shoppingListId, action, Config.getCustomerId(), new Callback<CommonResponseData>() {
            @Override
            public void success(CommonResponseData commonResponseData, Response response) {
                notifyObservers(commonResponseData);
            }

            @Override
            public void failure(RetrofitError error) {
                notifyObservers(error);
            }
        });
    }
}
