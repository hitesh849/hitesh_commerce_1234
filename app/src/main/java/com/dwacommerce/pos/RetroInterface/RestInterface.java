package com.dwacommerce.pos.RetroInterface;

import com.dwacommerce.pos.dao.AddToCartData;
import com.dwacommerce.pos.dao.CartItemsData;
import com.dwacommerce.pos.dao.CartPaymentData;
import com.dwacommerce.pos.dao.CategoryData;
import com.dwacommerce.pos.dao.CheckOutData;
import com.dwacommerce.pos.dao.ClearCartData;
import com.dwacommerce.pos.dao.CloseTerminalData;
import com.dwacommerce.pos.dao.CommonResponseData;
import com.dwacommerce.pos.dao.ConfigurationsData;
import com.dwacommerce.pos.dao.CountryData;
import com.dwacommerce.pos.dao.CustomerData;
import com.dwacommerce.pos.dao.LoginData;
import com.dwacommerce.pos.dao.LoyaltyPointsData;
import com.dwacommerce.pos.dao.OpenTerminalData;
import com.dwacommerce.pos.dao.PaidInOutData;
import com.dwacommerce.pos.dao.PaidInOutResponse;
import com.dwacommerce.pos.dao.PartyData;
import com.dwacommerce.pos.dao.ProductAttributesData;
import com.dwacommerce.pos.dao.ProductData;
import com.dwacommerce.pos.dao.PromoCodeData;
import com.dwacommerce.pos.dao.ReceiptData;
import com.dwacommerce.pos.dao.SaleData;
import com.dwacommerce.pos.dao.SaveSaleData;
import com.dwacommerce.pos.dao.SignOutData;
import com.dwacommerce.pos.dao.StateData;
import com.dwacommerce.pos.dao.UserData;
import com.dwacommerce.pos.utility.Constants;
import com.google.gson.JsonElement;

import java.util.HashMap;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.Header;
import retrofit.http.Headers;
import retrofit.http.POST;
import retrofit.http.Query;

/**
 * Created by admin on 5/12/2016.
 */
public interface RestInterface {
    @Headers({"Content-Type:application/json"})
    @POST("/posConfig")
    void settingRest(@Body HashMap<String, Object> map, @Query("USERNAME") String userName, @Query("PASSWORD") String password, @Query("tenantId") String tenantId, Callback<UserData> cb);

    @Headers({"Content-Type:application/json"})
    @POST("/webPosLogin")
    void loginRequest(@Body HashMap<String, Object> map, @Query("USERNAME") String userName, @Query("PASSWORD") String password, @Query("posTerminalId") String posTerminalId, @Query("productStoreId") String productStoreId, @Query("tenantId") String tenantId, Callback<LoginData> callback);

    @Headers({"Content-Type:application/json"})
    @POST("/savePosConfig")
    void saveConfigRequest(@Body HashMap<String, Object> map, @Query("productStoreId") String productStoreId, @Query("tenantId") String tenantId, Callback<ConfigurationsData> callback);

    @Headers({"Content-Type:application/json"})
    @POST("/showCart")
    void showCartRequest(@Body HashMap<String, Object> map, @Header("Cookie") String header, @Query(Constants.KEY_SESSION_ID) String jsessionId, @Query(Constants.KEY_POS_TERMINAL_ID) String posTerminalId, @Query("tenantId") String tenantId, Callback<CartItemsData> callback);

    @Headers({"Content-Type:application/json"})
    @POST("/deleteCartItem")
    void deleteCartItemRequest(@Body HashMap<String, Object> map, @Header("Cookie") String header, @Query("cartLineIndex") String cartLineIndex, @Query(Constants.KEY_SESSION_ID) String jsessionId, @Query(Constants.KEY_POS_TERMINAL_ID) String posTerminalId, @Query("tenantId") String tenantId, Callback<CommonResponseData> callback);

    @Headers({"Content-Type:application/json"})
    @POST("/updateCartItem")
    void updateCartItem(@Body HashMap<String, Object> map, @Header("Cookie") String header, @Query("cartLineIndex") String cartLineIndex, @Query("quantity") String quantity, @Query(Constants.KEY_SESSION_ID) String jsessionId, @Query(Constants.KEY_POS_TERMINAL_ID) String posTerminalId, @Query("tenantId") String tenantId, Callback<CommonResponseData> callback);

    @Headers({"Content-Type:application/json"})
    @POST("/findProduct")
    void findProductRequest(@Body HashMap<String, Object> map, @Query("searchText") String searchText, @Query("tenantId") String tenantId, Callback<ProductData> callback);

    @Headers({"Content-Type:application/json"})
    @POST("/findParties")
    void findParty(@Body HashMap<String, Object> map, @Query("searchType") String searchType, @Query("searchText") String searchText, @Query("tenantId") String tenantId, Callback<PartyData> callback);

