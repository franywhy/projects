
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
 *         &lt;element name="GetStudentOfClassEndingTaskComplianceRateByTimeResult" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
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
    "getStudentOfClassEndingTaskComplianceRateByTimeResult"
})
@XmlRootElement(name = "GetStudentOfClassEndingTaskComplianceRateByTimeResponse")
public class GetStudentOfClassEndingTaskComplianceRateByTimeResponse {

    @XmlElement(name = "GetStudentOfClassEndingTaskComplianceRateByTimeResult")
    protected String getStudentOfClassEndingTaskComplianceRateByTimeResult;

    /**
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGetStudentOfClassEndingTaskComplianceRateByTimeResult() {
        return getStudentOfClassEndingTaskComplianceRateByTimeResult;
    }

    /**
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGetStudentOfClassEndingTaskComplianceRateByTimeResult(String value) {
        this.getStudentOfClassEndingTaskComplianceRateByTimeResult = value;
    }

}
