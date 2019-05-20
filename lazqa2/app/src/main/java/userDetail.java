public class userDetail {
    private String uId ;
    private String phoneNumber;
    private String uName;
    private String uType;

    public userDetail( ) {
        this.uId = uId;
        this.phoneNumber = phoneNumber;
        this.uName = uName;
        this.uType = uType;
    }

   /* public userDetail(String uId, String phoneNumber, String uName, String uType) {
        this.uId = uId;
        this.phoneNumber = phoneNumber;
        this.uName = uName;
        this.uType = uType;
    }*/

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getuName() {
        return uName;
    }

    public void setuName(String uName) {
        this.uName = uName;
    }

    public String getuType() {
        return uType;
    }

    public void setuType(String uType) {
        this.uType = uType;
    }




}
