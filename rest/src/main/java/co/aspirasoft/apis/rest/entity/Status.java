package co.aspirasoft.apis.rest.entity;

/**
 * General status messages used to exchange information with the server.
 *
 * @author saifkhichi96
 * @version 1.0
 */
public class Status {

    private Integer code;
    private String message;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
