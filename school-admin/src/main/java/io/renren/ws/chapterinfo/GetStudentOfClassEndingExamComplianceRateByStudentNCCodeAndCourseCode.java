
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
 *         &lt;element name="StudentNCCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="CourseCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
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
    "studentNCCode",
    "courseCode"
})
@XmlRootElement(name = "GetStudentOfClassEndingExamComplianceRateByStudentNCCodeAndCourseCode")
public class GetStudentOfClassEndingExamComplianceRateByStudentNCCodeAndCourseCode {

    @XmlElement(name = "StudentNCCode")
    protected String studentNCCode;
    @XmlElement(name = "CourseCode")
    protected String courseCode;

    /**
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStudentNCCode() {
        return studentNCCode;
    }

    /**
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStudentNCCode(String value) {
        this.studentNCCode = value;
    }

    /**
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCourseCode() {
        return courseCode;
    }

    /**
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCourseCode(String value) {
        this.courseCode = value;
    }

}
