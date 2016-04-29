
package train;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class TrainService {

    @SerializedName("origin")
    @Expose
    private List<Origin> origin = new ArrayList<Origin>();
    @SerializedName("destination")
    @Expose
    private List<Destination> destination = new ArrayList<Destination>();
    @SerializedName("currentOrigins")
    @Expose
    private Object currentOrigins;
    @SerializedName("currentDestinations")
    @Expose
    private Object currentDestinations;
    @SerializedName("sta")
    @Expose
    private String sta;
    @SerializedName("eta")
    @Expose
    private String eta;
    @SerializedName("std")
    @Expose
    private String std;
    @SerializedName("etd")
    @Expose
    private String etd;
    @SerializedName("platform")
    @Expose
    private Object platform;
    @SerializedName("operator")
    @Expose
    private String operator;
    @SerializedName("operatorCode")
    @Expose
    private String operatorCode;
    @SerializedName("isCircularRoute")
    @Expose
    private Boolean isCircularRoute;
    @SerializedName("filterLocationCancelled")
    @Expose
    private Boolean filterLocationCancelled;
    @SerializedName("serviceType")
    @Expose
    private Integer serviceType;
    @SerializedName("serviceID")
    @Expose
    private String serviceID;
    @SerializedName("adhocAlerts")
    @Expose
    private Object adhocAlerts;

    /**
     * 
     * @return
     *     The origin
     */
    public List<Origin> getOrigin() {
        return origin;
    }

    /**
     * 
     * @param origin
     *     The origin
     */
    public void setOrigin(List<Origin> origin) {
        this.origin = origin;
    }

    /**
     * 
     * @return
     *     The destination
     */
    public List<Destination> getDestination() {
        return destination;
    }

    /**
     * 
     * @param destination
     *     The destination
     */
    public void setDestination(List<Destination> destination) {
        this.destination = destination;
    }

    /**
     * 
     * @return
     *     The currentOrigins
     */
    public Object getCurrentOrigins() {
        return currentOrigins;
    }

    /**
     * 
     * @param currentOrigins
     *     The currentOrigins
     */
    public void setCurrentOrigins(Object currentOrigins) {
        this.currentOrigins = currentOrigins;
    }

    /**
     * 
     * @return
     *     The currentDestinations
     */
    public Object getCurrentDestinations() {
        return currentDestinations;
    }

    /**
     * 
     * @param currentDestinations
     *     The currentDestinations
     */
    public void setCurrentDestinations(Object currentDestinations) {
        this.currentDestinations = currentDestinations;
    }

    /**
     * 
     * @return
     *     The sta
     */
    public String getSta() {
        return sta;
    }

    /**
     * 
     * @param sta
     *     The sta
     */
    public void setSta(String sta) {
        this.sta = sta;
    }

    /**
     * 
     * @return
     *     The eta
     */
    public String getEta() {
        return eta;
    }

    /**
     * 
     * @param eta
     *     The eta
     */
    public void setEta(String eta) {
        this.eta = eta;
    }

    /**
     * 
     * @return
     *     The std
     */
    public String getStd() {
        return std;
    }

    /**
     * 
     * @param std
     *     The std
     */
    public void setStd(String std) {
        this.std = std;
    }

    /**
     * 
     * @return
     *     The etd
     */
    public String getEtd() {
        return etd;
    }

    /**
     * 
     * @param etd
     *     The etd
     */
    public void setEtd(String etd) {
        this.etd = etd;
    }

    /**
     * 
     * @return
     *     The platform
     */
    public Object getPlatform() {
        return platform;
    }

    /**
     * 
     * @param platform
     *     The platform
     */
    public void setPlatform(Object platform) {
        this.platform = platform;
    }

    /**
     * 
     * @return
     *     The operator
     */
    public String getOperator() {
        return operator;
    }

    /**
     * 
     * @param operator
     *     The operator
     */
    public void setOperator(String operator) {
        this.operator = operator;
    }

    /**
     * 
     * @return
     *     The operatorCode
     */
    public String getOperatorCode() {
        return operatorCode;
    }

    /**
     * 
     * @param operatorCode
     *     The operatorCode
     */
    public void setOperatorCode(String operatorCode) {
        this.operatorCode = operatorCode;
    }

    /**
     * 
     * @return
     *     The isCircularRoute
     */
    public Boolean getIsCircularRoute() {
        return isCircularRoute;
    }

    /**
     * 
     * @param isCircularRoute
     *     The isCircularRoute
     */
    public void setIsCircularRoute(Boolean isCircularRoute) {
        this.isCircularRoute = isCircularRoute;
    }

    /**
     * 
     * @return
     *     The filterLocationCancelled
     */
    public Boolean getFilterLocationCancelled() {
        return filterLocationCancelled;
    }

    /**
     * 
     * @param filterLocationCancelled
     *     The filterLocationCancelled
     */
    public void setFilterLocationCancelled(Boolean filterLocationCancelled) {
        this.filterLocationCancelled = filterLocationCancelled;
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
     *     The serviceID
     */
    public String getServiceID() {
        return serviceID;
    }

    /**
     * 
     * @param serviceID
     *     The serviceID
     */
    public void setServiceID(String serviceID) {
        this.serviceID = serviceID;
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

    @Override
    public String toString(){

        String arrives = (this.sta != null ? "Arr " + (this.eta.equals("On time") ? "on time " + this.sta : "delayed " + this.eta) + " " : "");

        String departs = (this.std != null ? "Dep " + (this.etd.equals("On time") ? "on time " + this.std : "delayed " + this.etd) + " " : "");

        String fromStat = (this.origin.get(0) != null ? this.origin.get(0).getLocationName() : "");

        String toStat = (this.destination.get(0) != null ? this.destination.get(0).getLocationName() : "");

        String platform = (this.platform != null ? "Platform " + this.platform : "");

        return "[" + platform + "]" + "[" + arrives + departs + "]" + " " + fromStat + " to " + toStat;
    }

}
