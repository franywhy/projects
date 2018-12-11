
package io.renren.ws.chapterinfo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>anonymous complex type�� Java �ࡣ
 * 
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="GetETSBusinessOpportunityInfo_SyncToNCByTimeResult" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "getETSBusinessOpportunityInfoSyncToNCByTimeResult"
})
@XmlRootElement(name = "GetETSBusinessOpportunityInfo_SyncToNCByTimeResponse")
public class GetETSBusinessOpportunityInfoSyncToNCByTimeResponse {

    @XmlElement(name = "GetETSBusinessOpportunityInfo_SyncToNCByTimeResult")
    protected String getETSBusinessOpportunityInfoSyncToNCByTimeResult;

    /**
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGetETSBusinessOpportunityInfoSyncToNCByTimeResult() {
        return getETSBusinessOpportunityInfoSyncToNCByTimeResult;
    }

    /**
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGetETSBusinessOpportunityInfoSyncToNCByTimeResult(String value) {
        this.getETSBusinessOpportunityInfoSyncToNCByTimeResult = value;
    }

}
