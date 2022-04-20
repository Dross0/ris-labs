
package ru.gaidamaka.model.xml;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
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
 *       &amp;lt;sequence&amp;gt;
 *         &amp;lt;element ref="{http://openstreetmap.org/osm/0.6}bounds"/&amp;gt;
 *         &amp;lt;element ref="{http://openstreetmap.org/osm/0.6}node" maxOccurs="unbounded" minOccurs="0"/&amp;gt;
 *         &amp;lt;element ref="{http://openstreetmap.org/osm/0.6}way" maxOccurs="unbounded" minOccurs="0"/&amp;gt;
 *         &amp;lt;element ref="{http://openstreetmap.org/osm/0.6}relation" maxOccurs="unbounded" minOccurs="0"/&amp;gt;
 *       &amp;lt;/sequence&amp;gt;
 *       &amp;lt;attribute name="version" type="{http://www.w3.org/2001/XMLSchema}float" fixed="0.6" /&amp;gt;
 *       &amp;lt;attribute name="generator" type="{http://www.w3.org/2001/XMLSchema}string" fixed="CGImap 0.0.2" /&amp;gt;
 *     &amp;lt;/restriction&amp;gt;
 *   &amp;lt;/complexContent&amp;gt;
 * &amp;lt;/complexType&amp;gt;
 * &lt;/pre&gt;
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "bounds",
    "node",
    "way",
    "relation"
})
@XmlRootElement(name = "osm")
public class Osm {

    @XmlElement(required = true)
    protected Bounds bounds;
    protected List<Node> node;
    protected List<Way> way;
    protected List<Relation> relation;
    @XmlAttribute(name = "version")
    protected Float version;
    @XmlAttribute(name = "generator")
    protected String generator;

    /**
     * Gets the value of the bounds property.
     * 
     * @return
     *     possible object is
     *     {@link Bounds }
     *     
     */
    public Bounds getBounds() {
        return bounds;
    }

    /**
     * Sets the value of the bounds property.
     * 
     * @param value
     *     allowed object is
     *     {@link Bounds }
     *     
     */
    public void setBounds(Bounds value) {
        this.bounds = value;
    }

    /**
     * Gets the value of the node property.
     * 
     * &lt;p&gt;
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a &lt;CODE&gt;set&lt;/CODE&gt; method for the node property.
     * 
     * &lt;p&gt;
     * For example, to add a new item, do as follows:
     * &lt;pre&gt;
     *    getNode().add(newItem);
     * &lt;/pre&gt;
     * 
     * 
     * &lt;p&gt;
     * Objects of the following type(s) are allowed in the list
     * {@link Node }
     * 
     * 
     */
    public List<Node> getNode() {
        if (node == null) {
            node = new ArrayList<Node>();
        }
        return this.node;
    }

    /**
     * Gets the value of the way property.
     * 
     * &lt;p&gt;
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a &lt;CODE&gt;set&lt;/CODE&gt; method for the way property.
     * 
     * &lt;p&gt;
     * For example, to add a new item, do as follows:
     * &lt;pre&gt;
     *    getWay().add(newItem);
     * &lt;/pre&gt;
     * 
     * 
     * &lt;p&gt;
     * Objects of the following type(s) are allowed in the list
     * {@link Way }
     * 
     * 
     */
    public List<Way> getWay() {
        if (way == null) {
            way = new ArrayList<Way>();
        }
        return this.way;
    }

    /**
     * Gets the value of the relation property.
     * 
     * &lt;p&gt;
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a &lt;CODE&gt;set&lt;/CODE&gt; method for the relation property.
     * 
     * &lt;p&gt;
     * For example, to add a new item, do as follows:
     * &lt;pre&gt;
     *    getRelation().add(newItem);
     * &lt;/pre&gt;
     * 
     * 
     * &lt;p&gt;
     * Objects of the following type(s) are allowed in the list
     * {@link Relation }
     * 
     * 
     */
    public List<Relation> getRelation() {
        if (relation == null) {
            relation = new ArrayList<Relation>();
        }
        return this.relation;
    }

    /**
     * Gets the value of the version property.
     * 
     * @return
     *     possible object is
     *     {@link Float }
     *     
     */
    public float getVersion() {
        if (version == null) {
            return  0.6F;
        } else {
            return version;
        }
    }

    /**
     * Sets the value of the version property.
     * 
     * @param value
     *     allowed object is
     *     {@link Float }
     *     
     */
    public void setVersion(Float value) {
        this.version = value;
    }

    /**
     * Gets the value of the generator property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGenerator() {
        if (generator == null) {
            return "CGImap 0.0.2";
        } else {
            return generator;
        }
    }

    /**
     * Sets the value of the generator property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGenerator(String value) {
        this.generator = value;
    }

}
