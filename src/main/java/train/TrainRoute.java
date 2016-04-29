
package train;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class TrainRoute {

    @SerializedName("generatedAt")
    @Expose
    private String generatedAt;
    @SerializedName("serviceType")
    @Expose
    private Integer serviceType;
    @SerializedName("locationName")
    @Expose
    private String locationName;
    @SerializedName("crs")
    @Expose
    private String crs;
    @SerializedName("operator")
    @Expose
    private String operator;
    @SerializedName("operatorCode")
    @Expose
    private String operatorCode;
    @SerializedName("isCancelled")
    @Expose
    private Boolean isCancelled;
    @SerializedName("disruptionReason")
    @Expose
    private Object disruptionReason;
    @SerializedName("overdueMessage")
    @Expose
    private Object overdueMessage;
    @SerializedName("platform")
    @Expose
    private String platform;
    @SerializedName("sta")
    @Expose
    private String sta;
    @SerializedName("eta")
    @Expose
    private Object eta;
    @SerializedName("ata")
    @Expose
    private String ata;
    @SerializedName("std")
    @Expose
    private String std;
    @SerializedName("etd")
    @Expose
    private String etd;
    @SerializedName("atd")
    @Expose
    private Object atd;
    @SerializedName("adhocAlerts")
    @Expose
    private Object adhocAlerts;
    @SerializedName("previousCallingPoints")
    @Expose
    private List<PreviousCallingPoint> previousCallingPoints = new ArrayList<PreviousCallingPoint>();
    @SerializedName("subsequentCallingPoints")
    @Expose
    private List<SubsequentCallingPoint> subsequentCallingPoints = new ArrayList<SubsequentCallingPoint>();

    /**
     * 
     * @return
     *     The generatedAt
     */
    public String getGeneratedAt() {
        return generatedAt;
    }

    /**
     * 
     * @param generatedAt
     *     The generatedAt
     */
    public void setGeneratedAt(String generatedAt) {
        this.generatedAt = generatedAt;
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
     *     The isCancelled
     */
    public Boolean getIsCancelled() {
        return isCancelled;
    }

    /**
     * 
     * @param isCancelled
     *     The isCancelled
     */
    public void setIsCancelled(Boolean isCancelled) {
        this.isCancelled = isCancelled;
    }

    /**
     * 
     * @return
     *     The disruptionReason
     */
    public Object getDisruptionReason() {
        return disruptionReason;
    }

    /**
     * 
     * @param disruptionReason
     *     The disruptionReason
     */
    public void setDisruptionReason(Object disruptionReason) {
        this.disruptionReason = disruptionReason;
    }

    /**
     * 
     * @return
     *     The overdueMessage
     */
    public Object getOverdueMessage() {
        return overdueMessage;
    }

    /**
     * 
     * @param overdueMessage
     *     The overdueMessage
     */
    public void setOverdueMessage(Object overdueMessage) {
        this.overdueMessage = overdueMessage;
    }

    /**
     * 
     * @return
     *     The platform
     */
    public String getPlatform() {
        return platform;
    }

    /**
     * 
     * @param platform
     *     The platform
     */
    public void setPlatform(String platform) {
        this.platform = platform;
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
    public Object getEta() {
        return eta;
    }

    /**
     * 
     * @param eta
     *     The eta
     */
    public void setEta(Object eta) {
        this.eta = eta;
    }

    /**
     * 
     * @return
     *     The ata
     */
    public String getAta() {
        return ata;
    }

    /**
     * 
     * @param ata
     *     The ata
     */
    public void setAta(String ata) {
        this.ata = ata;
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
     *     The atd
     */
    public Object getAtd() {
        return atd;
    }

    /**
     * 
     * @param atd
     *     The atd
     */
    public void setAtd(Object atd) {
        this.atd = atd;
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

    /**
     * 
     * @return
     *     The previousCallingPoints
     */
    public List<PreviousCallingPoint> getPreviousCallingPoints() {
        return previousCallingPoints;
    }

    /**
     * 
     * @param previousCallingPoints
     *     The previousCallingPoints
     */
    public void setPreviousCallingPoints(List<PreviousCallingPoint> previousCallingPoints) {
        this.previousCallingPoints = previousCallingPoints;
    }

    /**
     * 
     * @return
     *     The subsequentCallingPoints
     */
    public List<SubsequentCallingPoint> getSubsequentCallingPoints() {
        return subsequentCallingPoints;
    }

    /**
     * 
     * @param subsequentCallingPoints
     *     The subsequentCallingPoints
     */
    public void setSubsequentCallingPoints(List<SubsequentCallingPoint> subsequentCallingPoints) {
        this.subsequentCallingPoints = subsequentCallingPoints;
    }

}
