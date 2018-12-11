
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
 *         &lt;element name="GetStudentOfClassEndingExamComplianceRateByTime_BIResult" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
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
    "getStudentOfClassEndingExamComplianceRateByTimeBIResult"
})
@XmlRootElement(name = "GetStudentOfClassEndingExamComplianceRateByTime_BIResponse")
public class GetStudentOfClassEndingExamComplianceRateByTimeBIResponse {

    @XmlElement(name = "GetStudentOfClassEndingExamComplianceRateByTime_BIResult")
    protected String getStudentOfClassEndingExamComplianceRateByTimeBIResult;

    /**
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGetStudentOfClassEndingExamComplianceRateByTimeBIResult() {
        return getStudentOfClassEndingExamComplianceRateByTimeBIResult;
    }

    /**
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGetStudentOfClassEndingExamComplianceRateByTimeBIResult(String value) {
        this.getStudentOfClassEndingExamComplianceRateByTimeBIResult = value;
    }

}