    @Headers({"Content-Type:application/json"})
    @POST("/addCartItem")
    void addCartItemRequest(@Body HashMap<String, Object> map, @Header("Cookie") String header, @Query(Constants.KEY_PRODUCT_STORE_ID) String productStoreId, @Query("add_product_id") String add_product_id, @Query("quantity") String quantity, @Query(Constants.KEY_SESSION_ID) String jsessionId, @Query(Constants.KEY_POS_TERMINAL_ID) String posTerminalId, @Query("tenantId") String tenantId, Callback<CommonResponseData> callback);


    @Headers({"Content-Type:application/json"})
    @POST("/addCartItem")
    void addCartItemWithBarCodeRequest(@Body HashMap<String, Object> map, @Header("Cookie") String header, @Query(Constants.KEY_PRODUCT_STORE_ID) String productStoreId, @Query("barCode") String barcode, @Query("quantity") String quantity, @Query(Constants.KEY_SESSION_ID) String jsessionId, @Query(Constants.KEY_POS_TERMINAL_ID) String posTerminalId, @Query("tenantId") String tenantId, Callback<AddToCartData> callback);

    @Headers({"Content-Type:application/json"})
    @POST("/clearCart")
    void clearCartRequest(@Body HashMap<String, Object> map, @Header("Cookie") String header, @Query(Constants.KEY_POS_TERMINAL_ID) String posTerminalId, @Query(Constants.KEY_SESSION_ID) String jsessionId, @Query("tenantId") String tenantId, Callback<ClearCartData> callback);

    @Headers({"Content-Type:application/json"})
    @POST("/getProductType")
    void getProductTypeRequest(@Body HashMap<String, Object> map, @Header("Cookie") String header, @Query("productId") String productId, @Query("tenantId") String tenantId, Callback<ProductAttributesData> callback);

    @Headers({"Content-Type:application/json"})
    @POST("/webPosLogout")
    void logoutRequest(@Body HashMap<String, Object> map, @Header("Cookie") String header, @Query(Constants.KEY_SESSION_ID) String jsessionId, @Query("tenantId") String tenantId, Callback<SignOutData> callback);

    @Headers({"Content-Type:application/json"})
    @POST("/getRelatedGeo")
    void getStateListRequest(@Body HashMap<String, Object> map, @Query("geoId") String geoId, @Query("tenantId") String tenantId, Callback<StateData> callback);

    @Headers({"Content-Type:application/json"})
    @POST("/getAllCategoryTree")
    void getCategoriesRequest(@Body HashMap<String, Object> map, @Query("tenantId") String tenantId, Callback<CategoryData> callback);

    @Headers({"Content-Type:application/json"})
    @POST("/getProductsByCategory")
    void getProductsByCategoryRequest(@Body HashMap<String, Object> map, @Query("productCategoryId") String productCategoryId, @Header("Cookie") String header, @Query("tenantId") String tenantId, Callback<ProductData> callback);

    @Headers({"Content-Type:application/json"})
    @POST("/setPartyToCart")
    void setPartyToCartRequest(@Body HashMap<String, Object> map, @Header("Cookie") String header, @Query("partyId") String partyId, @Query("contactMechPurposeTypeId") String contactMechPurposeTypeId, @Query("contactMechId") String contactMechId, @Query("jsessionid") String jsessionid, @Query("tenantId") String tenantId, Callback<CommonResponseData> callback);

    @Headers({"Content-Type:application/json"})
    @POST("/getPartyInfo")
    void getPartyInfoRequest(@Body HashMap<String, Object> map, @Header("Cookie") String header, @Query(Constants.KEY_POS_TERMINAL_ID) String posTerminalId, @Query("tenantId") String tenantId, Callback<CustomerData> callback);

    @Headers({"Content-Type:application/json"})
    @POST("/createParty")
    void createPartyRequest(@Body HashMap<String, Object> map, @Query("firstName") String firstName, @Query("lastName") String lastName, @Query("email") String email, @Query("address1") String address1, @Query("city") String city, @Query("postalCode") String postalCode, @Query("countryGeoId") String countryGeoId, @Query("stateProvinceGeoId") String stateProvinceGeoId, @Query("contact") String contact, @Query("tenantId") String tenantId, Callback<CommonResponseData> callback);

    @Headers({"Content-Type:application/json"})
    @POST("/addLoyaltyPoints")
    void addLoyaltyPointsRequest(@Body HashMap<String, Object> map, @Header("Cookie") String header, @Query("loyalityPointAmt") String loyalityPointAmt, @Query(Constants.KEY_POS_TERMINAL_ID) String posTerminalId, @Query("jsessionid") String jsessionid, @Query("tenantId") String tenantId, Callback<LoyaltyPointsData> callback);

    @Headers({"Content-Type:application/json"})
    @POST("/applyPromoCode")
    void applyPromoCodeRequest(@Body HashMap<String, Object> map, @Header("Cookie") String header, @Query("promoCode") String promoCode, @Query(Constants.KEY_POS_TERMINAL_ID) String posTerminalId, @Query("jsessionid") String jsessionid, @Query("tenantId") String tenantId, Callback<PromoCodeData> callback);

