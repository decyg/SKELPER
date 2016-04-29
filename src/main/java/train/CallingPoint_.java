
package train;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class CallingPoint_ {

    @SerializedName("locationName")
    @Expose
    private String locationName;
    @SerializedName("crs")
    @Expose
    private String crs;
    @SerializedName("st")
    @Expose
    private String st;
    @SerializedName("et")
    @Expose
    private String et;
    @SerializedName("at")
    @Expose
    private Object at;
    @SerializedName("adhocAlerts")
    @Expose
    private Object adhocAlerts;

    /**
     * 
     * @return
     *     The locationName
     */
    public String getLocationName() {
        return locationName;
    }

    /**
     * 
     * @param locationName
     *     The locationName
     */
    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    /**
     * 
     * @return
     *     The crs
     */
    public String getCrs() {
        return crs;
    }

    /**
     * 
     * @param crs
     *     The crs
     */
    public void setCrs(String crs) {
        this.crs = crs;
    }

    /**
     * 
     * @return
     *     The st
     */
    public String getSt() {
        return st;
    }

    /**
     * 
     * @param st
     *     The st
     */
    public void setSt(String st) {
        this.st = st;
    }

    /**
     * 
     * @return
     *     The et
     */
    public String getEt() {
        return et;
    }

    /**
     * 
     * @param et
     *     The et
     */
    public void setEt(String et) {
        this.et = et;
    }

    /**
     * 
     * @return
     *     The at
     */
    public Object getAt() {
        return at;
    }

    /**
     * 
     * @param at
     *     The at
     */
    public void setAt(Object at) {
        this.at = at;
    }

    /**
     * 
     * @return
     *     The adhocAlerts
     */
    public Object getAdhocAlerts() {
        return adhocAlerts;
    }

    /**
     * 
     * @param adhocAlerts
     *     The adhocAlerts
     */
    public void setAdhocAlerts(Object adhocAlerts) {
        this.adhocAlerts = adhocAlerts;
    }

}
