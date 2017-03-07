package models.Response;


public class ResponseObject {
    private int code;
    private String msg;

    public ResponseObject(int code, String msg){
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
