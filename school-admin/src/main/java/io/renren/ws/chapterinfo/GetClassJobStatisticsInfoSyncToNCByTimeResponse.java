
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
 *         &lt;element name="GetClassJobStatisticsInfo_Sync_To_NCByTimeResult" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
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
    "getClassJobStatisticsInfoSyncToNCByTimeResult"
})
@XmlRootElement(name = "GetClassJobStatisticsInfo_Sync_To_NCByTimeResponse")
public class GetClassJobStatisticsInfoSyncToNCByTimeResponse {

    @XmlElement(name = "GetClassJobStatisticsInfo_Sync_To_NCByTimeResult")
    protected String getClassJobStatisticsInfoSyncToNCByTimeResult;

    /**
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGetClassJobStatisticsInfoSyncToNCByTimeResult() {
        return getClassJobStatisticsInfoSyncToNCByTimeResult;
    }

    /**
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGetClassJobStatisticsInfoSyncToNCByTimeResult(String value) {
        this.getClassJobStatisticsInfoSyncToNCByTimeResult = value;
    }

}
