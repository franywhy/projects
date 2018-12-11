
package io.renren.ws.chapterinfo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
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
 *         &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="size" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="page" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="ssoKey" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
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
    "name",
    "size",
    "page",
    "ssoKey"
})
@XmlRootElement(name = "getUsers")
public class GetUsers {

    protected String name;
    protected int size;
    protected int page;
    protected String ssoKey;

    /**
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName() {
        return name;
    }

    /**
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * 
     */
    public int getSize() {
        return size;
    }

    /**
     * 
     */
    public void setSize(int value) {
        this.size = value;
    }

    /**
     * 
     */
    public int getPage() {
        return page;
    }

    /**
     * 
     */
    public void setPage(int value) {
        this.page = value;
    }

    /**
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSsoKey() {
        return ssoKey;
    }

    /**
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSsoKey(String value) {
        this.ssoKey = value;
    }

}
