
package train;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class Destination {

    @SerializedName("locationName")
    @Expose
    private String locationName;
    @SerializedName("crs")
    @Expose
    private String crs;
    @SerializedName("via")
    @Expose
    private Object via;
    @SerializedName("futureChangeTo")
    @Expose
    private Object futureChangeTo;
    @SerializedName("assocIsCancelled")
    @Expose
    private Boolean assocIsCancelled;

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
     *     The via
     */
    public Object getVia() {
        return via;
    }

    /**
     * 
     * @param via
     *     The via
     */
    public void setVia(Object via) {
        this.via = via;
    }

    /**
     * 
     * @return
     *     The futureChangeTo
     */
    public Object getFutureChangeTo() {
        return futureChangeTo;
    }

    /**
     * 
     * @param futureChangeTo
     *     The futureChangeTo
     */
    public void setFutureChangeTo(Object futureChangeTo) {
        this.futureChangeTo = futureChangeTo;
    }

    /**
     * 
     * @return
     *     The assocIsCancelled
     */
    public Boolean getAssocIsCancelled() {
        return assocIsCancelled;
    }

    /**
     * 
     * @param assocIsCancelled
     *     The assocIsCancelled
     */
    public void setAssocIsCancelled(Boolean assocIsCancelled) {
        this.assocIsCancelled = assocIsCancelled;
    }

}
