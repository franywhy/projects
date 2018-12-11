
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
 *         &lt;element name="StartTime" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="PageCount" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="PageNum" type="{http://www.w3.org/2001/XMLSchema}int"/>
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
    "startTime",
    "pageCount",
    "pageNum"
})
@XmlRootElement(name = "GetStudentCompletionrateByUpdateTime")
public class GetStudentCompletionrateByUpdateTime {

    @XmlElement(name = "StartTime")
    protected String startTime;
    @XmlElement(name = "PageCount")
    protected int pageCount;
    @XmlElement(name = "PageNum")
    protected int pageNum;

    /**
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStartTime() {
        return startTime;
    }

    /**
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStartTime(String value) {
        this.startTime = value;
    }

    /**
     * 
     */
    public int getPageCount() {
        return pageCount;
    }

    /**
     * 
     */
    public void setPageCount(int value) {
        this.pageCount = value;
    }

    /**
     * 
     */
    public int getPageNum() {
        return pageNum;
    }

    /**
     * 
     */
    public void setPageNum(int value) {
        this.pageNum = value;
    }

}
