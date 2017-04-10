package models.Response;

/**
 * Created by rudy on 12/02/17.
 */
public class RedirectResponse extends ResponseObject {

    private String url;
    public RedirectResponse(int code, String msg, String url) {
        super(code, msg);
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public RedirectResponse setUrl(String url) {
        this.url = url;
        return this;
    }
}