    @Headers({"Content-Type:application/json"})
    @POST("/checkout")
    void checkoutRequest(@Body HashMap<String, Object> map, @Header("Cookie") String header, @Query(Constants.KEY_POS_TERMINAL_ID) String posTerminalId, @Query("jsessionid") String jsessionid, @Query("tenantId") String tenantId, Callback<CheckOutData> callback);

    @Headers({"Content-Type:application/json"})
    @POST("/getCountryList")
    void getCountryList(@Body HashMap<String, Object> map, @Query("tenantId") String tenantId, Callback<CountryData> callback);

    @Headers({"Content-Type:application/json"})
    @POST("/cartPayment")
    void cashPaymentRequest(@Body HashMap<String, Object> map, @Header("Cookie") String header, @Query("paymentMode") String paymentMode, @Query("amount") String amount, @Query("jsessionid") String jsessionid, @Query(Constants.KEY_POS_TERMINAL_ID) String posTerminalId, @Query("tenantId") String tenantId, Callback<CartPaymentData> callback);

    @Headers({"Content-Type:application/json"})
    @POST("/cartPayment")
    void cartPaymentRequest(@Body HashMap<String, Object> map, @Header("Cookie") String header, @Query("paymentMode") String paymentMode, @Query("refNum") String refNum, @Query("amount") String amount, @Query("jsessionid") String jsessionid, @Query(Constants.KEY_POS_TERMINAL_ID) String posTerminalId, @Query("tenantId") String tenantId, Callback<CartPaymentData> callback);

    @Headers({"Content-Type:application/json"})
    @POST("/getOrderDetails")
    void getOrderDetailsRequest(@Body HashMap<String, Object> map, @Query("orderId") String orderId, @Query("tenantId") String tenantId, Callback<ReceiptData> callback);

    @Headers({"Content-Type:application/json"})
    @POST("/voidOrder")
    void voidOrderRequest(@Body HashMap<String, Object> map, @Query(Constants.KEY_POS_TERMINAL_ID) String posTerminalId, @Query("orderId") String orderId, @Query("tenantId") String tenantId, Callback<JsonElement> callback);

    @Headers({"Content-Type:application/json"})
    @POST("/getPaidInOutReasons")
    void getPaidInOutReasonsRequest(@Body HashMap<String, Object> map, @Query("paidType") String paidType, @Query("tenantId") String tenantId, Callback<PaidInOutData> callback);

    @Headers({"Content-Type:application/json"})
    @POST("/paidOutAndIn")
    void paidOutAndInRequest(@Body HashMap<String, Object> map, @Header("Cookie") String header, @Query(Constants.KEY_POS_TERMINAL_ID) String posTerminalId, @Query("amountInOut") String amountInOut, @Query("reasonCommentInOut") String reasonCommentInOut, @Query("reasonEnumId") String reasonEnumId, @Query("paidType") String paidType, @Query("tenantId") String tenantId, Callback<PaidInOutResponse> callback);

    @Headers({"Content-Type:application/json"})
    @POST("/saveSale")
    void saveSaleListRequest(@Body HashMap<String, Object> map, @Header("Cookie") String header, @Query(Constants.KEY_POS_TERMINAL_ID) String posTerminalId, @Query("shoppingListName") String shoppingListName, @Query("tenantId") String tenantId, Callback<SaveSaleData> callback);

    @Headers({"Content-Type:application/json"})
    @POST("/savedSaleList")
    void fetchSaveSaleListRequest(@Body HashMap<String, Object> map, @Header("Cookie") String header, @Query("tenantId") String tenantId, Callback<SaleData> callback);

    @Headers({"Content-Type:application/json"})
    @POST("/loadSale")
    void loadSaleRequest(@Body HashMap<String, Object> map, @Header("Cookie") String header, @Query(Constants.KEY_POS_TERMINAL_ID) String posTerminalId, @Query("shoppingListId") String shoppingListId, @Query("action") String action, @Query("tenantId") String tenantId, Callback<CommonResponseData> callback);

    @Headers({"Content-Type:application/json"})
    @POST("/openTerminal")
    void openTerminalRequest(@Body HashMap<String, Object> map, @Header("Cookie") String header, @Query(Constants.KEY_POS_TERMINAL_ID) String posTerminalId, @Query("startingDrawerAmount") String startingDrawerAmount, @Query("tenantId") String tenantId, Callback<OpenTerminalData> callback);

    @Headers({"Content-Type:application/json"})
    @POST("/closeTerminal")
    void closeTerminalRequest(@Body HashMap<String, Object> map, @Header("Cookie") String header, @Query(Constants.KEY_POS_TERMINAL_ID) String posTerminalId, @Query("cashAmount") String cashAmount, @Query("chequeAmount") String chequeAmount, @Query("CCAmount") String CCAmount, @Query("GCAmount") String GCAmount, @Query("otherAmount") String otherAmount, @Query("tenantId") String tenantId, Callback<CloseTerminalData> callback);
}

