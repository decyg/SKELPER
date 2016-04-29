
package train;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class StationJSON {

    @SerializedName("trainServices")
    @Expose
    private List<TrainService> trainServices = new ArrayList<TrainService>();
    @SerializedName("busServices")
    @Expose
    private Object busServices;
    @SerializedName("ferryServices")
    @Expose
    private Object ferryServices;
    @SerializedName("generatedAt")
    @Expose
    private String generatedAt;
    @SerializedName("locationName")
    @Expose
    private String locationName;
    @SerializedName("crs")
    @Expose
    private String crs;
    @SerializedName("filterLocationName")
    @Expose
    private Object filterLocationName;
    @SerializedName("filtercrs")
    @Expose
    private Object filtercrs;
    @SerializedName("filterType")
    @Expose
    private Integer filterType;
    @SerializedName("nrccMessages")
    @Expose
    private Object nrccMessages;
    @SerializedName("platformAvailable")
    @Expose
    private Boolean platformAvailable;
    @SerializedName("areServicesAvailable")
    @Expose
    private Boolean areServicesAvailable;

    /**
     * 
     * @return
     *     The trainServices
     */
    public List<TrainService> getTrainServices() {
        return trainServices;
    }

    /**
     * 
     * @param trainServices
     *     The trainServices
     */
    public void setTrainServices(List<TrainService> trainServices) {
        this.trainServices = trainServices;
    }

    /**
     * 
     * @return
     *     The busServices
     */
    public Object getBusServices() {
        return busServices;
    }

    /**
     * 
     * @param busServices
     *     The busServices
     */
    public void setBusServices(Object busServices) {
        this.busServices = busServices;
    }

    /**
     * 
     * @return
     *     The ferryServices
     */
    public Object getFerryServices() {
        return ferryServices;
    }

    /**
     * 
     * @param ferryServices
     *     The ferryServices
     */
    public void setFerryServices(Object ferryServices) {
        this.ferryServices = ferryServices;
    }

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
     *     The filterLocationName
     */
    public Object getFilterLocationName() {
        return filterLocationName;
    }

    /**
     * 
     * @param filterLocationName
     *     The filterLocationName
     */
    public void setFilterLocationName(Object filterLocationName) {
        this.filterLocationName = filterLocationName;
    }

    /**
     * 
     * @return
     *     The filtercrs
     */
    public Object getFiltercrs() {
        return filtercrs;
    }

    /**
     * 
     * @param filtercrs
     *     The filtercrs
     */
    public void setFiltercrs(Object filtercrs) {
        this.filtercrs = filtercrs;
    }

    /**
     * 
     * @return
     *     The filterType
     */
    public Integer getFilterType() {
        return filterType;
    }

    /**
     * 
     * @param filterType
     *     The filterType
     */
    public void setFilterType(Integer filterType) {
        this.filterType = filterType;
    }

    /**
     * 
     * @return
     *     The nrccMessages
     */
    public Object getNrccMessages() {
        return nrccMessages;
    }

    /**
     * 
     * @param nrccMessages
     *     The nrccMessages
     */
    public void setNrccMessages(Object nrccMessages) {
        this.nrccMessages = nrccMessages;
    }

    /**
     * 
     * @return
     *     The platformAvailable
     */
    public Boolean getPlatformAvailable() {
        return platformAvailable;
    }

    /**
     * 
     * @param platformAvailable
     *     The platformAvailable
     */
    public void setPlatformAvailable(Boolean platformAvailable) {
        this.platformAvailable = platformAvailable;
    }

    /**
     * 
     * @return
     *     The areServicesAvailable
     */
    public Boolean getAreServicesAvailable() {
        return areServicesAvailable;
    }

    /**
     * 
     * @param areServicesAvailable
     *     The areServicesAvailable
     */
    public void setAreServicesAvailable(Boolean areServicesAvailable) {
        this.areServicesAvailable = areServicesAvailable;
    }

}
