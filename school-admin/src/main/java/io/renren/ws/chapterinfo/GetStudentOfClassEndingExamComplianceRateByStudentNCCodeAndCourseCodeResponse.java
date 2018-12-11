
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
 *         &lt;element name="GetStudentOfClassEndingExamComplianceRateByStudentNCCodeAndCourseCodeResult" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
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
    "getStudentOfClassEndingExamComplianceRateByStudentNCCodeAndCourseCodeResult"
})
@XmlRootElement(name = "GetStudentOfClassEndingExamComplianceRateByStudentNCCodeAndCourseCodeResponse")
public class GetStudentOfClassEndingExamComplianceRateByStudentNCCodeAndCourseCodeResponse {

    @XmlElement(name = "GetStudentOfClassEndingExamComplianceRateByStudentNCCodeAndCourseCodeResult")
    protected String getStudentOfClassEndingExamComplianceRateByStudentNCCodeAndCourseCodeResult;

    /**
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGetStudentOfClassEndingExamComplianceRateByStudentNCCodeAndCourseCodeResult() {
        return getStudentOfClassEndingExamComplianceRateByStudentNCCodeAndCourseCodeResult;
    }

    /**
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGetStudentOfClassEndingExamComplianceRateByStudentNCCodeAndCourseCodeResult(String value) {
        this.getStudentOfClassEndingExamComplianceRateByStudentNCCodeAndCourseCodeResult = value;
    }

}
