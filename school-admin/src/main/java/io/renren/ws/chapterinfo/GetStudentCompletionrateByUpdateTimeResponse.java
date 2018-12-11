
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
 *         &lt;element name="GetStudentCompletionrateByUpdateTimeResult" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="isOK" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
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
    "getStudentCompletionrateByUpdateTimeResult",
    "isOK"
})
@XmlRootElement(name = "GetStudentCompletionrateByUpdateTimeResponse")
public class GetStudentCompletionrateByUpdateTimeResponse {

    @XmlElement(name = "GetStudentCompletionrateByUpdateTimeResult")
    protected String getStudentCompletionrateByUpdateTimeResult;
    protected boolean isOK;

    /**
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGetStudentCompletionrateByUpdateTimeResult() {
        return getStudentCompletionrateByUpdateTimeResult;
    }

    /**
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGetStudentCompletionrateByUpdateTimeResult(String value) {
        this.getStudentCompletionrateByUpdateTimeResult = value;
    }

    /**
     * 
     */
    public boolean isIsOK() {
        return isOK;
    }

    /**
     * 
     */
    public void setIsOK(boolean value) {
        this.isOK = value;
    }

}
