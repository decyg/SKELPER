
package train;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class PreviousCallingPoint {

    @SerializedName("callingPoint")
    @Expose
    private List<CallingPoint> callingPoint = new ArrayList<CallingPoint>();
    @SerializedName("serviceType")
    @Expose
    private Integer serviceType;
    @SerializedName("serviceTypeSpecified")
    @Expose
    private Boolean serviceTypeSpecified;
    @SerializedName("serviceChangeRequired")
    @Expose
    private Boolean serviceChangeRequired;
    @SerializedName("serviceChangeRequiredSpecified")
    @Expose
    private Boolean serviceChangeRequiredSpecified;
    @SerializedName("assocIsCancelled")
    @Expose
    private Boolean assocIsCancelled;
    @SerializedName("assocIsCancelledSpecified")
    @Expose
    private Boolean assocIsCancelledSpecified;

    /**
     * 
     * @return
     *     The callingPoint
     */
    public List<CallingPoint> getCallingPoint() {
        return callingPoint;
    }

    /**
     * 
     * @param callingPoint
     *     The callingPoint
     */
    public void setCallingPoint(List<CallingPoint> callingPoint) {
        this.callingPoint = callingPoint;
    }

    /**
     * 
     * @return
     *     The serviceType
     */
    public Integer getServiceType() {
        return serviceType;
    }

    /**
     * 
     * @param serviceType
     *     The serviceType
     */
    public void setServiceType(Integer serviceType) {
        this.serviceType = serviceType;
    }

    /**
     * 
     * @return
     *     The serviceTypeSpecified
     */
    public Boolean getServiceTypeSpecified() {
        return serviceTypeSpecified;
    }

    /**
     * 
     * @param serviceTypeSpecified
     *     The serviceTypeSpecified
     */
    public void setServiceTypeSpecified(Boolean serviceTypeSpecified) {
        this.serviceTypeSpecified = serviceTypeSpecified;
    }

    /**
     * 
     * @return
     *     The serviceChangeRequired
     */
    public Boolean getServiceChangeRequired() {
        return serviceChangeRequired;
    }

    /**
     * 
     * @param serviceChangeRequired
     *     The serviceChangeRequired
     */
    public void setServiceChangeRequired(Boolean serviceChangeRequired) {
        this.serviceChangeRequired = serviceChangeRequired;
    }

    /**
     * 
     * @return
     *     The serviceChangeRequiredSpecified
     */
    public Boolean getServiceChangeRequiredSpecified() {
        return serviceChangeRequiredSpecified;
    }

    /**
     * 
     * @param serviceChangeRequiredSpecified
     *     The serviceChangeRequiredSpecified
     */
    public void setServiceChangeRequiredSpecified(Boolean serviceChangeRequiredSpecified) {
        this.serviceChangeRequiredSpecified = serviceChangeRequiredSpecified;
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

    /**
     * 
     * @return
     *     The assocIsCancelledSpecified
     */
    public Boolean getAssocIsCancelledSpecified() {
        return assocIsCancelledSpecified;
    }

    /**
     * 
     * @param assocIsCancelledSpecified
     *     The assocIsCancelledSpecified
     */
    public void setAssocIsCancelledSpecified(Boolean assocIsCancelledSpecified) {
        this.assocIsCancelledSpecified = assocIsCancelledSpecified;
    }

}
