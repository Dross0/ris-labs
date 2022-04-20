
package ru.gaidamaka.model.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * &lt;p&gt;Java class for anonymous complex type.
 * 
 * &lt;p&gt;The following schema fragment specifies the expected content contained within this class.
 * 
 * &lt;pre&gt;
 * &amp;lt;complexType&amp;gt;
 *   &amp;lt;complexContent&amp;gt;
 *     &amp;lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&amp;gt;
 *       &amp;lt;attribute name="minlat" type="{http://www.w3.org/2001/XMLSchema}double" /&amp;gt;
 *       &amp;lt;attribute name="minlon" type="{http://www.w3.org/2001/XMLSchema}double" /&amp;gt;
 *       &amp;lt;attribute name="maxlat" type="{http://www.w3.org/2001/XMLSchema}double" /&amp;gt;
 *       &amp;lt;attribute name="maxlon" type="{http://www.w3.org/2001/XMLSchema}double" /&amp;gt;
 *     &amp;lt;/restriction&amp;gt;
 *   &amp;lt;/complexContent&amp;gt;
 * &amp;lt;/complexType&amp;gt;
 * &lt;/pre&gt;
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
@XmlRootElement(name = "bounds")
public class Bounds {

    @XmlAttribute(name = "minlat")
    protected Double minlat;
    @XmlAttribute(name = "minlon")
    protected Double minlon;
    @XmlAttribute(name = "maxlat")
    protected Double maxlat;
    @XmlAttribute(name = "maxlon")
    protected Double maxlon;

    /**
     * Gets the value of the minlat property.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getMinlat() {
        return minlat;
    }

    /**
     * Sets the value of the minlat property.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setMinlat(Double value) {
        this.minlat = value;
    }

    /**
     * Gets the value of the minlon property.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getMinlon() {
        return minlon;
    }

    /**
     * Sets the value of the minlon property.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setMinlon(Double value) {
        this.minlon = value;
    }

    /**
     * Gets the value of the maxlat property.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getMaxlat() {
        return maxlat;
    }

    /**
     * Sets the value of the maxlat property.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setMaxlat(Double value) {
        this.maxlat = value;
    }

    /**
     * Gets the value of the maxlon property.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getMaxlon() {
        return maxlon;
    }

    /**
     * Sets the value of the maxlon property.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setMaxlon(Double value) {
        this.maxlon = value;
    }

}
