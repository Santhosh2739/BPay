package ycash.wallet.json.pojo.wheretopay;

    import java.util.ArrayList;
    import java.util.List;
    import ycash.wallet.json.pojo.generic.GenericResponse;
    public class MerchantListResponse extends GenericResponse {
       ArrayList<CategoryDetailsListPojo> categoryDetails  = new ArrayList<CategoryDetailsListPojo>();
        ArrayList<MerchantCategoryDetailsListPojo> merchantDetails=new ArrayList<MerchantCategoryDetailsListPojo>();
       public ArrayList<CategoryDetailsListPojo> getCategoryDetails() {
            return categoryDetails;
        }
       public void setCategoryDetails(ArrayList<CategoryDetailsListPojo> categoryDetails) {
            this.categoryDetails = categoryDetails;
        }
       public ArrayList<MerchantCategoryDetailsListPojo> getMerchantDetails() {
            return merchantDetails;
        }
       public void setMerchantDetails(ArrayList<MerchantCategoryDetailsListPojo> merchantDetails) {
            this.merchantDetails = merchantDetails;
        }
    }





